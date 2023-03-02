package com.hoily.service.whale.api.controller;

import com.hoily.service.whale.acl.openai.OpenAIRestTemplate;
import com.hoily.service.whale.acl.openai.request.ChatCompletionRequest;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.acl.openai.response.ListResultResponse;
import com.hoily.service.whale.acl.openai.response.ModelInfoResponse;
import com.hoily.service.whale.contract.response.BaseResponse;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * OpenAI Controller
 *
 * @author vyckey
 * 2023/2/14 17:11
 */
@Slf4j
@RestController
@RequestMapping("api/openai")
@AllArgsConstructor
public class OpenAIController {
    private final OpenAIRestTemplate openAIRestTemplate;

    @GetMapping(value = "/models")
    @ResponseBody
    public BaseResponse<List<ModelInfoResponse>> listModels() {
        ListResultResponse<ModelInfoResponse> response = openAIRestTemplate.listModels();
        return BaseResponse.success(response.getData()).build();
    }

    @PostMapping(value = "/completion")
    @ResponseBody
    public BaseResponse<CompletionResponse> completion(@Valid @RequestBody Map<String, Object> request) {
        CreateCompletionRequest completionRequest = JsonUtils.convert(request, CreateCompletionRequest.class);
        if (StringUtils.isBlank(completionRequest.getModel())) {
            completionRequest.setModel("text-davinci-003");
        }
        if (completionRequest.getTemperature() == null) {
            completionRequest.setTemperature(0.5f);
        }
        try {
            CompletionResponse response = openAIRestTemplate.completion(completionRequest);
            return BaseResponse.success(response).build();
        } catch (Exception e) {
            log.error("openai completion fail, request:{}", JsonUtils.toJson(completionRequest), e);
            return BaseResponse.<CompletionResponse>fail(50000, e.getMessage()).build();
        }
    }

    @PostMapping(value = "/chat/completion")
    @ResponseBody
    public BaseResponse<CompletionResponse> chatCompletion(@Valid @RequestBody Map<String, Object> request) {
        ChatCompletionRequest completionRequest = JsonUtils.fromJson(JsonUtils.toJson(request), ChatCompletionRequest.class);
        if (StringUtils.isBlank(completionRequest.getModel())) {
            completionRequest.setModel("gpt-3.5-turbo");
        }
        if (completionRequest.getTemperature() == null) {
            completionRequest.setTemperature(0.5f);
        }
        try {
            CompletionResponse response = openAIRestTemplate.chatCompletion(completionRequest);
            return BaseResponse.success(response).build();
        } catch (Exception e) {
            log.error("openai chat completion fail, request:{}", JsonUtils.toJson(completionRequest), e);
            return BaseResponse.<CompletionResponse>fail(50000, e.getMessage()).build();
        }
    }

}
