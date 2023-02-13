package com.hoily.service.whale.acl.openai.response;

import lombok.Data;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 14:19
 */
@Data
public class EngineInfoResponse implements TypedObject {
    private String id;
    private String owner;
    private Boolean ready;

    @Override
    public String getObject() {
        return "engine";
    }
}
