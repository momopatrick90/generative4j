### Overview
Generative4j is java library (similar to langchain) that helps you: 
* Integrate and make calls ai/ml models, currently only OpenAI GPT models are supported.
* Use, extend and create PromptTemplates.
* Perform text splitting.
* Use, extend and create Summarizers.
* Use, extend and create AI Agents and tools. [WIP]

### Installing
* Add to you pom.cml
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://www.jitpack.io</url>
</repository>


<dependency>
      <groupId>com.github.momopatrick90</groupId>
      <artifactId>generative4j</artifactId>
      <version>1.0-SNAPSHOT</version>
</dependency>
```



### Sample creation of AIModel for OpenAI.
```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.TEXT_DAVINCI_003)
            .key("API_KEY")
            .build();
  }
}
```

### Calling the completion api.

```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.TEXT_DAVINCI_003)
            .key("API_KEY")
            .build();
    final String text = aiModel.completion("What is the capital of france?");
    System.out.println(text);
  }
}
```

#### Output
```text
The capital of France is Paris.
```

### Calling the chat completion api. 

```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.model.ChatCompletionMessage;
import v1.model.ChatCompletionRole;

public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.GPT_35_TURBO)
            .key("API_KEY")
            .build();
    final ChatCompletionMessage chatCompletionMessage = aiModel.chatCompletion(Arrays.asList(ChatCompletionMessage.builder()
                    .role(ChatCompletionRole.SYSTEM)
                    .content("You are a journalist")
                    .build(),
            ChatCompletionMessage.builder()
                    .role(ChatCompletionRole.USER)
                    .content("Write a story on Paris")
                    .build()
    ));

    System.out.println(chatCompletionMessage.getRole());
    System.out.println(chatCompletionMessage.getContent());
  }
}
```

#### Output
```text
Paris, the city of love, fashion, art, and culture. It is the place that most people dream of visiting at least once in their life...
```


### PromptTemplates
```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;
import v1.textsplitter.CharacterSplitter;

public class ExampleApp {
  public static void main(String[] args) {
    final PromptTemplate promptTemplate = PromptTemplate.builder()
            .text("My name is {name} and i come form {country}")
            .build();
    System.out.println("Rendered with kv: " + promptTemplate.format("name", "Patson", "country", "Canada"));

    final HashMap<String, String> kv = new HashMap<>();
    kv.put("name", "Patson");
    kv.put("country", "Canada");
    System.out.println("Rendered with map: " + promptTemplate.format(kv));
  }
}
```
#### Output
```text
Rendered with kv: My name is Patson and i come form Canada
Rendered with map: My name is Patson and i come form Canada
```

### TemplateModel
TemplateModel combine templates and models.

```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;
import v1.textsplitter.CharacterSplitter;

public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.TEXT_DAVINCI_003)
            .key("API_KEY")
            .build();
    
    final PromptTemplate bornTemplate = PromptTemplate.builder()
            .text("Where was  {name} born?")
            .build();

    final TemplateModel bornTemplateModel = TemplateModel.builder()
            .aiModel(aiModel)
            .promptTemplate(bornTemplate)
            .build();

    String result1 = bornTemplateModel.completion("name", "Obama");
    String result2 = bornTemplateModel.completion("name", "Gandhi");
    System.out.println("Result Obama:  " + result1);
    System.out.println("Result Gandhi: " + result2);
  }
}
```
#### Output
```text
Result Obama:  Barack Obama was born in Honolulu, Hawaii, United States.
Result Gandhi: Mohandas Karamchand Gandhi was born in Porbandar, a coastal town in present-day Gujarat, India on October 2, 1869.
```



### TextSplitting
```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;
import v1.textsplitter.CharacterSplitter;

public class ExampleApp {
  public static void main(String[] args) {
    // This will split the character, while trying to chunkSize 55.
    final CharacterSplitter characterSplitter = new CharacterSplitter(55);
    final List<String> chunks =  characterSplitter.split("Paris the city of love fashion art and culture. It is the place that most people dream of visiting at least once in their life",
            Arrays.asList(". "));
    System.out.println("Splitting using `. `   " + chunks);

    final List<String> chunks2 =  characterSplitter.split("Paris the city of love fashion art and culture. It is the place that most people dream of visiting at least once in their life",
            Arrays.asList(". ", " "));
    System.out.println("Splitting using `. `, ` `  " + chunks2);
  }
}
```

#### Output
```text
Splitting using `. `   [Paris the city of love fashion art and culture. , It is the place that most people dream of visiting at l, east once in their life]
Splitting using  `. `, ` `  [Paris the city of love fashion art and culture. It is , the place that most people dream of visiting at least , once in their life]
```

### Summarization
```java
import v1.aimodel.AIModel;
import v1.aimodel.OpenAI;
import v1.prompt.PromptTemplate;
import v1.templatemodel.TemplateModel;
import v1.textsplitter.CharacterSplitter;

public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.TEXT_DAVINCI_003)
            .key("API_KEY")
            .build();

    final String largeText = new String(Files.readAllBytes(Paths.get(Thread.currentThread().getContextClassLoader().getResource("prompteng.txt").toURI())));
    final List<String> splitText = CharacterSplitter.builder()
            .chunkSize(500)
            .build()
            .split(largeText, Arrays.asList(". "));
    Summarizer summarizer = SequentialSummarizer.createDefault(aiModel);
    String summarized = summarizer.summarize(splitText);
    System.out.println("Summarized largeText  " + summarized);
  }
}
```

#### Output
```text
Summarized largeText  OpenAI advocates for precision and careful analysis in text analysis to avoid accuracy issues caused by language model limitations. Breaking down tasks, using relevant techniques and quotes from source documents can reduce unrealistic information in analysis. The video emphasizes the completion of guidelines for prompting and encourages progression toward an iterative prompt development process.
```

# TODO
* Add Agents and Tools
  * AGIS
* Add Java doc / Tutorial
* Add Async
  * stream https://platform.openai.com/docs/api-reference/chat/create
* Easy LLM integration
* Add Vector support and storing
* More providers (Hugging, Cohere, LLama, etc)
* Patterns, chat with data, retrieval, search
* Improve modality
