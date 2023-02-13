package com.hoily.service.whale.infrastructure;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/12 19:48
 */
public class SecurityTest {
    @Test
    public void base64Test() {
        String password = "d877523e6eef27daebd7145af707e770";
        String base64String = Base64.encodeBase64String(password.getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals(password, new String(Base64.decodeBase64(base64String)));
    }

}