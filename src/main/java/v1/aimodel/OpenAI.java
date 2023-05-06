package v1.aimodel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import v1.model.*;
import v1.model.agent.metric.Metric;
import v1.model.agent.metric.MetricName;
import v1.model.agent.metric.MetricUnit;
import v1.model.agent.metric.Metrics;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Builder
public class OpenAI extends AIModel {
    private static final String CHAT_COMPLETIONS = "https://api.openai.com/v1/chat/completions";
    private static final String COMPLETIONS = "https://api.openai.com/v1/completions";
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String APP_JSON = "application/json";
    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String CHOICES = "choices";
    private static final String MESSAGE = "message";
    private static final String TEXT = "text";
    private static final String ROLE = "role";
    private static final String CONTENT = "content";
    private static final String FINISH_REASON = "finish_reason";
    private static final String PROMPT_TOKENS = "prompt_tokens";
    private static final String COMPLETION_TOKENS = "completion_tokens";
    private static final String TOTAL_TOKENS = "total_tokens";
    private static final String USAGE = "usage";
    private static Gson GSON = new Gson();
    private CloseableHttpClient closeableHttpClient;
    private String key;
    @Builder.Default
    private String defaultModel = null;
    @Builder.Default
    private Double defaultTemperature = null;
    @Builder.Default
    private boolean useChatAsCompletion = false;

    @Override
    public CompletionResponse completion(CompletionRequest completionRequest) {
        if (this.useChatAsCompletion) {
            return this.mapChatCompletionResponseToCompletionResponse(this.chatCompletion(this
                    .mapCompletionRequestToChatCompletionRequest(completionRequest)));
        }

        completionRequest = updateDefaults(completionRequest, this.defaultModel, this.defaultTemperature);
        final HttpPost httpPost = new HttpPost(COMPLETIONS);

        setDefaultHeaders(httpPost, completionRequest.getLanguage(), GSON.toJson(map(completionRequest)));

        try (final CloseableHttpResponse response = closeableHttpClient.execute(httpPost, HttpClientContext.create())) {
            final String responseString = EntityUtils.toString(response.getEntity());
            final JsonObject jsonObject = GSON.fromJson(responseString, JsonObject.class);
            return mapToCompletionResponse(jsonObject);
        } catch (ClientProtocolException e) {
            throw new Generative4jException(e);
        } catch (IOException e) {
            throw new Generative4jException(e);
        }
    }

