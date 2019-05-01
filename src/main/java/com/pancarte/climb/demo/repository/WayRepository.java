package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Way;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("wayRepository")
public interface WayRepository extends JpaRepository<Way,Integer> {

}
