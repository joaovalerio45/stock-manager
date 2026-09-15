package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.ServiceArea;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.ServiceAreaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceAreaService {

    private final ServiceAreaRepository serviceAreaRepository;

    public ServiceArea getServiceAreaById(Long id) {
        return serviceAreaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ServiceArea not found with ID: " + id));
    }

    public java.util.List<ServiceArea> getAllServiceAreas() {
        return serviceAreaRepository.findAll();
    }

    public ServiceArea getActiveServiceAreaById(Long id) {
        ServiceArea serviceArea = getServiceAreaById(id);
        if (!serviceArea.getActive()) {
            throw new BusinessRuleException("Service area '" + serviceArea.getName() + "' is inactive.");
        }
        return serviceArea;
    }
}
