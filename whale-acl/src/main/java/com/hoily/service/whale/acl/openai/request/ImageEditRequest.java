package com.hoily.service.whale.acl.openai.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images/create-edit">Creates an edited or extended image given an original image and a prompt.</a>
 *
 * @author vyckey
 * 2023/3/2 13:17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageEditRequest implements Serializable {
    /**
     * The image to edit. Must be a valid PNG file, less than 4MB, and square. If mask is not provided, image must have transparency, which will be used as the mask.
     */
    private String image;

    /**
     * A text description of the desired image(s). The maximum length is 1000 characters.
     */
    private String prompt;

    /**
     * An additional image whose fully transparent areas (e.g. where alpha is zero) indicate where image should be edited. Must be a valid PNG file, less than 4MB, and have the same dimensions as image.
     */
    private String mask;

    /**
     * Defaults to 1.
     * <p>The number of images to generate. Must be between 1 and 10.</p>
     */
    @JsonProperty("n")
    private Integer number;

    /**
     * Defaults to 1024x1024.
     * <p>The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024.</p>
     */
    private String size;

    /**
     * The format in which the generated images are returned. Must be one of url or b64_json.
     */
    @JsonProperty("response_format")
    private String responseFormat;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse. <a href="https://platform.openai.com/docs/guides/safety-best-practices/end-user-ids">Learn more</a>.
     */
    private String user;

    public ImageEditRequest(String image, String prompt) {
        this.image = image;
        this.prompt = prompt;
    }
}
