package Model;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public Message(String command, Map<String, String> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    private String command;
    private Map<String,String> parameters = new HashMap<>();
}