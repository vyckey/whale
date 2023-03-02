package com.hoily.service.whale.acl.openai;

import com.hoily.service.whale.acl.AclContext;
import com.hoily.service.whale.acl.openai.request.ChatCompletionRequest;
import com.hoily.service.whale.acl.openai.request.ChatMessage;
import com.hoily.service.whale.acl.openai.request.CreateCompletionRequest;
import com.hoily.service.whale.acl.openai.response.CompletionResponse;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 20:15
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AclContext.class)
@SpringBootTest
public class OpenAIRestTemplateTest {
    @Autowired
    private OpenAIRestTemplate openAIRestTemplate;

    @Test
    public void completionTest() {
        CreateCompletionRequest request = new CreateCompletionRequest("text-davinci-003");
        request.setPrompt("Say this is a test");
        request.setTemperature(0.0f);
        request.setMaxTokens(7);
        CompletionResponse response = openAIRestTemplate.completion(request);
        Optional<String> textOptional = Optional.ofNullable(response).map(CompletionResponse::getChoices)
                .filter(CollectionUtils::isNotEmpty).map(list -> list.get(0))
                .map(CompletionResponse.ChoiceResponse::getText);
        Assert.assertTrue(textOptional.isPresent());
    }

    @Test
    public void chatCompletionTest() {
        ChatCompletionRequest request = new ChatCompletionRequest("gpt-3.5-turbo");
        request.addMessage(ChatMessage.system("You are a helpful assistant."))
                .addMessage(ChatMessage.user("Who won the world series in 2020?"))
                .addMessage(ChatMessage.assistant("The Los Angeles Dodgers won the World Series in 2020."))
                .addMessage(ChatMessage.user("Where was it played?"));
        CompletionResponse response = openAIRestTemplate.chatCompletion(request);
        Optional<String> textOptional = Optional.ofNullable(response).map(CompletionResponse::getChoices)
                .filter(CollectionUtils::isNotEmpty).map(list -> list.get(0))
                .map(CompletionResponse.ChoiceResponse::getMessage)
                .map(ChatMessage::getContent);
        Assert.assertTrue(textOptional.isPresent());
    }
}