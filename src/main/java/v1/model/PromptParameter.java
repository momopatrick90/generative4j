package v1.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class PromptParameter {
    Map<String, String> promptParameters;
}
