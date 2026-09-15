package pt.armazem.stockmanager.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RequestItemRequest(
    @NotNull Long itemId,
    @NotNull @Positive BigDecimal requestedQuantity
) {}

