package pt.armazem.stockmanager.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemRequest(
    @NotBlank @Size(max = 20) String code,
    @NotBlank @Size(max = 50) String name,
    @NotNull Long subFamilyId,
    @NotNull Long measurementUnitId,
    @Size(max = 100) String description,
    BigDecimal vatRate
) {}
