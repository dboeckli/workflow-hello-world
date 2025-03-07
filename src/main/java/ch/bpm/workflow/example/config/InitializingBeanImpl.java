package ch.bpm.workflow.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import static ch.bpm.workflow.example.common.LogMessage.READING_CONFIG_CLASS;
import static ch.bpm.workflow.example.config.CamundaLdapConfiguration.printLdapIdentityProviderPlugin;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile({"local", "ci"})
public class InitializingBeanImpl implements InitializingBean {

    private final LdapIdentityProviderPlugin ldapIdentityProviderPlugin;

    private final ProcessEngineConfiguration processEngineConfiguration;

    private final CamundaBpmProperties camundaBpmProperties;

    @Override
    public void afterPropertiesSet() {
        log.info(READING_CONFIG_CLASS.getMessage(), this.getClass().getName(), printLdapIdentityProviderPlugin(ldapIdentityProviderPlugin));
        log.info(READING_CONFIG_CLASS.getMessage(), ldapIdentityProviderPlugin.getClass().getName(), printLdapIdentityProviderPlugin(ldapIdentityProviderPlugin));
        log.info(READING_CONFIG_CLASS.getMessage(), ldapIdentityProviderPlugin.getClass().getName(), "---------------------------------------------------------------------------");

        log.info(READING_CONFIG_CLASS.getMessage(), processEngineConfiguration.getClass().getName(), printSpringProcessEngineConfiguration(processEngineConfiguration));
        log.info(READING_CONFIG_CLASS.getMessage(), processEngineConfiguration.getClass().getName(), "---------------------------------------------------------------------------");

        log.info(READING_CONFIG_CLASS.getMessage(), camundaBpmProperties.getClass().getName(), printCamundaBpmProperties(camundaBpmProperties));
        log.info(READING_CONFIG_CLASS.getMessage(), camundaBpmProperties.getClass().getName(), "---------------------------------------------------------------------------");
    }

    private static String printSpringProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = (SpringProcessEngineConfiguration) processEngineConfiguration;

        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(
            springProcessEngineConfiguration, ToStringStyle.MULTI_LINE_STYLE) {
            @Override
            protected boolean accept(Field field) {
                return !field.getName().equals("managerPassword");
            }
        };
        return builder.toString();
    }

    private static String printCamundaBpmProperties(CamundaBpmProperties camundaBpmProperties) {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(
            camundaBpmProperties, ToStringStyle.MULTI_LINE_STYLE) {
            @Override
            protected boolean accept(Field field) {
                return !field.getName().equals("managerPassword");
            }
        };
        return builder.toString();
    }
}
