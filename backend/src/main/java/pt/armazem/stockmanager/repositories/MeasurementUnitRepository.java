package pt.armazem.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.armazem.stockmanager.domain.entities.MeasurementUnit;

public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit,Long>{
    
        
}
