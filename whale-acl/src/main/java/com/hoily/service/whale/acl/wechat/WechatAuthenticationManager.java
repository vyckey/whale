package com.hoily.service.whale.acl.wechat;

import com.google.common.collect.Lists;
import com.hoily.service.whale.acl.wechat.base.WechatResponse;
import com.hoily.service.whale.acl.wechat.security.AccessToken;
import com.hoily.service.whale.acl.wechat.security.AccessTokenDTO;
import com.hoily.service.whale.acl.wechat.security.AppConfig;
import com.hoily.service.whale.acl.wechat.security.EncryptionConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 09:58
 */
@Component
public class WechatAuthenticationManager {
    private final WechatRestTemplate wechatRestTemplate;
    private final Pair<String, AccessToken> accessTokenHolder = Pair.of(null, null);
    private AppConfig appConfig;
    private EncryptionConfig encryptionConfig;

    public WechatAuthenticationManager(WechatRestTemplate wechatRestTemplate) {
        this.wechatRestTemplate = wechatRestTemplate;
    }

    AppConfig getAppConfig() {
        return appConfig;
    }

    @Autowired
    public void setAppConfig(@Value("${wechat.app.id}") String appId, @Value("${wechat.app.secret}") String appSecretBase64) {
        this.appConfig = new AppConfig(appId, new String(Base64.decodeBase64(appSecretBase64)));
    }

    private EncryptionConfig getEncryptionConfig() {
        return encryptionConfig;
    }

    @Autowired
    public void setEncryptionConfig(@Value("${wechat.authentication.token}") String tokenBase64,
                                    @Value("${wechat.authentication.encodingAESKey}") String encodingAESKeyBase64) {
        this.encryptionConfig = new EncryptionConfig(new String(Base64.decodeBase64(tokenBase64)), new String(Base64.decodeBase64(encodingAESKeyBase64)));
    }

    /**
     * refer to <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html">微信开发文档 - 接入指南</a>
     */
    public boolean signatureValid(String timestamp, String nonce, String signature) {
        String token = getEncryptionConfig().getToken();
        String joinStr = Lists.newArrayList(token, timestamp, nonce).stream()
                .map(String::valueOf).sorted().collect(Collectors.joining(""));
        return DigestUtils.sha1Hex(joinStr).equals(signature);
    }


    public String getOrRefreshAccessToken() {
        AccessToken accessToken = accessTokenHolder.getValue();
        if (accessToken == null || accessToken.isExpired()) {
            synchronized (accessTokenHolder) {
                WechatResponse<AccessTokenDTO> response = wechatRestTemplate.requestAccessToken();
                if (response.isSuccess() && response.getResult() != null) {
                    AccessTokenDTO result = response.getResult();
                    accessToken = new AccessToken(result.getAccessToken(), result.getExpiredAfter());
                } else {
                    accessToken = null;
                }
                accessTokenHolder.setValue(accessToken);
            }
        }
        return accessToken != null ? accessToken.getAccessToken() : null;
    }
}
