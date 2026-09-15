package pt.armazem.stockmanager.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "document_items")
@Getter
@Setter
@NoArgsConstructor
public class DocumentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_price_excl_vat", nullable = false, precision = 12, scale = 4)
    private BigDecimal unitPriceExclVat = BigDecimal.ZERO;

    @Column(name = "vat_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal vatRate = BigDecimal.ZERO;

    @Column(name = "unit_price_incl_vat", nullable = false, precision = 12, scale = 4)
    private BigDecimal unitPriceInclVat = BigDecimal.ZERO;

    @Column(name = "total_line_excl_vat", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLineExclVat = BigDecimal.ZERO;

    @Column(name = "total_line_vat", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLineVat = BigDecimal.ZERO;

    @Column(name = "total_line_incl_vat", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalLineInclVat = BigDecimal.ZERO;
}