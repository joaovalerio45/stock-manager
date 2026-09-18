package pt.armazem.stockmanager.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.armazem.stockmanager.domain.entities.DocumentCounter;
import pt.armazem.stockmanager.domain.enums.OperationType;
import pt.armazem.stockmanager.repositories.DocumentCounterRepository;

@ExtendWith(MockitoExtension.class)
public class DocumentCounterServiceTest {

    @Mock
    private DocumentCounterRepository documentCounterRepository;

    @InjectMocks 
    private DocumentCounterService documentCounterService;


    @Test
    @DisplayName (value = "Creates a new documentcounter for the first document of a given type")
    void createDocCounterwithType(){
        OperationType op = OperationType.ENTRY;
        Integer year = 2026;

        when(documentCounterRepository.findByOperationTypeAndYear(op, year))
            .thenReturn(Optional.empty());

        when(documentCounterRepository.save(any(DocumentCounter.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Long result = documentCounterService.incrementDocCounter(op, year);

        assertEquals(1, result);
        verify(documentCounterRepository, times(1)).save(any(DocumentCounter.class));
    }

    @Test
    @DisplayName("Creates a new document entry for a already existing document counter, which should be incremented")
    void incrementExistingCounter(){
        DocumentCounter dc = new DocumentCounter();
        OperationType op = OperationType.ENTRY;
        Integer year = 2026;


        dc.setLastNumber(5L);
        dc.setOperationType(op);
        dc.setYear(2026);

        when(documentCounterRepository.findByOperationTypeAndYear(op, year))
            .thenReturn(Optional.of(dc));

        Long result = documentCounterService.incrementDocCounter(op, year);

        assertEquals(6L, result);
        verify(documentCounterRepository, times(1)).save(dc);

    }
}
