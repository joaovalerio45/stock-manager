package pt.armazem.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.armazem.stockmanager.domain.entities.Document;

public interface DocumentRepository extends JpaRepository<Document,Long>{

}
