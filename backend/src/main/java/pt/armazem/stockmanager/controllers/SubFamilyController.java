package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.SubFamily;
import pt.armazem.stockmanager.services.SubFamilyService;

@RestController
@RequestMapping("/api/sub-families")
@RequiredArgsConstructor
public class SubFamilyController {

    private final SubFamilyService subFamilyService;

    @GetMapping("/{id}")
    public SubFamily fetchSubFamilyById(@PathVariable Long id) {
        return subFamilyService.getSubFamilyById(id);
    }

    @GetMapping
    public List<SubFamily> fetchAllSubFamilies() {
        return subFamilyService.getAllSubFamilies();
    }
}
