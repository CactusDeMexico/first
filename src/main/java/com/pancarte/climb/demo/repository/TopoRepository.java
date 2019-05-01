package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.model.Topo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("topoRepository")
@Transactional
public interface TopoRepository extends JpaRepository<Topo, Long> {
    //todo: find last top
    //todo: find availabe topo
   // @Query("SELECT a FROM topo a")

    @Query(value = "SELECT * FROM topo u WHERE u.idtopo > 0",nativeQuery = true)
    List<Topo> findAllTopo();
    @Query(value = "SELECT idtopo FROM topo ORDER BY idtopo DESC LIMIT 1;",nativeQuery = true)
    int selectLastIdTopo();




}
