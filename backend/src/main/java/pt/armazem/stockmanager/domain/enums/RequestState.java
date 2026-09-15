package pt.armazem.stockmanager.domain.enums;

import lombok.Getter;

@Getter
public enum RequestState {
    PENDING,
    PREPARING,
    FULFILLED,
    CANCELED
}
