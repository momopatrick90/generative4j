package v1.model.agent;

import lombok.Builder;
import lombok.Getter;
import v1.model.ToolRequest;
import v1.model.ToolResponse;


/**
 * Lang chain use tool to interact with outside world, similar to OpenAi plugins.
 *
 * @author Not You Business
 * @version v1
 * @since 2023-04-28
 */
@Getter
public class Tool {
    String name;
    String description;

    /**
     * If its a local tool, descript how its should be invoked
     */
    String localToolDescription;
}
