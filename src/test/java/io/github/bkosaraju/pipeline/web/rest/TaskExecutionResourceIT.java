package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.domain.TaskExecutionConfig;
import io.github.bkosaraju.pipeline.domain.Task;
import io.github.bkosaraju.pipeline.domain.JobExecution;
import io.github.bkosaraju.pipeline.repository.TaskExecutionRepository;
import io.github.bkosaraju.pipeline.service.TaskExecutionService;
import io.github.bkosaraju.pipeline.service.dto.TaskExecutionCriteria;
import io.github.bkosaraju.pipeline.service.TaskExecutionQueryService;

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
 * Integration tests for the {@link TaskExecutionResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TaskExecutionResourceIT {

    private static final ZonedDateTime DEFAULT_JOB_ORDER_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_JOB_ORDER_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_JOB_ORDER_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TASK_EXECUTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TASK_EXECUTION_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TASK_EXECUTION_START_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TASK_EXECUTION_START_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TASK_EXECUTION_START_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_TASK_EXECUTION_END_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TASK_EXECUTION_END_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TASK_EXECUTION_END_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private TaskExecutionRepository taskExecutionRepository;

    @Autowired
    private TaskExecutionService taskExecutionService;

    @Autowired
    private TaskExecutionQueryService taskExecutionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskExecutionMockMvc;

    private TaskExecution taskExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskExecution createEntity(EntityManager em) {
        TaskExecution taskExecution = new TaskExecution()
            .jobOrderTimestamp(DEFAULT_JOB_ORDER_TIMESTAMP)
            .taskExecutionStatus(DEFAULT_TASK_EXECUTION_STATUS)
            .taskExecutionStartTimestamp(DEFAULT_TASK_EXECUTION_START_TIMESTAMP)
            .taskExecutionEndTimestamp(DEFAULT_TASK_EXECUTION_END_TIMESTAMP);
        return taskExecution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskExecution createUpdatedEntity(EntityManager em) {
        TaskExecution taskExecution = new TaskExecution()
            .jobOrderTimestamp(UPDATED_JOB_ORDER_TIMESTAMP)
            .taskExecutionStatus(UPDATED_TASK_EXECUTION_STATUS)
            .taskExecutionStartTimestamp(UPDATED_TASK_EXECUTION_START_TIMESTAMP)
            .taskExecutionEndTimestamp(UPDATED_TASK_EXECUTION_END_TIMESTAMP);
        return taskExecution;
    }

    @BeforeEach
    public void initTest() {
        taskExecution = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskExecution() throws Exception {
        int databaseSizeBeforeCreate = taskExecutionRepository.findAll().size();
        // Create the TaskExecution
        restTaskExecutionMockMvc.perform(post("/api/task-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecution)))
            .andExpect(status().isCreated());

        // Validate the TaskExecution in the database
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findAll();
        assertThat(taskExecutionList).hasSize(databaseSizeBeforeCreate + 1);
        TaskExecution testTaskExecution = taskExecutionList.get(taskExecutionList.size() - 1);
        assertThat(testTaskExecution.getJobOrderTimestamp()).isEqualTo(DEFAULT_JOB_ORDER_TIMESTAMP);
        assertThat(testTaskExecution.getTaskExecutionStatus()).isEqualTo(DEFAULT_TASK_EXECUTION_STATUS);
        assertThat(testTaskExecution.getTaskExecutionStartTimestamp()).isEqualTo(DEFAULT_TASK_EXECUTION_START_TIMESTAMP);
        assertThat(testTaskExecution.getTaskExecutionEndTimestamp()).isEqualTo(DEFAULT_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createTaskExecutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskExecutionRepository.findAll().size();

        // Create the TaskExecution with an existing ID
        taskExecution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskExecutionMockMvc.perform(post("/api/task-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecution)))
            .andExpect(status().isBadRequest());

        // Validate the TaskExecution in the database
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findAll();
        assertThat(taskExecutionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTaskExecutions() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList
        restTaskExecutionMockMvc.perform(get("/api/task-executions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobOrderTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].taskExecutionStatus").value(hasItem(DEFAULT_TASK_EXECUTION_STATUS)))
            .andExpect(jsonPath("$.[*].taskExecutionStartTimestamp").value(hasItem(sameInstant(DEFAULT_TASK_EXECUTION_START_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].taskExecutionEndTimestamp").value(hasItem(sameInstant(DEFAULT_TASK_EXECUTION_END_TIMESTAMP))));
    }
    
    @Test
    @Transactional
    public void getTaskExecution() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get the taskExecution
        restTaskExecutionMockMvc.perform(get("/api/task-executions/{id}", taskExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taskExecution.getId().intValue()))
            .andExpect(jsonPath("$.jobOrderTimestamp").value(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP)))
            .andExpect(jsonPath("$.taskExecutionStatus").value(DEFAULT_TASK_EXECUTION_STATUS))
            .andExpect(jsonPath("$.taskExecutionStartTimestamp").value(sameInstant(DEFAULT_TASK_EXECUTION_START_TIMESTAMP)))
            .andExpect(jsonPath("$.taskExecutionEndTimestamp").value(sameInstant(DEFAULT_TASK_EXECUTION_END_TIMESTAMP)));
    }


    @Test
    @Transactional
    public void getTaskExecutionsByIdFiltering() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        Long id = taskExecution.getId();

        defaultTaskExecutionShouldBeFound("id.equals=" + id);
        defaultTaskExecutionShouldNotBeFound("id.notEquals=" + id);

        defaultTaskExecutionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskExecutionShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskExecutionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskExecutionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp equals to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.equals=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.equals=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp not equals to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.notEquals=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp not equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.notEquals=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp in DEFAULT_JOB_ORDER_TIMESTAMP or UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.in=" + DEFAULT_JOB_ORDER_TIMESTAMP + "," + UPDATED_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp equals to UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.in=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp is not null
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.specified=true");

        // Get all the taskExecutionList where jobOrderTimestamp is null
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp is greater than or equal to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.greaterThanOrEqual=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp is greater than or equal to UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.greaterThanOrEqual=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp is less than or equal to DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.lessThanOrEqual=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp is less than or equal to SMALLER_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.lessThanOrEqual=" + SMALLER_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp is less than DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.lessThan=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp is less than UPDATED_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.lessThan=" + UPDATED_JOB_ORDER_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByJobOrderTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where jobOrderTimestamp is greater than DEFAULT_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("jobOrderTimestamp.greaterThan=" + DEFAULT_JOB_ORDER_TIMESTAMP);

        // Get all the taskExecutionList where jobOrderTimestamp is greater than SMALLER_JOB_ORDER_TIMESTAMP
        defaultTaskExecutionShouldBeFound("jobOrderTimestamp.greaterThan=" + SMALLER_JOB_ORDER_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus equals to DEFAULT_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.equals=" + DEFAULT_TASK_EXECUTION_STATUS);

        // Get all the taskExecutionList where taskExecutionStatus equals to UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.equals=" + UPDATED_TASK_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus not equals to DEFAULT_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.notEquals=" + DEFAULT_TASK_EXECUTION_STATUS);

        // Get all the taskExecutionList where taskExecutionStatus not equals to UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.notEquals=" + UPDATED_TASK_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus in DEFAULT_TASK_EXECUTION_STATUS or UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.in=" + DEFAULT_TASK_EXECUTION_STATUS + "," + UPDATED_TASK_EXECUTION_STATUS);

        // Get all the taskExecutionList where taskExecutionStatus equals to UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.in=" + UPDATED_TASK_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus is not null
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.specified=true");

        // Get all the taskExecutionList where taskExecutionStatus is null
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.specified=false");
    }
                @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus contains DEFAULT_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.contains=" + DEFAULT_TASK_EXECUTION_STATUS);

        // Get all the taskExecutionList where taskExecutionStatus contains UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.contains=" + UPDATED_TASK_EXECUTION_STATUS);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStatusNotContainsSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStatus does not contain DEFAULT_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldNotBeFound("taskExecutionStatus.doesNotContain=" + DEFAULT_TASK_EXECUTION_STATUS);

        // Get all the taskExecutionList where taskExecutionStatus does not contain UPDATED_TASK_EXECUTION_STATUS
        defaultTaskExecutionShouldBeFound("taskExecutionStatus.doesNotContain=" + UPDATED_TASK_EXECUTION_STATUS);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp equals to DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.equals=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp equals to UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.equals=" + UPDATED_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp not equals to DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.notEquals=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp not equals to UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.notEquals=" + UPDATED_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp in DEFAULT_TASK_EXECUTION_START_TIMESTAMP or UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.in=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP + "," + UPDATED_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp equals to UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.in=" + UPDATED_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is not null
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.specified=true");

        // Get all the taskExecutionList where taskExecutionStartTimestamp is null
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is greater than or equal to DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.greaterThanOrEqual=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is greater than or equal to UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.greaterThanOrEqual=" + UPDATED_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is less than or equal to DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.lessThanOrEqual=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is less than or equal to SMALLER_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.lessThanOrEqual=" + SMALLER_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is less than DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.lessThan=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is less than UPDATED_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.lessThan=" + UPDATED_TASK_EXECUTION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionStartTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is greater than DEFAULT_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionStartTimestamp.greaterThan=" + DEFAULT_TASK_EXECUTION_START_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionStartTimestamp is greater than SMALLER_TASK_EXECUTION_START_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionStartTimestamp.greaterThan=" + SMALLER_TASK_EXECUTION_START_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp equals to DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.equals=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp equals to UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.equals=" + UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp not equals to DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.notEquals=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp not equals to UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.notEquals=" + UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp in DEFAULT_TASK_EXECUTION_END_TIMESTAMP or UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.in=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP + "," + UPDATED_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp equals to UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.in=" + UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is not null
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.specified=true");

        // Get all the taskExecutionList where taskExecutionEndTimestamp is null
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is greater than or equal to DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.greaterThanOrEqual=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is greater than or equal to UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.greaterThanOrEqual=" + UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is less than or equal to DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.lessThanOrEqual=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is less than or equal to SMALLER_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.lessThanOrEqual=" + SMALLER_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsLessThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is less than DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.lessThan=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is less than UPDATED_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.lessThan=" + UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionEndTimestampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is greater than DEFAULT_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldNotBeFound("taskExecutionEndTimestamp.greaterThan=" + DEFAULT_TASK_EXECUTION_END_TIMESTAMP);

        // Get all the taskExecutionList where taskExecutionEndTimestamp is greater than SMALLER_TASK_EXECUTION_END_TIMESTAMP
        defaultTaskExecutionShouldBeFound("taskExecutionEndTimestamp.greaterThan=" + SMALLER_TASK_EXECUTION_END_TIMESTAMP);
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskExecutionConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);
        TaskExecutionConfig taskExecutionConfig = TaskExecutionConfigResourceIT.createEntity(em);
        em.persist(taskExecutionConfig);
        em.flush();
        taskExecution.addTaskExecutionConfig(taskExecutionConfig);
        taskExecutionRepository.saveAndFlush(taskExecution);
        Long taskExecutionConfigId = taskExecutionConfig.getId();

        // Get all the taskExecutionList where taskExecutionConfig equals to taskExecutionConfigId
        defaultTaskExecutionShouldBeFound("taskExecutionConfigId.equals=" + taskExecutionConfigId);

        // Get all the taskExecutionList where taskExecutionConfig equals to taskExecutionConfigId + 1
        defaultTaskExecutionShouldNotBeFound("taskExecutionConfigId.equals=" + (taskExecutionConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        taskExecution.setTask(task);
        taskExecutionRepository.saveAndFlush(taskExecution);
        Long taskId = task.getId();

        // Get all the taskExecutionList where task equals to taskId
        defaultTaskExecutionShouldBeFound("taskId.equals=" + taskId);

        // Get all the taskExecutionList where task equals to taskId + 1
        defaultTaskExecutionShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }


    @Test
    @Transactional
    public void getAllTaskExecutionsByJobExecutionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskExecutionRepository.saveAndFlush(taskExecution);
        JobExecution jobExecution = JobExecutionResourceIT.createEntity(em);
        em.persist(jobExecution);
        em.flush();
        taskExecution.setJobExecution(jobExecution);
        taskExecutionRepository.saveAndFlush(taskExecution);
        Long jobExecutionId = jobExecution.getId();

        // Get all the taskExecutionList where jobExecution equals to jobExecutionId
        defaultTaskExecutionShouldBeFound("jobExecutionId.equals=" + jobExecutionId);

        // Get all the taskExecutionList where jobExecution equals to jobExecutionId + 1
        defaultTaskExecutionShouldNotBeFound("jobExecutionId.equals=" + (jobExecutionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskExecutionShouldBeFound(String filter) throws Exception {
        restTaskExecutionMockMvc.perform(get("/api/task-executions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobOrderTimestamp").value(hasItem(sameInstant(DEFAULT_JOB_ORDER_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].taskExecutionStatus").value(hasItem(DEFAULT_TASK_EXECUTION_STATUS)))
            .andExpect(jsonPath("$.[*].taskExecutionStartTimestamp").value(hasItem(sameInstant(DEFAULT_TASK_EXECUTION_START_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].taskExecutionEndTimestamp").value(hasItem(sameInstant(DEFAULT_TASK_EXECUTION_END_TIMESTAMP))));

        // Check, that the count call also returns 1
        restTaskExecutionMockMvc.perform(get("/api/task-executions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskExecutionShouldNotBeFound(String filter) throws Exception {
        restTaskExecutionMockMvc.perform(get("/api/task-executions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskExecutionMockMvc.perform(get("/api/task-executions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTaskExecution() throws Exception {
        // Get the taskExecution
        restTaskExecutionMockMvc.perform(get("/api/task-executions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskExecution() throws Exception {
        // Initialize the database
        taskExecutionService.save(taskExecution);

        int databaseSizeBeforeUpdate = taskExecutionRepository.findAll().size();

        // Update the taskExecution
        TaskExecution updatedTaskExecution = taskExecutionRepository.findById(taskExecution.getId()).get();
        // Disconnect from session so that the updates on updatedTaskExecution are not directly saved in db
        em.detach(updatedTaskExecution);
        updatedTaskExecution
            .jobOrderTimestamp(UPDATED_JOB_ORDER_TIMESTAMP)
            .taskExecutionStatus(UPDATED_TASK_EXECUTION_STATUS)
            .taskExecutionStartTimestamp(UPDATED_TASK_EXECUTION_START_TIMESTAMP)
            .taskExecutionEndTimestamp(UPDATED_TASK_EXECUTION_END_TIMESTAMP);

        restTaskExecutionMockMvc.perform(put("/api/task-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskExecution)))
            .andExpect(status().isOk());

        // Validate the TaskExecution in the database
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findAll();
        assertThat(taskExecutionList).hasSize(databaseSizeBeforeUpdate);
        TaskExecution testTaskExecution = taskExecutionList.get(taskExecutionList.size() - 1);
        assertThat(testTaskExecution.getJobOrderTimestamp()).isEqualTo(UPDATED_JOB_ORDER_TIMESTAMP);
        assertThat(testTaskExecution.getTaskExecutionStatus()).isEqualTo(UPDATED_TASK_EXECUTION_STATUS);
        assertThat(testTaskExecution.getTaskExecutionStartTimestamp()).isEqualTo(UPDATED_TASK_EXECUTION_START_TIMESTAMP);
        assertThat(testTaskExecution.getTaskExecutionEndTimestamp()).isEqualTo(UPDATED_TASK_EXECUTION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskExecution() throws Exception {
        int databaseSizeBeforeUpdate = taskExecutionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskExecutionMockMvc.perform(put("/api/task-executions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskExecution)))
            .andExpect(status().isBadRequest());

        // Validate the TaskExecution in the database
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findAll();
        assertThat(taskExecutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaskExecution() throws Exception {
        // Initialize the database
        taskExecutionService.save(taskExecution);

        int databaseSizeBeforeDelete = taskExecutionRepository.findAll().size();

        // Delete the taskExecution
        restTaskExecutionMockMvc.perform(delete("/api/task-executions/{id}", taskExecution.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findAll();
        assertThat(taskExecutionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
