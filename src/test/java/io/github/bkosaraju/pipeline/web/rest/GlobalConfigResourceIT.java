package io.github.bkosaraju.pipeline.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.bkosaraju.pipeline.PipelineApp;
import io.github.bkosaraju.pipeline.domain.GlobalConfig;
import io.github.bkosaraju.pipeline.repository.GlobalConfigRepository;
import io.github.bkosaraju.pipeline.service.GlobalConfigQueryService;
import io.github.bkosaraju.pipeline.service.GlobalConfigService;
import io.github.bkosaraju.pipeline.service.dto.GlobalConfigCriteria;
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
 * Integration tests for the {@link GlobalConfigResource} REST controller.
 */
@SpringBootTest(classes = PipelineApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GlobalConfigResourceIT {
    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_TYPE = "BBBBBBBBBB";

    @Autowired
    private GlobalConfigRepository globalConfigRepository;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private GlobalConfigQueryService globalConfigQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlobalConfigMockMvc;

    private GlobalConfig globalConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalConfig createEntity(EntityManager em) {
        GlobalConfig globalConfig = new GlobalConfig()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .configType(DEFAULT_CONFIG_TYPE);
        return globalConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalConfig createUpdatedEntity(EntityManager em) {
        GlobalConfig globalConfig = new GlobalConfig()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .configType(UPDATED_CONFIG_TYPE);
        return globalConfig;
    }

    @BeforeEach
    public void initTest() {
        globalConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createGlobalConfig() throws Exception {
        int databaseSizeBeforeCreate = globalConfigRepository.findAll().size();
        // Create the GlobalConfig
        restGlobalConfigMockMvc
            .perform(
                post("/api/global-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalConfig))
            )
            .andExpect(status().isCreated());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeCreate + 1);
        GlobalConfig testGlobalConfig = globalConfigList.get(globalConfigList.size() - 1);
        assertThat(testGlobalConfig.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testGlobalConfig.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testGlobalConfig.getConfigType()).isEqualTo(DEFAULT_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void createGlobalConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = globalConfigRepository.findAll().size();

        // Create the GlobalConfig with an existing ID
        globalConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalConfigMockMvc
            .perform(
                post("/api/global-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigs() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE)));
    }

    @Test
    @Transactional
    public void getGlobalConfig() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get the globalConfig
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs/{id}", globalConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(globalConfig.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.configType").value(DEFAULT_CONFIG_TYPE));
    }

    @Test
    @Transactional
    public void getGlobalConfigsByIdFiltering() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        Long id = globalConfig.getId();

        defaultGlobalConfigShouldBeFound("id.equals=" + id);
        defaultGlobalConfigShouldNotBeFound("id.notEquals=" + id);

        defaultGlobalConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGlobalConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultGlobalConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGlobalConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey equals to DEFAULT_CONFIG_KEY
        defaultGlobalConfigShouldBeFound("configKey.equals=" + DEFAULT_CONFIG_KEY);

        // Get all the globalConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldNotBeFound("configKey.equals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey not equals to DEFAULT_CONFIG_KEY
        defaultGlobalConfigShouldNotBeFound("configKey.notEquals=" + DEFAULT_CONFIG_KEY);

        // Get all the globalConfigList where configKey not equals to UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldBeFound("configKey.notEquals=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyIsInShouldWork() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey in DEFAULT_CONFIG_KEY or UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldBeFound("configKey.in=" + DEFAULT_CONFIG_KEY + "," + UPDATED_CONFIG_KEY);

        // Get all the globalConfigList where configKey equals to UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldNotBeFound("configKey.in=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey is not null
        defaultGlobalConfigShouldBeFound("configKey.specified=true");

        // Get all the globalConfigList where configKey is null
        defaultGlobalConfigShouldNotBeFound("configKey.specified=false");
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey contains DEFAULT_CONFIG_KEY
        defaultGlobalConfigShouldBeFound("configKey.contains=" + DEFAULT_CONFIG_KEY);

        // Get all the globalConfigList where configKey contains UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldNotBeFound("configKey.contains=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigKeyNotContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configKey does not contain DEFAULT_CONFIG_KEY
        defaultGlobalConfigShouldNotBeFound("configKey.doesNotContain=" + DEFAULT_CONFIG_KEY);

        // Get all the globalConfigList where configKey does not contain UPDATED_CONFIG_KEY
        defaultGlobalConfigShouldBeFound("configKey.doesNotContain=" + UPDATED_CONFIG_KEY);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueIsEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue equals to DEFAULT_CONFIG_VALUE
        defaultGlobalConfigShouldBeFound("configValue.equals=" + DEFAULT_CONFIG_VALUE);

        // Get all the globalConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldNotBeFound("configValue.equals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue not equals to DEFAULT_CONFIG_VALUE
        defaultGlobalConfigShouldNotBeFound("configValue.notEquals=" + DEFAULT_CONFIG_VALUE);

        // Get all the globalConfigList where configValue not equals to UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldBeFound("configValue.notEquals=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueIsInShouldWork() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue in DEFAULT_CONFIG_VALUE or UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldBeFound("configValue.in=" + DEFAULT_CONFIG_VALUE + "," + UPDATED_CONFIG_VALUE);

        // Get all the globalConfigList where configValue equals to UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldNotBeFound("configValue.in=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue is not null
        defaultGlobalConfigShouldBeFound("configValue.specified=true");

        // Get all the globalConfigList where configValue is null
        defaultGlobalConfigShouldNotBeFound("configValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue contains DEFAULT_CONFIG_VALUE
        defaultGlobalConfigShouldBeFound("configValue.contains=" + DEFAULT_CONFIG_VALUE);

        // Get all the globalConfigList where configValue contains UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldNotBeFound("configValue.contains=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigValueNotContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configValue does not contain DEFAULT_CONFIG_VALUE
        defaultGlobalConfigShouldNotBeFound("configValue.doesNotContain=" + DEFAULT_CONFIG_VALUE);

        // Get all the globalConfigList where configValue does not contain UPDATED_CONFIG_VALUE
        defaultGlobalConfigShouldBeFound("configValue.doesNotContain=" + UPDATED_CONFIG_VALUE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType equals to DEFAULT_CONFIG_TYPE
        defaultGlobalConfigShouldBeFound("configType.equals=" + DEFAULT_CONFIG_TYPE);

        // Get all the globalConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldNotBeFound("configType.equals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType not equals to DEFAULT_CONFIG_TYPE
        defaultGlobalConfigShouldNotBeFound("configType.notEquals=" + DEFAULT_CONFIG_TYPE);

        // Get all the globalConfigList where configType not equals to UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldBeFound("configType.notEquals=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeIsInShouldWork() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType in DEFAULT_CONFIG_TYPE or UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldBeFound("configType.in=" + DEFAULT_CONFIG_TYPE + "," + UPDATED_CONFIG_TYPE);

        // Get all the globalConfigList where configType equals to UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldNotBeFound("configType.in=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType is not null
        defaultGlobalConfigShouldBeFound("configType.specified=true");

        // Get all the globalConfigList where configType is null
        defaultGlobalConfigShouldNotBeFound("configType.specified=false");
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType contains DEFAULT_CONFIG_TYPE
        defaultGlobalConfigShouldBeFound("configType.contains=" + DEFAULT_CONFIG_TYPE);

        // Get all the globalConfigList where configType contains UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldNotBeFound("configType.contains=" + UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigsByConfigTypeNotContainsSomething() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList where configType does not contain DEFAULT_CONFIG_TYPE
        defaultGlobalConfigShouldNotBeFound("configType.doesNotContain=" + DEFAULT_CONFIG_TYPE);

        // Get all the globalConfigList where configType does not contain UPDATED_CONFIG_TYPE
        defaultGlobalConfigShouldBeFound("configType.doesNotContain=" + UPDATED_CONFIG_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGlobalConfigShouldBeFound(String filter) throws Exception {
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].configType").value(hasItem(DEFAULT_CONFIG_TYPE)));

        // Check, that the count call also returns 1
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGlobalConfigShouldNotBeFound(String filter) throws Exception {
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGlobalConfigMockMvc
            .perform(get("/api/global-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingGlobalConfig() throws Exception {
        // Get the globalConfig
        restGlobalConfigMockMvc.perform(get("/api/global-configs/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGlobalConfig() throws Exception {
        // Initialize the database
        globalConfigService.save(globalConfig);

        int databaseSizeBeforeUpdate = globalConfigRepository.findAll().size();

        // Update the globalConfig
        GlobalConfig updatedGlobalConfig = globalConfigRepository.findById(globalConfig.getId()).get();
        // Disconnect from session so that the updates on updatedGlobalConfig are not directly saved in db
        em.detach(updatedGlobalConfig);
        updatedGlobalConfig.configKey(UPDATED_CONFIG_KEY).configValue(UPDATED_CONFIG_VALUE).configType(UPDATED_CONFIG_TYPE);

        restGlobalConfigMockMvc
            .perform(
                put("/api/global-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGlobalConfig))
            )
            .andExpect(status().isOk());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeUpdate);
        GlobalConfig testGlobalConfig = globalConfigList.get(globalConfigList.size() - 1);
        assertThat(testGlobalConfig.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testGlobalConfig.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testGlobalConfig.getConfigType()).isEqualTo(UPDATED_CONFIG_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingGlobalConfig() throws Exception {
        int databaseSizeBeforeUpdate = globalConfigRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalConfigMockMvc
            .perform(
                put("/api/global-configs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGlobalConfig() throws Exception {
        // Initialize the database
        globalConfigService.save(globalConfig);

        int databaseSizeBeforeDelete = globalConfigRepository.findAll().size();

        // Delete the globalConfig
        restGlobalConfigMockMvc
            .perform(delete("/api/global-configs/{id}", globalConfig.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
