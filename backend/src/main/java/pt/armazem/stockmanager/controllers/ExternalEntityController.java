package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.ExternalEntity;
import pt.armazem.stockmanager.services.ExternalEntityService;

@RestController
@RequestMapping("/api/external-entities")
@RequiredArgsConstructor
public class ExternalEntityController {

    private final ExternalEntityService externalEntityService;

    @GetMapping("/{id}")
    public ExternalEntity fetchExternalEntityById(@PathVariable Long id) {
        return externalEntityService.getExternalEntityById(id);
    }

    @GetMapping
    public List<ExternalEntity> fetchAllExternalEntities() {
        return externalEntityService.getAllExternalEntities();
    }
}
