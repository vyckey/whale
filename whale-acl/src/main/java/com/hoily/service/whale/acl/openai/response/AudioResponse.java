package com.hoily.service.whale.acl.openai.response;

import lombok.Data;

/**
 * Audio response
 *
 * @author vyckey
 * 2023/3/2 13:48
 */
@Data
public class AudioResponse implements TypedObject {
    private String text;

    @Override
    public String getObject() {
        return "audio";
    }
}
