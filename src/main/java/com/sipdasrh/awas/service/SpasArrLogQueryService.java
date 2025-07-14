package com.sipdasrh.awas.service;

import com.sipdasrh.awas.domain.*; // for static metamodels
import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.repository.SpasArrLogRepository;
import com.sipdasrh.awas.service.criteria.SpasArrLogCriteria;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import com.sipdasrh.awas.service.mapper.SpasArrLogMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SpasArrLog} entities in the database.
 * The main input is a {@link SpasArrLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SpasArrLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpasArrLogQueryService extends QueryService<SpasArrLog> {

    private static final Logger LOG = LoggerFactory.getLogger(SpasArrLogQueryService.class);

    private final SpasArrLogRepository spasArrLogRepository;

    private final SpasArrLogMapper spasArrLogMapper;

    public SpasArrLogQueryService(SpasArrLogRepository spasArrLogRepository, SpasArrLogMapper spasArrLogMapper) {
        this.spasArrLogRepository = spasArrLogRepository;
        this.spasArrLogMapper = spasArrLogMapper;
    }

    /**
     * Return a {@link List} of {@link SpasArrLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SpasArrLogDTO> findByCriteria(SpasArrLogCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<SpasArrLog> specification = createSpecification(criteria);
        return spasArrLogMapper.toDto(spasArrLogRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpasArrLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SpasArrLog> specification = createSpecification(criteria);
        return spasArrLogRepository.count(specification);
    }

    /**
     * Function to convert {@link SpasArrLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SpasArrLog> createSpecification(SpasArrLogCriteria criteria) {
        Specification<SpasArrLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SpasArrLog_.id),
                buildRangeSpecification(criteria.getTimeLog(), SpasArrLog_.timeLog),
                buildRangeSpecification(criteria.getTimeRetrieve(), SpasArrLog_.timeRetrieve),
                buildStringSpecification(criteria.getLogValue(), SpasArrLog_.logValue),
                buildSpecification(criteria.getSpasArrInstallId(), root ->
                    root.join(SpasArrLog_.spasArrInstall, JoinType.LEFT).get(SpasArrInstall_.id)
                )
            );
        }
        return specification;
    }
}
