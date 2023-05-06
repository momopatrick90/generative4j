package v1.model.metric;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Metrics {
    List<Metric> metrics;
}
