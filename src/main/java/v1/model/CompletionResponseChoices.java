package v1.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CompletionResponseChoices {
    List<CompletionResponseChoice> completionResponseChoiceList;
}
