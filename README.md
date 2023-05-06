### Overview
Generative4j java library that helps you: 
* Integrate with ai/ml models, currently on OpenAI GPT is supported.
* Use, extend and create PromptTemplates.
* Perform text splitting.
* Use, extend and create Summarizers.
* Use, extend and create AI Agents and tools.

### Installing
* Todo

### Sample creation of AIModel for OpenAI.
```java
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
public class ExampleApp {
  public static void main(String[] args) {
    final AIModel aiModel = OpenAI.builder()
            .closeableHttpClient(HttpClientBuilder.create().build())
            .defaultModel(OpenAI.TEXT_DAVINCI_003)
            .key("API_KEY")
            .build();
    String text = aiModel.completion("What is the capital of france?");
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
public class ExampleApp {
  public static void main(String[] args) {
    PromptTemplate promptTemplate = PromptTemplate.builder()
            .text("My name is {name} and i come form {country}")
            .build();
    System.out.println("Rendered with kv: " + promptTemplate.format("name", "Patson", "country", "Canada"));

    HashMap<String, String> kv = new HashMap<>();
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

### TextSplitting
```java
public class ExampleApp {
  public static void main(String[] args) {
    PromptTemplate promptTemplate = PromptTemplate.builder()
            .text("My name is {name} and i come form {country}")
            .build();
    System.out.println("Rendered with kv: " + promptTemplate.format("name", "Patson", "country", "Canada"));

    HashMap<String, String> kv = new HashMap<>();
    kv.put("name", "Patson");
    kv.put("country", "Canada");
    System.out.println("Rendered with map: " + promptTemplate.format(kv));
  }
}
```

# TODO 
* Localization
* Async
  * stream https://platform.openai.com/docs/api-reference/chat/create
* Easy tool integration
* Easy LLM integration
* TTI/ITI integration
* Cost/metric/usage https://platform.openai.com/docs/guides/embeddings/what-are-embeddings
* Remove dependencies
* API https://platform.openai.com/docs/api-reference/chat/create
