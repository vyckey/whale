package com.hoily.service.whale.application.wechat.command;

import com.beust.jcommander.JCommander;
import com.google.common.collect.ImmutableSet;
import com.hoily.service.whale.application.wechat.OpenAITaskType;
import com.hoily.service.whale.application.wechat.UserState;
import com.hoily.service.whale.application.wechat.UserStateManager;
import com.hoily.service.whale.infrastructure.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/2 15:38
 */
@Slf4j
@Component
@AllArgsConstructor
public class CommandExecutor {
    private static final String HOILY_CMD = "/hoily";
    private UserStateManager userStateManager;

    public boolean isCommand(String text) {
        return text.startsWith(HOILY_CMD);
    }

    public String execute(String command, String user) {
        // initialize commands
        HoilyCommand hoilyCommand = new HoilyCommand();
        Set<Object> commands = ImmutableSet.of(new ChatGPTCommand());

        // create JCommander instance
        JCommander jCommander = new JCommander(hoilyCommand);
        commands.forEach(jCommander::addCommand);
        String[] args = StringUtils.split(command.substring(HOILY_CMD.length()), " ");
        jCommander.parse(args);

        if (hoilyCommand.isHelp()) {
            return hoilyCommand.help(jCommander);
        }

        String parsedCommand = jCommander.getParsedCommand();
        JCommander subCommander = jCommander.getCommands().get(parsedCommand);
        List<Object> subCommands = subCommander != null ? subCommander.getObjects() : Collections.emptyList();
        for (Object subCommand : subCommands) {
            if (subCommand instanceof ChatGPTCommand) {
                return execute((ChatGPTCommand) subCommand, user);
            }
        }
        return "hello, i am hoily, nice to meet you! wuhu~~~";
    }

    private String execute(ChatGPTCommand command, String user) {
        final String output = "hoily get it!";
        UserState userState = userStateManager.createUserStateIfAbsent(user);
        if (command.isReset()) {
            userState.reset();
            return output;
        }

        if (StringUtils.isNotBlank(command.getTaskType())) {
            OpenAITaskType taskType = OpenAITaskType.of(command.getTaskType());
            if (taskType != null) {
                userState.setOpenAITaskType(taskType);
            }
        }
        if (StringUtils.isNotBlank(command.getTaskDesc())) {
            userState.setOpenAITask(command.getTaskDesc());
        }
        if (StringUtils.isNotBlank(command.getModel())) {
            userState.setOpenAIModel(command.getModel());
        }
        return output;
    }
}
