package com.hoily.service.whale.acl.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.hoily.service.whale.acl.openai.request.AudioTranscriptionRequest;
import com.hoily.service.whale.acl.openai.request.AudioTranslationRequest;
import com.hoily.service.whale.acl.openai.request.ChatCompletionRequest;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.request.CreateEditRequest;
import com.hoily.service.whale.acl.openai.request.ImageEditRequest;
import com.hoily.service.whale.acl.openai.request.ImageGenerateRequest;
import com.hoily.service.whale.acl.openai.request.ImageVariationRequest;
import com.hoily.service.whale.acl.openai.response.AudioResponse;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import com.hoily.service.whale.acl.openai.response.EngineInfoResponse;
import com.hoily.service.whale.acl.openai.response.ImageResponse;
import com.hoily.service.whale.acl.openai.response.ListResultResponse;
import com.hoily.service.whale.acl.openai.response.ModelInfoResponse;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * description is here
 * <p>
 * refer to <a href="https://platform.openai.com/docs/api-reference/models/retrieve">OpenAI API reference</a>
 *
 * @author vyckey
 * 2023/2/12 13:18
 */
@Slf4j
@Component
public class OpenAIRestTemplate {
    private static final String OPENAI_DOMAIN = "https://api.openai.com";
    private final HttpServletRequest request;
    private final RestTemplate restTemplate;
    private String apiKey;

    public OpenAIRestTemplate(HttpServletRequest request, RestTemplate restTemplate) {
        this.request = request;
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setBase64ApiKey(@Value("${openai.authentication.api_key}") String apiKey) {
        this.apiKey = apiKey;
    }

    private <T> T exchange(String uri, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(OPENAI_DOMAIN + "/" + uri, method, requestEntity, responseType, uriVariables);
            if (log.isDebugEnabled()) {
                log.debug("doExchange: call service with request {} and response {}", JsonUtils.toJson(request), JsonUtils.toJson(response));
            }
            return response.getBody();
        } catch (HttpStatusCodeException ce) {
            throw new OpenAIRestException("[" + uri + "]: http status code: " + ce.getStatusText());
        } catch (RestClientException rce) {
            throw new OpenAIRestException("[" + uri + "]: rest ex", rce);
        } catch (Exception e) {
            throw new OpenAIRestException("[" + uri + "]: internal ex", e);
        }
    }

    private <T> T exchange(String uri, HttpMethod method, Object request, TypeReference<T> responseType, Object... uriVariables) {
        String requestBody = (request != null) ? JsonUtils.toJson(request) : null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.set("Authorization", "Bearer " + getApiKey());

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
        return exchange(uri, method, httpEntity, new ParameterizedTypeReference<T>() {
            @Override
            public Type getType() {
                return responseType.getType();
            }
        }, uriVariables);
    }

    private String getApiKey() {
        return apiKey;
    }

    /**
     * Lists the currently available models, and provides basic information about each one such as the owner and availability.
     */
    public ListResultResponse<ModelInfoResponse> listModels() {
        return exchange("v1/models", HttpMethod.GET, null, new TypeReference<ListResultResponse<ModelInfoResponse>>() {
        });
    }

    /**
     * Retrieves a model instance, providing basic information about the model such as the owner and permissioning.
     */
    public ModelInfoResponse getModel(String modelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(modelId), "model is required");
        return exchange("v1/models/{model}", HttpMethod.GET, null, new TypeReference<ModelInfoResponse>() {
        }, modelId);
    }

    /**
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     */
    public CompletionResponse completion(CreateCompletionRequest request) {
        return exchange("v1/completions", HttpMethod.POST, request, new TypeReference<CompletionResponse>() {
        });
    }

    /**
     * Given a chat conversation, the model will return a chat completion response.
     */
    public CompletionResponse chatCompletion(ChatCompletionRequest request) {
        return exchange("v1/chat/completions", HttpMethod.POST, request, new TypeReference<CompletionResponse>() {
        });
    }

    /**
     * Given a prompt and an instruction, the model will return an edited version of the prompt.
     */
    public CompletionResponse edit(CreateEditRequest request) {
        return exchange("v1/edits", HttpMethod.POST, request, new TypeReference<CompletionResponse>() {
        });
    }

    /**
     * Given a prompt and/or an input image, the model will generate a new image.
     */
    public ImageResponse createImage(ImageGenerateRequest request) {
        return exchange("v1/images/generations", HttpMethod.POST, request, new TypeReference<ImageResponse>() {
        });
    }

    /**
     * Creates an edited or extended image given an original image and a prompt.
     */
    public ImageResponse editImage(ImageEditRequest request) {
        return exchange("v1/images/edits", HttpMethod.POST, request, new TypeReference<ImageResponse>() {
        });
    }

    /**
     * Creates a variation of a given image.
     */
    public ImageResponse variationImage(ImageVariationRequest request) {
        return exchange("v1/images/variations", HttpMethod.POST, request, new TypeReference<ImageResponse>() {
        });
    }

    /**
     * Transcribes audio into the input language.
     */
    public AudioResponse transcriptionAudio(AudioTranscriptionRequest request) {
        return exchange("v1/audio/transcriptions", HttpMethod.POST, request, new TypeReference<AudioResponse>() {
        });
    }

    /**
     * Translates audio into into English.
     */
    public AudioResponse translateAudio(AudioTranslationRequest request) {
        return exchange("v1/audio/translations", HttpMethod.POST, request, new TypeReference<AudioResponse>() {
        });
    }

    /**
     * Lists the currently available (non-finetuned) models, and provides basic information about each one such as the owner and availability.
     */
    public ListResultResponse<EngineInfoResponse> listEngines() {
        return exchange("v1/engines", HttpMethod.GET, null, new TypeReference<ListResultResponse<EngineInfoResponse>>() {
        });
    }

    /**
     * Retrieves a model instance, providing basic information about it such as the owner and availability.
     */
    public EngineInfoResponse getEngine(String engineId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(engineId), "engine_id is required");
        return exchange("v1/engines/{engine_id}", HttpMethod.GET, null, new TypeReference<EngineInfoResponse>() {
        }, engineId);
    }
}
