package v1.aimodel;

import v1.model.ChatCompletionRequest;
import v1.model.ChatCompletionResponse;
import v1.model.CompletionRequest;
import v1.model.CompletionResponse;
import v1.model.agent.AgentModel;

import java.util.Map;

public abstract class AIModel {
    public abstract CompletionResponse completion(CompletionRequest toolRequest);
    public abstract ChatCompletionResponse chatCompletion(ChatCompletionRequest chatCompletionRequest);
}
