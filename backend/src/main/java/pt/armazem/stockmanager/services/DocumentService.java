package pt.armazem.stockmanager.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Document;
import pt.armazem.stockmanager.domain.entities.DocumentItem;
import pt.armazem.stockmanager.domain.entities.DocumentType;
import pt.armazem.stockmanager.domain.entities.Item;
import pt.armazem.stockmanager.domain.entities.Request;
import pt.armazem.stockmanager.domain.entities.RequestItem;
import pt.armazem.stockmanager.domain.enums.OperationType;
import pt.armazem.stockmanager.dtos.DocumentItemRequest;
import pt.armazem.stockmanager.dtos.DocumentRequest;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.DocumentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentCounterService documentCounterService;
    private final DocumentTypeService documentTypeService;
    private final WarehouseService warehouseService;
    private final ServiceAreaService serviceAreaService;
    private final ItemService itemService;
    private final WarehouseStockService warehouseStockService;
    private final ExternalEntityService externalEntityService;
    private final RequestService requestService;

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document createDocument(DocumentRequest dr) {
        DocumentType dt = documentTypeService.getActiveDocumentTypeById(dr.documentTypeId());
        OperationType ot = dt.getOperationType();
        int year = dr.documentDate().getYear();

        validateOriginDestination(ot, dr);

        Long sn = documentCounterService.incrementDocCounter(ot, year);
        String idc = String.format("%s-%d/%d", ot.getPrefix(), year, sn);

        Document doc = new Document();
        doc.setDocumentType(dt);
        doc.setOperationType(ot);
        doc.setYear(year);
        doc.setSequenceNumber(sn);
        doc.setInternalDocumentNumber(idc);
        doc.setOriginDocumentNumber(dr.originDocumentNumber());
        doc.setDocumentDate(dr.documentDate());
        doc.setObservations(dr.observations());

        if (dr.originWarehouseId() != null) {
            doc.setOriginWarehouse(warehouseService.getActiveWarehouseById(dr.originWarehouseId()));
        }
        if (dr.originServiceAreaId() != null) {
            doc.setOriginServiceArea(serviceAreaService.getActiveServiceAreaById(dr.originServiceAreaId()));
        }
        if (dr.destinationWarehouseId() != null) {
            doc.setDestinationWarehouse(warehouseService.getActiveWarehouseById(dr.destinationWarehouseId()));
        }
        if (dr.destinationServiceAreaId() != null) {
            doc.setDestinationServiceArea(serviceAreaService.getActiveServiceAreaById(dr.destinationServiceAreaId()));
        }
        if (dr.externalEntityId() != null) {
            doc.setExternalEntity(externalEntityService.getActiveExternalEntityById(dr.externalEntityId()));
        }
        Request req = null;
        if (dr.requestId() != null) {
            req = requestService.getFulfillableRequestById(dr.requestId());
            doc.setRequest(req);
        }

        populateDocument(doc, dr.items());
        Document savedDoc = documentRepository.save(doc);

        if (req != null) {
            for (DocumentItem line : doc.getItems()) {
                for (RequestItem reqItem : req.getItems()) {
                    if (reqItem.getItem().getId().equals(line.getItem().getId())) {
                        reqItem.setFulfilledQuantity(reqItem.getFulfilledQuantity().add(line.getQuantity()));
                    }
                }
            }
            requestService.fulfillRequest(req);
        }

        return savedDoc;
    }

    public Document updateDocumentItems(Long id, List<DocumentItemRequest> dir) {
        Document doc = getDocumentById(id);

        if (doc.getDocumentType().getStockMovementEnabled()) {
            for (DocumentItem line : doc.getItems()) {
                if (doc.getOriginWarehouse() != null) {
                    warehouseStockService.addStock(doc.getOriginWarehouse(), line.getItem(), line.getQuantity());
                }
                if (doc.getDestinationWarehouse() != null) {
                    warehouseStockService.deductStock(doc.getDestinationWarehouse(), line.getItem(), line.getQuantity());
                }
            }
        }
        doc.getItems().clear();

        populateDocument(doc, dir);

        return documentRepository.save(doc);
    }

    private Document populateDocument(Document doc, List<DocumentItemRequest> dir) {
        for (DocumentItemRequest request : dir) {
            Item item = itemService.getActiveItemById(request.itemId());

            BigDecimal unitPrice = request.unitPriceExclVat() != null ? request.unitPriceExclVat() : item.getLastPriceNoVat();
            if (unitPrice == null) {
                throw new BusinessRuleException("Unit price for '" + item.getName() + "' is required.");
            }

            BigDecimal vatRate = request.vatRate() != null ? request.vatRate() : item.getVatRate();
            if (vatRate == null) {
                throw new BusinessRuleException("VAT rate for '" + item.getName() + "' is required.");
            }

            BigDecimal totalExclVat = unitPrice.multiply(request.quantity());
            BigDecimal totalVat = totalExclVat.multiply(vatRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal totalInclVat = totalExclVat.add(totalVat);
            BigDecimal unitPriceInclVat = unitPrice.add(unitPrice.multiply(vatRate).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));

            DocumentItem line = new DocumentItem();
            line.setDocument(doc);
            line.setItem(item);
            line.setQuantity(request.quantity());
            line.setUnitPriceExclVat(unitPrice);
            line.setVatRate(vatRate);
            line.setUnitPriceInclVat(unitPriceInclVat);
            line.setTotalLineExclVat(totalExclVat);
            line.setTotalLineVat(totalVat);
            line.setTotalLineInclVat(totalInclVat);

            doc.getItems().add(line);

            if (doc.getDocumentType().getStockMovementEnabled()) {
                if (doc.getOriginWarehouse() != null) {
                    warehouseStockService.deductStock(doc.getOriginWarehouse(), item, line.getQuantity());
                }
                if (doc.getDestinationWarehouse() != null) {
                    warehouseStockService.addStock(doc.getDestinationWarehouse(), item, line.getQuantity());
                }
            }
        }
        return doc;
    }

    private void validateOriginDestination(OperationType ot, DocumentRequest dr) {
        switch (ot) {
            case ENTRY -> {
                if (dr.destinationWarehouseId() == null)
                    throw new BusinessRuleException("ENTRY requires a destination warehouse.");
            }
            case WITHDRAWAL -> {
                if (dr.originWarehouseId() == null)
                    throw new BusinessRuleException("WITHDRAWAL requires an origin warehouse.");
            }
            case TRANSFER -> {
                if (dr.originWarehouseId() == null || dr.destinationWarehouseId() == null)
                    throw new BusinessRuleException("TRANSFER requires both origin and destination warehouses.");
            }
            case RETURN -> {
                if (dr.destinationWarehouseId() == null)
                    throw new BusinessRuleException("RETURN requires a destination warehouse.");
            }
            case ADJUSTMENT -> {
                if (dr.originWarehouseId() == null)
                    throw new BusinessRuleException("ADJUSTMENT requires a warehouse.");
            }
            case REQUEST -> {
                    throw new BusinessRuleException("Can't create a document with REQUEST type.");
            }
        }
    }
}