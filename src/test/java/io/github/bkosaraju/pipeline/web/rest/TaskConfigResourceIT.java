package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.TaskConfig;
import io.github.bkosaraju.pipeline.domain.Task;
import io.github.bkosaraju.pipeline.repository.TaskConfigRepository;
import io.github.bkosaraju.pipeline.service.TaskConfigService;
import io.github.bkosaraju.pipeline.service.dto.TaskConfigCriteria;
import io.github.bkosaraju.pipeline.service.TaskConfigQueryService;

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

import io.github.bkosaraju.pipeline.domain.enumeration.ConfigType;
/**
 * Integration tests for the {@link TaskConfigResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TaskConfigResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final ConfigType DEFAULT_CONFIG_TYPE = ConfigType.STATIC;
    private static final ConfigType UPDATED_CONFIG_TYPE = ConfigType.AWS_SSM;

    private static final Float DEFAULT_CONFIG_VERSION = 1F;
    private static final Float UPDATED_CONFIG_VERSION = 2F;
    private static final Float SMALLER_CONFIG_VERSION = 1F - 1F;

    @Autowired
    private TaskConfigRepository taskConfigRepository;

    @Autowired
    private TaskConfigService taskConfigService;

    @Autowired
    private TaskConfigQueryService taskConfigQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskConfigMockMvc;

    private TaskConfig taskConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskConfig createEntity(EntityManager em) {
        TaskConfig taskConfig = new TaskConfig()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .configType(DEFAULT_CONFIG_TYPE)
            .configVersion(DEFAULT_CONFIG_VERSION);
        return taskConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskConfig createUpdatedEntity(EntityManager em) {
        TaskConfig taskConfig = new TaskConfig()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configType(UPDATED_CONFIG_TYPE)
            .configVersion(UPDATED_CONFIG_VERSION);
        return taskConfig;
    }

    @BeforeEach
    public void initTest() {
        taskConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskConfig() throws Exception {
        int databaseSizeBeforeCreate = taskConfigRepository.findAll().size();
        // Create the TaskConfig
        restTaskConfigMockMvc.perform(post("/api/task-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskConfig)))
            .andExpect(status().isCreated());

        // Validate the TaskConfig in the database
        List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
        assertThat(taskConfigList).hasSize(databaseSizeBeforeCreate + 1);
        TaskConfig testTaskConfig = taskConfigList.get(taskConfigList.size() - 1);
        assertThat(testTaskConfig.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testTaskConfig.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testTaskConfig.getConfigType()).isEqualTo(DEFAULT_CONFIG_TYPE);
        assertThat(testTaskConfig.getConfigVersion()).isEqualTo(DEFAULT_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void createTaskConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskConfigRepository.findAll().size();

        // Create the TaskConfig with an existing ID
        taskConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskConfigMockMvc.perform(post("/api/task-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskConfig)))
            .andExpect(status().isBadRequest());

        // Validate the TaskConfig in the database
        List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
        assertThat(taskConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTaskConfigs() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList
        restTaskConfigMockMvc.perform(get("/api/task-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTaskConfig() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get the taskConfig
        restTaskConfigMockMvc.perform(get("/api/task-configs/{id}", taskConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskConfig.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.configType").value(DEFAULT_CONFIG_TYPE.toString()))
            .andExpect(jsonPath("$.configVersion").value(DEFAULT_CONFIG_VERSION.doubleValue()));
    }


    @Test
    @Transactional
    public void getTaskConfigsByIdFiltering() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        Long id = taskConfig.getId();

        defaultTaskConfigShouldBeFound("id.equals=" + id);
        defaultTaskConfigShouldNotBeFound("id.notEquals=" + id);

        defaultTaskConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskConfigShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey equals to DEFAULT_CONFIG_KEY
        defaultTaskConfigShouldBeFound("configKey.equals=" + DEFAULT_CONFIG_KEY);

        // Get all the taskConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultTaskConfigShouldNotBeFound("configKey.equals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey not equals to DEFAULT_CONFIG_KEY
        defaultTaskConfigShouldNotBeFound("configKey.notEquals=" + DEFAULT_CONFIG_KEY);

        // Get all the taskConfigList where configKey not equals to UPDATED_CONFIG_KEY
        defaultTaskConfigShouldBeFound("configKey.notEquals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyIsInShouldWork() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey in DEFAULT_CONFIG_KEY or UPDATED_CONFIG_KEY
        defaultTaskConfigShouldBeFound("configKey.in=" + DEFAULT_CONFIG_KEY + "," + UPDATED_CONFIG_KEY);

        // Get all the taskConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultTaskConfigShouldNotBeFound("configKey.in=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey is not null
        defaultTaskConfigShouldBeFound("configKey.specified=true");

        // Get all the taskConfigList where configKey is null
        defaultTaskConfigShouldNotBeFound("configKey.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyContainsSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey contains DEFAULT_CONFIG_KEY
        defaultTaskConfigShouldBeFound("configKey.contains=" + DEFAULT_CONFIG_KEY);

        // Get all the taskConfigList where configKey contains UPDATED_CONFIG_KEY
        defaultTaskConfigShouldNotBeFound("configKey.contains=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigKeyNotContainsSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configKey does not contain DEFAULT_CONFIG_KEY
        defaultTaskConfigShouldNotBeFound("configKey.doesNotContain=" + DEFAULT_CONFIG_KEY);

        // Get all the taskConfigList where configKey does not contain UPDATED_CONFIG_KEY
        defaultTaskConfigShouldBeFound("configKey.doesNotContain=" + UPDATED_CONFIG_KEY);
    }


    @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue equals to DEFAULT_CONFIG_VALUE
        defaultTaskConfigShouldBeFound("configValue.equals=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldNotBeFound("configValue.equals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue not equals to DEFAULT_CONFIG_VALUE
        defaultTaskConfigShouldNotBeFound("configValue.notEquals=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskConfigList where configValue not equals to UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldBeFound("configValue.notEquals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueIsInShouldWork() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue in DEFAULT_CONFIG_VALUE or UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldBeFound("configValue.in=" + DEFAULT_CONFIG_VALUE + "," + UPDATED_CONFIG_VALUE);

        // Get all the taskConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldNotBeFound("configValue.in=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue is not null
        defaultTaskConfigShouldBeFound("configValue.specified=true");

        // Get all the taskConfigList where configValue is null
        defaultTaskConfigShouldNotBeFound("configValue.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueContainsSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue contains DEFAULT_CONFIG_VALUE
        defaultTaskConfigShouldBeFound("configValue.contains=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskConfigList where configValue contains UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldNotBeFound("configValue.contains=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigValueNotContainsSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configValue does not contain DEFAULT_CONFIG_VALUE
        defaultTaskConfigShouldNotBeFound("configValue.doesNotContain=" + DEFAULT_CONFIG_VALUE);

        // Get all the taskConfigList where configValue does not contain UPDATED_CONFIG_VALUE
        defaultTaskConfigShouldBeFound("configValue.doesNotContain=" + UPDATED_CONFIG_VALUE);
    }


    @Test
    @Transactional
    public void getAllTaskConfigsByConfigTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configType equals to DEFAULT_CONFIG_TYPE
        defaultTaskConfigShouldBeFound("configType.equals=" + DEFAULT_CONFIG_TYPE);

        // Get all the taskConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultTaskConfigShouldNotBeFound("configType.equals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configType not equals to DEFAULT_CONFIG_TYPE
        defaultTaskConfigShouldNotBeFound("configType.notEquals=" + DEFAULT_CONFIG_TYPE);

        // Get all the taskConfigList where configType not equals to UPDATED_CONFIG_TYPE
        defaultTaskConfigShouldBeFound("configType.notEquals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configType in DEFAULT_CONFIG_TYPE or UPDATED_CONFIG_TYPE
        defaultTaskConfigShouldBeFound("configType.in=" + DEFAULT_CONFIG_TYPE + "," + UPDATED_CONFIG_TYPE);

        // Get all the taskConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultTaskConfigShouldNotBeFound("configType.in=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configType is not null
        defaultTaskConfigShouldBeFound("configType.specified=true");

        // Get all the taskConfigList where configType is null
        defaultTaskConfigShouldNotBeFound("configType.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion equals to DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.equals=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.equals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion not equals to DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.notEquals=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion not equals to UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.notEquals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsInShouldWork() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion in DEFAULT_CONFIG_VERSION or UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.in=" + DEFAULT_CONFIG_VERSION + "," + UPDATED_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.in=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion is not null
        defaultTaskConfigShouldBeFound("configVersion.specified=true");

        // Get all the taskConfigList where configVersion is null
        defaultTaskConfigShouldNotBeFound("configVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion is greater than or equal to DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.greaterThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion is greater than or equal to UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.greaterThanOrEqual=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion is less than or equal to DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.lessThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion is less than or equal to SMALLER_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.lessThanOrEqual=" + SMALLER_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion is less than DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.lessThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion is less than UPDATED_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.lessThan=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllTaskConfigsByConfigVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);

        // Get all the taskConfigList where configVersion is greater than DEFAULT_CONFIG_VERSION
        defaultTaskConfigShouldNotBeFound("configVersion.greaterThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the taskConfigList where configVersion is greater than SMALLER_CONFIG_VERSION
        defaultTaskConfigShouldBeFound("configVersion.greaterThan=" + SMALLER_CONFIG_VERSION);
    }


    @Test
    @Transactional
    public void getAllTaskConfigsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        taskConfigRepository.saveAndFlush(taskConfig);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        taskConfig.setTask(task);
        taskConfigRepository.saveAndFlush(taskConfig);
        Long taskId = task.getId();

        // Get all the taskConfigList where task equals to taskId
        defaultTaskConfigShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskConfigList where task equals to taskId + 1
        defaultTaskConfigShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskConfigShouldBeFound(String filter) throws Exception {
        restTaskConfigMockMvc.perform(get("/api/task-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));

        // Check, that the count call also returns 1
        restTaskConfigMockMvc.perform(get("/api/task-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskConfigShouldNotBeFound(String filter) throws Exception {
        restTaskConfigMockMvc.perform(get("/api/task-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskConfigMockMvc.perform(get("/api/task-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTaskConfig() throws Exception {
        // Get the taskConfig
        restTaskConfigMockMvc.perform(get("/api/task-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskConfig() throws Exception {
        // Initialize the database
        taskConfigService.save(taskConfig);

        int databaseSizeBeforeUpdate = taskConfigRepository.findAll().size();

        // Update the taskConfig
        TaskConfig updatedTaskConfig = taskConfigRepository.findById(taskConfig.getId()).get();
        // Disconnect from session so that the updates on updatedTaskConfig are not directly saved in db
        em.detach(updatedTaskConfig);
        updatedTaskConfig
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configType(UPDATED_CONFIG_TYPE)
            .configVersion(UPDATED_CONFIG_VERSION);

        restTaskConfigMockMvc.perform(put("/api/task-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskConfig)))
            .andExpect(status().isOk());

        // Validate the TaskConfig in the database
        List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
        assertThat(taskConfigList).hasSize(databaseSizeBeforeUpdate);
        TaskConfig testTaskConfig = taskConfigList.get(taskConfigList.size() - 1);
        assertThat(testTaskConfig.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testTaskConfig.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testTaskConfig.getConfigType()).isEqualTo(UPDATED_CONFIG_TYPE);
        assertThat(testTaskConfig.getConfigVersion()).isEqualTo(UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskConfig() throws Exception {
        int databaseSizeBeforeUpdate = taskConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskConfigMockMvc.perform(put("/api/task-configs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskConfig)))
            .andExpect(status().isBadRequest());

        // Validate the TaskConfig in the database
        List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
        assertThat(taskConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaskConfig() throws Exception {
        // Initialize the database
        taskConfigService.save(taskConfig);

        int databaseSizeBeforeDelete = taskConfigRepository.findAll().size();

        // Delete the taskConfig
        restTaskConfigMockMvc.perform(delete("/api/task-configs/{id}", taskConfig.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskConfig> taskConfigList = taskConfigRepository.findAll();
        assertThat(taskConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
