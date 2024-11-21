package com.codilien.hostelmanagement.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class AuditConfig {

}
