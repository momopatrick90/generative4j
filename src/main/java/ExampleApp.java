import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.model.ChatCompletionMessage;
import v1.model.ChatCompletionRole;
import v1.prompt.PromptTemplate;
import v1.summarize.SequentialSummarizer;
import v1.summarize.Summarizer;
import v1.templatemodel.TemplateModel;
import v1.textsplitter.CharacterSplitter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExampleApp {
    public static void main(String[] args) {
        final AIModel aiModel = OpenAI.builder()
                .defaultModel(OpenAI.GPT_35_TURBO)
                .useChatAsCompletion(true)              // GPT_35_TURBO does not completion api, but is cheaper.
                .key(System.getenv("OPENAI_KEY"))
                .build();

        final String completion = aiModel.completion("What is the capital of france?");
        System.out.println("Completion:     " + completion);
        System.out.println("\n\n\n");


        final ChatCompletionMessage chatCompletionMessage = aiModel.chatCompletion(Arrays.asList(ChatCompletionMessage.builder()
                        .role(ChatCompletionRole.SYSTEM)
                        .content("You are a journalist")
                        .build(),
                ChatCompletionMessage.builder()
                        .role(ChatCompletionRole.USER)
                        .content("Write a story on Paris")
                        .build()
        ));
        System.out.println("ChatCompletion Role:        " + chatCompletionMessage.getRole());
        System.out.println("ChatCompletion Text:        " + chatCompletionMessage.getContent());
        System.out.println("\n\n\n");


        final PromptTemplate promptTemplate = PromptTemplate.builder()
                .text("My name is {name} and i come form {country}")
                .build();
        System.out.println("Template format with kv:     " + promptTemplate.format("name", "Patson", "country", "Canada"));

        final HashMap<String, String> kv = new HashMap<>();
        kv.put("name", "Patson");
        kv.put("country", "Canada");
        System.out.println("Template format with map:     " + promptTemplate.format(kv));
        System.out.println("\n\n\n");


        final PromptTemplate bornTemplate = PromptTemplate.builder()
                .text("Where was {name} born?")
                .build();
        final TemplateModel bornTemplateModel = TemplateModel.builder()
                .aiModel(aiModel)
                .promptTemplate(bornTemplate)
                        .build();

        String result1 = bornTemplateModel.completion("name", "Obama");
        String result2 = bornTemplateModel.completion("name", "Gandhi");
        System.out.println("Template model result Obama:  " + result1);
        System.out.println("Template model result Gandhi: " + result2);
        System.out.println("\n\n\n");


        // This will split the character, while trying to chunkSize 55.
        final CharacterSplitter characterSplitter = new CharacterSplitter(55);
        final List<String> chunks =  characterSplitter.split("Paris the city of love fashion art and culture. It is the place that most people dream of visiting at least once in their life",
                Arrays.asList(". "));
        System.out.println("Splitting using `. `   " + chunks);

        final List<String> chunks2 =  characterSplitter.split("Paris the city of love fashion art and culture. It is the place that most people dream of visiting at least once in their life",
                Arrays.asList(". ", " "));
        System.out.println("Splitting using `. `, ` `  " + chunks2);
        System.out.println("\n\n\n");

        String summarizingPrompt = "";
        Summarizer summarizer = SequentialSummarizer.builder()
                .templateModel(TemplateModel.builder()
                        .promptTemplate(PromptTemplate.builder()
                                .text("summarizingPrompt")
                                .build())
                        .aiModel(aiModel)
                        .build())
                .build();

       // String text = aiModel.completion("Write a story on Paris?");
        //System.out.println(text);
    }
}
