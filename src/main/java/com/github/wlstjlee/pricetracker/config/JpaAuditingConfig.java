package com.github.wlstjlee.pricetracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// JPA Auditing(@CreatedDate, @LastModifiedDate) 활성화를 위한 설정
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
