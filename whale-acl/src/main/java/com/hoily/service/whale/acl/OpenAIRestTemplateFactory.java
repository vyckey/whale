package com.hoily.service.whale.acl;

import com.hoily.service.whale.acl.openai.OpenAIRestTemplate;
import com.hoily.service.whale.infrastructure.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/22 19:57
 */
@Component
public class OpenAIRestTemplateFactory {
    private final HttpServletRequest request;
    private final RestTemplate restTemplate;
    private OpenAIRestTemplate defaultTemplate;

    public OpenAIRestTemplateFactory(HttpServletRequest request, RestTemplate restTemplate) {
        this.request = request;
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setDefaultTemplate(@Value("${openai.authentication.api_key}") String defaultApiKey) {
        this.defaultTemplate = new OpenAIRestTemplate(request, restTemplate).setApiKey(defaultApiKey);
    }

    public OpenAIRestTemplate createTemplate(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            return defaultTemplate;
        }
        return new OpenAIRestTemplate(request, restTemplate).setApiKey(apiKey);
    }

    @Bean
    public OpenAIRestTemplate defaultTemplate() {
        return defaultTemplate;
    }
}
