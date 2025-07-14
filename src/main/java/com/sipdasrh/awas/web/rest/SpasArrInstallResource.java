package com.sipdasrh.awas.web.rest;

import com.sipdasrh.awas.repository.SpasArrInstallRepository;
import com.sipdasrh.awas.service.SpasArrInstallQueryService;
import com.sipdasrh.awas.service.SpasArrInstallService;
import com.sipdasrh.awas.service.criteria.SpasArrInstallCriteria;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
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
 * REST controller for managing {@link com.sipdasrh.awas.domain.SpasArrInstall}.
 */
@RestController
@RequestMapping("/api/spas-arr-installs")
public class SpasArrInstallResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpasArrInstallResource.class);

    private static final String ENTITY_NAME = "serviceAwasSpasArrInstall";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpasArrInstallService spasArrInstallService;

    private final SpasArrInstallRepository spasArrInstallRepository;

    private final SpasArrInstallQueryService spasArrInstallQueryService;

    public SpasArrInstallResource(
        SpasArrInstallService spasArrInstallService,
        SpasArrInstallRepository spasArrInstallRepository,
        SpasArrInstallQueryService spasArrInstallQueryService
    ) {
        this.spasArrInstallService = spasArrInstallService;
        this.spasArrInstallRepository = spasArrInstallRepository;
        this.spasArrInstallQueryService = spasArrInstallQueryService;
    }

    /**
     * {@code POST  /spas-arr-installs} : Create a new spasArrInstall.
     *
     * @param spasArrInstallDTO the spasArrInstallDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spasArrInstallDTO, or with status {@code 400 (Bad Request)} if the spasArrInstall has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpasArrInstallDTO> createSpasArrInstall(@RequestBody SpasArrInstallDTO spasArrInstallDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SpasArrInstall : {}", spasArrInstallDTO);
        if (spasArrInstallDTO.getId() != null) {
            throw new BadRequestAlertException("A new spasArrInstall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        spasArrInstallDTO = spasArrInstallService.save(spasArrInstallDTO);
        return ResponseEntity.created(new URI("/api/spas-arr-installs/" + spasArrInstallDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, spasArrInstallDTO.getId().toString()))
            .body(spasArrInstallDTO);
    }

    /**
     * {@code PUT  /spas-arr-installs/:id} : Updates an existing spasArrInstall.
     *
     * @param id the id of the spasArrInstallDTO to save.
     * @param spasArrInstallDTO the spasArrInstallDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasArrInstallDTO,
     * or with status {@code 400 (Bad Request)} if the spasArrInstallDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spasArrInstallDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpasArrInstallDTO> updateSpasArrInstall(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpasArrInstallDTO spasArrInstallDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SpasArrInstall : {}, {}", id, spasArrInstallDTO);
        if (spasArrInstallDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasArrInstallDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasArrInstallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        spasArrInstallDTO = spasArrInstallService.update(spasArrInstallDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasArrInstallDTO.getId().toString()))
            .body(spasArrInstallDTO);
    }

    /**
     * {@code PATCH  /spas-arr-installs/:id} : Partial updates given fields of an existing spasArrInstall, field will ignore if it is null
     *
     * @param id the id of the spasArrInstallDTO to save.
     * @param spasArrInstallDTO the spasArrInstallDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasArrInstallDTO,
     * or with status {@code 400 (Bad Request)} if the spasArrInstallDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spasArrInstallDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spasArrInstallDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpasArrInstallDTO> partialUpdateSpasArrInstall(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpasArrInstallDTO spasArrInstallDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SpasArrInstall partially : {}, {}", id, spasArrInstallDTO);
        if (spasArrInstallDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasArrInstallDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasArrInstallRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpasArrInstallDTO> result = spasArrInstallService.partialUpdate(spasArrInstallDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasArrInstallDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /spas-arr-installs} : get all the spasArrInstalls.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spasArrInstalls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpasArrInstallDTO>> getAllSpasArrInstalls(
        SpasArrInstallCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SpasArrInstalls by criteria: {}", criteria);

        Page<SpasArrInstallDTO> page = spasArrInstallQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spas-arr-installs/count} : count all the spasArrInstalls.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpasArrInstalls(SpasArrInstallCriteria criteria) {
        LOG.debug("REST request to count SpasArrInstalls by criteria: {}", criteria);
        return ResponseEntity.ok().body(spasArrInstallQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /spas-arr-installs/:id} : get the "id" spasArrInstall.
     *
     * @param id the id of the spasArrInstallDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spasArrInstallDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpasArrInstallDTO> getSpasArrInstall(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SpasArrInstall : {}", id);
        Optional<SpasArrInstallDTO> spasArrInstallDTO = spasArrInstallService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spasArrInstallDTO);
    }

    /**
     * {@code DELETE  /spas-arr-installs/:id} : delete the "id" spasArrInstall.
     *
     * @param id the id of the spasArrInstallDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpasArrInstall(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SpasArrInstall : {}", id);
        spasArrInstallService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
