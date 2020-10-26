package io.github.bkosaraju.pipeline.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.bkosaraju.pipeline.web.rest.TestUtil;

public class GlobalConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalConfig.class);
        GlobalConfig globalConfig1 = new GlobalConfig();
        globalConfig1.setId(1L);
        GlobalConfig globalConfig2 = new GlobalConfig();
        globalConfig2.setId(globalConfig1.getId());
        assertThat(globalConfig1).isEqualTo(globalConfig2);
        globalConfig2.setId(2L);
        assertThat(globalConfig1).isNotEqualTo(globalConfig2);
        globalConfig1.setId(null);
        assertThat(globalConfig1).isNotEqualTo(globalConfig2);
    }
}
