package com.xcly.forever.common.model;

import com.xcly.forever.common.auth.ResponseStatus;
import lombok.Builder;
import lombok.Data;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
@Builder
@Data
public class AuthResponse<T> {
    private int code = ResponseStatus.SUCCESS.getCode();
    private String msg = ResponseStatus.SUCCESS.getMsg();
    private T data;
}
