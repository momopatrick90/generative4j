package v1.model.agent;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import v1.model.agent.metric.Metrics;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class AgentState {
    String agentName;
    AgentRequest agentRequest;
    Double startEpochMillis;
    Metrics metrics;
    Map<String, Object> parameters;
    Memory memory;
    List<Action> nextActions;
    List<Action> previousActions;
    Object latestModelResponse;
    Object latestToolResponse;
}