    @Override
    public ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletionRequest) {
        chatCompletionRequest = updateDefaults(chatCompletionRequest, this.defaultModel, this.defaultTemperature);

        final HttpPost httpPost = new HttpPost(CHAT_COMPLETIONS);
        setDefaultHeaders(httpPost, chatCompletionRequest.getLanguage(), GSON.toJson(map(chatCompletionRequest)));

        try (final CloseableHttpResponse response = closeableHttpClient.execute(httpPost, HttpClientContext.create())) {
            final String responseString = EntityUtils.toString(response.getEntity());
            final JsonObject jsonObject = GSON.fromJson(responseString, JsonObject.class);
            return mapToChatCompletionResponse(jsonObject);
        } catch (ClientProtocolException e) {
            throw new Generative4jException(e);
        } catch (IOException e) {
            throw new Generative4jException(e);
        }
    }

    private void setDefaultHeaders(final HttpPost httpPost, final String language, final String body) {
        httpPost.setHeader(CONTENT_TYPE, APP_JSON);
        httpPost.setHeader(ACCEPT, APP_JSON);
        httpPost.setHeader(AUTHORIZATION, BEARER_PREFIX + key);
        if (language != null) {
            httpPost.setHeader(ACCEPT_LANGUAGE, language);
        }
        httpPost.setEntity(new StringEntity(body,  "UTF-8"));
    }

    private OpenAiChatCompletionRequest map(ChatCompletionRequest chatCompletionRequest) {
        return OpenAiChatCompletionRequest.builder()
                .model(chatCompletionRequest.getModel())
                .messages(chatCompletionRequest.getMessages())
                .temperature(chatCompletionRequest.getTemperature())
                .build();
    }

    private OpenAICompletionRequest map(CompletionRequest completionRequest) {
        return OpenAICompletionRequest.builder()
                .model(completionRequest.getModel())
                .prompt(completionRequest.getPrompt())
                .temperature(completionRequest.getTemperature())
                .build();
    }

    private ChatCompletionResponse mapToChatCompletionResponse(final JsonObject response) {
        final LinkedList<ChatCompletionResponseChoice> choices = new LinkedList<>();
        Optional.ofNullable(response)
                .map(jsonObject -> jsonObject.getAsJsonArray(CHOICES))
                .ifPresent(jsonArray -> jsonArray.forEach(jsonElement -> {
                    ChatCompletionResponseChoice chatCompletionResponseChoice =
                            ChatCompletionResponseChoice.builder()
                                    .message(ChatCompletionMessage.builder()
                                            .role(jsonElement.getAsJsonObject().get(MESSAGE).getAsJsonObject().get(ROLE).getAsString())
                                            .content(jsonElement.getAsJsonObject().get(MESSAGE).getAsJsonObject().get(CONTENT).getAsString())
                                            .build())
                                    .finishReason(jsonElement.getAsJsonObject().get(FINISH_REASON).getAsString())
                                    .build();

                    choices.add(chatCompletionResponseChoice);
                }));

        return ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices
                        .builder()
                        .chatCompletionResponseChoiceList(choices)
                        .build())
                .metrics(Metrics.builder()
                        .metrics(mapMetrics(response))
                        .build())
                .build();
    }

    private CompletionResponse mapToCompletionResponse(final JsonObject response) {
        final LinkedList<CompletionResponseChoice> choices = new LinkedList<>();
        Optional.ofNullable(response)
                .map(jsonObject -> jsonObject.getAsJsonArray(CHOICES))
                .ifPresent(jsonArray -> jsonArray.forEach(jsonElement -> {
                    CompletionResponseChoice completionResponseChoice =
                            CompletionResponseChoice.builder()
                                    .text(jsonElement.getAsJsonObject().get(TEXT).getAsString())
                                    .finishReason(jsonElement.getAsJsonObject().get(FINISH_REASON).getAsString())
                                    .build();

                    choices.add(completionResponseChoice);
                }));

        return CompletionResponse.builder()
                .completionResponseChoices(CompletionResponseChoices
                        .builder()
                        .completionResponseChoiceList(choices)
                        .build())
                .metrics(Metrics.builder()
                        .metrics(mapMetrics(response))
                        .build())
                .build();
    }

    private LinkedList<Metric> mapMetrics(final JsonObject response) {
        final LinkedList<Metric> metrics = new LinkedList<>();
        Optional.ofNullable(response)
                .map(jsonObject -> jsonObject.get(USAGE).getAsJsonObject())
                .map(jsonObject -> jsonObject.get(PROMPT_TOKENS))
                .ifPresent(jsonElement -> {
                    Metric metric = Metric.builder()
                            .component(getClass().getName())
                            .unit(MetricUnit.COUNT)
                            .name(MetricName.PROMPT_TOKEN)
                            .value(jsonElement.getAsDouble())
                            .build();
                    metrics.add(metric);
                });
        Optional.ofNullable(response)
                .map(jsonObject -> jsonObject.get(USAGE).getAsJsonObject())
                .map(jsonObject -> jsonObject.get(COMPLETION_TOKENS))
                .ifPresent(jsonElement -> {
                    Metric metric = Metric.builder()
                            .component(getClass().getName())
                            .unit(MetricUnit.COUNT)
                            .name(MetricName.COMPLETION_TOKEN)
                            .value(jsonElement.getAsDouble())
                            .build();
                    metrics.add(metric);
                });
        Optional.ofNullable(response)
                .map(jsonObject -> jsonObject.get(USAGE).getAsJsonObject())
                .map(jsonObject -> jsonObject.get(TOTAL_TOKENS))
                .ifPresent(jsonElement -> {
                    Metric metric = Metric.builder()
                            .component(getClass().getName())
                            .unit(MetricUnit.COUNT)
                            .name(MetricName.TOTAL_TOKEN)
                            .value(jsonElement.getAsDouble())
                            .build();
                    metrics.add(metric);
                });
        return metrics;
    }

    private ChatCompletionRequest mapCompletionRequestToChatCompletionRequest(CompletionRequest completionRequest) {
        return ChatCompletionRequest.builder()
                .language(completionRequest.getLanguage())
                .model(completionRequest.getModel())
                .temperature(completionRequest.getTemperature())
                .messages(Arrays.asList(ChatCompletionMessage.builder()
                        .role(ChatCompletionRole.USER)
                        .content(completionRequest.getPrompt())
                        .build()))
                .build();
    }

    private CompletionResponse mapChatCompletionResponseToCompletionResponse(ChatCompletionResponse chatCompletionResponse) {
        List<ChatCompletionResponseChoice> chatCompletionResponseChoices =
                chatCompletionResponse.getChatCompletionResponseChoices().getChatCompletionResponseChoiceList();
        ChatCompletionResponseChoice lastChoice = chatCompletionResponseChoices.get(chatCompletionResponseChoices.size()-1);

        return CompletionResponse.builder()
                .completionResponseChoices(CompletionResponseChoices.builder()
                        .completionResponseChoiceList(Arrays.asList(CompletionResponseChoice.builder()
                                        .text(lastChoice.getMessage().getContent())
                                        .finishReason(lastChoice.getFinishReason())
                                .build()))
                        .build())
                .metrics(chatCompletionResponse.getMetrics())
                .build();
    }

    @Builder
    @Getter
    public static class OpenAiChatCompletionRequest {
        String model;
        List<ChatCompletionMessage> messages;
        Double temperature;
    }

    @Builder
    @Getter
    public static class OpenAICompletionRequest {
        String model;
        Double temperature;
        String prompt;
    }
}
