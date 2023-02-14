package com.hoily.service.whale.api.controller;

import com.google.common.collect.Maps;
import com.hoily.service.whale.contract.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/14 13:31
 */
@Slf4j
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class APIController {

    @GetMapping("")
    @ResponseBody
    public BaseResponse<?> abstractInfo() {
        Map<String, Object> infoMap = Maps.newHashMap();
        infoMap.put("author", "vyckey");
        infoMap.put("server", "whale-api");
        infoMap.put("time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return BaseResponse.success(infoMap).build();
    }

}
