package io.github.bkosaraju.pipeline.web.rest;

import static io.github.bkosaraju.pipeline.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.JobTaskOrder;
import io.github.bkosaraju.pipeline.domain.Task;
import io.github.bkosaraju.pipeline.domain.TaskConfig;
import io.github.bkosaraju.pipeline.domain.TaskExecution;
import io.github.bkosaraju.pipeline.domain.enumeration.TaskType;
import io.github.bkosaraju.pipeline.repository.TaskRepository;
import io.github.bkosaraju.pipeline.service.TaskQueryService;
import io.github.bkosaraju.pipeline.service.TaskService;
import io.github.bkosaraju.pipeline.service.dto.TaskCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TaskResourceIT {
    private static final String DEFAULT_TASK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TASK_NAME = "BBBBBBBBBB";

    private static final TaskType DEFAULT_TASK_TYPE = TaskType.SFTP;
    private static final TaskType UPDATED_TASK_TYPE = TaskType.SHELL;

    private static final ZonedDateTime DEFAULT_CREATE_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskQueryService taskQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task().taskName(DEFAULT_TASK_NAME).taskType(DEFAULT_TASK_TYPE).createTimeStamp(DEFAULT_CREATE_TIME_STAMP);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task().taskName(UPDATED_TASK_NAME).taskType(UPDATED_TASK_TYPE).createTimeStamp(UPDATED_CREATE_TIME_STAMP);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        // Create the Task
        restTaskMockMvc
            .perform(
                post("/api/tasks").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskName()).isEqualTo(DEFAULT_TASK_NAME);
        assertThat(testTask.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);
        assertThat(testTask.getCreateTimeStamp()).isEqualTo(DEFAULT_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task with an existing ID
        task.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(
                post("/api/tasks").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIME_STAMP))));
    }

    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.taskName").value(DEFAULT_TASK_NAME))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()))
            .andExpect(jsonPath("$.createTimeStamp").value(sameInstant(DEFAULT_CREATE_TIME_STAMP)));
    }

    @Test
    @Transactional
    public void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName equals to DEFAULT_TASK_NAME
        defaultTaskShouldBeFound("taskName.equals=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName equals to UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.equals=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName not equals to DEFAULT_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.notEquals=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName not equals to UPDATED_TASK_NAME
        defaultTaskShouldBeFound("taskName.notEquals=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName in DEFAULT_TASK_NAME or UPDATED_TASK_NAME
        defaultTaskShouldBeFound("taskName.in=" + DEFAULT_TASK_NAME + "," + UPDATED_TASK_NAME);

        // Get all the taskList where taskName equals to UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.in=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName is not null
        defaultTaskShouldBeFound("taskName.specified=true");

        // Get all the taskList where taskName is null
        defaultTaskShouldNotBeFound("taskName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName contains DEFAULT_TASK_NAME
        defaultTaskShouldBeFound("taskName.contains=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName contains UPDATED_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.contains=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskName does not contain DEFAULT_TASK_NAME
        defaultTaskShouldNotBeFound("taskName.doesNotContain=" + DEFAULT_TASK_NAME);

        // Get all the taskList where taskName does not contain UPDATED_TASK_NAME
        defaultTaskShouldBeFound("taskName.doesNotContain=" + UPDATED_TASK_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType equals to DEFAULT_TASK_TYPE
        defaultTaskShouldBeFound("taskType.equals=" + DEFAULT_TASK_TYPE);

        // Get all the taskList where taskType equals to UPDATED_TASK_TYPE
        defaultTaskShouldNotBeFound("taskType.equals=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType not equals to DEFAULT_TASK_TYPE
        defaultTaskShouldNotBeFound("taskType.notEquals=" + DEFAULT_TASK_TYPE);

        // Get all the taskList where taskType not equals to UPDATED_TASK_TYPE
        defaultTaskShouldBeFound("taskType.notEquals=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType in DEFAULT_TASK_TYPE or UPDATED_TASK_TYPE
        defaultTaskShouldBeFound("taskType.in=" + DEFAULT_TASK_TYPE + "," + UPDATED_TASK_TYPE);

        // Get all the taskList where taskType equals to UPDATED_TASK_TYPE
        defaultTaskShouldNotBeFound("taskType.in=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskType is not null
        defaultTaskShouldBeFound("taskType.specified=true");

        // Get all the taskList where taskType is null
        defaultTaskShouldNotBeFound("taskType.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.equals=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.equals=" + UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.notEquals=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.notEquals=" + UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.in=" + DEFAULT_CREATE_TIME_STAMP + "," + UPDATED_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.in=" + UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp is not null
        defaultTaskShouldBeFound("createTimeStamp.specified=true");

        // Get all the taskList where createTimeStamp is null
        defaultTaskShouldNotBeFound("createTimeStamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp is greater than or equal to DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.greaterThanOrEqual=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp is greater than or equal to UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.greaterThanOrEqual=" + UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp is less than or equal to DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.lessThanOrEqual=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp is less than or equal to SMALLER_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.lessThanOrEqual=" + SMALLER_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp is less than DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.lessThan=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp is less than UPDATED_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.lessThan=" + UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByCreateTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where createTimeStamp is greater than DEFAULT_CREATE_TIME_STAMP
        defaultTaskShouldNotBeFound("createTimeStamp.greaterThan=" + DEFAULT_CREATE_TIME_STAMP);

        // Get all the taskList where createTimeStamp is greater than SMALLER_CREATE_TIME_STAMP
        defaultTaskShouldBeFound("createTimeStamp.greaterThan=" + SMALLER_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        TaskConfig taskConfig = TaskConfigResourceIT.createEntity(em);
        em.persist(taskConfig);
        em.flush();
        task.addTaskConfig(taskConfig);
        taskRepository.saveAndFlush(task);
        Long taskConfigId = taskConfig.getId();

        // Get all the taskList where taskConfig equals to taskConfigId
        defaultTaskShouldBeFound("taskConfigId.equals=" + taskConfigId);

        // Get all the taskList where taskConfig equals to taskConfigId + 1
        defaultTaskShouldNotBeFound("taskConfigId.equals=" + (taskConfigId + 1));
    }

    @Test
    @Transactional
    public void getAllTasksByJobTaskOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        JobTaskOrder jobTaskOrder = JobTaskOrderResourceIT.createEntity(em);
        em.persist(jobTaskOrder);
        em.flush();
        task.addJobTaskOrder(jobTaskOrder);
        taskRepository.saveAndFlush(task);
        Long jobTaskOrderId = jobTaskOrder.getId();

        // Get all the taskList where jobTaskOrder equals to jobTaskOrderId
        defaultTaskShouldBeFound("jobTaskOrderId.equals=" + jobTaskOrderId);

        // Get all the taskList where jobTaskOrder equals to jobTaskOrderId + 1
        defaultTaskShouldNotBeFound("jobTaskOrderId.equals=" + (jobTaskOrderId + 1));
    }

    @Test
    @Transactional
    public void getAllTasksByTaskExecutionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        TaskExecution taskExecution = TaskExecutionResourceIT.createEntity(em);
        em.persist(taskExecution);
        em.flush();
        task.addTaskExecution(taskExecution);
        taskRepository.saveAndFlush(task);
        Long taskExecutionId = taskExecution.getId();

        // Get all the taskList where taskExecution equals to taskExecutionId
        defaultTaskShouldBeFound("taskExecutionId.equals=" + taskExecutionId);

        // Get all the taskList where taskExecution equals to taskExecutionId + 1
        defaultTaskShouldNotBeFound("taskExecutionId.equals=" + (taskExecutionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskName").value(hasItem(DEFAULT_TASK_NAME)))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIME_STAMP))));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask.taskName(UPDATED_TASK_NAME).taskType(UPDATED_TASK_TYPE).createTimeStamp(UPDATED_CREATE_TIME_STAMP);

        restTaskMockMvc
            .perform(
                put("/api/tasks")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTaskName()).isEqualTo(UPDATED_TASK_NAME);
        assertThat(testTask.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);
        assertThat(testTask.getCreateTimeStamp()).isEqualTo(UPDATED_CREATE_TIME_STAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put("/api/tasks").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(task))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc
            .perform(delete("/api/tasks/{id}", task.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
