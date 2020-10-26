package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.repository.TaskExecutionConfigRepository;
import io.github.bkosaraju.pipeline.service.TaskExecutionConfigService;
import io.github.bkosaraju.pipeline.service.dto.TaskExecutionConfigCriteria;
import io.github.bkosaraju.pipeline.service.TaskExecutionConfigQueryService;

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
    private static final Float SMALLER_CONFIG_VERSION = 1F - 1F;

    @Autowired
    private TaskExecutionConfigRepository taskExecutionConfigRepository;

    @Autowired
    private TaskExecutionConfigService taskExecutionConfigService;

    @Autowired
    private TaskExecutionConfigQueryService taskExecutionConfigQueryService;

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
    public void getTaskExecutionConfigsByIdFiltering() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        Long id = taskExecutionConfig.getId();

        defaultTaskExecutionConfigShouldBeFound("id.equals=" + id);
        defaultTaskExecutionConfigShouldNotBeFound("id.notEquals=" + id);

        defaultTaskExecutionConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskExecutionConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskExecutionConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskExecutionConfigShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey equals to DEFAULT_CONFIG_KEY
        defaultTaskExecutionConfigShouldBeFound("configKey.equals=" + DEFAULT_CONFIG_KEY);

        // Get all the taskExecutionConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldNotBeFound("configKey.equals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey not equals to DEFAULT_CONFIG_KEY
        defaultTaskExecutionConfigShouldNotBeFound("configKey.notEquals=" + DEFAULT_CONFIG_KEY);

        // Get all the taskExecutionConfigList where configKey not equals to UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldBeFound("configKey.notEquals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey in DEFAULT_CONFIG_KEY or UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldBeFound("configKey.in=" + DEFAULT_CONFIG_KEY + "," + UPDATED_CONFIG_KEY);

        // Get all the taskExecutionConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldNotBeFound("configKey.in=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey is not null
        defaultTaskExecutionConfigShouldBeFound("configKey.specified=true");

        // Get all the taskExecutionConfigList where configKey is null
        defaultTaskExecutionConfigShouldNotBeFound("configKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey contains DEFAULT_CONFIG_KEY
        defaultTaskExecutionConfigShouldBeFound("configKey.contains=" + DEFAULT_CONFIG_KEY);

        // Get all the taskExecutionConfigList where configKey contains UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldNotBeFound("configKey.contains=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigKeyNotContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configKey does not contain DEFAULT_CONFIG_KEY
        defaultTaskExecutionConfigShouldNotBeFound("configKey.doesNotContain=" + DEFAULT_CONFIG_KEY);

        // Get all the taskExecutionConfigList where configKey does not contain UPDATED_CONFIG_KEY
        defaultTaskExecutionConfigShouldBeFound("configKey.doesNotContain=" + UPDATED_CONFIG_KEY);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue equals to DEFAULT_CONFIG_VALUE
        defaultTaskExecutionConfigShouldBeFound("configValue.equals=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskExecutionConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldNotBeFound("configValue.equals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue not equals to DEFAULT_CONFIG_VALUE
        defaultTaskExecutionConfigShouldNotBeFound("configValue.notEquals=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskExecutionConfigList where configValue not equals to UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldBeFound("configValue.notEquals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue in DEFAULT_CONFIG_VALUE or UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldBeFound("configValue.in=" + DEFAULT_CONFIG_VALUE + "," + UPDATED_CONFIG_VALUE);

        // Get all the taskExecutionConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldNotBeFound("configValue.in=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue is not null
        defaultTaskExecutionConfigShouldBeFound("configValue.specified=true");

        // Get all the taskExecutionConfigList where configValue is null
        defaultTaskExecutionConfigShouldNotBeFound("configValue.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue contains DEFAULT_CONFIG_VALUE
        defaultTaskExecutionConfigShouldBeFound("configValue.contains=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskExecutionConfigList where configValue contains UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldNotBeFound("configValue.contains=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigValueNotContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configValue does not contain DEFAULT_CONFIG_VALUE
        defaultTaskExecutionConfigShouldNotBeFound("configValue.doesNotContain=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskExecutionConfigList where configValue does not contain UPDATED_CONFIG_VALUE
        defaultTaskExecutionConfigShouldBeFound("configValue.doesNotContain=" + UPDATED_CONFIG_VALUE);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion equals to DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.equals=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.equals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion not equals to DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.notEquals=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion not equals to UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.notEquals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion in DEFAULT_CONFIG_VERSION or UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.in=" + DEFAULT_CONFIG_VERSION + "," + UPDATED_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.in=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion is not null
        defaultTaskExecutionConfigShouldBeFound("configVersion.specified=true");

        // Get all the taskExecutionConfigList where configVersion is null
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion is greater than or equal to DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.greaterThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion is greater than or equal to UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.greaterThanOrEqual=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion is less than or equal to DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.lessThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion is less than or equal to SMALLER_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.lessThanOrEqual=" + SMALLER_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion is less than DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.lessThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion is less than UPDATED_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.lessThan=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByConfigVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);

        // Get all the taskExecutionConfigList where configVersion is greater than DEFAULT_CONFIG_VERSION
        defaultTaskExecutionConfigShouldNotBeFound("configVersion.greaterThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskExecutionConfigList where configVersion is greater than SMALLER_CONFIG_VERSION
        defaultTaskExecutionConfigShouldBeFound("configVersion.greaterThan=" + SMALLER_CONFIG_VERSION);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionConfigsByTaskExecutionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);
        TaskExecution taskExecution = TaskExecutionResourceIT.createEntity(em);
        em.persist(taskExecution);
        em.flush();
        taskExecutionConfig.setTaskExecution(taskExecution);
        taskExecutionConfigRepository.saveAndFlush(taskExecutionConfig);
        Long taskExecutionId = taskExecution.getId();

        // Get all the taskExecutionConfigList where taskExecution equals to taskExecutionId
        defaultTaskExecutionConfigShouldBeFound("taskExecutionId.equals=" + taskExecutionId);

        // Get all the taskExecutionConfigList where taskExecution equals to taskExecutionId + 1
        defaultTaskExecutionConfigShouldNotBeFound("taskExecutionId.equals=" + (taskExecutionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskExecutionConfigShouldBeFound(String filter) throws Exception {
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskExecutionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));

        // Check, that the count call also returns 1
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskExecutionConfigShouldNotBeFound(String filter) throws Exception {
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskExecutionConfigMockMvc.perform(get("/api/task-execution-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        taskExecutionConfigService.save(taskExecutionConfig);

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
        taskExecutionConfigService.save(taskExecutionConfig);

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
