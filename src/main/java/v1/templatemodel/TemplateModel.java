package v1.templatemodel;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import v1.aimodel.AIModel;
import v1.model.PromptParameter;
import v1.prompt.PromptTemplate;
import v1.transformer.AIModelOutputToTool;
import v1.utils.ListUtils;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class TemplateModel {
    private static Gson GSON = new Gson();
    final AIModel aiModel;
    final PromptTemplate promptTemplate;

    public String completion(final String... keyValuePairs) {
        return completion(ListUtils.keyValuesToMap(keyValuePairs));
    }

    public String completion(final PromptParameter promptParameter) {
        return completion(promptParameter.getPromptParameters());
    }

    public String completion(final Map<String, String> parameters) {
        final String prompt = promptTemplate.format(parameters);
        return aiModel.completion(prompt);
    }

}
