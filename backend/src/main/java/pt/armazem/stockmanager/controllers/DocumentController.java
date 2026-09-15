package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Document;
import pt.armazem.stockmanager.dtos.DocumentItemRequest;
import pt.armazem.stockmanager.dtos.DocumentRequest;
import pt.armazem.stockmanager.services.DocumentService;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/{id}")
    public Document fetchDocumentById(@PathVariable Long id){
        return documentService.getDocumentById(id);
    }

    @GetMapping
    public List<Document> fetchAllDocuments(){
        return documentService.getAllDocuments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Document receiveDocumentRequest(@Valid @RequestBody DocumentRequest request){
        return documentService.createDocument(request);
    }

    @PutMapping("/{id}/items")
    public Document updateDocumentById(@PathVariable Long id, @Valid @RequestBody List<DocumentItemRequest> request){
        return documentService.updateDocumentItems(id, request);
    }

}

