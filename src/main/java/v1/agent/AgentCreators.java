package v1.agent;

import v1.model.agent.AgentDefinition;
import v1.model.agent.AgentState;

import java.util.Map;
import java.util.function.Function;

// samples https://python.langchain.com/en/latest/modules/agents/agents/custom_llm_agent.html

public class AgentCreators {
    public static void createNoParamterAgent() {

    }



    public static void createAutonomousAgent() {

    }

    public static void createAgent(final AgentDefinition agentDefinition,
                                   final Function<AgentState, Object> preActionCallBack,
                                   final Function<AgentState, Object> postActionCallBack,
                                   final Map<String, Function<Object, Object>> localToolMap) {


    }

    public static void createSummarizingAgent() {

    }


    public static void createSimpleAgent() {

    }
}
