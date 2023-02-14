package com.hoily.service.whale.api.controller;

import com.hoily.service.whale.acl.openai.OpenAIRestTemplate;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.contract.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * description is here
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

    @PostMapping(value = "/completion")
    @ResponseBody
    public BaseResponse<CompletionResponse> completion(@Valid @RequestBody SimpleCompletionRequest request) {
        CreateCompletionRequest completionRequest = new CreateCompletionRequest(request.getModel());
        completionRequest.setPrompt(request.getPrompt());
        completionRequest.setMaxTokens(request.getMaxTokens());
        CompletionResponse response = openAIRestTemplate.completion(completionRequest);
        return BaseResponse.success(response).build();
    }

    @Data
    static class SimpleCompletionRequest implements Serializable {
        @NotBlank(message = "model is required")
        private String model = "text-davinci-003";

        @NotBlank(message = "prompt is required")
        private String prompt;

        @NotNull(message = "maxTokens is required")
        private Integer maxTokens = 2048;
    }
}
