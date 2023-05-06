### Overview
Generative4j java library that helps you: 
* Integrate with ai/ml models, currently on OpenAI GPT is supported.
* Use, extend and create PromptTemplates.
* Perform text splitting.
* Use, extend and create Summarizers.
* Use, extend and create AI Agents and tools.


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

## PromptTemplates




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
