package com.hoily.service.whale.contract.response;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 08:18
 */
@Getter
@ToString
public class BaseResponse<T> implements Serializable {
    protected boolean success;
    protected Integer errorCode;
    protected String errorMsg;
    protected T result;

    private BaseResponse() {
    }

    private BaseResponse(Builder<T> builder) {
        this.success = builder.success;
        this.errorCode = builder.errorCode;
        this.errorMsg = builder.errorMsg;
        this.result = builder.result;
    }

    public static <T> BaseResponse<T> createSuccess() {
        return new Builder<T>(true).build();
    }

    public static <T> Builder<T> success() {
        return new Builder<>(true);
    }

    public static <T> Builder<T> success(T data) {
        return new Builder<T>(true).result(data);
    }

    public static <T> Builder<T> fail() {
        return new Builder<>(false);
    }

    public static <T> Builder<T> fail(int errorCode, String errorMsg) {
        return new Builder<T>(false).errorCode(errorCode).errorMsg(errorMsg);
    }

    public static final class Builder<T> {
        private boolean success;
        private Integer errorCode;
        private String errorMsg;
        private T result;

        private Builder(boolean success) {
            this.success = success;
        }

        public Builder<T> errorCode(Integer errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder<T> errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public Builder<T> result(T result) {
            this.result = result;
            return this;
        }

        public BaseResponse<T> build() {
            return new BaseResponse<>(this);
        }
    }

}

