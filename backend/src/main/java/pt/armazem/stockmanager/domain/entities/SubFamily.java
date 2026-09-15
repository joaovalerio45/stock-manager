package pt.armazem.stockmanager.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "subfamilies",
    uniqueConstraints = {
        @UniqueConstraint(name = "subfamily_family_code", columnNames = {"family_id", "code"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SubFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
