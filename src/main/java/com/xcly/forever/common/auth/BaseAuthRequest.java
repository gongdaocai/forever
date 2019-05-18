package com.xcly.forever.common.auth;

import com.xcly.forever.common.config.AuthConfig;
import com.xcly.forever.common.exception.AuthException;
import com.xcly.forever.common.model.AuthResponse;
import com.xcly.forever.common.model.AuthSource;
import com.xcly.forever.common.model.AuthToken;
import com.xcly.forever.common.model.AuthUser;
import com.xcly.forever.common.utils.AuthConfigChecker;
import com.xcly.forever.common.utils.UrlBuilder;
import lombok.Data;
/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @since 1.8
 */
@Data
public abstract class BaseAuthRequest implements AuthRequest {
    protected AuthConfig config;
    protected AuthSource source;

    public BaseAuthRequest(AuthConfig config, AuthSource source) {
        this.config = config;
        this.source = source;
        if (!AuthConfigChecker.isSupportedAuth(config)) {
            throw new AuthException(ResponseStatus.PARAMETER_INCOMPLETE);
        }
    }

    protected abstract AuthToken getAccessToken(String code);

    protected abstract AuthUser getUserInfo(AuthToken authToken);

    @Override
    public AuthResponse login(String code) {
        try {
            AuthUser user = this.getUserInfo(this.getAccessToken(code));
            return AuthResponse.builder().data(user).build();
        } catch (Exception e) {
            return AuthResponse.builder().code(500).msg(e.getMessage()).build();
        }
    }

    @Override
    public String authorize() {
        String authorizeUrl = null;
        switch (source) {
            case WEIBO:
                authorizeUrl = UrlBuilder.getWeiboAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case BAIDU:
                authorizeUrl = UrlBuilder.getBaiduAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case DINGTALK:
                authorizeUrl = UrlBuilder.getDingTalkQrConnectUrl(config.getClientId(), config.getRedirectUri());
                break;
            case GITEE:
                authorizeUrl = UrlBuilder.getGiteeAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case GITHUB:
                authorizeUrl = UrlBuilder.getGithubAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case CSDN:
                authorizeUrl = UrlBuilder.getCsdnAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case CODING:
                authorizeUrl = UrlBuilder.getCodingAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case TENCEN_CLOUD:
                authorizeUrl = UrlBuilder.getTencentCloudAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case OSCHINA:
                authorizeUrl = UrlBuilder.getOschinaAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case ALIPAY:
                authorizeUrl = UrlBuilder.getAlipayAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case QQ:
                authorizeUrl = UrlBuilder.getQqAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case WECHAT:
                authorizeUrl = UrlBuilder.getWeChatAuthorizeUrl(config.getClientId(), config.getRedirectUri());
                break;
            case GOOGLE:
                break;
            default:
                break;
        }
        return authorizeUrl;
    }
}
