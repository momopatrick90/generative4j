package v1.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

// https://platform.openai.com/docs/api-reference/completions/create
// https://console.anthropic.com/docs/api/reference

@Builder(toBuilder = true)
@Getter
public class CompletionRequest {
    String model;
    Double temperature;
    String prompt;
    String language;
}
