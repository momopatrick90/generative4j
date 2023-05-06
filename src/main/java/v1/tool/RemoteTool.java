package v1.tool;

import lombok.Builder;
import lombok.Getter;

/**
 * Create a tool based on OpenAI plugins https://platform.openai.com/docs/plugins/introduction.
 */

@Getter
public abstract class RemoteTool extends Tool {
    public abstract Object invoke(Object object);
}
