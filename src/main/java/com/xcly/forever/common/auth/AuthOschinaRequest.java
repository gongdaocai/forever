package com.xcly.forever.common.auth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.xcly.forever.common.config.AuthConfig;
import com.xcly.forever.common.exception.AuthException;
import com.xcly.forever.common.model.AuthSource;
import com.xcly.forever.common.model.AuthToken;
import com.xcly.forever.common.model.AuthUser;
import com.xcly.forever.common.model.AuthUserGender;
import com.xcly.forever.common.utils.UrlBuilder;

/**
 * oschina登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class AuthOschinaRequest extends BaseAuthRequest {

    public AuthOschinaRequest(AuthConfig config) {
        super(config, AuthSource.OSCHINA);
    }

    @Override
    protected AuthToken getAccessToken(String code) {
        String accessTokenUrl = UrlBuilder.getOschinaAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
        HttpResponse response = HttpRequest.post(accessTokenUrl).execute();
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());
        if (accessTokenObject.containsKey("error")) {
            throw new AuthException("Unable to get token from oschina using code [" + code + "]");
        }
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();
        HttpResponse response = HttpRequest.get(UrlBuilder.getOschinaUserInfoUrl(accessToken)).execute();
        JSONObject object = JSONObject.parseObject(response.body());
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
        return AuthUser.builder()
                .username(object.getString("name"))
                .nickname(object.getString("name"))
                .avatar(object.getString("avatar"))
                .blog(object.getString("url"))
                .location(object.getString("location"))
                .gender(AuthUserGender.getRealGender(object.getString("gender")))
                .email(object.getString("email"))
                .accessToken(accessToken)
                .source(AuthSource.OSCHINA)
                .build();
    }
}
