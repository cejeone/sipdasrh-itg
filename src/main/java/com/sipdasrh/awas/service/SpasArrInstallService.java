package com.sipdasrh.awas.service;

import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sipdasrh.awas.domain.SpasArrInstall}.
 */
public interface SpasArrInstallService {
    /**
     * Save a spasArrInstall.
     *
     * @param spasArrInstallDTO the entity to save.
     * @return the persisted entity.
     */
    SpasArrInstallDTO save(SpasArrInstallDTO spasArrInstallDTO);

    /**
     * Updates a spasArrInstall.
     *
     * @param spasArrInstallDTO the entity to update.
     * @return the persisted entity.
     */
    SpasArrInstallDTO update(SpasArrInstallDTO spasArrInstallDTO);

    /**
     * Partially updates a spasArrInstall.
     *
     * @param spasArrInstallDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpasArrInstallDTO> partialUpdate(SpasArrInstallDTO spasArrInstallDTO);

    /**
     * Get the "id" spasArrInstall.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpasArrInstallDTO> findOne(Long id);

    /**
     * Delete the "id" spasArrInstall.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
