package v1.model.agent.metric;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class Metric {
    String name;
    String unit;
    Double value;
    String component; // component emptiing the metircs e.g. OpenAI, e.g GoogleSerp
}
