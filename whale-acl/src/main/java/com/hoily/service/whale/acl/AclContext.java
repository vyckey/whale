package com.hoily.service.whale.acl;

import com.hoily.service.whale.infrastructure.InfrastructureContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description is here
 *
 * @author vyckey
 * 2023/2/10 10:30
 */
@Import(InfrastructureContext.class)
@Configuration
@ComponentScan("com.hoily.service.whale.acl")
@AllArgsConstructor
public class AclContext {
    @Bean("wechatExecutor")
    public ExecutorService wechatExecutor() {
        return new ThreadPoolExecutor(10, 10, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4));
    }
}
