package com.hoily.service.whale.acl.openai.response;

import lombok.Data;

import java.util.List;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 13:25
 */
@Data
public class ListResultResponse<T> implements TypedObject {
    private List<T> data;

    @Override
    public String getObject() {
        return "list";
    }
}
