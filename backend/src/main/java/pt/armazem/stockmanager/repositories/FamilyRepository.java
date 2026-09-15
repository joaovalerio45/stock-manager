package pt.armazem.stockmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.armazem.stockmanager.domain.entities.Family;

public interface FamilyRepository extends JpaRepository<Family,Long>{

}
