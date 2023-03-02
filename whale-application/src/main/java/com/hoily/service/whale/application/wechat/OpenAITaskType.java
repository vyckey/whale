package com.hoily.service.whale.application.wechat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OpenAI task type
 *
 * @author vyckey
 * 2023/3/2 19:39
 */
@Getter
@AllArgsConstructor
public enum OpenAITaskType {
    CHAT_OLD("chat_old"),
    CHAT("chat"),
    IMAGE("image"),
    ;

    private final String code;

    public static OpenAITaskType of(String code) {
        for (OpenAITaskType taskType : values()) {
            if (taskType.getCode().equals(code)) {
                return taskType;
            }
        }
        return null;
    }
}
