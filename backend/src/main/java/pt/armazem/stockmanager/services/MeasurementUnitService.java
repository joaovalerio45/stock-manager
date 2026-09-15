package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.MeasurementUnit;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.MeasurementUnitRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MeasurementUnitService {

    private final MeasurementUnitRepository measurementUnitRepository;

    public MeasurementUnit getMeasurementUnitById(Long id) {
        return measurementUnitRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MeasurementUnit not found with ID: " + id));
    }

    public java.util.List<MeasurementUnit> getAllMeasurementUnits() {
        return measurementUnitRepository.findAll();
    }

    public MeasurementUnit getActiveMeasurementUnitById(Long id) {
        MeasurementUnit unit = getMeasurementUnitById(id);
        if (!unit.getActive()) {
            throw new BusinessRuleException("Measurement unit '" + unit.getName() + "' is inactive.");
        }
        return unit;
    }
}
