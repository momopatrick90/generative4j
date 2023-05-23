package v1.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class Document {
    String text;
    Map<String, Object> meta;
}
