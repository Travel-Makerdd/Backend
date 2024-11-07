package com.hsu.travelmaker.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomApiResponse<T> {

    private int status;
    private T data; // 어떤 형태로 값이 반환될지 모름
    private String message;

    // 통신 성공
    public static <T> CustomApiResponse<T> createSuccess(int status, T data, String message) {
        return new CustomApiResponse<T>(status, data, message);
    }

    // 통신 실패
    public static <T> CustomApiResponse<T> createFailWithout (int status, String message) {
        return new CustomApiResponse<T>(status, null, message);
    }

}
