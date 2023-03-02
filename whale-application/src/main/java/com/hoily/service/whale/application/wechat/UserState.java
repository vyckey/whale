package com.hoily.service.whale.application.wechat;

import lombok.Data;

/**
 * User state
 *
 * @author vyckey
 * 2023/3/2 15:46
 */
@Data
public class UserState {
    private final String userId;

    private boolean openAIChatMode;
    private String openAIModel;

    public UserState(String userId) {
        this.userId = userId;
    }
}
