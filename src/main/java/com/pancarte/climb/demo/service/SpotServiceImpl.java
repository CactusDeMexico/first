package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.repository.SpotRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("spotService")
public class SpotServiceImpl  implements SpotService{
    @Autowired
    private SpotRepository spotRepository;

    @Override
    public List<Spot> findAllSpot() {
        return spotRepository.findAllSpot();
    }
    @Override
    public List<Spot> findByIdtopo(@Param("idtopo") int idtopo) {

        return spotRepository.findByIdtopo(idtopo);
    }
}
