package io.github.bkosaraju.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.Job;
import io.github.bkosaraju.pipeline.domain.JobConfig;
import io.github.bkosaraju.pipeline.repository.JobConfigRepository;
import io.github.bkosaraju.pipeline.service.JobConfigQueryService;
import io.github.bkosaraju.pipeline.service.JobConfigService;
import io.github.bkosaraju.pipeline.service.dto.JobConfigCriteria;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobConfigResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class JobConfigResourceIT {
    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_TYPE = "BBBBBBBBBB";

    @Autowired
    private JobConfigRepository jobConfigRepository;

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private JobConfigQueryService jobConfigQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobConfigMockMvc;

    private JobConfig jobConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobConfig createEntity(EntityManager em) {
        JobConfig jobConfig = new JobConfig()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .configType(DEFAULT_CONFIG_TYPE);
        return jobConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobConfig createUpdatedEntity(EntityManager em) {
        JobConfig jobConfig = new JobConfig()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configType(UPDATED_CONFIG_TYPE);
        return jobConfig;
    }

    @BeforeEach
    public void initTest() {
        jobConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobConfig() throws Exception {
        int databaseSizeBeforeCreate = jobConfigRepository.findAll().size();
        // Create the JobConfig
        restJobConfigMockMvc
            .perform(
                post("/api/job-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobConfig))
            )
            .andExpect(status().isCreated());

        // Validate the JobConfig in the database
        List<JobConfig> jobConfigList = jobConfigRepository.findAll();
        assertThat(jobConfigList).hasSize(databaseSizeBeforeCreate + 1);
        JobConfig testJobConfig = jobConfigList.get(jobConfigList.size() - 1);
        assertThat(testJobConfig.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testJobConfig.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testJobConfig.getConfigType()).isEqualTo(DEFAULT_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void createJobConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobConfigRepository.findAll().size();

        // Create the JobConfig with an existing ID
        jobConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobConfigMockMvc
            .perform(
                post("/api/job-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobConfig in the database
        List<JobConfig> jobConfigList = jobConfigRepository.findAll();
        assertThat(jobConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobConfigs() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList
        restJobConfigMockMvc
            .perform(get("/api/job-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE)));
    }

    @Test
    @Transactional
    public void getJobConfig() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get the jobConfig
        restJobConfigMockMvc
            .perform(get("/api/job-configs/{id}", jobConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobConfig.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.configType").value(DEFAULT_CONFIG_TYPE));
    }

    @Test
    @Transactional
    public void getJobConfigsByIdFiltering() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        Long id = jobConfig.getId();

        defaultJobConfigShouldBeFound("id.equals=" + id);
        defaultJobConfigShouldNotBeFound("id.notEquals=" + id);

        defaultJobConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultJobConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey equals to DEFAULT_CONFIG_KEY
        defaultJobConfigShouldBeFound("configKey.equals=" + DEFAULT_CONFIG_KEY);

        // Get all the jobConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultJobConfigShouldNotBeFound("configKey.equals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey not equals to DEFAULT_CONFIG_KEY
        defaultJobConfigShouldNotBeFound("configKey.notEquals=" + DEFAULT_CONFIG_KEY);

        // Get all the jobConfigList where configKey not equals to UPDATED_CONFIG_KEY
        defaultJobConfigShouldBeFound("configKey.notEquals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyIsInShouldWork() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey in DEFAULT_CONFIG_KEY or UPDATED_CONFIG_KEY
        defaultJobConfigShouldBeFound("configKey.in=" + DEFAULT_CONFIG_KEY + "," + UPDATED_CONFIG_KEY);

        // Get all the jobConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultJobConfigShouldNotBeFound("configKey.in=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey is not null
        defaultJobConfigShouldBeFound("configKey.specified=true");

        // Get all the jobConfigList where configKey is null
        defaultJobConfigShouldNotBeFound("configKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey contains DEFAULT_CONFIG_KEY
        defaultJobConfigShouldBeFound("configKey.contains=" + DEFAULT_CONFIG_KEY);

        // Get all the jobConfigList where configKey contains UPDATED_CONFIG_KEY
        defaultJobConfigShouldNotBeFound("configKey.contains=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigKeyNotContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configKey does not contain DEFAULT_CONFIG_KEY
        defaultJobConfigShouldNotBeFound("configKey.doesNotContain=" + DEFAULT_CONFIG_KEY);

        // Get all the jobConfigList where configKey does not contain UPDATED_CONFIG_KEY
        defaultJobConfigShouldBeFound("configKey.doesNotContain=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueIsEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue equals to DEFAULT_CONFIG_VALUE
        defaultJobConfigShouldBeFound("configValue.equals=" + DEFAULT_CONFIG_VALUE);

        // Get all the jobConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultJobConfigShouldNotBeFound("configValue.equals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue not equals to DEFAULT_CONFIG_VALUE
        defaultJobConfigShouldNotBeFound("configValue.notEquals=" + DEFAULT_CONFIG_VALUE);

        // Get all the jobConfigList where configValue not equals to UPDATED_CONFIG_VALUE
        defaultJobConfigShouldBeFound("configValue.notEquals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueIsInShouldWork() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue in DEFAULT_CONFIG_VALUE or UPDATED_CONFIG_VALUE
        defaultJobConfigShouldBeFound("configValue.in=" + DEFAULT_CONFIG_VALUE + "," + UPDATED_CONFIG_VALUE);

        // Get all the jobConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultJobConfigShouldNotBeFound("configValue.in=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue is not null
        defaultJobConfigShouldBeFound("configValue.specified=true");

        // Get all the jobConfigList where configValue is null
        defaultJobConfigShouldNotBeFound("configValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue contains DEFAULT_CONFIG_VALUE
        defaultJobConfigShouldBeFound("configValue.contains=" + DEFAULT_CONFIG_VALUE);

        // Get all the jobConfigList where configValue contains UPDATED_CONFIG_VALUE
        defaultJobConfigShouldNotBeFound("configValue.contains=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigValueNotContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configValue does not contain DEFAULT_CONFIG_VALUE
        defaultJobConfigShouldNotBeFound("configValue.doesNotContain=" + DEFAULT_CONFIG_VALUE);

        // Get all the jobConfigList where configValue does not contain UPDATED_CONFIG_VALUE
        defaultJobConfigShouldBeFound("configValue.doesNotContain=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType equals to DEFAULT_CONFIG_TYPE
        defaultJobConfigShouldBeFound("configType.equals=" + DEFAULT_CONFIG_TYPE);

        // Get all the jobConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultJobConfigShouldNotBeFound("configType.equals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType not equals to DEFAULT_CONFIG_TYPE
        defaultJobConfigShouldNotBeFound("configType.notEquals=" + DEFAULT_CONFIG_TYPE);

        // Get all the jobConfigList where configType not equals to UPDATED_CONFIG_TYPE
        defaultJobConfigShouldBeFound("configType.notEquals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeIsInShouldWork() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType in DEFAULT_CONFIG_TYPE or UPDATED_CONFIG_TYPE
        defaultJobConfigShouldBeFound("configType.in=" + DEFAULT_CONFIG_TYPE + "," + UPDATED_CONFIG_TYPE);

        // Get all the jobConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultJobConfigShouldNotBeFound("configType.in=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType is not null
        defaultJobConfigShouldBeFound("configType.specified=true");

        // Get all the jobConfigList where configType is null
        defaultJobConfigShouldNotBeFound("configType.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType contains DEFAULT_CONFIG_TYPE
        defaultJobConfigShouldBeFound("configType.contains=" + DEFAULT_CONFIG_TYPE);

        // Get all the jobConfigList where configType contains UPDATED_CONFIG_TYPE
        defaultJobConfigShouldNotBeFound("configType.contains=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByConfigTypeNotContainsSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);

        // Get all the jobConfigList where configType does not contain DEFAULT_CONFIG_TYPE
        defaultJobConfigShouldNotBeFound("configType.doesNotContain=" + DEFAULT_CONFIG_TYPE);

        // Get all the jobConfigList where configType does not contain UPDATED_CONFIG_TYPE
        defaultJobConfigShouldBeFound("configType.doesNotContain=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllJobConfigsByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        jobConfigRepository.saveAndFlush(jobConfig);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        jobConfig.setJob(job);
        jobConfigRepository.saveAndFlush(jobConfig);
        Long jobId = job.getId();

        // Get all the jobConfigList where job equals to jobId
        defaultJobConfigShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobConfigList where job equals to jobId + 1
        defaultJobConfigShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobConfigShouldBeFound(String filter) throws Exception {
        restJobConfigMockMvc
            .perform(get("/api/job-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE)));

        // Check, that the count call also returns 1
        restJobConfigMockMvc
            .perform(get("/api/job-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobConfigShouldNotBeFound(String filter) throws Exception {
        restJobConfigMockMvc
            .perform(get("/api/job-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobConfigMockMvc
            .perform(get("/api/job-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingJobConfig() throws Exception {
        // Get the jobConfig
        restJobConfigMockMvc.perform(get("/api/job-configs/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobConfig() throws Exception {
        // Initialize the database
        jobConfigService.save(jobConfig);

        int databaseSizeBeforeUpdate = jobConfigRepository.findAll().size();

        // Update the jobConfig
        JobConfig updatedJobConfig = jobConfigRepository.findById(jobConfig.getId()).get();
        // Disconnect from session so that the updates on updatedJobConfig are not directly saved in db
        em.detach(updatedJobConfig);
        updatedJobConfig.configKey(UPDATED_CONFIG_KEY).configValue(UPDATED_CONFIG_VALUE).configType(UPDATED_CONFIG_TYPE);

        restJobConfigMockMvc
            .perform(
                put("/api/job-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobConfig))
            )
            .andExpect(status().isOk());

        // Validate the JobConfig in the database
        List<JobConfig> jobConfigList = jobConfigRepository.findAll();
        assertThat(jobConfigList).hasSize(databaseSizeBeforeUpdate);
        JobConfig testJobConfig = jobConfigList.get(jobConfigList.size() - 1);
        assertThat(testJobConfig.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testJobConfig.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testJobConfig.getConfigType()).isEqualTo(UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingJobConfig() throws Exception {
        int databaseSizeBeforeUpdate = jobConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobConfigMockMvc
            .perform(
                put("/api/job-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobConfig in the database
        List<JobConfig> jobConfigList = jobConfigRepository.findAll();
        assertThat(jobConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobConfig() throws Exception {
        // Initialize the database
        jobConfigService.save(jobConfig);

        int databaseSizeBeforeDelete = jobConfigRepository.findAll().size();

        // Delete the jobConfig
        restJobConfigMockMvc
            .perform(delete("/api/job-configs/{id}", jobConfig.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobConfig> jobConfigList = jobConfigRepository.findAll();
        assertThat(jobConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
