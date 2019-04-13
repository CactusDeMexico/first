package com.pancarte.climb.demo.controller;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.model.Topo;
import com.pancarte.climb.demo.service.SpotService;
import com.pancarte.climb.demo.service.TopoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TopoController {
    @Autowired
    private TopoService topoService;
    @Autowired
    private SpotService spotService;

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    public ModelAndView lastTopo() {
        ModelAndView model = new ModelAndView();

        List<Topo> topos = topoService.findAllTopo();
        List<Spot> spots = spotService.findAllSpot();

        for (Topo topo : topos) {

            System.out.println(topo.getIdtopo()+" TOPO "+topo.getLieu());
            for ( Spot spot : spots) {
                if(topo.getIdtopo() == spot.getIdtopo()) {
                    System.out.println(topo.getIdtopo() == spot.getIdtopo());
                    System.out.println(" le nom du spot " + spot.getNom());
                    System.out.println("DESCRIPTION " + spot.getDescription());
                    System.out.println(spot.getIdtopo());
                }

            }
        }
        System.out.println("END");


        model.addObject("spot", spots);
        model.addObject("topo", topos);
        //model.addObject("test",spots);
        model.setViewName("user/test");
        return model;
    }
}
