package v1.textsplitter;

import java.util.List;

public interface TextSplitter {
    List<String> split(String String, String regex);
}
