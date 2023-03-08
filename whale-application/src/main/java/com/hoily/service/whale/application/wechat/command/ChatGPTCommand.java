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
@Parameters(commandNames = "chatgpt", separators = "=", commandDescription = "ChatGPT commands")
public class ChatGPTCommand {
    @Parameter(names = {"--reset"}, description = "reset chatgpt config")
    private boolean reset;

    @Parameter(names = {"--task-type", "-tt"}, description = "set task type, include: chat_old, chat, image")
    private String taskType;

    @Parameter(names = {"--task", "-t"}, description = "set task description")
    private String taskDesc;

    @Parameter(names = {"--model", "-m"}, description = "set model name")
    private String model;

    @Parameter
    private List<String> parameters;

}
