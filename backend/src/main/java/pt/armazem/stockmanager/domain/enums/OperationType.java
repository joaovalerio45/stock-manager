package pt.armazem.stockmanager.domain.enums;

import lombok.Getter;

@Getter
public enum OperationType {
    ENTRY("ENT"),
    WITHDRAWAL("SAI"),
    RETURN("DEV"),
    TRANSFER("TRF"),
    ADJUSTMENT("AJU"),
    REQUEST("REQ");

    private final String prefix;

    OperationType(String prefix){
        this.prefix = prefix;
}


}
