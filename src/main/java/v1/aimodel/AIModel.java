package v1.aimodel;

import v1.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class AIModel {
    public String completion(String prompt) {
        final CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .build();

        final CompletionResponse completionResponse = this.completion(completionRequest);

        final List<CompletionResponseChoice> choices = completionResponse
                .getCompletionResponseChoices().getCompletionResponseChoiceList();
        return choices.get(choices.size()-1).getText();
    }

    public CompletionResponse completion(CompletionRequest toolRequest) {
        throw new Generative4jException("Not Implemented");
    }

    public ChatCompletionMessage chatCompletion(List<ChatCompletionMessage> chatCompletionMessages) {
        final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(new ArrayList<>(chatCompletionMessages))
                .build();

        final ChatCompletionResponse chatCompletionResponse =
                this.chatCompletion(chatCompletionRequest);

        List<ChatCompletionResponseChoice> choices = chatCompletionResponse
                .getChatCompletionResponseChoices()
                .getChatCompletionResponseChoiceList();


        return choices.get(choices.size()-1)
                .getMessage();
    }

    public ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletionRequest) {
        throw new Generative4jException("Not Implemented");
    }

    protected ChatCompletionRequest updateDefaults(ChatCompletionRequest chatCompletionRequest, String defaultModel, Double defaultTemperature) {
        final String model = chatCompletionRequest.getModel() != null ? chatCompletionRequest.getModel() : defaultModel;
        final Double temperature = chatCompletionRequest.getTemperature() != null? chatCompletionRequest.getTemperature() : defaultTemperature;

        return chatCompletionRequest.toBuilder()
                .model(model)
                .temperature(temperature)
                .build();
    }

    protected CompletionRequest updateDefaults(CompletionRequest completionRequest, String defaultModel, Double defaultTemperature) {
        final String model = completionRequest.getModel() != null ? completionRequest.getModel() : defaultModel;
        final Double temperature = completionRequest.getTemperature() != null? completionRequest.getTemperature() : defaultTemperature;
        return completionRequest.toBuilder()
                .model(model)
                .temperature(temperature)
                .build();
    }
}
