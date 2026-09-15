package pt.armazem.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.armazem.stockmanager.domain.entities.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType,Long>{

}
