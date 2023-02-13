package com.hoily.service.whale.acl.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 14:04
 */
@Data
public class CompletionResponse implements TypedObject {
    private String id;

    private String model;

    private List<ChoiceResponse> choices;

    private UsageResponse usage;

    private Date createdAt;

    private String object;

    @Data
    public static class ChoiceResponse implements Serializable {

        private String text;

        private Integer index;

        @JsonProperty("logprobs")
        private Number logProbs;

        @JsonProperty("finish_reason")
        private String finishReason;

    }

    @Data
    public static class UsageResponse implements Serializable {

        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        @JsonProperty("total_tokens")
        private Integer totalTokens;

    }

    public void setCreated(Integer created) {
        if (created != null) {
            this.createdAt = new Date(created * 1000L);
        } else {
            this.createdAt = null;
        }
    }

}
