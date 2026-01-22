package tr.kontas.fluentvalidation.spring.autoconfigure;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import tr.kontas.fluentvalidation.spring.SpringContextHolder;
import tr.kontas.fluentvalidation.spring.SpringValidateAspect;

@AutoConfiguration
@ConditionalOnClass(ApplicationContext.class)
public class FluentValidationAutoConfiguration {

    @Bean
    @ConditionalOnProperty(
            prefix = "tr.kontas.fluentvalidation.spring",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    @ConditionalOnMissingBean
    public SpringValidateAspect springValidateAspect() {
        return new SpringValidateAspect();
    }

    @Bean
    @ConditionalOnClass(EntityManagerFactory.class)
    @ConditionalOnMissingBean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
