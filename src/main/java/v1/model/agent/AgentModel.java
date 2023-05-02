package v1.model.agent;

import lombok.Builder;
import lombok.Getter;
import v1.model.PromptTemplate;

@Getter
@Builder
public class AgentModel {
    String name;
    PromptTemplate executorPrompt;
}
