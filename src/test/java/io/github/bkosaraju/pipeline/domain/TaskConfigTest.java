package io.github.bkosaraju.pipeline.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.bkosaraju.pipeline.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class TaskConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskConfig.class);
        TaskConfig taskConfig1 = new TaskConfig();
        taskConfig1.setId(1L);
        TaskConfig taskConfig2 = new TaskConfig();
        taskConfig2.setId(taskConfig1.getId());
        assertThat(taskConfig1).isEqualTo(taskConfig2);
        taskConfig2.setId(2L);
        assertThat(taskConfig1).isNotEqualTo(taskConfig2);
        taskConfig1.setId(null);
        assertThat(taskConfig1).isNotEqualTo(taskConfig2);
    }
}
