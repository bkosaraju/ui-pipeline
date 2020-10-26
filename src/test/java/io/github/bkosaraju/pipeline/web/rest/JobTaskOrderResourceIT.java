package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.domain.Job;
import io.github.bkosaraju.pipeline.domain.Task;
import io.github.bkosaraju.pipeline.repository.JobTaskOrderRepository;
import io.github.bkosaraju.pipeline.service.JobTaskOrderService;
import io.github.bkosaraju.pipeline.service.dto.JobTaskOrderCriteria;
import io.github.bkosaraju.pipeline.service.JobTaskOrderQueryService;

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
 * Integration tests for the {@link JobTaskOrderResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class JobTaskOrderResourceIT {

    private static final Integer DEFAULT_TASK_SEQ_ID = 1;
    private static final Integer UPDATED_TASK_SEQ_ID = 2;
    private static final Integer SMALLER_TASK_SEQ_ID = 1 - 1;

    private static final Boolean DEFAULT_JOB_TASK_STATUS_FLAG = false;
    private static final Boolean UPDATED_JOB_TASK_STATUS_FLAG = true;

    private static final Float DEFAULT_CONFIG_VERSION = 1F;
    private static final Float UPDATED_CONFIG_VERSION = 2F;
    private static final Float SMALLER_CONFIG_VERSION = 1F - 1F;

    @Autowired
    private JobTaskOrderRepository jobTaskOrderRepository;

    @Autowired
    private JobTaskOrderService jobTaskOrderService;

    @Autowired
    private JobTaskOrderQueryService jobTaskOrderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobTaskOrderMockMvc;

    private JobTaskOrder jobTaskOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTaskOrder createEntity(EntityManager em) {
        JobTaskOrder jobTaskOrder = new JobTaskOrder()
            .taskSeqId(DEFAULT_TASK_SEQ_ID)
            .jobTaskStatusFlag(DEFAULT_JOB_TASK_STATUS_FLAG)
            .configVersion(DEFAULT_CONFIG_VERSION);
        return jobTaskOrder;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTaskOrder createUpdatedEntity(EntityManager em) {
        JobTaskOrder jobTaskOrder = new JobTaskOrder()
            .taskSeqId(UPDATED_TASK_SEQ_ID)
            .jobTaskStatusFlag(UPDATED_JOB_TASK_STATUS_FLAG)
            .configVersion(UPDATED_CONFIG_VERSION);
        return jobTaskOrder;
    }

    @BeforeEach
    public void initTest() {
        jobTaskOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTaskOrder() throws Exception {
        int databaseSizeBeforeCreate = jobTaskOrderRepository.findAll().size();
        // Create the JobTaskOrder
        restJobTaskOrderMockMvc.perform(post("/api/job-task-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTaskOrder)))
            .andExpect(status().isCreated());

        // Validate the JobTaskOrder in the database
        List<JobTaskOrder> jobTaskOrderList = jobTaskOrderRepository.findAll();
        assertThat(jobTaskOrderList).hasSize(databaseSizeBeforeCreate + 1);
        JobTaskOrder testJobTaskOrder = jobTaskOrderList.get(jobTaskOrderList.size() - 1);
        assertThat(testJobTaskOrder.getTaskSeqId()).isEqualTo(DEFAULT_TASK_SEQ_ID);
        assertThat(testJobTaskOrder.isJobTaskStatusFlag()).isEqualTo(DEFAULT_JOB_TASK_STATUS_FLAG);
        assertThat(testJobTaskOrder.getConfigVersion()).isEqualTo(DEFAULT_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void createJobTaskOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobTaskOrderRepository.findAll().size();

        // Create the JobTaskOrder with an existing ID
        jobTaskOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobTaskOrderMockMvc.perform(post("/api/job-task-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTaskOrder)))
            .andExpect(status().isBadRequest());

        // Validate the JobTaskOrder in the database
        List<JobTaskOrder> jobTaskOrderList = jobTaskOrderRepository.findAll();
        assertThat(jobTaskOrderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJobTaskOrders() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTaskOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskSeqId").value(hasItem(DEFAULT_TASK_SEQ_ID)))
            .andExpect(jsonPath("$.[*].jobTaskStatusFlag").value(hasItem(DEFAULT_JOB_TASK_STATUS_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getJobTaskOrder() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get the jobTaskOrder
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders/{id}", jobTaskOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobTaskOrder.getId().intValue()))
            .andExpect(jsonPath("$.taskSeqId").value(DEFAULT_TASK_SEQ_ID))
            .andExpect(jsonPath("$.jobTaskStatusFlag").value(DEFAULT_JOB_TASK_STATUS_FLAG.booleanValue()))
            .andExpect(jsonPath("$.configVersion").value(DEFAULT_CONFIG_VERSION.doubleValue()));
    }


    @Test
    @Transactional
    public void getJobTaskOrdersByIdFiltering() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        Long id = jobTaskOrder.getId();

        defaultJobTaskOrderShouldBeFound("id.equals=" + id);
        defaultJobTaskOrderShouldNotBeFound("id.notEquals=" + id);

        defaultJobTaskOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobTaskOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultJobTaskOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobTaskOrderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId equals to DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.equals=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId equals to UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.equals=" + UPDATED_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId not equals to DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.notEquals=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId not equals to UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.notEquals=" + UPDATED_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsInShouldWork() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId in DEFAULT_TASK_SEQ_ID or UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.in=" + DEFAULT_TASK_SEQ_ID + "," + UPDATED_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId equals to UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.in=" + UPDATED_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId is not null
        defaultJobTaskOrderShouldBeFound("taskSeqId.specified=true");

        // Get all the jobTaskOrderList where taskSeqId is null
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId is greater than or equal to DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.greaterThanOrEqual=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId is greater than or equal to UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.greaterThanOrEqual=" + UPDATED_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId is less than or equal to DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.lessThanOrEqual=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId is less than or equal to SMALLER_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.lessThanOrEqual=" + SMALLER_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsLessThanSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId is less than DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.lessThan=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId is less than UPDATED_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.lessThan=" + UPDATED_TASK_SEQ_ID);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskSeqIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where taskSeqId is greater than DEFAULT_TASK_SEQ_ID
        defaultJobTaskOrderShouldNotBeFound("taskSeqId.greaterThan=" + DEFAULT_TASK_SEQ_ID);

        // Get all the jobTaskOrderList where taskSeqId is greater than SMALLER_TASK_SEQ_ID
        defaultJobTaskOrderShouldBeFound("taskSeqId.greaterThan=" + SMALLER_TASK_SEQ_ID);
    }


    @Test
    @Transactional
    public void getAllJobTaskOrdersByJobTaskStatusFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where jobTaskStatusFlag equals to DEFAULT_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldBeFound("jobTaskStatusFlag.equals=" + DEFAULT_JOB_TASK_STATUS_FLAG);

        // Get all the jobTaskOrderList where jobTaskStatusFlag equals to UPDATED_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldNotBeFound("jobTaskStatusFlag.equals=" + UPDATED_JOB_TASK_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByJobTaskStatusFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where jobTaskStatusFlag not equals to DEFAULT_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldNotBeFound("jobTaskStatusFlag.notEquals=" + DEFAULT_JOB_TASK_STATUS_FLAG);

        // Get all the jobTaskOrderList where jobTaskStatusFlag not equals to UPDATED_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldBeFound("jobTaskStatusFlag.notEquals=" + UPDATED_JOB_TASK_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByJobTaskStatusFlagIsInShouldWork() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where jobTaskStatusFlag in DEFAULT_JOB_TASK_STATUS_FLAG or UPDATED_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldBeFound("jobTaskStatusFlag.in=" + DEFAULT_JOB_TASK_STATUS_FLAG + "," + UPDATED_JOB_TASK_STATUS_FLAG);

        // Get all the jobTaskOrderList where jobTaskStatusFlag equals to UPDATED_JOB_TASK_STATUS_FLAG
        defaultJobTaskOrderShouldNotBeFound("jobTaskStatusFlag.in=" + UPDATED_JOB_TASK_STATUS_FLAG);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByJobTaskStatusFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where jobTaskStatusFlag is not null
        defaultJobTaskOrderShouldBeFound("jobTaskStatusFlag.specified=true");

        // Get all the jobTaskOrderList where jobTaskStatusFlag is null
        defaultJobTaskOrderShouldNotBeFound("jobTaskStatusFlag.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion equals to DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.equals=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.equals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion not equals to DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.notEquals=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion not equals to UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.notEquals=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsInShouldWork() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion in DEFAULT_CONFIG_VERSION or UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.in=" + DEFAULT_CONFIG_VERSION + "," + UPDATED_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion equals to UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.in=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion is not null
        defaultJobTaskOrderShouldBeFound("configVersion.specified=true");

        // Get all the jobTaskOrderList where configVersion is null
        defaultJobTaskOrderShouldNotBeFound("configVersion.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion is greater than or equal to DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.greaterThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion is greater than or equal to UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.greaterThanOrEqual=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion is less than or equal to DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.lessThanOrEqual=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion is less than or equal to SMALLER_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.lessThanOrEqual=" + SMALLER_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion is less than DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.lessThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion is less than UPDATED_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.lessThan=" + UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void getAllJobTaskOrdersByConfigVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);

        // Get all the jobTaskOrderList where configVersion is greater than DEFAULT_CONFIG_VERSION
        defaultJobTaskOrderShouldNotBeFound("configVersion.greaterThan=" + DEFAULT_CONFIG_VERSION);

        // Get all the jobTaskOrderList where configVersion is greater than SMALLER_CONFIG_VERSION
        defaultJobTaskOrderShouldBeFound("configVersion.greaterThan=" + SMALLER_CONFIG_VERSION);
    }


    @Test
    @Transactional
    public void getAllJobTaskOrdersByJobIsEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);
        Job job = JobResourceIT.createEntity(em);
        em.persist(job);
        em.flush();
        jobTaskOrder.setJob(job);
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);
        Long jobId = job.getId();

        // Get all the jobTaskOrderList where job equals to jobId
        defaultJobTaskOrderShouldBeFound("jobId.equals=" + jobId);

        // Get all the jobTaskOrderList where job equals to jobId + 1
        defaultJobTaskOrderShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }


    @Test
    @Transactional
    public void getAllJobTaskOrdersByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);
        Task task = TaskResourceIT.createEntity(em);
        em.persist(task);
        em.flush();
        jobTaskOrder.setTask(task);
        jobTaskOrderRepository.saveAndFlush(jobTaskOrder);
        Long taskId = task.getId();

        // Get all the jobTaskOrderList where task equals to taskId
        defaultJobTaskOrderShouldBeFound("taskId.equals=" + taskId);

        // Get all the jobTaskOrderList where task equals to taskId + 1
        defaultJobTaskOrderShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobTaskOrderShouldBeFound(String filter) throws Exception {
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobTaskOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskSeqId").value(hasItem(DEFAULT_TASK_SEQ_ID)))
            .andExpect(jsonPath("$.[*].jobTaskStatusFlag").value(hasItem(DEFAULT_JOB_TASK_STATUS_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].configVersion").value(hasItem(DEFAULT_CONFIG_VERSION.doubleValue())));

        // Check, that the count call also returns 1
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobTaskOrderShouldNotBeFound(String filter) throws Exception {
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingJobTaskOrder() throws Exception {
        // Get the jobTaskOrder
        restJobTaskOrderMockMvc.perform(get("/api/job-task-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTaskOrder() throws Exception {
        // Initialize the database
        jobTaskOrderService.save(jobTaskOrder);

        int databaseSizeBeforeUpdate = jobTaskOrderRepository.findAll().size();

        // Update the jobTaskOrder
        JobTaskOrder updatedJobTaskOrder = jobTaskOrderRepository.findById(jobTaskOrder.getId()).get();
        // Disconnect from session so that the updates on updatedJobTaskOrder are not directly saved in db
        em.detach(updatedJobTaskOrder);
        updatedJobTaskOrder
            .taskSeqId(UPDATED_TASK_SEQ_ID)
            .jobTaskStatusFlag(UPDATED_JOB_TASK_STATUS_FLAG)
            .configVersion(UPDATED_CONFIG_VERSION);

        restJobTaskOrderMockMvc.perform(put("/api/job-task-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobTaskOrder)))
            .andExpect(status().isOk());

        // Validate the JobTaskOrder in the database
        List<JobTaskOrder> jobTaskOrderList = jobTaskOrderRepository.findAll();
        assertThat(jobTaskOrderList).hasSize(databaseSizeBeforeUpdate);
        JobTaskOrder testJobTaskOrder = jobTaskOrderList.get(jobTaskOrderList.size() - 1);
        assertThat(testJobTaskOrder.getTaskSeqId()).isEqualTo(UPDATED_TASK_SEQ_ID);
        assertThat(testJobTaskOrder.isJobTaskStatusFlag()).isEqualTo(UPDATED_JOB_TASK_STATUS_FLAG);
        assertThat(testJobTaskOrder.getConfigVersion()).isEqualTo(UPDATED_CONFIG_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingJobTaskOrder() throws Exception {
        int databaseSizeBeforeUpdate = jobTaskOrderRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobTaskOrderMockMvc.perform(put("/api/job-task-orders").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(jobTaskOrder)))
            .andExpect(status().isBadRequest());

        // Validate the JobTaskOrder in the database
        List<JobTaskOrder> jobTaskOrderList = jobTaskOrderRepository.findAll();
        assertThat(jobTaskOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJobTaskOrder() throws Exception {
        // Initialize the database
        jobTaskOrderService.save(jobTaskOrder);

        int databaseSizeBeforeDelete = jobTaskOrderRepository.findAll().size();

        // Delete the jobTaskOrder
        restJobTaskOrderMockMvc.perform(delete("/api/job-task-orders/{id}", jobTaskOrder.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobTaskOrder> jobTaskOrderList = jobTaskOrderRepository.findAll();
        assertThat(jobTaskOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
