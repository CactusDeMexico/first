package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.repository.SpotRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
}
