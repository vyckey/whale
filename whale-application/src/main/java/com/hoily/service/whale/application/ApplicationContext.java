package com.hoily.service.whale.application;

import com.hoily.service.whale.acl.AclContext;
import com.hoily.service.whale.infrastructure.InfrastructureContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@ComponentScan("com.hoily.service.whale.application")
@Import({InfrastructureContext.class, AclContext.class})
public class ApplicationContext {

}
