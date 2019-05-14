package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Publication;
import com.pancarte.climb.demo.model.Secteur;
import com.pancarte.climb.demo.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SecteurRepository")
public interface SecteurRepository extends JpaRepository<Secteur,Integer> {
    @Query(value = "SELECT * FROM secteur u WHERE u.idsecteur > 0",nativeQuery = true)
    List<Secteur> findAllSecteur();

    @Query(value = "SELECT idsecteur FROM secteur ORDER BY idsecteur DESC LIMIT 1;",nativeQuery = true)
    int selectLastIdSecteur();

    @Query(value = "SELECT * FROM secteur u WHERE u.nom =:nom",nativeQuery = true)
    List<Secteur> findByName(@Param("nom") String nom);
    @Query(value = "SELECT * FROM secteur u WHERE u.lieu =:nom",nativeQuery = true)
    List<Spot> findByLieu(@Param("lieu") String nom);
    @Query(value = "SELECT * FROM secteur u WHERE u.type =:nom",nativeQuery = true)
    List<Spot> findByType(@Param("type") String nom);
}
