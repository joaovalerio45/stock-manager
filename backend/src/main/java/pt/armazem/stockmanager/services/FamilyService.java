package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Family;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.FamilyRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;

    public Family getFamilyById(Long id) {
        return familyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Family not found with ID: " + id));
    }

    public java.util.List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }

    public Family getActiveFamilyById(Long id) {
        Family family = getFamilyById(id);
        if (!family.getActive()) {
            throw new BusinessRuleException("Family '" + family.getName() + "' is inactive.");
        }
        return family;
    }
}

