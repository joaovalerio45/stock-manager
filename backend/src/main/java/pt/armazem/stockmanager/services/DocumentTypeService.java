package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.DocumentType;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.DocumentTypeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentType getDocumentTypeById(Long id) {
        return documentTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("DocumentType not found with ID: " + id));
    }

    public java.util.List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll();
    }

    public DocumentType getActiveDocumentTypeById(Long id) {
        DocumentType documentType = getDocumentTypeById(id);
        if (!documentType.getActive()) {
            throw new BusinessRuleException("Document type '" + documentType.getName() + "' is inactive.");
        }
        return documentType;
    }
}
