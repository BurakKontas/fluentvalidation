package tr.kontas.fluentvalidation.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("tr.kontas.fluentvalidation.spring")
class TestConfig {
}