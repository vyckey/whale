package com.hoily.service.whale.acl.openai.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <a href="https://platform.openai.com/docs/api-reference/audio/create">Translates audio into into English.</a>
 *
 * @author vyckey
 * 2023/3/2 13:24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AudioTranslationRequest implements Serializable {
    /**
     * ID of the model to use. Only whisper-1 is currently available.
     */
    private String model;

    /**
     * The audio file to translate, in one of these formats: mp3, mp4, mpeg, mpga, m4a, wav, or webm.
     */
    private String file;

    /**
     * An optional text to guide the model's style or continue a previous audio segment. The prompt should be in English.
     */
    private String prompt;

    /**
     * Defaults to json.
     * <p>The format of the transcript output, in one of these options: json, text, srt, verbose_json, or vtt.</p>
     */
    @JsonProperty("response_format")
    private String responseFormat;

    /**
     * Defaults to 0.
     * <p>The sampling temperature, between 0 and 1. Higher values like 0.8 will make the output more random,
     * while lower values like 0.2 will make it more focused and deterministic. If set to 0, the model will use log
     * probability to automatically increase the temperature until certain thresholds are hit.</p>
     */
    private Float temperature;
}
