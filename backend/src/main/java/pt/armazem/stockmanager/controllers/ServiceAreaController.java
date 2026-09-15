package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.ServiceArea;
import pt.armazem.stockmanager.services.ServiceAreaService;

@RestController
@RequestMapping("/api/service-areas")
@RequiredArgsConstructor
public class ServiceAreaController {

    private final ServiceAreaService serviceAreaService;

    @GetMapping("/{id}")
    public ServiceArea fetchServiceAreaById(@PathVariable Long id) {
        return serviceAreaService.getServiceAreaById(id);
    }

    @GetMapping
    public List<ServiceArea> fetchAllServiceAreas() {
        return serviceAreaService.getAllServiceAreas();
    }
}
