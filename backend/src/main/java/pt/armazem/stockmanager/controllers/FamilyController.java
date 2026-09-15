package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Family;
import pt.armazem.stockmanager.services.FamilyService;

@RestController
@RequestMapping("/api/families")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping("/{id}")
    public Family fetchFamilyById(@PathVariable Long id) {
        return familyService.getFamilyById(id);
    }

    @GetMapping
    public List<Family> fetchAllFamilies() {
        return familyService.getAllFamilies();
    }
}
