package ch.swissperform.workflow.example.config;

import ch.swissperform.workflow.example.common.LogMessage;
import java.util.Arrays;
import java.util.stream.StreamSupport;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ConfigChangeListener {

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        final Environment env = event.getApplicationContext().getEnvironment();
        log.info(LogMessage.RECEIVED_CONTEXT_REFRESH_EVENT.getMessage());
        log.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
        final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(sources.spliterator(), false)
                .filter(EnumerablePropertySource.class::isInstance)
                .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(prop ->
                        !(prop.contains("credentials") ||
                                StringUtils.containsIgnoreCase(prop,"jwt.key-value") ||
                                StringUtils.containsIgnoreCase(prop,"password")))
                .forEach(prop -> log.info("{}: {}", prop, env.getProperty(prop)));
    }

}


