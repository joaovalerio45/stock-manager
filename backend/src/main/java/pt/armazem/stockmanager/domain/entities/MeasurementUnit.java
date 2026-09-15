package pt.armazem.stockmanager.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "measurement_units")
@Getter
@Setter
@NoArgsConstructor
public class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "abbreviation", nullable = false, unique = true, length = 10)
    private String abbreviation;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "allows_decimals", nullable = false)
    private Boolean allowsDecimals = false;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}