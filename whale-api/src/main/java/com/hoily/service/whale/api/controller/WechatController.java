package com.hoily.service.whale.api.controller;

import com.hoily.service.whale.acl.wechat.WechatAuthenticationManager;
import com.hoily.service.whale.acl.wechat.message.OfficialMessageDTO;
import com.hoily.service.whale.acl.wechat.message.UserMessageDTO;
import com.hoily.service.whale.application.wechat.WechatMessageService;
import com.hoily.service.whale.infrastructure.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 08:08
 */
@Slf4j
@RestController
@RequestMapping("api/wechat")
@AllArgsConstructor
public class WechatController {
    private final WechatAuthenticationManager authenticationManager;
    private final WechatMessageService wechatMessageService;

    @GetMapping("access/validate")
    public String validateAccess(@RequestHeader String signature, @RequestHeader String timestamp, @RequestHeader String nonce, @RequestHeader String echostr) {
        if (authenticationManager.signatureValid(timestamp, nonce, signature)) {
            return echostr;
        }
        return "";
    }

    @PostMapping(value = "/message/reply", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public OfficialMessageDTO replyMessage(@RequestBody UserMessageDTO request) {
        OfficialMessageDTO officialMessage = wechatMessageService.autoReply(request);
        log.info("wechat reply message. user:{}\nofficial:{}", JsonUtils.toJson(request), JsonUtils.toJson(officialMessage));
        return officialMessage;
    }
}
