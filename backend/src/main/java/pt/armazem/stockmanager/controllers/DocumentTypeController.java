package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.DocumentType;
import pt.armazem.stockmanager.services.DocumentTypeService;

@RestController
@RequestMapping("/api/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    @GetMapping("/{id}")
    public DocumentType fetchDocumentTypeById(@PathVariable Long id) {
        return documentTypeService.getDocumentTypeById(id);
    }

    @GetMapping
    public List<DocumentType> fetchAllDocumentTypes() {
        return documentTypeService.getAllDocumentTypes();
    }
}
