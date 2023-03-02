package com.hoily.service.whale.application.wechat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * {@link UserState} manager
 *
 * @author vyckey
 * 2023/3/2 15:44
 */
@Component
public class UserStateManager {
    private final Cache<String, UserState> userStateCache = CacheBuilder.newBuilder()
            .maximumSize(10000L)
            .expireAfterWrite(7, TimeUnit.DAYS)
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public UserState getUserState(String userId) {
        return userStateCache.getIfPresent(userId);
    }

    @SneakyThrows
    public UserState createUserStateIfAbsent(String userId) {
        return userStateCache.get(userId, () -> new UserState(userId));
    }
}
