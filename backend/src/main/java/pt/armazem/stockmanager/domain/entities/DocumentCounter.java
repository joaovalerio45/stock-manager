package pt.armazem.stockmanager.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.armazem.stockmanager.domain.enums.OperationType;

@Entity
@Table(
    name = "document_counters",
    uniqueConstraints = { @UniqueConstraint(name = "document_counter_operation_year", columnNames = {"operation_type", "year"})}
)
@Getter
@Setter
@NoArgsConstructor
public class DocumentCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 20)
    private OperationType operationType;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber = 0L;
}
