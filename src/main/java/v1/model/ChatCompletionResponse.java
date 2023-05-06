package v1.model;

import lombok.Builder;
import lombok.Getter;
import v1.model.metric.Metrics;

@Builder
@Getter
public class ChatCompletionResponse {
    Metrics metrics;
    ChatCompletionResponseChoices chatCompletionResponseChoices;
}
