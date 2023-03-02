package com.hoily.service.whale.acl.openai.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Image response
 *
 * @author vyckey
 * 2023/3/2 13:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImageResponse extends ListResultResponse<ImageResponse.ImageResult> {
    private Date createdAt;

    public void setCreated(Integer created) {
        if (created != null) {
            this.createdAt = new Date(created * 1000L);
        } else {
            this.createdAt = null;
        }
    }

    @Data
    public static class ImageResult implements Serializable {
        private String url;
    }
}
