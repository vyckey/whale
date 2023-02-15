package com.hoily.service.whale.api.controller;

import com.hoily.service.whale.acl.wechat.WechatAuthenticationManager;
import com.hoily.service.whale.acl.wechat.base.XmlWrapper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @ResponseBody
    public String validateAccess(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        log.info("wechat access validate... {}", timestamp);
        if (authenticationManager.signatureValid(timestamp, nonce, signature)) {
            return echostr;
        }
        return "invalid signature";
    }

    @PostMapping(value = "/message/reply", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public XmlWrapper<OfficialMessageDTO> replyMessage(@RequestBody XmlWrapper<UserMessageDTO> request) {
        UserMessageDTO userMessage = request.getObject();
        log.info("wechat reply message. user:{}", JsonUtils.toJson(userMessage));
        OfficialMessageDTO officialMessage = wechatMessageService.autoReply(userMessage);
        log.info("wechat reply message. from_user:{}\nofficial:{}", userMessage.getFromUserName(), JsonUtils.toJson(officialMessage));
        return XmlWrapper.of(officialMessage);
    }
}