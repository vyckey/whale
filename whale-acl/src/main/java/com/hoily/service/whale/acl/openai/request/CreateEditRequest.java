package com.hoily.service.whale.acl.openai.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Creates a new edit for the provided input, instruction, and parameters.
 *
 * @author vyckey
 * 2023/2/12 14:22
 */
@Data
public class CreateEditRequest implements Serializable {
    /**
     * ID of the model to use. You can use the text-davinci-edit-001 or code-davinci-edit-001 model with this endpoint.
     */
    private String model;

    /**
     * The input text to use as a starting point for the edit.
     */
    private String input;

    /**
     * The instruction that tells the model how to edit the prompt.
     */
    private String instruction;

    /**
     * Defaults to 1. How many edits to generate for the input and instruction.
     */
    @JsonProperty("n")
    private Integer number;

    /**
     * Defaults to 1. What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.
     * <p>
     * We generally recommend altering this or top_p but not both.
     */
    private Float temperature;

    /**
     * Defaults to 1. An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
     * <p>
     * We generally recommend altering this or temperature but not both.
     */
    @JsonProperty("top_n")
    private Float topN;

    public CreateEditRequest(String model, String instruction) {
        this.model = model;
        this.instruction = instruction;
    }
}
