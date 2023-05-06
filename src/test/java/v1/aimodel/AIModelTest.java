package v1.aimodel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import v1.model.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIModelTest {

    @Test
    void completionString() {
        // Arrange
        String prompt = "prompt";
        AIModel aiModel = Mockito.mock(AIModel.class);

        CompletionResponse completionResponse = CompletionResponse.builder()
                .completionResponseChoices(CompletionResponseChoices.builder()
                        .completionResponseChoiceList(Arrays.asList(CompletionResponseChoice
                                .builder()
                                        .text("choice1")
                                .build()))
                        .build())
                .build();
        Mockito.when(aiModel.completion(Mockito.anyString()))
                        .thenCallRealMethod();
        Mockito.when(aiModel.completion(Mockito.any(CompletionRequest.class)))
                .thenReturn(completionResponse);

        // Act
        String completionResult = aiModel.completion(prompt);

        // Assert
        ArgumentCaptor<CompletionRequest> completionRequestArgumentCaptor
                = ArgumentCaptor.forClass(CompletionRequest.class);
        Mockito.verify(aiModel).completion(completionRequestArgumentCaptor.capture());

        Assertions.assertEquals("choice1", completionResult);
        Assertions.assertEquals("prompt", completionRequestArgumentCaptor.getValue()
                .getPrompt());
    }

    @Test
    void completion() {
        Assertions.assertThrows(Generative4jException.class,
                () -> new AIModel().completion(CompletionRequest.builder().build()));
    }

    @Test
    void chatCompletionChatMessage() {
        // Arrange
        AIModel aiModel = Mockito.mock(AIModel.class);
        ChatCompletionResponse completionResponse = ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices.builder()

                        .chatCompletionResponseChoiceList(Arrays.asList(ChatCompletionResponseChoice.builder()
                                        .message(ChatCompletionMessage.builder()
                                                .content("choice1")
                                                .build())
                                .build()))
                        .build())
                        .build();
        Mockito.when(aiModel.chatCompletion(Mockito.anyListOf(ChatCompletionMessage.class)))
                .thenCallRealMethod();
        Mockito.when(aiModel.chatCompletion(Mockito.any(ChatCompletionRequest.class)))
                .thenReturn(completionResponse);


        // Act
        ChatCompletionMessage chatCompletionMessage = aiModel.chatCompletion(Arrays.asList(ChatCompletionMessage.builder()
                        .content("prompt")
                .build()));

        // Assert
        ArgumentCaptor<List> chatCompletionMessageArgumentCaptor
                = ArgumentCaptor.forClass(List.class);
        Mockito.verify(aiModel).chatCompletion(chatCompletionMessageArgumentCaptor.capture());

        Assertions.assertEquals("choice1", chatCompletionMessage.getContent());
        Assertions.assertEquals("prompt", ((ChatCompletionMessage)chatCompletionMessageArgumentCaptor.getValue()
                .get(0)).getContent());
    }


    @Test
    void chatCompletion() {
        Assertions.assertThrows(Generative4jException.class,
                () -> new AIModel().chatCompletion(ChatCompletionRequest.builder().build()));
    }

    @Test
    void updateDefaultsCompletionOverrideValues_Completion() {
        // Arrange
        String defaultModel = "defaultModel";
        Double defaultTemperature = 0.5;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("test prompt")
                .build();
        AIModel aiModel = new AIModel();

        // Act
        CompletionRequest result = aiModel.updateDefaults(completionRequest, defaultModel, defaultTemperature);

        // Assert
        assertEquals(defaultModel, result.getModel());
        assertEquals(defaultTemperature, result.getTemperature());
    }

    @Test
    void updateDefaultsCompletionRequestDoesNotOverrideValues_Completion() {
        // Arrange
        String defaultModel = "defaultModel";
        Double defaultTemperature = 0.5;
        String specifiedModel = "specifiedModel";
        Double specifiedTemperature = 0.8;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("test prompt")
                .model(specifiedModel)
                .temperature(specifiedTemperature)
                .build();
        AIModel aiModel = new AIModel();

        // Act
        CompletionRequest result = aiModel.updateDefaults(completionRequest, defaultModel, defaultTemperature);

        // Assert
        assertEquals(specifiedModel, result.getModel());
        assertEquals(specifiedTemperature, result.getTemperature());
    }


    @Test
    void completionUpdateDefaultsCompletionOverrideValues_ChatCompletion() {
        // Arrange
        String defaultModel = "defaultModel";
        Double defaultTemperature = 0.5;
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .build();
        AIModel aiModel = new AIModel();

        // Act
        ChatCompletionRequest result = aiModel.updateDefaults(completionRequest, defaultModel, defaultTemperature);

        // Assert
        assertEquals(defaultModel, result.getModel());
        assertEquals(defaultTemperature, result.getTemperature());
    }

    @Test
    void uefaultsCompletionRequestDoesNotOverrideValues_ChatCompletion() {
        // Arrange
        String defaultModel = "defaultModel";
        Double defaultTemperature = 0.5;
        String specifiedModel = "specifiedModel";
        Double specifiedTemperature = 0.8;
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(specifiedModel)
                .temperature(specifiedTemperature)
                .build();
        AIModel aiModel = new AIModel();

        // Act
        ChatCompletionRequest result = aiModel.updateDefaults(completionRequest, defaultModel, defaultTemperature);

        // Assert
        assertEquals(specifiedModel, result.getModel());
        assertEquals(specifiedTemperature, result.getTemperature());
    }
}