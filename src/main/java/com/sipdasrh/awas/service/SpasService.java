package com.sipdasrh.awas.service;

import com.sipdasrh.awas.service.dto.SpasDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sipdasrh.awas.domain.Spas}.
 */
public interface SpasService {
    /**
     * Save a spas.
     *
     * @param spasDTO the entity to save.
     * @return the persisted entity.
     */
    SpasDTO save(SpasDTO spasDTO);

    /**
     * Updates a spas.
     *
     * @param spasDTO the entity to update.
     * @return the persisted entity.
     */
    SpasDTO update(SpasDTO spasDTO);

    /**
     * Partially updates a spas.
     *
     * @param spasDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpasDTO> partialUpdate(SpasDTO spasDTO);

    /**
     * Get the "id" spas.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpasDTO> findOne(Long id);

    /**
     * Delete the "id" spas.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
