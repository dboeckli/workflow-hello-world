package ch.bpm.workflow.example.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogMessageTest {

    @Test
    void getMessageContainsLogIdPrefix() {
        assertThat(LogMessage.READING_CONFIG_CLASS.getMessage()).startsWith("[logid=10001]");
    }

    @Test
    void getMessageKeepsFormattingTemplate() {
        assertThat(LogMessage.READING_CONFIG_CLASS.getMessage())
            .isEqualTo("[logid=10001] Reading configuration from {} config map: {}");
    }

    @Test
    void idsAreUnique() {
        assertThat(LogMessage.READING_CONFIG_CLASS).isNotEqualTo(LogMessage.JACKSON_CONFIG_START)
            .isNotEqualTo(LogMessage.JACKSON_CONFIG_MODULES);
    }

}
