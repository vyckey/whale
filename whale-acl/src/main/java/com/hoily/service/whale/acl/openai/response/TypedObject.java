package com.hoily.service.whale.acl.openai.response;

import java.io.Serializable;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 13:27
 */
public interface TypedObject extends Serializable {
    String getObject();
}
