package com.hoily.service.whale.api.aop;

import com.hoily.service.whale.contract.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 08:14
 */
@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ApiControllerAdvice {
    private HttpServletRequest request;

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<?> handleAny(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        return BaseResponse.fail(500000, "System Error").build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<?> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return BaseResponse.fail(500000, "System Error").build();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<?> handleBrokenPipe(IOException ex) {
        log.error(ex.getMessage(), ex);
        return BaseResponse.fail(500000, "IO Error").build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ResponseBody
    public BaseResponse<?> handleNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage());
        return BaseResponse.fail(500000, "bad media type").build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleNotValid(MethodArgumentNotValidException e) {
        log.warn("request not valid {}", e.getMessage());
        String errorMsg = e.getBindingResult().hasErrors() ?
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage() : "invalid params";
        return BaseResponse.fail(500000, errorMsg).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BaseResponse<?> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("request not readable, {}", e.getMessage(), e);
        return BaseResponse.fail(500000, "bad request params or body").build();
    }

//    @ExceptionHandler(BusinessException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public BaseResponse<?> handleBusinessException(BusinessException ex) {
//        log.error(ex.getMessage(), ex);
//        return BaseResponse.fail(500000, "System Error").build();
//    }
//
//    @ExceptionHandler(SystemException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public BaseResponse<?> handleSystemException(SystemException ex) {
//        log.error(ex.getMessage(), ex);
//        return BaseResponse.fail(500000, "System Error").build();
//    }
}
