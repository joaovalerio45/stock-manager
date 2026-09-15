package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.ExternalEntity;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.ExternalEntityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ExternalEntityService {

    private final ExternalEntityRepository externalEntityRepository;

    public ExternalEntity getExternalEntityById(Long id) {
        return externalEntityRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ExternalEntity not found with ID: " + id));
    }

    public java.util.List<ExternalEntity> getAllExternalEntities() {
        return externalEntityRepository.findAll();
    }

    public ExternalEntity getActiveExternalEntityById(Long id) {
        ExternalEntity entity = getExternalEntityById(id);
        if (!entity.getActive()) {
            throw new BusinessRuleException("External entity '" + entity.getName() + "' is inactive.");
        }
        return entity;
    }
}

