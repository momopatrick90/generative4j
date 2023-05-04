package v1.textsplitter;

import lombok.AllArgsConstructor;
import v1.model.Generative4jException;
import v1.utils.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

@AllArgsConstructor
public class CharacterLimitSplitter implements TextSplitter{
    int chunkLimit;
    // TODO split window size, make sure split takes into account largest window

    @Override
    public List<String> split(String text, List<String> splits) {
        if (chunkLimit >= text.length()) {
            return Arrays.asList(text);
        }

        // `\n\n` should match before `\n`
        final ArrayList<String> splitsReverseSorted = new ArrayList<>(splits);
        splitsReverseSorted.sort(Comparator.comparing(s -> -s.length()));

        if (chunkLimit <= 0) {
            throw new Generative4jException("Chunk limit for split should be larger than 0, actual: " + chunkLimit);
        }

        final LinkedList<String> result = new LinkedList<>();

        int lastSplitIndex = -1;
        int lastStart = 0;
        for(int i = 0; i < text.length(); i++) {
            for(final String split: splitsReverseSorted) {
                final String toCompare = StringUtils.subStringCheckbounds(text, i, i+split.length());
                if (split.equals(toCompare)) {
                    lastSplitIndex = i + split.length() - 1;
                    break;
                }
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
