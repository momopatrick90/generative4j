package v1.model.agent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Action {
    Object input;
    Object response;
    String type;
    String componentName;
    String updateParameter;
}
