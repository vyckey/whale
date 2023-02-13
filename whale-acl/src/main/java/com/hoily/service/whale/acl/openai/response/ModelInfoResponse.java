package com.hoily.service.whale.acl.openai.response;

import lombok.Data;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 13:22
 */
@Data
public class ModelInfoResponse implements TypedObject {
    private String id;
    private String ownBy;
    private String[] permissions;

    @Override
    public String getObject() {
        return "model";
    }
}
