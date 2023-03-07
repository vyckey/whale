package com.hoily.service.whale.application.wechat;

import com.hoily.service.whale.acl.openai.OpenAIRestTemplate;
import com.hoily.service.whale.acl.openai.request.ChatCompletionRequest;
import com.hoily.service.whale.acl.openai.request.ChatMessage;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.request.ImageGenerateRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.acl.openai.response.ImageResponse;
import com.hoily.service.whale.acl.wechat.WechatRestTemplate;
import com.hoily.service.whale.acl.wechat.base.WechatResponse;
import com.hoily.service.whale.acl.wechat.customer.message.MessageTypingRequest;
import com.hoily.service.whale.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.whale.acl.wechat.message.OfficialTextMessageDTO;
import com.hoily.service.whale.acl.wechat.message.UserMessageDTO;
import com.hoily.service.whale.acl.wechat.message.UserTextMessageDTO;
import com.hoily.service.whale.acl.wechat.util.MessageHelper;
import com.hoily.service.whale.application.wechat.command.CommandExecutor;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import com.hoily.service.whale.infrastructure.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 20:24
 */
@Slf4j
@Component
public class WechatMessageService {
    private static final String DEFAULT_ANSWER = "不要意思，暂时回答不了你的问题哦~请联系管理员微信号\"vyckey0213\"！";
    private final OpenAIRestTemplate openAIRestTemplate;
    private final WechatRestTemplate wechatRestTemplate;
    private final UserStateManager userStateManager;
    private final CommandExecutor commandExecutor;
    private final ExecutorService executorService;

    public WechatMessageService(OpenAIRestTemplate openAIRestTemplate, WechatRestTemplate wechatRestTemplate,
                                UserStateManager userStateManager, CommandExecutor commandExecutor,
                                @Qualifier("wechatExecutor") ExecutorService executorService) {
        this.openAIRestTemplate = openAIRestTemplate;
        this.wechatRestTemplate = wechatRestTemplate;
        this.userStateManager = userStateManager;
        this.commandExecutor = commandExecutor;
        this.executorService = executorService;
    }

    private Optional<String> completion(String model, String user, String content) {
        CreateCompletionRequest request = new CreateCompletionRequest(model);
        request.setPrompt(content);
        request.setTemperature(0.4f);
        request.setMaxTokens(1024);
        request.setUser(user);
        try {
            CompletionResponse response = openAIRestTemplate.completion(request);
            log.info("chatgpt completion '{}'\nresponse:{}", content, JsonUtils.toJson(response));
            return Optional.ofNullable(response).map(CompletionResponse::getPossibleContent).map(text -> StringUtils.trim(text, '\n'));
        } catch (Exception e) {
            log.error("chatgpt completion '{}' ex", content, e);
            return Optional.empty();
        }
    }

    private Optional<String> chatCompletion(String model, String user, String content) {
        ChatCompletionRequest request = new ChatCompletionRequest(model, ChatMessage.user(content));
        request.setTemperature(0.4f);
        request.setMaxTokens(1024);
        request.setUser(user);
        try {
            CompletionResponse response = openAIRestTemplate.chatCompletion(request);
            log.info("chatgpt chat completion '{}'\nresponse:{}", content, JsonUtils.toJson(response));
            return Optional.ofNullable(response).map(CompletionResponse::getPossibleContent).map(text -> StringUtils.trim(text, '\n'));
        } catch (Exception e) {
            log.error("chatgpt chat completion '{}' ex", content, e);
            return Optional.empty();
        }
    }

    private Optional<String> createImage(String user, String content) {
        ImageGenerateRequest request = new ImageGenerateRequest(content);
        request.setUser(user);
        try {
            ImageResponse response = openAIRestTemplate.createImage(request);
            log.info("chatgpt image create '{}'\nresponse:{}", content, JsonUtils.toJson(response));
            return Optional.ofNullable(response).map(ImageResponse::getPossibleImageUrl).map(text -> StringUtils.trim(text, '\n'));
        } catch (Exception e) {
            log.error("chatgpt image create '{}' ex", content, e);
            return Optional.empty();
        }
    }

    public OfficialMessageDTO handleMessage(UserTextMessageDTO userMessage) {
        String user = userMessage.getFromUserName();
        String content = userMessage.getContent();
        UserState userState = userStateManager.getUserState(user);
        Optional<String> modelOptional = Optional.ofNullable(userState).map(UserState::getOpenAIModel);

        OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
        String defaultAnswer = "不要意思，暂时回答不了你的问题哦~请联系管理员微信号\"vyckey0213\"！";
        Optional<String> answerOptional;
        if (commandExecutor.isCommand(content)) {
            answerOptional = Optional.ofNullable(commandExecutor.execute(content, userMessage.getFromUserName()));
            officialMessage.setContent(answerOptional.orElse(defaultAnswer));
        } else if (userState == null || OpenAITaskType.CHAT.equals(userState.getOpenAITaskType())) {
            answerOptional = chatCompletion(modelOptional.orElse("gpt-3.5-turbo"), user, content);
            officialMessage.setContent(answerOptional.orElse(defaultAnswer));
        } else if (OpenAITaskType.IMAGE.equals(userState.getOpenAITaskType())) {
            answerOptional = createImage(user, content);
            officialMessage.setContent(answerOptional.orElse(defaultAnswer));
        } else {
            answerOptional = completion(modelOptional.orElse("text-davinci-003"), user, content);
            officialMessage.setContent(answerOptional.orElse(defaultAnswer));
        }

        officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
        officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
        return officialMessage;
    }

    public OfficialMessageDTO handleMessage(UserMessageDTO userMessage) {
        if (userMessage instanceof UserTextMessageDTO) {
            return handleMessage((UserTextMessageDTO) userMessage);
        }
        return null;
    }

    public OfficialMessageDTO handleMessage(UserMessageDTO userMessage, long timeout, TimeUnit timeUnit) {
        Future<OfficialMessageDTO> future = null;
        try {
            future = executorService.submit(() -> handleMessage(userMessage));
            return future.get(timeout, timeUnit);
        } catch (TimeoutException e) {
            Future<OfficialMessageDTO> finalFuture = future;
            // try to async send message
            executorService.execute(() -> asyncSendMessage(userMessage.getFromUserName(), finalFuture));
            wechatRestTemplate.sendMessageTyping(MessageTypingRequest.typing(userMessage.getFromUserName()));
            return null;
        } catch (Exception e) {
            log.error("wechat-send ex", e);
            // send default message
            OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
            officialMessage.setContent(DEFAULT_ANSWER);
            officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
            officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
            return officialMessage;
        }
    }

    private void asyncSendMessage(String userId, Future<OfficialMessageDTO> future) {
        try {
            OfficialMessageDTO messageDTO = future.get();
            Map<String, Object> customerMessage = MessageHelper.toCustomerMessage(messageDTO);
            if (customerMessage == null) {
                return;
            }
            WechatResponse<Void> response = wechatRestTemplate.sendCustomerMessage(messageDTO);
            if (!response.isSuccess()) {
                wechatRestTemplate.sendMessageTyping(MessageTypingRequest.cancel(userId));
                log.warn("wechat-send {} fail {}", customerMessage, response.getErrorMsg());
            }
        } catch (Exception e) {
            wechatRestTemplate.sendMessageTyping(MessageTypingRequest.cancel(userId));
            log.error("wechat-async-send ex", e);
        }
    }
}
