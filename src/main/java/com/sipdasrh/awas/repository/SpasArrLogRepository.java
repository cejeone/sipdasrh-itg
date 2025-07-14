package com.sipdasrh.awas.repository;

import com.sipdasrh.awas.domain.SpasArrLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpasArrLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpasArrLogRepository extends JpaRepository<SpasArrLog, Long>, JpaSpecificationExecutor<SpasArrLog> {}
