package com.xcly.forever.common.auth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.xcly.forever.common.config.AuthConfig;
import com.xcly.forever.common.exception.AuthException;
import com.xcly.forever.common.model.AuthSource;
import com.xcly.forever.common.model.AuthToken;
import com.xcly.forever.common.model.AuthUser;
import com.xcly.forever.common.utils.UrlBuilder;

/**
 * Gitee登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class AuthGiteeRequest extends BaseAuthRequest {

    public AuthGiteeRequest(AuthConfig config) {
        super(config, AuthSource.GITEE);
    }

    @Override
    protected AuthToken getAccessToken(String code) {
        String accessTokenUrl = UrlBuilder.getGiteeAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
        HttpResponse response = HttpRequest.post(accessTokenUrl).execute();
        JSONObject accessTokenObject = JSONObject.parseObject(response.body());
        if (accessTokenObject.containsKey("error")) {
            throw new AuthException("Unable to get token from gitee using code [" + code + "]");
        }
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();
        HttpResponse response = HttpRequest.get(UrlBuilder.getGiteeUserInfoUrl(accessToken)).execute();
        String userInfo = response.body();
        JSONObject object = JSONObject.parseObject(userInfo);
        return AuthUser.builder()
                .username(object.getString("login"))
                .avatar(object.getString("avatar_url"))
                .blog(object.getString("blog"))
                .nickname(object.getString("name"))
                .company(object.getString("company"))
                .location(object.getString("address"))
                .email(object.getString("email"))
                .remark(object.getString("bio"))
                .accessToken(accessToken)
                .source(AuthSource.GITEE)
                .build();
    }
}
