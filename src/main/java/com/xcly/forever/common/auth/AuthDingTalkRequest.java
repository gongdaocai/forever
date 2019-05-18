package com.xcly.forever.common.auth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.xcly.forever.common.config.AuthConfig;
import com.xcly.forever.common.exception.AuthException;
import com.xcly.forever.common.model.AuthDingTalkErrorCode;
import com.xcly.forever.common.model.AuthSource;
import com.xcly.forever.common.model.AuthToken;
import com.xcly.forever.common.model.AuthUser;
import com.xcly.forever.common.utils.GlobalAuthUtil;
import com.xcly.forever.common.utils.UrlBuilder;

import java.util.Objects;

/**
 * 钉钉登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
public class AuthDingTalkRequest extends BaseAuthRequest {

    public AuthDingTalkRequest(AuthConfig config) {
        super(config, AuthSource.DINGTALK);
    }

    @Override
    protected AuthToken getAccessToken(String code) {
        return AuthToken.builder()
                .accessCode(code)
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String code = authToken.getAccessCode();
        // 根据timestamp, appSecret计算签名值
        String stringToSign = System.currentTimeMillis() + "";
        String urlEncodeSignature = GlobalAuthUtil.generateDingTalkSignature(config.getClientSecret(), stringToSign);
        HttpResponse response = HttpRequest.post(UrlBuilder.getDingTalkUserInfoUrl(urlEncodeSignature, stringToSign, config.getClientId()))
                .body(Objects.requireNonNull(new JSONObject().put("tmp_auth_code", code)))
                .execute();
        String userInfo = response.body();
        JSONObject object = new JSONObject(userInfo);
        AuthDingTalkErrorCode errorCode = AuthDingTalkErrorCode.getErrorCode(object.getInt("errcode"));
        if (!AuthDingTalkErrorCode.EC0.equals(errorCode)) {
            throw new AuthException(errorCode.getDesc());
        }
        object = object.getJSONObject("user_info");
        return AuthUser.builder()
                .nickname(object.getStr("nick"))
                .source(AuthSource.DINGTALK)
                .build();
    }
}
