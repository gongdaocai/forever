package com.xcly.forever.common.utils;


import com.xcly.forever.common.config.AuthConfig;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class AuthConfigChecker {

    /**
     * 是否支持第三方登录
     *
     * @param config config
     * @return true or false
     */
    public static boolean isSupportedAuth(AuthConfig config) {
        return StringUtils.isNotEmpty(config.getClientId()) && StringUtils.isNotEmpty(config.getClientSecret()) && StringUtils.isNotEmpty(config.getRedirectUri());
    }
}
