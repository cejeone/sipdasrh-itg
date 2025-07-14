package com.sipdasrh.awas.service;

import com.sipdasrh.awas.domain.*; // for static metamodels
import com.sipdasrh.awas.domain.Spas;
import com.sipdasrh.awas.repository.SpasRepository;
import com.sipdasrh.awas.service.criteria.SpasCriteria;
import com.sipdasrh.awas.service.dto.SpasDTO;
import com.sipdasrh.awas.service.mapper.SpasMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Spas} entities in the database.
 * The main input is a {@link SpasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SpasDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpasQueryService extends QueryService<Spas> {

    private static final Logger LOG = LoggerFactory.getLogger(SpasQueryService.class);

    private final SpasRepository spasRepository;

    private final SpasMapper spasMapper;

    public SpasQueryService(SpasRepository spasRepository, SpasMapper spasMapper) {
        this.spasRepository = spasRepository;
        this.spasMapper = spasMapper;
    }

    /**
     * Return a {@link Page} of {@link SpasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpasDTO> findByCriteria(SpasCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Spas> specification = createSpecification(criteria);
        return spasRepository.findAll(specification, page).map(spasMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpasCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Spas> specification = createSpecification(criteria);
        return spasRepository.count(specification);
    }

    /**
     * Function to convert {@link SpasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Spas> createSpecification(SpasCriteria criteria) {
        Specification<Spas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Spas_.id),
                buildStringSpecification(criteria.getNamaSpas(), Spas_.namaSpas),
                buildStringSpecification(criteria.getNamaManufaktur(), Spas_.namaManufaktur),
                buildRangeSpecification(criteria.getTipeSpas(), Spas_.tipeSpas),
                buildSpecification(criteria.getInstallId(), root -> root.join(Spas_.installs, JoinType.LEFT).get(SpasArrInstall_.id))
            );
        }
        return specification;
    }
}
