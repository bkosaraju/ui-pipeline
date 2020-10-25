package io.github.bkosaraju.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bkosaraju.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class JobConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobConfig.class);
        JobConfig jobConfig1 = new JobConfig();
        jobConfig1.setId(1L);
        JobConfig jobConfig2 = new JobConfig();
        jobConfig2.setId(jobConfig1.getId());
        assertThat(jobConfig1).isEqualTo(jobConfig2);
        jobConfig2.setId(2L);
        assertThat(jobConfig1).isNotEqualTo(jobConfig2);
        jobConfig1.setId(null);
        assertThat(jobConfig1).isNotEqualTo(jobConfig2);
    }
}
