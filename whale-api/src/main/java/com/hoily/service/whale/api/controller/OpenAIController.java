package com.hoily.service.whale.api.controller;

import com.hoily.service.whale.acl.OpenAIRestTemplateFactory;
import com.hoily.service.whale.acl.openai.request.ChatCompletionRequest;
import com.hoily.service.whale.acl.openai.request.ChatMessage;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.acl.openai.response.ListResultResponse;
import com.hoily.service.whale.acl.openai.response.ModelInfoResponse;
import com.hoily.service.whale.contract.response.BaseResponse;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * OpenAI Controller
 *
 * @author vyckey
 * 2023/2/14 17:11
 */
@Slf4j
@Controller
@RequestMapping("api/openai")
@AllArgsConstructor
public class OpenAIController {
    private final OpenAIRestTemplateFactory openAIRestTemplateFactory;

    @GetMapping(value = "/models")
    @ResponseBody
    public BaseResponse<List<ModelInfoResponse>> listModels(@RequestHeader(value = "API_KEY", required = false) String apiKey) {
        ListResultResponse<ModelInfoResponse> response = openAIRestTemplateFactory.createTemplate(apiKey).listModels();
        return BaseResponse.success(response.getData()).build();
    }

    @PostMapping(value = "/completion")
    @ResponseBody
    public BaseResponse<CompletionResponse> completion(@Valid @RequestBody Map<String, Object> request,
                                                       @RequestHeader(value = "API_KEY", required = false) String apiKey) {
        CreateCompletionRequest completionRequest = JsonUtils.convert(request, CreateCompletionRequest.class);
        if (StringUtils.isBlank(completionRequest.getModel())) {
            completionRequest.setModel("text-davinci-003");
        }
        if (completionRequest.getTemperature() == null) {
            completionRequest.setTemperature(0.5f);
        }
        try {
            CompletionResponse response = openAIRestTemplateFactory.createTemplate(apiKey).completion(completionRequest);
            return BaseResponse.success(response).build();
        } catch (Exception e) {
            log.error("openai completion fail, request:{}", JsonUtils.toJson(completionRequest), e);
            return BaseResponse.<CompletionResponse>fail(50000, e.getMessage()).build();
        }
    }

    @PostMapping(value = "/chat/completion")
    @ResponseBody
    public BaseResponse<CompletionResponse> chatCompletion(@Valid @RequestBody Map<String, Object> request,
                                                           @RequestHeader(value = "API_KEY", required = false) String apiKey) {
        ChatCompletionRequest completionRequest = JsonUtils.fromJson(JsonUtils.toJson(request), ChatCompletionRequest.class);
        if (StringUtils.isBlank(completionRequest.getModel())) {
            completionRequest.setModel("gpt-3.5-turbo-0301");
        }
        if (completionRequest.getTemperature() == null) {
            completionRequest.setTemperature(0.5f);
        }
        try {
            CompletionResponse response = openAIRestTemplateFactory.createTemplate(apiKey).chatCompletion(completionRequest);
            return BaseResponse.success(response).build();
        } catch (Exception e) {
            log.error("openai chat completion fail, request:{}", JsonUtils.toJson(completionRequest), e);
            return BaseResponse.<CompletionResponse>fail(50000, e.getMessage()).build();
        }
    }

    @GetMapping(value = "/chat/completion")
    public String chatCompletion(@RequestHeader(value = "API_KEY", required = false) String apiKey,
                                 @RequestParam(value = "model", required = false, defaultValue = "gpt-3.5-turbo-0301") String model,
                                 @RequestParam("content") String content, Model uiModel) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest(model);
        completionRequest.addMessage(ChatMessage.user(content));
        if (completionRequest.getTemperature() == null) {
            completionRequest.setTemperature(0.5f);
        }
        try {
            uiModel.addAttribute("question", content);
            CompletionResponse response = openAIRestTemplateFactory.createTemplate(apiKey).chatCompletion(completionRequest);
            String answer = Optional.ofNullable(response).map(CompletionResponse::getPossibleContent).orElse("");
            uiModel.addAttribute("answer", answer);
        } catch (Exception e) {
            log.error("openai chat completion fail, content:{}", content, e);
            uiModel.addAttribute("answer", "Sorry, something is wrong! Please contact author \"vyckey\".");
        }
        return "/openai/completion";
    }

}
