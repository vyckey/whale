package com.hoily.service.whale.application.wechat;

import com.hoily.service.whale.acl.openai.OpenAIRestTemplate;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.whale.acl.wechat.message.OfficialTextMessageDTO;
import com.hoily.service.whale.acl.wechat.message.UserMessageDTO;
import com.hoily.service.whale.acl.wechat.message.UserTextMessageDTO;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import com.hoily.service.whale.infrastructure.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 20:24
 */
@Slf4j
@Component
@AllArgsConstructor
public class WechatMessageService {
    private final OpenAIRestTemplate openAIRestTemplate;

    private Optional<String> generateChatGPTAnswer(String prompt) {
        CreateCompletionRequest request = new CreateCompletionRequest("text-davinci-003");
        request.setPrompt(prompt);
        request.setTemperature(0.4f);
        request.setMaxTokens(1024);
        try {
            CompletionResponse response = openAIRestTemplate.completion(request);
            log.info("chatgpt completion '{}'\nresponse:{}", prompt, JsonUtils.toJson(response));
            return Optional.ofNullable(response).map(CompletionResponse::getChoices)
                    .filter(CollectionUtils::isNotEmpty).map(list -> list.get(0))
                    .map(CompletionResponse.ChoiceResponse::getText)
                    .map(text ->StringUtils.trim(text, '\n'));
        } catch (Exception e) {
            log.error("chatgpt completion '{}' ex", prompt, e);
            return Optional.empty();
        }
    }

    public OfficialMessageDTO handleMessage(UserMessageDTO userMessage) {
        if (userMessage instanceof UserTextMessageDTO) {
            UserTextMessageDTO userTextMessage = (UserTextMessageDTO) userMessage;
            Optional<String> answerOptional = generateChatGPTAnswer(userTextMessage.getContent());
            OfficialTextMessageDTO officialMessage = new OfficialTextMessageDTO();
            officialMessage.setUserName(userMessage.getToUserName(), userMessage.getFromUserName());
            officialMessage.setCreateTime(System.currentTimeMillis() / 1000L);
            officialMessage.setContent(answerOptional.orElse("不要意思，暂时回答不了你的问题哦~请联系管理员微信号\"vyckey0213\"！"));
            return officialMessage;
        }
        return null;
    }
}
