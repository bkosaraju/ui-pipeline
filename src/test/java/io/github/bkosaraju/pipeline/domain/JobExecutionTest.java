package io.github.bkosaraju.pipeline.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.bkosaraju.pipeline.web.rest.TestUtil;

public class JobExecutionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobExecution.class);
        JobExecution jobExecution1 = new JobExecution();
        jobExecution1.setId(1L);
        JobExecution jobExecution2 = new JobExecution();
        jobExecution2.setId(jobExecution1.getId());
        assertThat(jobExecution1).isEqualTo(jobExecution2);
        jobExecution2.setId(2L);
        assertThat(jobExecution1).isNotEqualTo(jobExecution2);
        jobExecution1.setId(null);
        assertThat(jobExecution1).isNotEqualTo(jobExecution2);
    }
}
