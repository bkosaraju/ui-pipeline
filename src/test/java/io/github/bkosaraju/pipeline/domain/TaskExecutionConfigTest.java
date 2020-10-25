package io.github.bkosaraju.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bkosaraju.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class TaskExecutionConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskExecutionConfig.class);
        TaskExecutionConfig taskExecutionConfig1 = new TaskExecutionConfig();
        taskExecutionConfig1.setId(1L);
        TaskExecutionConfig taskExecutionConfig2 = new TaskExecutionConfig();
        taskExecutionConfig2.setId(taskExecutionConfig1.getId());
        assertThat(taskExecutionConfig1).isEqualTo(taskExecutionConfig2);
        taskExecutionConfig2.setId(2L);
        assertThat(taskExecutionConfig1).isNotEqualTo(taskExecutionConfig2);
        taskExecutionConfig1.setId(null);
        assertThat(taskExecutionConfig1).isNotEqualTo(taskExecutionConfig2);
    }
}
