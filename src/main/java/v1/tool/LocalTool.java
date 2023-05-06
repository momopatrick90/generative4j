package v1.tool;

import lombok.Builder;
import lombok.Getter;


@Getter
public abstract class LocalTool extends Tool {
    public abstract Object invoke(Object object);
}
