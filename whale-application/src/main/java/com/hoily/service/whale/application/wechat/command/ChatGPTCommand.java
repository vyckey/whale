package com.hoily.service.whale.application.wechat.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Data;

import java.util.List;

/**
 * ChatGPT command
 *
 * @author vyckey
 */
@Data
@Parameters(commandNames = "chatgpt", commandDescription = "ChatGPT commands")
public class ChatGPTCommand {
    @Parameter(names = {"--chat", "-c"}, description = "enable chat mode")
    private boolean chatMode;

    @Parameter(names = {"--model", "-m"}, description = "set model name")
    private String model;

    @Parameter
    private List<String> parameters;

}
