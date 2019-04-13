package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("spotRepository")
@Transactional
public interface SpotRepository extends JpaRepository<Spot, Long> {


    @Query(value = "SELECT * FROM spot u WHERE u.idspot > 0",nativeQuery = true)
    List<Spot> findAllSpot();
}
