package ch.swissperform.workflow.example.config;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static ch.swissperform.workflow.example.common.LogMessage.READING_CONFIG_CLASS;
import static ch.swissperform.workflow.example.config.CamundaLdapConfiguration.printLdapIdentityProviderPlugin;

@Component
@Log4j2
@Profile({"local", "ci"})
public class InitializingBeanImpl implements InitializingBean {

    private final LdapIdentityProviderPlugin ldapIdentityProviderPlugin;

    public InitializingBeanImpl(LdapIdentityProviderPlugin ldapIdentityProviderPlugin) {
        this.ldapIdentityProviderPlugin = ldapIdentityProviderPlugin;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(READING_CONFIG_CLASS.getMessage(), this.getClass().getName(), printLdapIdentityProviderPlugin(ldapIdentityProviderPlugin));
    }
}
