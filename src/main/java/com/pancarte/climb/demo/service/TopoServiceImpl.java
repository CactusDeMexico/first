package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.model.Topo;
import com.pancarte.climb.demo.repository.TopoRepository;
import com.pancarte.climb.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("topoService")
public class TopoServiceImpl implements TopoService{

    @Autowired
    private TopoRepository topoRepository;

    @Override
    public List<Topo> findAllTopo() {

        return topoRepository.findAllTopo();
    }



}
