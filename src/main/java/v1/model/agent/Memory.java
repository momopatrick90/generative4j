package v1.model.agent;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class Memory {
    Map<String, Object> parameters;
}
