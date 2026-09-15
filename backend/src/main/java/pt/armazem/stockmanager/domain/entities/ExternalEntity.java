package pt.armazem.stockmanager.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pt.armazem.stockmanager.domain.enums.EntityType;

@Entity
@Table(name = "external_entities")
@Setter
@Getter
@NoArgsConstructor
public class ExternalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "abbreviation", nullable = false, length = 100)
    private String abbreviation;

    @Column(length = 9)
    private String nif;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private EntityType type;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
