package com.sipdasrh.awas.web.rest;

import com.sipdasrh.awas.repository.SpasRepository;
import com.sipdasrh.awas.service.SpasQueryService;
import com.sipdasrh.awas.service.SpasService;
import com.sipdasrh.awas.service.criteria.SpasCriteria;
import com.sipdasrh.awas.service.dto.SpasDTO;
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
 * REST controller for managing {@link com.sipdasrh.awas.domain.Spas}.
 */
@RestController
@RequestMapping("/api/spas")
public class SpasResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpasResource.class);

    private static final String ENTITY_NAME = "serviceAwasSpas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpasService spasService;

    private final SpasRepository spasRepository;

    private final SpasQueryService spasQueryService;

    public SpasResource(SpasService spasService, SpasRepository spasRepository, SpasQueryService spasQueryService) {
        this.spasService = spasService;
        this.spasRepository = spasRepository;
        this.spasQueryService = spasQueryService;
    }

    /**
     * {@code POST  /spas} : Create a new spas.
     *
     * @param spasDTO the spasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spasDTO, or with status {@code 400 (Bad Request)} if the spas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpasDTO> createSpas(@RequestBody SpasDTO spasDTO) throws URISyntaxException {
        LOG.debug("REST request to save Spas : {}", spasDTO);
        if (spasDTO.getId() != null) {
            throw new BadRequestAlertException("A new spas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        spasDTO = spasService.save(spasDTO);
        return ResponseEntity.created(new URI("/api/spas/" + spasDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, spasDTO.getId().toString()))
            .body(spasDTO);
    }

    /**
     * {@code PUT  /spas/:id} : Updates an existing spas.
     *
     * @param id the id of the spasDTO to save.
     * @param spasDTO the spasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasDTO,
     * or with status {@code 400 (Bad Request)} if the spasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpasDTO> updateSpas(@PathVariable(value = "id", required = false) final Long id, @RequestBody SpasDTO spasDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Spas : {}, {}", id, spasDTO);
        if (spasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        spasDTO = spasService.update(spasDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasDTO.getId().toString()))
            .body(spasDTO);
    }

    /**
     * {@code PATCH  /spas/:id} : Partial updates given fields of an existing spas, field will ignore if it is null
     *
     * @param id the id of the spasDTO to save.
     * @param spasDTO the spasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spasDTO,
     * or with status {@code 400 (Bad Request)} if the spasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the spasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the spasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpasDTO> partialUpdateSpas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpasDTO spasDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Spas partially : {}, {}", id, spasDTO);
        if (spasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpasDTO> result = spasService.partialUpdate(spasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /spas} : get all the spas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpasDTO>> getAllSpas(
        SpasCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Spas by criteria: {}", criteria);

        Page<SpasDTO> page = spasQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spas/count} : count all the spas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSpas(SpasCriteria criteria) {
        LOG.debug("REST request to count Spas by criteria: {}", criteria);
        return ResponseEntity.ok().body(spasQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /spas/:id} : get the "id" spas.
     *
     * @param id the id of the spasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpasDTO> getSpas(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Spas : {}", id);
        Optional<SpasDTO> spasDTO = spasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spasDTO);
    }

    /**
     * {@code DELETE  /spas/:id} : delete the "id" spas.
     *
     * @param id the id of the spasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpas(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Spas : {}", id);
        spasService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
