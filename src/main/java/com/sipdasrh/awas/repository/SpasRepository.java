package com.sipdasrh.awas.repository;

import com.sipdasrh.awas.domain.Spas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Spas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpasRepository extends JpaRepository<Spas, Long>, JpaSpecificationExecutor<Spas> {}
