package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.domain.Job;
import io.github.bkosaraju.pipeline.repository.JobExecutionRepository;
import io.github.bkosaraju.pipeline.service.JobExecutionService;
import io.github.bkosaraju.pipeline.service.dto.JobExecutionCriteria;
import io.github.bkosaraju.pipeline.service.JobExecutionQueryService;

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
 * Integration tests for the {@link JobExecutionResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class JobExecutionResourceIT {

    private static final ZonedDateTime DEFAULT_JOB_EXECUTION_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_JOB_EXECUTION_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_JOB_EXECUTION_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_JOB_ORDER_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_JOB_ORDER_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_JOB_ORDER_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_JOB_EXECUTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_JOB_EXECUTION_STATUS = "BBBBBBBBBB";

    @Autowired
    private JobExecutionRepository jobExecutionRepository;

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private JobExecutionQueryService jobExecutionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobExecutionMockMvc;

    private JobExecution jobExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobExecution createEntity(EntityManager em) {
        JobExecution jobExecution = new JobExecution()
            .jobExecutionTimestamp(DEFAULT_JOB_EXECUTION_TIMESTAMP)
            .jobOrderTimestamp(DEFAULT_JOB_ORDER_TIMESTAMP)
            .jobExecutionStatus(DEFAULT_JOB_EXECUTION_STATUS);
        return jobExecution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobExecution createUpdatedEntity(EntityManager em) {
        JobExecution jobExecution = new JobExecution()
            .jobExecutionTimestamp(UPDATED_JOB_EXECUTION_TIMESTAMP)
            .jobOrderTimestamp(UPDATED_JOB_ORDER_TIMESTAMP)
            .jobExecutionStatus(UPDATED_JOB_EXECUTION_STATUS);
        return jobExecution;
    }

    @BeforeEach
    public void initTest() {
        jobExecution = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobExecution() throws Exception {
        int databaseSizeBeforeCreate = jobExecutionRepository.findAll().size();
        // Create the JobExecution
        restJobExecutionMockMvc.perform(post("/api/job-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobExecution)))
            .andExpect(status().isCreated());

        // Validate the JobExecution in the database
        List<JobExecution> jobExecutionList = jobExecutionRepository.findAll();
        assertThat(jobExecutionList).hasSize(databaseSizeBeforeCreate + 1);
        JobExecution testJobExecution = jobExecutionList.get(jobExecutionList.size() - 1);
        assertThat(testJobExecution.getJobExecutionTimestamp()).isEqualTo(DEFAULT_JOB_EXECUTION_TIMESTAMP);
        assertThat(testJobExecution.getJobOrderTimestamp()).isEqualTo(DEFAULT_JOB_ORDER_TIMESTAMP);
        assertThat(testJobExecution.getJobExecutionStatus()).isEqualTo(DEFAULT_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void createJobExecutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobExecutionRepository.findAll().size();

        // Create the JobExecution with an existing ID
        jobExecution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobExecutionMockMvc.perform(post("/api/job-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobExecution)))
            .andExpect(status().isBadRequest());

        // Validate the JobExecution in the database
        List<JobExecution> jobExecutionList = jobExecutionRepository.findAll();
        assertThat(jobExecutionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJobExecutions() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList
        restJobExecutionMockMvc.perform(get("/api/job-executions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobExecutionTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_EXECUTION_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].jobOrderTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].jobExecutionStatus").value(hasItem(DEFAULT_JOB_EXECUTION_STATUS)));
    }
    
    @Test
    @Transactional
    public void getJobExecution() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get the jobExecution
        restJobExecutionMockMvc.perform(get("/api/job-executions/{id}", jobExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobExecution.getId().intValue()))
            .andExpect(jsonPath("$.jobExecutionTimestamp").value(sameInstant(DEFAULT_JOB_EXECUTION_TIMESTAMP)))
            .andExpect(jsonPath("$.jobOrderTimestamp").value(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP)))
            .andExpect(jsonPath("$.jobExecutionStatus").value(DEFAULT_JOB_EXECUTION_STATUS));
    }


    @Test
    @Transactional
    public void getJobExecutionsByIdFiltering() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        Long id = jobExecution.getId();

        defaultJobExecutionShouldBeFound("id.equals=" + id);
        defaultJobExecutionShouldNotBeFound("id.notEquals=" + id);

        defaultJobExecutionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobExecutionShouldNotBeFound("id.greaterThan=" + id);

        defaultJobExecutionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobExecutionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp equals to DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.equals=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp equals to UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.equals=" + UPDATED_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp not equals to DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.notEquals=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp not equals to UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.notEquals=" + UPDATED_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp in DEFAULT_JOB_EXECUTION_TIMESTAMP or UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.in=" + DEFAULT_JOB_EXECUTION_TIMESTAMP + "," + UPDATED_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp equals to UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.in=" + UPDATED_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp is not null
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.specified=true");

        // Get all the jobExecutionList where jobExecutionTimestamp is null
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp is greater than or equal to DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.greaterThanOrEqual=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp is greater than or equal to UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.greaterThanOrEqual=" + UPDATED_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp is less than or equal to DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.lessThanOrEqual=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp is less than or equal to SMALLER_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.lessThanOrEqual=" + SMALLER_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp is less than DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.lessThan=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp is less than UPDATED_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.lessThan=" + UPDATED_JOB_EXECUTION_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionTimestamp is greater than DEFAULT_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobExecutionTimestamp.greaterThan=" + DEFAULT_JOB_EXECUTION_TIMESTAMP);

        // Get all the jobExecutionList where jobExecutionTimestamp is greater than SMALLER_JOB_EXECUTION_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobExecutionTimestamp.greaterThan=" + SMALLER_JOB_EXECUTION_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp equals to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.equals=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.equals=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp not equals to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.notEquals=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp not equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.notEquals=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp in DEFAULT_JOB_ORDER_TIMESTAMP or UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.in=" + DEFAULT_JOB_ORDER_TIMESTAMP + "," + UPDATED_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.in=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp is not null
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.specified=true");

        // Get all the jobExecutionList where jobOrderTimestamp is null
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp is greater than or equal to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.greaterThanOrEqual=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp is greater than or equal to UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.greaterThanOrEqual=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp is less than or equal to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.lessThanOrEqual=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp is less than or equal to SMALLER_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.lessThanOrEqual=" + SMALLER_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp is less than DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.lessThan=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp is less than UPDATED_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.lessThan=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobOrderTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobOrderTimestamp is greater than DEFAULT_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldNotBeFound("jobOrderTimestamp.greaterThan=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the jobExecutionList where jobOrderTimestamp is greater than SMALLER_JOB_ORDER_TIMESTAMP
        defaultJobExecutionShouldBeFound("jobOrderTimestamp.greaterThan=" + SMALLER_JOB_ORDER_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus equals to DEFAULT_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldBeFound("jobExecutionStatus.equals=" + DEFAULT_JOB_EXECUTION_STATUS);

        // Get all the jobExecutionList where jobExecutionStatus equals to UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.equals=" + UPDATED_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus not equals to DEFAULT_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.notEquals=" + DEFAULT_JOB_EXECUTION_STATUS);

        // Get all the jobExecutionList where jobExecutionStatus not equals to UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldBeFound("jobExecutionStatus.notEquals=" + UPDATED_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus in DEFAULT_JOB_EXECUTION_STATUS or UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldBeFound("jobExecutionStatus.in=" + DEFAULT_JOB_EXECUTION_STATUS + "," + UPDATED_JOB_EXECUTION_STATUS);

        // Get all the jobExecutionList where jobExecutionStatus equals to UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.in=" + UPDATED_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus is not null
        defaultJobExecutionShouldBeFound("jobExecutionStatus.specified=true");

        // Get all the jobExecutionList where jobExecutionStatus is null
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.specified=false");
    }
                @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusContainsSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus contains DEFAULT_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldBeFound("jobExecutionStatus.contains=" + DEFAULT_JOB_EXECUTION_STATUS);

        // Get all the jobExecutionList where jobExecutionStatus contains UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.contains=" + UPDATED_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllJobExecutionsByJobExecutionStatusNotContainsSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);

        // Get all the jobExecutionList where jobExecutionStatus does not contain DEFAULT_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldNotBeFound("jobExecutionStatus.doesNotContain=" + DEFAULT_JOB_EXECUTION_STATUS);

        // Get all the jobExecutionList where jobExecutionStatus does not contain UPDATED_JOB_EXECUTION_STATUS
        defaultJobExecutionShouldBeFound("jobExecutionStatus.doesNotContain=" + UPDATED_JOB_EXECUTION_STATUS);
    }


    @Test
    @Transactional
    public void getAllJobExecutionsByTaskExecutionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);
        TaskExecution taskExecution = TaskExecutionResourceIT.createEntity(em);
        em.persist(taskExecution);
        em.flush();
        jobExecution.addTaskExecution(taskExecution);
        jobExecutionRepository.saveAndFlush(jobExecution);
        Long taskExecutionId = taskExecution.getId();

        // Get all the jobExecutionList where taskExecution equals to taskExecutionId
        defaultJobExecutionShouldBeFound("taskExecutionId.equals=" + taskExecutionId);

        // Get all the jobExecutionList where taskExecution equals to taskExecutionId + 1
        defaultJobExecutionShouldNotBeFound("taskExecutionId.equals=" + (taskExecutionId + 1));
    }


    @Test
    @Transactional
    public void getAllJobExecutionsByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        jobExecutionRepository.saveAndFlush(jobExecution);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        jobExecution.setJob(job);
        jobExecutionRepository.saveAndFlush(jobExecution);
        Long jobId = job.getId();

        // Get all the jobExecutionList where job equals to jobId
        defaultJobExecutionShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobExecutionList where job equals to jobId + 1
        defaultJobExecutionShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobExecutionShouldBeFound(String filter) throws Exception {
        restJobExecutionMockMvc.perform(get("/api/job-executions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobExecutionTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_EXECUTION_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].jobOrderTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].jobExecutionStatus").value(hasItem(DEFAULT_JOB_EXECUTION_STATUS)));

        // Check, that the count call also returns 1
        restJobExecutionMockMvc.perform(get("/api/job-executions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobExecutionShouldNotBeFound(String filter) throws Exception {
        restJobExecutionMockMvc.perform(get("/api/job-executions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobExecutionMockMvc.perform(get("/api/job-executions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingJobExecution() throws Exception {
        // Get the jobExecution
        restJobExecutionMockMvc.perform(get("/api/job-executions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobExecution() throws Exception {
        // Initialize the database
        jobExecutionService.save(jobExecution);

        int databaseSizeBeforeUpdate = jobExecutionRepository.findAll().size();

        // Update the jobExecution
        JobExecution updatedJobExecution = jobExecutionRepository.findById(jobExecution.getId()).get();
        // Disconnect from session so that the updates on updatedJobExecution are not directly saved in db
        em.detach(updatedJobExecution);
        updatedJobExecution
            .jobExecutionTimestamp(UPDATED_JOB_EXECUTION_TIMESTAMP)
            .jobOrderTimestamp(UPDATED_JOB_ORDER_TIMESTAMP)
            .jobExecutionStatus(UPDATED_JOB_EXECUTION_STATUS);

        restJobExecutionMockMvc.perform(put("/api/job-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobExecution)))
            .andExpect(status().isOk());

        // Validate the JobExecution in the database
        List<JobExecution> jobExecutionList = jobExecutionRepository.findAll();
        assertThat(jobExecutionList).hasSize(databaseSizeBeforeUpdate);
        JobExecution testJobExecution = jobExecutionList.get(jobExecutionList.size() - 1);
        assertThat(testJobExecution.getJobExecutionTimestamp()).isEqualTo(UPDATED_JOB_EXECUTION_TIMESTAMP);
        assertThat(testJobExecution.getJobOrderTimestamp()).isEqualTo(UPDATED_JOB_ORDER_TIMESTAMP);
        assertThat(testJobExecution.getJobExecutionStatus()).isEqualTo(UPDATED_JOB_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingJobExecution() throws Exception {
        int databaseSizeBeforeUpdate = jobExecutionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobExecutionMockMvc.perform(put("/api/job-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobExecution)))
            .andExpect(status().isBadRequest());

        // Validate the JobExecution in the database
        List<JobExecution> jobExecutionList = jobExecutionRepository.findAll();
        assertThat(jobExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobExecution() throws Exception {
        // Initialize the database
        jobExecutionService.save(jobExecution);

        int databaseSizeBeforeDelete = jobExecutionRepository.findAll().size();

        // Delete the jobExecution
        restJobExecutionMockMvc.perform(delete("/api/job-executions/{id}", jobExecution.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobExecution> jobExecutionList = jobExecutionRepository.findAll();
        assertThat(jobExecutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
