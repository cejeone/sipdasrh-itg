package com.sipdasrh.awas.service.impl;

import com.sipdasrh.awas.domain.SpasArrInstall;
import com.sipdasrh.awas.repository.SpasArrInstallRepository;
import com.sipdasrh.awas.service.SpasArrInstallService;
import com.sipdasrh.awas.service.dto.SpasArrInstallDTO;
import com.sipdasrh.awas.service.mapper.SpasArrInstallMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.sipdasrh.awas.domain.SpasArrInstall}.
 */
@Service
@Transactional
public class SpasArrInstallServiceImpl implements SpasArrInstallService {

    private static final Logger LOG = LoggerFactory.getLogger(SpasArrInstallServiceImpl.class);

    private final SpasArrInstallRepository spasArrInstallRepository;

    private final SpasArrInstallMapper spasArrInstallMapper;

    public SpasArrInstallServiceImpl(SpasArrInstallRepository spasArrInstallRepository, SpasArrInstallMapper spasArrInstallMapper) {
        this.spasArrInstallRepository = spasArrInstallRepository;
        this.spasArrInstallMapper = spasArrInstallMapper;
    }

    @Override
    public SpasArrInstallDTO save(SpasArrInstallDTO spasArrInstallDTO) {
        LOG.debug("Request to save SpasArrInstall : {}", spasArrInstallDTO);
        SpasArrInstall spasArrInstall = spasArrInstallMapper.toEntity(spasArrInstallDTO);
        spasArrInstall = spasArrInstallRepository.save(spasArrInstall);
        return spasArrInstallMapper.toDto(spasArrInstall);
    }

    @Override
    public SpasArrInstallDTO update(SpasArrInstallDTO spasArrInstallDTO) {
        LOG.debug("Request to update SpasArrInstall : {}", spasArrInstallDTO);
        SpasArrInstall spasArrInstall = spasArrInstallMapper.toEntity(spasArrInstallDTO);
        spasArrInstall = spasArrInstallRepository.save(spasArrInstall);
        return spasArrInstallMapper.toDto(spasArrInstall);
    }

    @Override
    public Optional<SpasArrInstallDTO> partialUpdate(SpasArrInstallDTO spasArrInstallDTO) {
        LOG.debug("Request to partially update SpasArrInstall : {}", spasArrInstallDTO);

        return spasArrInstallRepository
            .findById(spasArrInstallDTO.getId())
            .map(existingSpasArrInstall -> {
                spasArrInstallMapper.partialUpdate(existingSpasArrInstall, spasArrInstallDTO);

                return existingSpasArrInstall;
            })
            .map(spasArrInstallRepository::save)
            .map(spasArrInstallMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpasArrInstallDTO> findOne(Long id) {
        LOG.debug("Request to get SpasArrInstall : {}", id);
        return spasArrInstallRepository.findById(id).map(spasArrInstallMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SpasArrInstall : {}", id);
        spasArrInstallRepository.deleteById(id);
    }
}
