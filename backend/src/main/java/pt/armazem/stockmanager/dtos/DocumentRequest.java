package pt.armazem.stockmanager.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentRequest(
    @NotNull Long documentTypeId,
    @Size(max = 50) String originDocumentNumber,
    @NotNull LocalDate documentDate,
    Long originWarehouseId,
    Long originServiceAreaId,
    Long destinationWarehouseId,
    Long destinationServiceAreaId,
    @NotEmpty @Valid List<DocumentItemRequest> items,
    Long requestId,
    Long externalEntityId,
    @Size(max = 500) String observations
) {}
