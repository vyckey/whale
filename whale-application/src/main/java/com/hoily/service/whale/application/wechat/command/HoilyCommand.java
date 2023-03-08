package com.hoily.service.whale.application.wechat.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Data;

/**
 * Hoily command
 *
 * @author vyckey
 */
@Data
@Parameters(commandNames = "hoily", separators = "=", commandDescription = "hoily commands")
public class HoilyCommand {
    @Parameter(names = {"--help", "-h"}, description = "help", help = true)
    private boolean help;

    public String help(JCommander jCommander) {
        StringBuilder stringBuilder = new StringBuilder();
        jCommander.getUsageFormatter().usage(stringBuilder);
        return stringBuilder.toString();
    }

}
