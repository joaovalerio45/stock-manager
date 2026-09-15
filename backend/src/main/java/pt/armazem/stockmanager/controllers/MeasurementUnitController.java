package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.MeasurementUnit;
import pt.armazem.stockmanager.services.MeasurementUnitService;

@RestController
@RequestMapping("/api/measurement-units")
@RequiredArgsConstructor
public class MeasurementUnitController {

    private final MeasurementUnitService measurementUnitService;

    @GetMapping("/{id}")
    public MeasurementUnit fetchMeasurementUnitById(@PathVariable Long id) {
        return measurementUnitService.getMeasurementUnitById(id);
    }

    @GetMapping
    public List<MeasurementUnit> fetchAllMeasurementUnits() {
        return measurementUnitService.getAllMeasurementUnits();
    }
}
