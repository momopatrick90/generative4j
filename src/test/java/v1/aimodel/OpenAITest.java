package v1.aimodel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import v1.model.*;
import v1.model.metric.MetricUnit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;


@Slf4j
class OpenAITest {
    @Test
    void completion() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model",0d, false);

        // Act
        final CompletionResponse completionResponse = openAIClient.completion(CompletionRequest.builder()
                .prompt("prompt")
                .temperature(27d)
                .language("en")
                .model("model")
                .build());

        // Assert
        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("prompt_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(9d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("completion_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(12d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("total_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(21d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getCompletionResponseChoices()
                .getCompletionResponseChoiceList()
                .stream()
                .anyMatch(choice -> choice.getFinishReason().equals("stop")
                        && choice.getText().equals("textresponse")));
    }

    @Test
    void completionError() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponseError());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model", 0d, false);

        // Act Assert
        Generative4jException exception = Assertions.assertThrows(Generative4jException.class, () -> openAIClient.completion(CompletionRequest.builder()
                .prompt("prompt")
                .temperature(27d)
                .language("en")
                .model("model")
                .build()));
        Assertions.assertTrue(exception.getMessage().contains("error-message"));
    }

    @Test
    void completionParameters() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model",0d, false);

        // Act
        final CompletionResponse completionResponse = openAIClient.completion(CompletionRequest.builder()
                .prompt("prompt")
                .temperature(27d)
                .language("en")
                .model("model")
                .build());

        // Assert
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<HttpClientContext> httpClientContextArgumentCaptor = ArgumentCaptor.forClass(HttpClientContext.class);
        Mockito.verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), httpClientContextArgumentCaptor.capture());

        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept-Language")[0].getValue(), "en");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Authorization")[0].getValue(), "Bearer key");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept")[0].getValue(), "application/json");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Content-type")[0].getValue(), "application/json");
    }

    @Test
    void completionParametersDefaultModel() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model-str",0d, false);

        // Act
        final CompletionResponse completionResponse = openAIClient.completion(CompletionRequest.builder()
                .prompt("prompt")
                .temperature(27d)
                .language("en")
                .build());

        // Assert
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<HttpClientContext> httpClientContextArgumentCaptor = ArgumentCaptor.forClass(HttpClientContext.class);
        Mockito.verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), httpClientContextArgumentCaptor.capture());
        InputStream inputStream = httpPostArgumentCaptor.getValue().getEntity().getContent();

        String result= new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        Assertions.assertTrue(result.contains("default-model-str"));
    }

    @Test
    void completionUseChatAsCompletion() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model",0d, true);

        // Act
        final CompletionResponse completionResponse = openAIClient.completion(CompletionRequest.builder()
                .prompt("prompt")
                .temperature(27d)
                .language("en")
                .model("model")
                .build());

        // Assert
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<HttpClientContext> httpClientContextArgumentCaptor = ArgumentCaptor.forClass(HttpClientContext.class);
        Mockito.verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), httpClientContextArgumentCaptor.capture());

        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("prompt_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(9d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("completion_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(12d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("total_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(21d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(completionResponse.getCompletionResponseChoices()
                .getCompletionResponseChoiceList()
                .stream()
                .anyMatch(choice -> choice.getFinishReason().equals("stop")
                        && choice.getText().equals("I am a robot. I was programmed to do tasks.")));
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept-Language")[0].getValue(), "en");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Authorization")[0].getValue(), "Bearer key");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept")[0].getValue(), "application/json");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Content-type")[0].getValue(), "application/json");
    }

    @Test
    void chatCompletion() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model",0d, false);

        // Act
        ChatCompletionResponse chatCompletionResponse = openAIClient.chatCompletion(ChatCompletionRequest.builder()
                .messages(Arrays.asList(ChatCompletionMessage.builder()
                        .content("content")
                        .role("role")
                        .build()))
                .temperature(27d)
                .language("en")
                .model("model")
                .build());

        // Assert
        Assertions.assertTrue(chatCompletionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("prompt_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(9d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(chatCompletionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("completion_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(12d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(chatCompletionResponse.getMetrics()
                .getMetrics()
                .stream()
                .anyMatch(metric -> metric.getName().equals("total_tokens") && metric.getUnit().equals(MetricUnit.COUNT) &&
                        metric.getValue().equals(21d) && metric.getComponent().equals(OpenAI.class.getName())));
        Assertions.assertTrue(chatCompletionResponse.getChatCompletionResponseChoices()
                .getChatCompletionResponseChoiceList()
                .stream()
                .anyMatch(choice -> choice.getFinishReason().equals("stop")
                        && choice.getMessage().getContent().equals("I am a robot. I was programmed to do tasks.")
                        && choice.getMessage().getRole().equals("assistant")));
    }

    @Test
    void chatCompletionException() throws IOException {
        // Arrange
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponseError());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model", 0d, false);

        // Act
        Generative4jException exception = Assertions.assertThrows(Generative4jException.class, () -> openAIClient.chatCompletion(ChatCompletionRequest.builder()
                .messages(Arrays.asList(ChatCompletionMessage.builder()
                        .content("content")
                        .role("role")
                        .build()))
                .temperature(27d)
                .language("en")
                .model("model")
                .build()));
        Assertions.assertTrue(exception.getMessage().contains("error-message"));
    }

    @Test
    void chatCompletionParameters() throws IOException {
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model",0d, false);

        // Act
         openAIClient.chatCompletion(ChatCompletionRequest.builder()
                .messages(Arrays.asList(ChatCompletionMessage.builder()
                        .content("content")
                        .role("role")
                        .build()))
                .temperature(27d)
                .model("model")
                         .language("en")
                .build());

        // Assert
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<HttpClientContext> httpClientContextArgumentCaptor = ArgumentCaptor.forClass(HttpClientContext.class);
        Mockito.verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), httpClientContextArgumentCaptor.capture());

        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept-Language")[0].getValue(), "en");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Authorization")[0].getValue(), "Bearer key");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Accept")[0].getValue(), "application/json");
        Assertions.assertEquals(httpPostArgumentCaptor.getValue()
                .getHeaders("Content-type")[0].getValue(), "application/json");
    }

    @Test
    void chatCompletionParametersDefaultModel() throws IOException {
        final CloseableHttpClient closeableHttpClient = mockCloseableHttpClientWithTitle(createChatResponse());
        final OpenAI openAIClient = new OpenAI(closeableHttpClient, "key", "default-model-str",0d, false);

        // Act
        openAIClient.chatCompletion(ChatCompletionRequest.builder()
                .messages(Arrays.asList(ChatCompletionMessage.builder()
                        .content("content")
                        .role("role")
                        .build()))
                .temperature(27d)
                .build());

        // Assert
        ArgumentCaptor<HttpPost> httpPostArgumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
        ArgumentCaptor<HttpClientContext> httpClientContextArgumentCaptor = ArgumentCaptor.forClass(HttpClientContext.class);
        Mockito.verify(closeableHttpClient).execute(httpPostArgumentCaptor.capture(), httpClientContextArgumentCaptor.capture());
        InputStream inputStream = httpPostArgumentCaptor.getValue().getEntity().getContent();

        String result= new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        Assertions.assertTrue(result.contains("default-model-str"));
    }

    private CloseableHttpClient mockCloseableHttpClientWithTitle(JsonObject result) throws IOException {

        final CloseableHttpClient closeableHttpClient = Mockito.mock(CloseableHttpClient.class);
        Mockito.when(closeableHttpClient.execute(Mockito.any(HttpUriRequest.class), Mockito.any(HttpClientContext.class)))
                .then(arg -> {
                    HttpUriRequest httpUriRequest = (HttpUriRequest) arg.getArguments() [0];

                    final HttpEntity httpEntity = Mockito.mock(HttpEntity.class);
                    Mockito.when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(result.toString().getBytes()));
                    Mockito.when(httpEntity.getContentLength()).thenReturn(-1L);

                    final CloseableHttpResponse closeableHttpResponse = Mockito.mock(CloseableHttpResponse.class);
                    Mockito.when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
                    return closeableHttpResponse;
                });

        return closeableHttpClient;
    }

    private JsonObject createResponse() {
        return new Gson().fromJson(" {\n" +
                "  \"id\": \"chatcmpl-123\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1677652288,\n" +
                "  \"choices\": [{\n" +
                "    \"text\": \"textresponse\",\n" +
                "    \"finish_reason\": \"stop\"\n" +
                "  }"  +
                "],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 9,\n" +
                "    \"completion_tokens\": 12,\n" +
                "    \"total_tokens\": 21\n" +
                "  }\n" +
                "} ", JsonObject.class);
    }

    private JsonObject createChatResponse() {
        return new Gson().fromJson(" {\n" +
                "  \"id\": \"chatcmpl-123\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1677652288,\n" +
                "  \"choices\": [{\n" +
                "    \"index\": 0,\n" +
                "    \"message\": {\n" +
                "      \"role\": \"assistant\",\n" +
                "      \"content\": \"I am a robot. I was built to assist humans.\"\n" +
                "    },\n" +
                "    \"finish_reason\": \"stop\"\n" +
                "  }," +
                "  {\n" +
                "    \"index\": 0,\n" +
                "    \"message\": {\n" +
                "      \"role\": \"assistant\",\n" +
                "      \"content\": \"I am a robot. I was programmed to do tasks.\"\n" +
                "    },\n" +
                "    \"finish_reason\": \"stop\"\n" +
                "  }" +
                "],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 9,\n" +
                "    \"completion_tokens\": 12,\n" +
                "    \"total_tokens\": 21\n" +
                "  }\n" +
                "} ", JsonObject.class);
    }

    private JsonObject createChatResponseError() {
        return new Gson().fromJson(" {\n" +
                "  \"error\": \"error-message\"" +
                "} ", JsonObject.class);
    }
}