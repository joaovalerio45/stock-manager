package pt.armazem.stockmanager.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.DocumentCounter;
import pt.armazem.stockmanager.domain.enums.OperationType;
import pt.armazem.stockmanager.repositories.DocumentCounterRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentCounterService {

    private final DocumentCounterRepository documentCounterRepository;

    public Long incrementDocCounter(OperationType ot, Integer year) {
        DocumentCounter dc = documentCounterRepository.findByOperationTypeAndYear(ot, year)
            .orElseGet(() -> {
                DocumentCounter dcNew = new DocumentCounter();
                dcNew.setOperationType(ot);
                dcNew.setYear(year);
                dcNew.setLastNumber(0L);
                return dcNew;
            });
        Long nextNumber = dc.getLastNumber() + 1;
        dc.setLastNumber(nextNumber);
        documentCounterRepository.save(dc);
        return nextNumber;
    }
}
