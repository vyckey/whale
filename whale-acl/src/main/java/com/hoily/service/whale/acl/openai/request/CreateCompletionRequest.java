package com.hoily.service.whale.acl.openai.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <a href="https://platform.openai.com/docs/api-reference/completions/create">Creates a completion for the provided prompt and parameters</a>
 *
 * @author vyckey
 * 2023/2/12 13:35
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCompletionRequest implements Serializable {
    /**
     * ID of the model to use. You can use the List models API to see all of your available models, or see our Model overview for descriptions of them.
     */
    private String model;

    /**
     * Defaults to <|endoftext|>. The prompt(s) to generate completions for, encoded as a string, array of strings, array of tokens, or array of token arrays.
     * Note that <|endoftext|> is the document separator that the model sees during training, so if a prompt is not specified the model will generate as if from the beginning of a new document.
     */
    private Object prompt;

    /**
     * The suffix that comes after a completion of inserted text.
     */
    private String suffix;

    /**
     * Defaults to 16. The maximum number of tokens to generate in the completion.
     * The token count of your prompt plus max_tokens cannot exceed the model's context length. Most models have a context length of 2048 tokens (except for the newest models, which support 4096).
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * Defaults to 1. What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.
     * We generally recommend altering this or top_p but not both.
     */
    private Float temperature;

    /**
     * Defaults to 1. An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
     * We generally recommend altering this or temperature but not both.
     */
    @JsonProperty("top_p")
    private Float topProbability;

    /**
     * Defaults to 1. How many completions to generate for each prompt.
     * Note: Because this parameter generates many completions, it can quickly consume your token quota. Use carefully and ensure that you have reasonable settings for max_tokens and stop.
     */
    @JsonProperty("n")
    private Integer number;

    /**
     * Defaults to false. Whether to stream back partial progress. If set, tokens will be sent as data-only server-sent events as they become available, with the stream terminated by a data: [DONE] message.
     */
    private Boolean stream;

    /**
     * Include the log probabilities on the logprobs most likely tokens, as well the chosen tokens. For example, if logprobs is 5, the API will return a list of the 5 most likely tokens. The API will always return the logprob of the sampled token, so there may be up to logprobs+1 elements in the response.
     * The maximum value for logprobs is 5. If you need more than this, please contact us through our Help center and describe your use case.
     */
    @JsonProperty("logprobs")
    private Integer logProbs;

    /**
     * Defaults to false. Echo back the prompt in addition to the completion
     */
    private Boolean echo;

    /**
     * Up to 4 sequences where the API will stop generating further tokens. The returned text will not contain the stop sequence.
     */
    private Object stop;

    /**
     * Defaults to 0. Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
     */
    @JsonProperty("presence_penalty")
    private Float presencePenalty;

    /**
     * Defaults to 0. Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
     */
    @JsonProperty("frequency_penalty")
    private Float frequencyPenalty;

    /**
     * Defaults to 1.Generates best_of completions server-side and returns the "best" (the one with the highest log probability per token). Results cannot be streamed.
     * <p>
     * When used with n, best_of controls the number of candidate completions and n specifies how many to return – best_of must be greater than n.
     * <p>
     * Note: Because this parameter generates many completions, it can quickly consume your token quota. Use carefully and ensure that you have reasonable settings for max_tokens and stop.
     */
    @JsonProperty("best_of")
    private Integer bestOf;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     * <p>
     * Accepts a json object that maps tokens (specified by their token ID in the GPT tokenizer) to an associated bias value from -100 to 100. You can use this tokenizer tool (which works for both GPT-2 and GPT-3) to convert text to token IDs. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of the relevant token.
     * <p>
     * As an example, you can pass {"50256": -100} to prevent the <|endoftext|> token from being generated.
     */
    @JsonProperty("logit_bias")
    private Map<String, Float> logitBias;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     */
    private String user;

    public CreateCompletionRequest(String model) {
        this.model = model;
    }

    @JsonSetter
    protected void setPrompt(Object prompt) {
        this.prompt = prompt;
    }

    public void setPrompt(String... prompt) {
        if (prompt.length == 0) {
            this.prompt = null;
        } else if (prompt.length == 1) {
            this.prompt = prompt[0];
        } else {
            this.prompt = prompt;
        }
    }

    public void setPrompt(String[][] prompt) {
        this.prompt = prompt;
    }
}
