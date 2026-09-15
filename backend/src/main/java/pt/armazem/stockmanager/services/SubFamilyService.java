package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.SubFamily;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.SubFamilyRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SubFamilyService {

    private final SubFamilyRepository subFamilyRepository;

    public SubFamily getSubFamilyById(Long id) {
        return subFamilyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SubFamily not found with ID: " + id));
    }

    public java.util.List<SubFamily> getAllSubFamilies() {
        return subFamilyRepository.findAll();
    }

    public SubFamily getActiveSubFamilyById(Long id) {
        SubFamily subFamily = getSubFamilyById(id);
        if (!subFamily.getActive()) {
            throw new BusinessRuleException("Subfamily '" + subFamily.getName() + "' is inactive.");
        }
        return subFamily;
    }
}
