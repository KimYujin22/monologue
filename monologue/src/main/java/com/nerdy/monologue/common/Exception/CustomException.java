package com.nerdy.monologue.common.Exception;

import com.nerdy.monologue.common.ResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getResponseCode());
        this.responseCode = responseCode;
    }
}
