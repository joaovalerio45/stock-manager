package pt.armazem.stockmanager.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Request;
import pt.armazem.stockmanager.domain.entities.RequestItem;
import pt.armazem.stockmanager.domain.enums.OperationType;
import pt.armazem.stockmanager.domain.enums.RequestState;
import pt.armazem.stockmanager.dtos.RequestItemRequest;
import pt.armazem.stockmanager.dtos.RequestRequest;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.RequestRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final ServiceAreaService serviceAreaService;
    private final WarehouseService warehouseService;
    private final DocumentCounterService documentCounterService;
    private final ItemService itemService;

    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getFulfillableRequestById(Long id) {
        Request request = requestRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));
        if (request.getState() != RequestState.PREPARING) {
            throw new BusinessRuleException("Request '" + request.getNumber() + "' is in state " + request.getState() + ". Only requests in PREPARING state can be fulfilled.");
        }
        return request;
    }

    public Request createRequest(RequestRequest request) {
        Request req = new Request();
        req.setServiceArea(serviceAreaService.getActiveServiceAreaById(request.serviceAreaId()));
        req.setWarehouse(warehouseService.getActiveWarehouseById(request.warehouseId()));
        
        int year = LocalDate.now().getYear();
        Long seq = documentCounterService.incrementDocCounter(OperationType.REQUEST, year);
        String reqNumber = String.format("%s-%d/%d", OperationType.REQUEST.getPrefix(), year, seq);
        req.setNumber(reqNumber);

        if (request.requestNotes() != null) {
            req.setRequestNotes(request.requestNotes());
        }

        for (RequestItemRequest line : request.items()) {
            RequestItem requestItem = new RequestItem();
            requestItem.setItem(itemService.getActiveItemById(line.itemId()));
            requestItem.setRequestedQuantity(line.requestedQuantity());
            requestItem.setRequest(req);
            req.getItems().add(requestItem);
        }

        return requestRepository.save(req);
    }

    public Request updateRequest(Long id, RequestRequest request) {
        Request req = requestRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));

        if (req.getState() != RequestState.PENDING) {
            throw new BusinessRuleException("Request cannot be edited because it is in state: " + req.getState());
        }

        req.setRequestNotes(request.requestNotes());
        
        req.getItems().clear();
        for (RequestItemRequest line : request.items()) {
            RequestItem requestItem = new RequestItem();
            requestItem.setItem(itemService.getActiveItemById(line.itemId()));
            requestItem.setRequestedQuantity(line.requestedQuantity());
            requestItem.setRequest(req);
            req.getItems().add(requestItem);
        }

        return requestRepository.save(req);
    }

    public Request markPreparing(Long id) {
        Request req = requestRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));
        if (req.getState() != RequestState.PENDING) {
            throw new BusinessRuleException("Only PENDING requests can be marked as PREPARING. Current state: " + req.getState());
        }
        req.setState(RequestState.PREPARING);
        return requestRepository.save(req);
    }

    public Request releasePreparing(Long id) {
        Request req = requestRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));
        if (req.getState() != RequestState.PREPARING) {
            throw new BusinessRuleException("Only PREPARING requests can be released back to PENDING. Current state: " + req.getState());
        }
        req.setState(RequestState.PENDING);
        return requestRepository.save(req);
    }

    public Request cancelRequest(Long id) {
        Request req = requestRepository.findByIdForUpdate(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));
        if (req.getState() == RequestState.FULFILLED) {
            throw new BusinessRuleException("Cannot cancel a request that has already been fulfilled.");
        }
        if (req.getState() == RequestState.CANCELED) {
            throw new BusinessRuleException("Request is already canceled.");
        }
        req.setState(RequestState.CANCELED);
        return requestRepository.save(req);
    }

    public void fulfillRequest(Request req) {
        req.setState(RequestState.FULFILLED);
        req.setFulfillmentDate(LocalDateTime.now());
        requestRepository.save(req);
    }
}
