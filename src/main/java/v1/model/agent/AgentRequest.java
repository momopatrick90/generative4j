package v1.model.agent;

import lombok.Builder;
import lombok.Getter;
import v1.model.PromptParameter;

import java.util.List;

@Builder
@Getter
public class AgentRequest {
    PromptParameter promptParameter;
    List<Action> initialActions;
    Boolean autonomous;
}
