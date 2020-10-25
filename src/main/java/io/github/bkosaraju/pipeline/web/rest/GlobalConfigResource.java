package io.github.bkosaraju.pipeline.web.rest;

import io.github.bkosaraju.pipeline.domain.GlobalConfig;
import io.github.bkosaraju.pipeline.service.GlobalConfigQueryService;
import io.github.bkosaraju.pipeline.service.GlobalConfigService;
import io.github.bkosaraju.pipeline.service.dto.GlobalConfigCriteria;
import io.github.bkosaraju.pipeline.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link io.github.bkosaraju.pipeline.domain.GlobalConfig}.
 */
@RestController
@RequestMapping("/api")
public class GlobalConfigResource {
    private final Logger log = LoggerFactory.getLogger(GlobalConfigResource.class);

    private static final String ENTITY_NAME = "globalConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalConfigService globalConfigService;

    private final GlobalConfigQueryService globalConfigQueryService;

    public GlobalConfigResource(GlobalConfigService globalConfigService, GlobalConfigQueryService globalConfigQueryService) {
        this.globalConfigService = globalConfigService;
        this.globalConfigQueryService = globalConfigQueryService;
    }

    /**
     * {@code POST  /global-configs} : Create a new globalConfig.
     *
     * @param globalConfig the globalConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalConfig, or with status {@code 400 (Bad Request)} if the globalConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/global-configs")
    public ResponseEntity<GlobalConfig> createGlobalConfig(@RequestBody GlobalConfig globalConfig) throws URISyntaxException {
        log.debug("REST request to save GlobalConfig : {}", globalConfig);
        if (globalConfig.getId() != null) {
            throw new BadRequestAlertException("A new globalConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalConfig result = globalConfigService.save(globalConfig);
        return ResponseEntity
            .created(new URI("/api/global-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /global-configs} : Updates an existing globalConfig.
     *
     * @param globalConfig the globalConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalConfig,
     * or with status {@code 400 (Bad Request)} if the globalConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/global-configs")
    public ResponseEntity<GlobalConfig> updateGlobalConfig(@RequestBody GlobalConfig globalConfig) throws URISyntaxException {
        log.debug("REST request to update GlobalConfig : {}", globalConfig);
        if (globalConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GlobalConfig result = globalConfigService.save(globalConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /global-configs} : get all the globalConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalConfigs in body.
     */
    @GetMapping("/global-configs")
    public ResponseEntity<List<GlobalConfig>> getAllGlobalConfigs(GlobalConfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GlobalConfigs by criteria: {}", criteria);
        Page<GlobalConfig> page = globalConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /global-configs/count} : count all the globalConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/global-configs/count")
    public ResponseEntity<Long> countGlobalConfigs(GlobalConfigCriteria criteria) {
        log.debug("REST request to count GlobalConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(globalConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /global-configs/:id} : get the "id" globalConfig.
     *
     * @param id the id of the globalConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/global-configs/{id}")
    public ResponseEntity<GlobalConfig> getGlobalConfig(@PathVariable Long id) {
        log.debug("REST request to get GlobalConfig : {}", id);
        Optional<GlobalConfig> globalConfig = globalConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalConfig);
    }

    /**
     * {@code DELETE  /global-configs/:id} : delete the "id" globalConfig.
     *
     * @param id the id of the globalConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/global-configs/{id}")
    public ResponseEntity<Void> deleteGlobalConfig(@PathVariable Long id) {
        log.debug("REST request to delete GlobalConfig : {}", id);
        globalConfigService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
