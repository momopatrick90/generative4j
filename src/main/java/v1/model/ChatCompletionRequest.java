package v1.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class ChatCompletionRequest {
    String model;
    List<ChatCompletionMessage> messages;
    Double temperature;
    String language;
}
