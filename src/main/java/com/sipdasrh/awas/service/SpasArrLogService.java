package com.sipdasrh.awas.service;

import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sipdasrh.awas.domain.SpasArrLog}.
 */
public interface SpasArrLogService {
    /**
     * Save a spasArrLog.
     *
     * @param spasArrLogDTO the entity to save.
     * @return the persisted entity.
     */
    SpasArrLogDTO save(SpasArrLogDTO spasArrLogDTO);

    /**
     * Updates a spasArrLog.
     *
     * @param spasArrLogDTO the entity to update.
     * @return the persisted entity.
     */
    SpasArrLogDTO update(SpasArrLogDTO spasArrLogDTO);

    /**
     * Partially updates a spasArrLog.
     *
     * @param spasArrLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpasArrLogDTO> partialUpdate(SpasArrLogDTO spasArrLogDTO);

    /**
     * Get the "id" spasArrLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpasArrLogDTO> findOne(Long id);

    /**
     * Delete the "id" spasArrLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
