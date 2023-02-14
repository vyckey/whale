package com.hoily.service.whale.acl.wechat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/9 21:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OfficialImageMessageDTO extends UserMessageDTO {
    public static final String MSG_TYPE = "image";

    private Long mediaId;

    @Override
    public String getMsgType() {
        return MSG_TYPE;
    }
}
