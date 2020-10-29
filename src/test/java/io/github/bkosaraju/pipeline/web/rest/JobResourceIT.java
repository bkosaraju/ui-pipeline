package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.Job;
import io.github.bkosaraju.pipeline.domain.JobConfig;
import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.repository.JobRepository;
import io.github.bkosaraju.pipeline.service.JobService;
import io.github.bkosaraju.pipeline.service.dto.JobCriteria;
import io.github.bkosaraju.pipeline.service.JobQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static io.github.bkosaraju.pipeline.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class JobResourceIT {

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_JOB_STATUS_FLAG = 1;
    private static final Integer UPDATED_JOB_STATUS_FLAG = 2;
    private static final Integer SMALLER_JOB_STATUS_FLAG = 1 - 1;

    private static final ZonedDateTime DEFAULT_CREATE_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobQueryService jobQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobMockMvc;

    private Job job;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .jobName(DEFAULT_JOB_NAME)
            .jobStatusFlag(DEFAULT_JOB_STATUS_FLAG)
            .createTimestamp(DEFAULT_CREATE_TIMESTAMP);
        return job;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity(EntityManager em) {
        Job job = new Job()
            .jobName(UPDATED_JOB_NAME)
            .jobStatusFlag(UPDATED_JOB_STATUS_FLAG)
            .createTimestamp(UPDATED_CREATE_TIMESTAMP);
        return job;
    }

    @BeforeEach
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();
        // Create the Job
        restJobMockMvc.perform(post("/api/jobs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testJob.getJobStatusFlag()).isEqualTo(DEFAULT_JOB_STATUS_FLAG);
        assertThat(testJob.getCreateTimestamp()).isEqualTo(DEFAULT_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createJobWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job with an existing ID
        job.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc.perform(post("/api/jobs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME)))
            .andExpect(jsonPath("$.[*].jobStatusFlag").value(hasItem(DEFAULT_JOB_STATUS_FLAG)))
            .andExpect(jsonPath("$.[*].createTimestamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIMESTAMP))));
    }
    
    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME))
            .andExpect(jsonPath("$.jobStatusFlag").value(DEFAULT_JOB_STATUS_FLAG))
            .andExpect(jsonPath("$.createTimestamp").value(sameInstant(DEFAULT_CREATE_TIMESTAMP)));
    }


    @Test
    @Transactional
    public void getJobsByIdFiltering() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        Long id = job.getId();

        defaultJobShouldBeFound("id.equals=" + id);
        defaultJobShouldNotBeFound("id.notEquals=" + id);

        defaultJobShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobShouldNotBeFound("id.greaterThan=" + id);

        defaultJobShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllJobsByJobNameIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName equals to DEFAULT_JOB_NAME
        defaultJobShouldBeFound("jobName.equals=" + DEFAULT_JOB_NAME);

        // Get all the jobList where jobName equals to UPDATED_JOB_NAME
        defaultJobShouldNotBeFound("jobName.equals=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllJobsByJobNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName not equals to DEFAULT_JOB_NAME
        defaultJobShouldNotBeFound("jobName.notEquals=" + DEFAULT_JOB_NAME);

        // Get all the jobList where jobName not equals to UPDATED_JOB_NAME
        defaultJobShouldBeFound("jobName.notEquals=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllJobsByJobNameIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName in DEFAULT_JOB_NAME or UPDATED_JOB_NAME
        defaultJobShouldBeFound("jobName.in=" + DEFAULT_JOB_NAME + "," + UPDATED_JOB_NAME);

        // Get all the jobList where jobName equals to UPDATED_JOB_NAME
        defaultJobShouldNotBeFound("jobName.in=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllJobsByJobNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName is not null
        defaultJobShouldBeFound("jobName.specified=true");

        // Get all the jobList where jobName is null
        defaultJobShouldNotBeFound("jobName.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobsByJobNameContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName contains DEFAULT_JOB_NAME
        defaultJobShouldBeFound("jobName.contains=" + DEFAULT_JOB_NAME);

        // Get all the jobList where jobName contains UPDATED_JOB_NAME
        defaultJobShouldNotBeFound("jobName.contains=" + UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void getAllJobsByJobNameNotContainsSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobName does not contain DEFAULT_JOB_NAME
        defaultJobShouldNotBeFound("jobName.doesNotContain=" + DEFAULT_JOB_NAME);

        // Get all the jobList where jobName does not contain UPDATED_JOB_NAME
        defaultJobShouldBeFound("jobName.doesNotContain=" + UPDATED_JOB_NAME);
    }


    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag equals to DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.equals=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag equals to UPDATED_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.equals=" + UPDATED_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag not equals to DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.notEquals=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag not equals to UPDATED_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.notEquals=" + UPDATED_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag in DEFAULT_JOB_STATUS_FLAG or UPDATED_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.in=" + DEFAULT_JOB_STATUS_FLAG + "," + UPDATED_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag equals to UPDATED_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.in=" + UPDATED_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag is not null
        defaultJobShouldBeFound("jobStatusFlag.specified=true");

        // Get all the jobList where jobStatusFlag is null
        defaultJobShouldNotBeFound("jobStatusFlag.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag is greater than or equal to DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.greaterThanOrEqual=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag is greater than or equal to UPDATED_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.greaterThanOrEqual=" + UPDATED_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag is less than or equal to DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.lessThanOrEqual=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag is less than or equal to SMALLER_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.lessThanOrEqual=" + SMALLER_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag is less than DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.lessThan=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag is less than UPDATED_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.lessThan=" + UPDATED_JOB_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobsByJobStatusFlagIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where jobStatusFlag is greater than DEFAULT_JOB_STATUS_FLAG
        defaultJobShouldNotBeFound("jobStatusFlag.greaterThan=" + DEFAULT_JOB_STATUS_FLAG);

        // Get all the jobList where jobStatusFlag is greater than SMALLER_JOB_STATUS_FLAG
        defaultJobShouldBeFound("jobStatusFlag.greaterThan=" + SMALLER_JOB_STATUS_FLAG);
    }


    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp equals to DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.equals=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp equals to UPDATED_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.equals=" + UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp not equals to DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.notEquals=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp not equals to UPDATED_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.notEquals=" + UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp in DEFAULT_CREATE_TIMESTAMP or UPDATED_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.in=" + DEFAULT_CREATE_TIMESTAMP + "," + UPDATED_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp equals to UPDATED_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.in=" + UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp is not null
        defaultJobShouldBeFound("createTimestamp.specified=true");

        // Get all the jobList where createTimestamp is null
        defaultJobShouldNotBeFound("createTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp is greater than or equal to DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.greaterThanOrEqual=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp is greater than or equal to UPDATED_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.greaterThanOrEqual=" + UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp is less than or equal to DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.lessThanOrEqual=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp is less than or equal to SMALLER_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.lessThanOrEqual=" + SMALLER_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp is less than DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.lessThan=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp is less than UPDATED_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.lessThan=" + UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobsByCreateTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList where createTimestamp is greater than DEFAULT_CREATE_TIMESTAMP
        defaultJobShouldNotBeFound("createTimestamp.greaterThan=" + DEFAULT_CREATE_TIMESTAMP);

        // Get all the jobList where createTimestamp is greater than SMALLER_CREATE_TIMESTAMP
        defaultJobShouldBeFound("createTimestamp.greaterThan=" + SMALLER_CREATE_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllJobsByJobConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        JobConfig jobConfig = JobConfigResourceIT.createEntity(em);
        em.persist(jobConfig);
        em.flush();
        job.addJobConfig(jobConfig);
        jobRepository.saveAndFlush(job);
        Long jobConfigId = jobConfig.getId();

        // Get all the jobList where jobConfig equals to jobConfigId
        defaultJobShouldBeFound("jobConfigId.equals=" + jobConfigId);

        // Get all the jobList where jobConfig equals to jobConfigId + 1
        defaultJobShouldNotBeFound("jobConfigId.equals=" + (jobConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByJobTaskOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        JobTaskOrder jobTaskOrder = JobTaskOrderResourceIT.createEntity(em);
        em.persist(jobTaskOrder);
        em.flush();
        job.addJobTaskOrder(jobTaskOrder);
        jobRepository.saveAndFlush(job);
        Long jobTaskOrderId = jobTaskOrder.getId();

        // Get all the jobList where jobTaskOrder equals to jobTaskOrderId
        defaultJobShouldBeFound("jobTaskOrderId.equals=" + jobTaskOrderId);

        // Get all the jobList where jobTaskOrder equals to jobTaskOrderId + 1
        defaultJobShouldNotBeFound("jobTaskOrderId.equals=" + (jobTaskOrderId + 1));
    }


    @Test
    @Transactional
    public void getAllJobsByJobExecutionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        JobExecution jobExecution = JobExecutionResourceIT.createEntity(em);
        em.persist(jobExecution);
        em.flush();
        job.addJobExecution(jobExecution);
        jobRepository.saveAndFlush(job);
        Long jobExecutionId = jobExecution.getId();

        // Get all the jobList where jobExecution equals to jobExecutionId
        defaultJobShouldBeFound("jobExecutionId.equals=" + jobExecutionId);

        // Get all the jobList where jobExecution equals to jobExecutionId + 1
        defaultJobShouldNotBeFound("jobExecutionId.equals=" + (jobExecutionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobShouldBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME)))
            .andExpect(jsonPath("$.[*].jobStatusFlag").value(hasItem(DEFAULT_JOB_STATUS_FLAG)))
            .andExpect(jsonPath("$.[*].createTimestamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIMESTAMP))));

        // Check, that the count call also returns 1
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobShouldNotBeFound(String filter) throws Exception {
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobMockMvc.perform(get("/api/jobs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).get();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .jobName(UPDATED_JOB_NAME)
            .jobStatusFlag(UPDATED_JOB_STATUS_FLAG)
            .createTimestamp(UPDATED_CREATE_TIMESTAMP);

        restJobMockMvc.perform(put("/api/jobs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJob)))
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testJob.getJobStatusFlag()).isEqualTo(UPDATED_JOB_STATUS_FLAG);
        assertThat(testJob.getCreateTimestamp()).isEqualTo(UPDATED_CREATE_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc.perform(put("/api/jobs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(job)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobService.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Delete the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
