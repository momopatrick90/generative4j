package v1.templatemodel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import v1.aimodel.AIModel;
import v1.model.PromptParameter;
import v1.prompt.PromptTemplate;

import java.util.HashMap;


class TemplateModelTest {

    @Test
    void completion() {
        //Arrange
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final HashMap<String, String> map = new HashMap<>();
        map.put("parameter", "paramValue");
        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.completion(Mockito.anyString())).thenReturn("test message");
        final TemplateModel templateModel = new TemplateModel(aiModel, promptTemplate);

        // Act
        final String result = templateModel.completion(map);

        // Assert
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(aiModel).completion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue(),
                "a prompt containing paramValue");
    }

    @Test
    void completionPromptParameter() {
        //Arrange
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final HashMap<String, String> map = new HashMap<>();
        map.put("parameter", "paramValue");
        final PromptParameter promptParameter = PromptParameter.builder()
                .promptParameters(map)
                .build();

        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.completion(Mockito.anyString())).thenReturn("test message");
        final TemplateModel templateModel = new TemplateModel(aiModel, promptTemplate);


        // Act
        final String result = templateModel.completion(promptParameter);

        // Assert
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(aiModel).completion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue(),
                "a prompt containing paramValue");
    }

    @Test
    void completionKeyValues() {
        //Arrange
        final PromptTemplate promptTemplate = new PromptTemplate("a prompt containing {parameter}");
        final AIModel aiModel = Mockito.mock(AIModel.class);
        Mockito.when(aiModel.completion(Mockito.anyString())).thenReturn("test message");
        final TemplateModel templateModel = new TemplateModel(aiModel, promptTemplate);


        // Act
        final String result = templateModel.completion( "parameter", "paramValue");

        // Assert
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(aiModel).completion(argumentCaptor.capture());
        Assertions.assertEquals(result,"test message");
        Assertions.assertEquals(argumentCaptor.getValue(),
                "a prompt containing paramValue");
    }
}