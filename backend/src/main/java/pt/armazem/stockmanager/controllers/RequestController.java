package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Request;
import pt.armazem.stockmanager.dtos.RequestRequest;
import pt.armazem.stockmanager.services.RequestService;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{id}")
    public Request fetchRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id);
    }

    @GetMapping
    public List<Request> fetchAllRequests() {
        return requestService.getAllRequests();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Request createRequest(@Valid @RequestBody RequestRequest request) {
        return requestService.createRequest(request);
    }

    @PutMapping("/{id}")
    public Request updateRequest(@PathVariable Long id, @Valid @RequestBody RequestRequest request) {
        return requestService.updateRequest(id, request);
    }

    @PatchMapping("/{id}/preparing")
    public Request markPreparing(@PathVariable Long id) {
        return requestService.markPreparing(id);
    }

    @PatchMapping("/{id}/release")
    public Request releasePreparing(@PathVariable Long id) {
        return requestService.releasePreparing(id);
    }

    @PatchMapping("/{id}/cancel")
    public Request cancelRequest(@PathVariable Long id) {
        return requestService.cancelRequest(id);
    }
}
