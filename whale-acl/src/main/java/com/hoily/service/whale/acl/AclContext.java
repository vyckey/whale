package com.hoily.service.whale.acl;

import com.hoily.service.whale.infrastructure.InfrastructureContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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

}
