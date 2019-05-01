package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Secteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SecteurRepository")
public interface SecteurRepository extends JpaRepository<Secteur,Integer> {
    @Query(value = "SELECT * FROM secteur u WHERE u.idsecteur > 0",nativeQuery = true)
    List<Secteur> findAllSecteur();

    @Query(value = "SELECT idsecteur FROM secteur ORDER BY idsecteur DESC LIMIT 1;",nativeQuery = true)
    int selectLastIdSecteur();
}
