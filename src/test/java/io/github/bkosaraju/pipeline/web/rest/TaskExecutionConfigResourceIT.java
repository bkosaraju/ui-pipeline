package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.repository.TaskExecutionConfigRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TaskExecutionConfigResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TaskExecutionConfigResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final Float DEFAULT_CONFIG_VERSION = 1F;
    private static final Float UPDATED_CONFIG_VERSION = 2F;

    @Autowired
    private TaskExecutionConfigRepository taskExecutionConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskExecutionConfigMockMvc;

    private TaskExecutionConfig taskExecutionConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskExecutionConfig createEntity(EntityManager em) {
        TaskExecutionConfig taskExecutionConfig = new TaskExecutionConfig()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .configVersion(DEFAULT_CONFIG_VERSION);
        return taskExecutionConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskExecutionConfig createUpdatedEntity(EntityManager em) {
        TaskExecutionConfig taskExecutionConfig = new TaskExecutionConfig()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configVersion(UPDATED_CONFIG_VERSION);
        return taskExecutionConfig;
    }

    @BeforeEach
    public void initTest() {
        taskExecutionConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskExecutionConfig() throws Exception {
        int databaseSizeBeforeCreate = taskExecutionConfigRepository.findAll().size();
        // Create the TaskExecutionConfig
        restTaskExecutionConfigMockMvc.perform(post("/api/task-execution-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecutionConfig)))
            .andExpect(status().isCreated());

        // Validate the TaskExecutionConfig in the database
        List<TaskExecutionConfig> taskExecutionConfigList = taskExecutionConfigRepository.findAll();
        assertThat(taskExecutionConfigList).hasSize(databaseSizeBeforeCreate + 1);
        TaskExecutionConfig testTaskExecutionConfig = taskExecutionConfigList.get(taskExecutionConfigList.size() - 1);
        assertThat(testTaskExecutionConfig.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testTaskExecutionConfig.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testTaskExecutionConfig.getConfigVersion()).isEqualTo(DEFAULT_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void createTaskExecutionConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskExecutionConfigRepository.findAll().size();

        // Create the TaskExecutionConfig with an existing ID
        taskExecutionConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskExecutionConfigMockMvc.perform(post("/api/task-execution-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecutionConfig)))
            .andExpect(status().isBadRequest());

        // Validate the TaskExecutionConfig in the database
        List<TaskExecutionConfig> taskExecutionConfigList = taskExecutionConfigRepository.findAll();
        assertThat(taskExecutionConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionConfigs() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskExecutionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTaskExecutionConfig() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get the taskExecutionConfig
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs/{id}", taskExecutionConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskExecutionConfig.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.configVersion").value(DEFAULT_CONFIG_VERSION.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTaskExecutionConfig() throws Exception {
        // Get the taskExecutionConfig
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskExecutionConfig() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        int databaseSizeBeforeUpdate = taskExecutionConfigRepository.findAll().size();

        // Update the taskExecutionConfig
        TaskExecutionConfig updatedTaskExecutionConfig = taskExecutionConfigRepository.findById(taskExecutionConfig.getId()).get();
        // Disconnect from session so that the updates on updatedTaskExecutionConfig are not directly saved in db
        em.detach(updatedTaskExecutionConfig);
        updatedTaskExecutionConfig
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configVersion(UPDATED_CONFIG_VERSION);

        restTaskExecutionConfigMockMvc.perform(put("/api/task-execution-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskExecutionConfig)))
            .andExpect(status().isOk());

        // Validate the TaskExecutionConfig in the database
        List<TaskExecutionConfig> taskExecutionConfigList = taskExecutionConfigRepository.findAll();
        assertThat(taskExecutionConfigList).hasSize(databaseSizeBeforeUpdate);
        TaskExecutionConfig testTaskExecutionConfig = taskExecutionConfigList.get(taskExecutionConfigList.size() - 1);
        assertThat(testTaskExecutionConfig.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testTaskExecutionConfig.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testTaskExecutionConfig.getConfigVersion()).isEqualTo(UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskExecutionConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskExecutionConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskExecutionConfigMockMvc.perform(put("/api/task-execution-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecutionConfig)))
            .andExpect(status().isBadRequest());

        // Validate the TaskExecutionConfig in the database
        List<TaskExecutionConfig> taskExecutionConfigList = taskExecutionConfigRepository.findAll();
        assertThat(taskExecutionConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaskExecutionConfig() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        int databaseSizeBeforeDelete = taskExecutionConfigRepository.findAll().size();

        // Delete the taskExecutionConfig
        restTaskExecutionConfigMockMvc.perform(delete("/api/task-execution-configs/{id}", taskExecutionConfig.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskExecutionConfig> taskExecutionConfigList = taskExecutionConfigRepository.findAll();
        assertThat(taskExecutionConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
