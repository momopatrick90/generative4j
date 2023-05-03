package v1.agent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import v1.aimodel.AIModel;
import v1.model.ChatCompletionMessage;
import v1.model.ChatCompletionRequest;
import v1.model.ChatCompletionResponse;
import v1.model.ChatCompletionResponseChoice;
import v1.model.ChatCompletionResponseChoices;
import v1.model.PromptParameter;
import v1.model.PromptTemplate;

import java.util.Arrays;
import java.util.HashMap;


class ModelInvokerTest {

    @Test
    void complete() {
        //Arrange
        final ModelInvoker modelInvoker = new ModelInvoker();
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("parameter", "paramValue");
        final ChatCompletionResponseChoice mockChoice = ChatCompletionResponseChoice.builder()
                .message(ChatCompletionMessage.builder().content("test message").build())
                .build();
        final ChatCompletionResponse mockResponse = ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices.builder()
                        .chatCompletionResponseChoiceList(Arrays.asList(mockChoice))
                        .build())
                .build();
        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.chatCompletion(Mockito.any())).thenReturn(mockResponse);

        // Act
        final String result = modelInvoker.complete(aiModel, promptTemplate, map);

        // Assert
        final ArgumentCaptor<ChatCompletionRequest> argumentCaptor = ArgumentCaptor.forClass(ChatCompletionRequest.class);
        Mockito.verify(aiModel).chatCompletion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue().getMessages().get(0).getContent(),
                "a prompt containing paramValue");
    }

    @Test
    void completePromptParameter() {
        //Arrange
        final ModelInvoker modelInvoker = new ModelInvoker();
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final HashMap<String, Object> map = new HashMap<>();
        map.put("parameter", "paramValue");
        final PromptParameter promptParameter = PromptParameter.builder()
                .promptParameters(map)
                .build();
        final ChatCompletionResponseChoice mockChoice = ChatCompletionResponseChoice.builder()
                .message(ChatCompletionMessage.builder().content("test message").build())
                .build();
        final ChatCompletionResponse mockResponse = ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices.builder()
                        .chatCompletionResponseChoiceList(Arrays.asList(mockChoice))
                        .build())
                .build();
        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.chatCompletion(Mockito.any())).thenReturn(mockResponse);

        // Act
        final String result = modelInvoker.complete(aiModel, promptTemplate, promptParameter);

        // Assert
        final ArgumentCaptor<ChatCompletionRequest> argumentCaptor = ArgumentCaptor.forClass(ChatCompletionRequest.class);
        Mockito.verify(aiModel).chatCompletion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue().getMessages().get(0).getContent(),
                "a prompt containing paramValue");
    }

    @Test
    void completeKeyValues() {
        //Arrange
        final ModelInvoker modelInvoker = new ModelInvoker();
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final ChatCompletionResponseChoice mockChoice = ChatCompletionResponseChoice.builder()
                .message(ChatCompletionMessage.builder().content("test message").build())
                .build();
        final ChatCompletionResponse mockResponse = ChatCompletionResponse.builder()
                .chatCompletionResponseChoices(ChatCompletionResponseChoices.builder()
                        .chatCompletionResponseChoiceList(Arrays.asList(mockChoice))
                        .build())
                .build();
        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.chatCompletion(Mockito.any())).thenReturn(mockResponse);

        // Act
        final String result = modelInvoker.complete(aiModel, promptTemplate, "parameter", "paramValue");

        // Assert
        final ArgumentCaptor<ChatCompletionRequest> argumentCaptor = ArgumentCaptor.forClass(ChatCompletionRequest.class);
        Mockito.verify(aiModel).chatCompletion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue().getMessages().get(0).getContent(),
                "a prompt containing paramValue");
    }
}