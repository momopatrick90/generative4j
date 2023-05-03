package v1.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompletionResponseChoice {
    String text;
    String finishReason;
}
