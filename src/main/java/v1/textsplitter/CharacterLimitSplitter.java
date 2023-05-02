package v1.textsplitter;

import lombok.AllArgsConstructor;
import v1.model.Generative4jException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@AllArgsConstructor
public class CharacterLimitSplitter implements TextSplitter{
    int chunkLimit;


    @Override
    public List<String> split(String text, String regex) {
        if (chunkLimit >= text.length()) {
            return Arrays.asList(text);
        }

        if (chunkLimit <= 0) {
            throw new Generative4jException("Chunk limit for split should be larger than 0, actual: " + chunkLimit);
        }

        final LinkedList<String> result = new LinkedList<>();

        int lastSplitIndex = -1;
        int lastStart = 0;
        for(int i = 0; i < text.length(); i++) {
            if (Pattern.matches(regex, text.substring(i, i+1))) {
                lastSplitIndex = i;
            }

            if ((chunkLimit == i - lastStart)) {

                if (lastSplitIndex > 0) {
                    result.add(text.substring(lastStart, lastSplitIndex+1));
                    lastStart = lastSplitIndex+1;
                    i = lastSplitIndex;
                    lastSplitIndex = -1;
                } else {
                    result.add(text.substring(lastStart, i));
                    lastStart = i;
                }
            }

            if (i == text.length()-1) {
                result.add(text.substring(lastStart));
            }
        }

        return result;
    }

}
