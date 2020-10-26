package io.github.bkosaraju.pipeline.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.bkosaraju.pipeline.web.rest.TestUtil;

public class JobTaskOrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobTaskOrder.class);
        JobTaskOrder jobTaskOrder1 = new JobTaskOrder();
        jobTaskOrder1.setId(1L);
        JobTaskOrder jobTaskOrder2 = new JobTaskOrder();
        jobTaskOrder2.setId(jobTaskOrder1.getId());
        assertThat(jobTaskOrder1).isEqualTo(jobTaskOrder2);
        jobTaskOrder2.setId(2L);
        assertThat(jobTaskOrder1).isNotEqualTo(jobTaskOrder2);
        jobTaskOrder1.setId(null);
        assertThat(jobTaskOrder1).isNotEqualTo(jobTaskOrder2);
    }
}
