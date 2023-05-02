package v1.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChatCompletionResponseChoices {
    List<ChatCompletionResponseChoice> choices;
}
