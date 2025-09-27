package com.sipdasrh.awas.web.rest;

import com.sipdasrh.awas.repository.SpasArrLogRepository;
import com.sipdasrh.awas.service.SpasArrLogQueryService;
import com.sipdasrh.awas.service.SpasArrLogService;
import com.sipdasrh.awas.service.criteria.SpasArrLogCriteria;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import com.sipdasrh.awas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sipdasrh.awas.domain.SpasArrLog}.
 */
@RestController
@RequestMapping("/api/spas-arr-logs")
public class SpasArrLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpasArrLogResource.class);

    private static final String ENTITY_NAME = "serviceAwasSpasArrLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpasArrLogService spasArrLogService;

    private final SpasArrLogRepository spasArrLogRepository;

    private final SpasArrLogQueryService spasArrLogQueryService;

    public SpasArrLogResource(
        SpasArrLogService spasArrLogService,
        SpasArrLogRepository spasArrLogRepository,
        SpasArrLogQueryService spasArrLogQueryService
    ) {
        this.spasArrLogService = spasArrLogService;
        this.spasArrLogRepository = spasArrLogRepository;
        this.spasArrLogQueryService = spasArrLogQueryService;
    }

    /**
     * {@code POST  /spas-arr-logs} : Create a new spasArrLog.
     *
     * @param spasArrLogDTO the spasArrLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spasArrLogDTO, or with status {@code 400 (Bad Request)} if the spasArrLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpasArrLogDTO> createSpasArrLog(@RequestBody SpasArrLogDTO spasArrLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save SpasArrLog : {}", spasArrLogDTO);
        if (spasArrLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new spasArrLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        spasArrLogDTO = spasArrLogService.save(spasArrLogDTO);
        return ResponseEntity.created(new URI("/api/spas-arr-logs/" + spasArrLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, spasArrLogDTO.getId().toString()))
            .body(spasArrLogDTO);
    }

    /**
     * {@code PUT  /spas-arr-logs/:id} : Updates an existing spasArrLog.
     *
     * @param id the id of the spasArrLogDTO to save.
     * @param spasArrLogDTO the spasArrLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasArrLogDTO,
     * or with status {@code 400 (Bad Request)} if the spasArrLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spasArrLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpasArrLogDTO> updateSpasArrLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpasArrLogDTO spasArrLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SpasArrLog : {}, {}", id, spasArrLogDTO);
        if (spasArrLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasArrLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasArrLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        spasArrLogDTO = spasArrLogService.update(spasArrLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasArrLogDTO.getId().toString()))
            .body(spasArrLogDTO);
    }

    /**
     * {@code PATCH  /spas-arr-logs/:id} : Partial updates given fields of an existing spasArrLog, field will ignore if it is null
     *
     * @param id the id of the spasArrLogDTO to save.
     * @param spasArrLogDTO the spasArrLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasArrLogDTO,
     * or with status {@code 400 (Bad Request)} if the spasArrLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spasArrLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spasArrLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpasArrLogDTO> partialUpdateSpasArrLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpasArrLogDTO spasArrLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SpasArrLog partially : {}, {}", id, spasArrLogDTO);
        if (spasArrLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasArrLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasArrLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpasArrLogDTO> result = spasArrLogService.partialUpdate(spasArrLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasArrLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /spas-arr-logs} : get all the spasArrLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spasArrLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpasArrLogDTO>> getAllSpasArrLogs(SpasArrLogCriteria criteria) {
        LOG.debug("REST request to get SpasArrLogs by criteria: {}", criteria);

        List<SpasArrLogDTO> entityList = spasArrLogQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /spas-arr-logs/pages} : get all the spasArrLogs with pagination and criteria.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spasArrLogs in body.
     */
    @GetMapping("/pages")
    public ResponseEntity<List<SpasArrLogDTO>> getAllSpasArrLogPages(
        SpasArrLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get SpasArrLogs Paged by criteria: {}", criteria);

        Page<SpasArrLogDTO> page = spasArrLogQueryService.findPagesByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spas-arr-logs/count} : count all the spasArrLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpasArrLogs(SpasArrLogCriteria criteria) {
        LOG.debug("REST request to count SpasArrLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(spasArrLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /spas-arr-logs/:id} : get the "id" spasArrLog.
     *
     * @param id the id of the spasArrLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spasArrLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpasArrLogDTO> getSpasArrLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SpasArrLog : {}", id);
        Optional<SpasArrLogDTO> spasArrLogDTO = spasArrLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spasArrLogDTO);
    }

    /**
     * {@code DELETE  /spas-arr-logs/:id} : delete the "id" spasArrLog.
     *
     * @param id the id of the spasArrLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpasArrLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SpasArrLog : {}", id);
        spasArrLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
