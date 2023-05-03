package v1.model;

import lombok.Builder;
import lombok.Getter;
import v1.model.agent.metric.Metric;
import v1.model.agent.metric.Metrics;

import java.util.List;

@Builder
@Getter
public class CompletionResponse {
    CompletionResponseChoices completionResponseChoices;
    Metrics metrics;
}
