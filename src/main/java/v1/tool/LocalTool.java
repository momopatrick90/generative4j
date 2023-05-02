package v1.tool;

import lombok.Builder;
import lombok.Getter;
import v1.model.agent.Tool;

@Getter
public abstract class LocalTool extends Tool {
    public abstract Object invoke(Object object);
}
