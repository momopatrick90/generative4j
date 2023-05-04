package v1.agent;

import com.google.gson.Gson;
import lombok.Builder;
import v1.aimodel.AIModel;
import v1.model.ChatCompletionMessage;
import v1.model.ChatCompletionRequest;
import v1.model.ChatCompletionResponse;
import v1.model.ChatCompletionResponseChoice;
import v1.model.ChatCompletionRole;
import v1.model.Generative4jException;
import v1.model.PromptParameter;
import v1.model.PromptTemplate;
import v1.model.agent.AgentState;
import v1.model.agent.Tool;
import v1.prompt.PromptTemplateRenderer;
import v1.transformer.AIModelOutputToTool;
import v1.utils.ListUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class ModelInvoker {
    private static Gson GSON = new Gson();

    public String complete(final AIModel aiModel, final PromptTemplate promptTemplate, final String... keyValuePairs) {
        return complete(aiModel, promptTemplate, ListUtils.keyValuesToMapObject(keyValuePairs));
    }

    public String complete(final AIModel aiModel, final PromptTemplate promptTemplate, final PromptParameter promptParameter) {
        return complete(aiModel, promptTemplate, promptParameter.getPromptParameters());
    }

    public String complete(final AIModel aiModel, final PromptTemplate promptTemplate, final Map<String, Object> parameters) {
        final String prompt = PromptTemplateRenderer.format(promptTemplate,
                parameters);
        final ChatCompletionMessage chatCompletionMessage = ChatCompletionMessage
                .builder()
                .role(ChatCompletionRole.USER)
                .content(prompt)
                .build();

        final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(Arrays.asList(chatCompletionMessage))
                .build();

        final ChatCompletionResponse chatCompletionResponse =
                aiModel.chatCompletion(chatCompletionRequest);

        final List<ChatCompletionResponseChoice> choices = chatCompletionResponse
                .getChatCompletionResponseChoices().getChatCompletionResponseChoiceList();
        return choices.get(choices.size()-1).getMessage().getContent();
    }

    // https://github.com/hwchase17/langchain/blob/master/langchain/agents/mrkl/base.py
    public static void executeMRKL(final AIModel aiModel, final PromptTemplate promptTemplate,
                                 final List<Tool> tools, final AIModelOutputToTool aiModelOutputToTool,
                                 final String... kvPairs) {

    }
    /**
    public Object runAuto(final AgentRequest agentRequest,
                    final Consumer<AgentState> preActionCallBack,
                    final Consumer<AgentState> postActionCallBack) {
        final AgentState agentState = initialiseAgentState(agentRequest);

        while (true) {
            if (preActionCallBack != null)
                preActionCallBack.accept(agentState);

            final Action nextAction = agentState.getNextActions().remove(0);

            if (TaskType.CALL_MODEL.equals(nextAction.getType())) {
                callModel(nextAction, agentState);
            } else if (TaskType.CALL_TOOL.equals(nextAction.getType())) {
                callTool(nextAction, agentState);
            } else if (TaskType.STOP.equals(nextAction.getType())) {
                return nextAction.getResponse();
            }

            if (agentState.getNextActions().size() == 0) {
                return agentState.getLatestToolResponse();
            }

            agentState.getPreviousActions().add(nextAction);

            if (postActionCallBack != null)
                postActionCallBack.accept(agentState);
        }
    }

    private void callTool(Action action, AgentState agentState) {
        MetricUtils.addMetric(agentState.getMetrics(), Metric
                .builder()
                .name(MetricName.TOOL_CALL)
                .component(action.getComponentName())
                .value(1d)
                .unit(MetricUnit.COUNT)
                .build());

        final Tool tool = agentDefinition.getTools().stream()
                .filter(t -> action.getComponentName().equals(t.getName()))
                .findFirst()
                .get();

        Object response = null;
        if (tool instanceof LocalTool) {
            final LocalTool localTool = (LocalTool) tool;
            response = localTool.invoke(action.getInput());
        } else {
            // TODO remote tools;
        }

        updateParameter(agentState, action.getUpdateParameter(), response);
        action.setResponse(response);
        agentState.setLatestToolResponse(response);
    }

    private void callModel(Action action, AgentState agentState) {
        final AgentModel agentModel = agentDefinition.getModels().stream()
                .filter(t -> action.getComponentName().equals(t.getName()))
                .findFirst()
                .get();

        MetricUtils.addMetric(agentState.getMetrics(), Metric
                .builder()
                .name(MetricName.MODEL_CALL)
                .component(agentModel.getName())
                .value(1d)
                .unit(MetricUnit.COUNT)
                .build());

        String text = aiModel.call(agentModel, agentState.getParameters());

        action.setResponse(text);
        agentState.setLatestModelResponse(text);
        updateParameter(agentState, action.getUpdateParameter(), text);

        if (agentState.getAgentRequest().getAutonomous()) {
            agentState.getNextActions().addAll(GSON.fromJson(text, ArrayList.class));
        }
    }


    private AgentState initialiseAgentState(final AgentRequest agentRequest) {
        final Action defaultInitialAction = Action
                .builder()
                .type(TaskType.CALL_MODEL)
                .build();
        final LinkedList<Action> actions = agentRequest.getInitialActions() == null ?
                new LinkedList<>(Arrays.asList(defaultInitialAction)) : new LinkedList<>(agentRequest.getInitialActions());


        final Map<String, Object> parametersMap;
        if (agentRequest.getPromptParameter() == null) {
            parametersMap = new HashMap<>();
        } else {
            parametersMap = new HashMap<>();
            Optional.ofNullable(agentRequest.getPromptParameter().getPromptParameters())
                    .ifPresent(map -> map.forEach((key, value) -> parametersMap.put(key, value)));
        }


        return AgentState
                .builder()
                .agentRequest(agentRequest)
                .parameters(parametersMap)
                .nextActions(actions)
                .metrics(Metrics.builder()
                        .metrics(new LinkedList<>())
                        .build())
                .startEpochMillis((double)Instant.now().toEpochMilli())
                .build();
    }*/

    // TODO utils
    private void updateParameter(AgentState agentState, String parameter, Object value) {
        if (parameter!= null) {
            agentState.getParameters().put(parameter, value);
        }
    }
}
