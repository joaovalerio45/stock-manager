package pt.armazem.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.armazem.stockmanager.domain.entities.ExternalEntity;

public interface ExternalEntityRepository extends JpaRepository<ExternalEntity,Long>{

}
