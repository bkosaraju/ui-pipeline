package io.github.bkosaraju.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bkosaraju.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class JobTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Job.class);
        Job job1 = new Job();
        job1.setId(1L);
        Job job2 = new Job();
        job2.setId(job1.getId());
        assertThat(job1).isEqualTo(job2);
        job2.setId(2L);
        assertThat(job1).isNotEqualTo(job2);
        job1.setId(null);
        assertThat(job1).isNotEqualTo(job2);
    }
}
