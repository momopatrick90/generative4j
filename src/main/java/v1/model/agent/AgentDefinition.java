package v1.model.agent;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class AgentDefinition {
    String language6391;
    List<Tool> tools;
    List<AgentModel> models;
}
