package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.model.Way;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("wayRepository")
public interface WayRepository extends JpaRepository<Way,Integer> {
    @Query(value = "SELECT * FROM voie u WHERE u.nom =:nom",nativeQuery = true)
    List<Spot> findByName(@Param("nom") String nom);

    @Query(value = "SELECT * FROM voie u WHERE u.cotation =:nom",nativeQuery = true)
    List<Spot> findByCotation(@Param("cotation") String nom);

    @Query(value = "SELECT * FROM voie u WHERE u.equipees =:nom",nativeQuery = true)
    List<Spot> findByCotation(@Param("equipees") boolean nom);

}
