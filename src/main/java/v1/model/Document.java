package v1.model;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Document {
    String text;
    String source;
}
