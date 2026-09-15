package pt.armazem.stockmanager.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestRequest(
    @NotNull Long serviceAreaId,
    @NotNull Long warehouseId,
    @NotEmpty @Valid List<RequestItemRequest> items,
    @Size(max = 500) String requestNotes
) {}
