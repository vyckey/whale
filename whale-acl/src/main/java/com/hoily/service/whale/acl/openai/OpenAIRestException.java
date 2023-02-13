package com.hoily.service.whale.acl.openai;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 13:20
 */
public class OpenAIRestException extends RuntimeException {
    public OpenAIRestException(String message) {
        super(message);
    }

    public OpenAIRestException(String message, Throwable cause) {
        super(message, cause);
    }
}
