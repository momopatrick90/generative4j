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
import v1.model.agent.AgentModel;
import v1.model.agent.metric.Metric;
import v1.model.agent.metric.MetricName;
import v1.model.agent.metric.MetricUnit;
import v1.model.agent.metric.Metrics;
import v1.prompt.PromptTemplateRenderer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
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
    private String defaultModel;
    private Boolean useChatAsCompletion;

    @Override
    public CompletionResponse completion(CompletionRequest toolRequest) {
        if (this.useChatAsCompletion) {

        }
        return null;
    }

    @Override
    public ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletionRequest) {
        final HttpPost httpPost = new HttpPost(CHAT_COMPLETIONS);
        httpPost.setHeader(CONTENT_TYPE, APP_JSON);
        httpPost.setHeader(ACCEPT, APP_JSON);
        httpPost.setHeader(AUTHORIZATION, BEARER_PREFIX + key);

        if (chatCompletionRequest.getLanguage() != null) {
            httpPost.setHeader(ACCEPT_LANGUAGE, chatCompletionRequest.getLanguage());
        }

        chatCompletionRequest = updateModelIfNotPresent(chatCompletionRequest);

        final StringEntity stringEntity = new StringEntity(GSON.toJson(map(chatCompletionRequest)),  "UTF-8");
        httpPost.setEntity(stringEntity);


        try (final CloseableHttpResponse response = closeableHttpClient.execute(httpPost, HttpClientContext.create())) {
            final String responseString = EntityUtils.toString(response.getEntity());
            final JsonObject jsonObject = GSON.fromJson(responseString, JsonObject.class);
            return map(jsonObject);
        } catch (ClientProtocolException e) {
            throw new Generative4jException(e);
        } catch (IOException e) {
            throw new Generative4jException(e);
        }
    }

    private ChatCompletionRequest updateModelIfNotPresent(ChatCompletionRequest chatCompletionRequest) {
        if (chatCompletionRequest.getModel() == null) {
            return chatCompletionRequest.toBuilder()
                    .model(this.defaultModel)
                    .build();
        }
        return chatCompletionRequest;
    }

    private ChatGPTRequest map(ChatCompletionRequest chatCompletionRequest) {
        return ChatGPTRequest.builder()
                .model(chatCompletionRequest.getModel())
                .messages(chatCompletionRequest.getMessages())
                .temperature(chatCompletionRequest.getTemperature())
                .build();
    }

    private ChatCompletionResponse map(final JsonObject response) {
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

        return ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices
                        .builder()
                        .choices(choices)
                        .build())
                .metrics(Metrics.builder()
                        .metrics(metrics)
                        .build())
                .build();
    }

    @Builder
    @Getter
    public static class ChatGPTRequest {
        String model;
        List<ChatCompletionMessage> messages;
        Double temperature;
    }
}
