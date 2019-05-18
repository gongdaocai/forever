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
 * 腾讯云登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class AuthTencentCloudRequest extends BaseAuthRequest {

    public AuthTencentCloudRequest(AuthConfig config) {
        super(config, AuthSource.TENCEN_CLOUD);
    }

    @Override
    protected AuthToken getAccessToken(String code) {
        String accessTokenUrl = UrlBuilder.getTencentCloudAccessTokenUrl(config.getClientId(), config.getClientSecret(), code);
        HttpResponse response = HttpRequest.get(accessTokenUrl).execute();
        JSONObject object = JSONObject.parseObject(response.body());
        if (object.getIntValue("code") != 0) {
            throw new AuthException("Unable to get token from tencent cloud using code [" + code + "]: " + object.get("msg"));
        }
        return AuthToken.builder()
                .accessToken(object.getString("access_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();
        HttpResponse response = HttpRequest.get(UrlBuilder.getTencentCloudUserInfoUrl(accessToken)).execute();
        JSONObject object = JSONObject.parseObject(response.body());
        if (object.getIntValue("code") != 0) {
            throw new AuthException(object.getString("msg"));
        }
        object = object.getJSONObject("data");
        return AuthUser.builder()
                .username(object.getString("name"))
                .avatar("https://dev.tencent.com/" + object.getString("avatar"))
                .blog("https://dev.tencent.com/" + object.getString("path"))
                .nickname(object.getString("name"))
                .company(object.getString("company"))
                .location(object.getString("location"))
                .gender(AuthUserGender.getRealGender(object.getString("sex")))
                .email(object.getString("email"))
                .remark(object.getString("slogan"))
                .accessToken(accessToken)
                .source(AuthSource.TENCEN_CLOUD)
                .build();
    }
}
