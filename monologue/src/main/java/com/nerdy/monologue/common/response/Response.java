package com.nerdy.monologue.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nerdy.monologue.common.ResponseCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private boolean success; // 성공 여부
    private String code;     // 응답 코드
    private T data;          // 데이터 (성공 시 포함)

    // 성공 응답 (데이터 포함)
    public static <T> Response<T> ok(T data) {
        return new Response<>(true, ResponseCode.OK.getResponseCode(), data);
    }

    // 성공 응답 (데이터 없음)
    public static <T> Response<T> ok() {
        return new Response<>(true, ResponseCode.OK.getResponseCode(), null);
    }

    // 실패 응답 (데이터 없음)
    public static <T> Response<T> fail(ResponseCode responseCode) {
        return new Response<>(false, responseCode.getResponseCode(), null);
    }

    // 실패 응답 (데이터 포함)
    public static <T> Response<T> fail(ResponseCode responseCode, T data) {
        return new Response<>(false, responseCode.getResponseCode(), data);
    }
}
