package v1.model;

import lombok.Data;

// https://platform.openai.com/docs/api-reference/completions/create
// https://console.anthropic.com/docs/api/reference
@Data
public class CompletionRequest {
    String model;
    Double temperature;
    String prompt;
}
