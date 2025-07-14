package com.sipdasrh.awas.service.impl;

import com.sipdasrh.awas.domain.SpasArrLog;
import com.sipdasrh.awas.repository.SpasArrLogRepository;
import com.sipdasrh.awas.service.SpasArrLogService;
import com.sipdasrh.awas.service.dto.SpasArrLogDTO;
import com.sipdasrh.awas.service.mapper.SpasArrLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sipdasrh.awas.domain.SpasArrLog}.
 */
@Service
@Transactional
public class SpasArrLogServiceImpl implements SpasArrLogService {

    private static final Logger LOG = LoggerFactory.getLogger(SpasArrLogServiceImpl.class);

    private final SpasArrLogRepository spasArrLogRepository;

    private final SpasArrLogMapper spasArrLogMapper;

    public SpasArrLogServiceImpl(SpasArrLogRepository spasArrLogRepository, SpasArrLogMapper spasArrLogMapper) {
        this.spasArrLogRepository = spasArrLogRepository;
        this.spasArrLogMapper = spasArrLogMapper;
    }

    @Override
    public SpasArrLogDTO save(SpasArrLogDTO spasArrLogDTO) {
        LOG.debug("Request to save SpasArrLog : {}", spasArrLogDTO);
        SpasArrLog spasArrLog = spasArrLogMapper.toEntity(spasArrLogDTO);
        spasArrLog = spasArrLogRepository.save(spasArrLog);
        return spasArrLogMapper.toDto(spasArrLog);
    }

    @Override
    public SpasArrLogDTO update(SpasArrLogDTO spasArrLogDTO) {
        LOG.debug("Request to update SpasArrLog : {}", spasArrLogDTO);
        SpasArrLog spasArrLog = spasArrLogMapper.toEntity(spasArrLogDTO);
        spasArrLog = spasArrLogRepository.save(spasArrLog);
        return spasArrLogMapper.toDto(spasArrLog);
    }

    @Override
    public Optional<SpasArrLogDTO> partialUpdate(SpasArrLogDTO spasArrLogDTO) {
        LOG.debug("Request to partially update SpasArrLog : {}", spasArrLogDTO);

        return spasArrLogRepository
            .findById(spasArrLogDTO.getId())
            .map(existingSpasArrLog -> {
                spasArrLogMapper.partialUpdate(existingSpasArrLog, spasArrLogDTO);

                return existingSpasArrLog;
            })
            .map(spasArrLogRepository::save)
            .map(spasArrLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpasArrLogDTO> findOne(Long id) {
        LOG.debug("Request to get SpasArrLog : {}", id);
        return spasArrLogRepository.findById(id).map(spasArrLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SpasArrLog : {}", id);
        spasArrLogRepository.deleteById(id);
    }
}
