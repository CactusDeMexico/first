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
        String lastSpotDescription = "";
        String lastSpotName="";
        for (Topo topo : topos) {

            System.out.println(topo.getIdtopo()+" TOPO "+topo.getLieu());
            for ( Spot spot : spots) {
                if(topo.getIdtopo() == spot.getIdtopo()) {
                    System.out.println(topo.getIdtopo() == spot.getIdtopo());
                    System.out.println(" le nom du spot " + spot.getNom());
                    System.out.println("DESCRIPTION " + spot.getDescription());
                    System.out.println(spot.getIdtopo());
                    System.out.println(spots.size()+" LA TAILLE");
                    lastSpotName=spot.getNom();
                    lastSpotDescription = spot.getDescription();
                }

            }
        }
        System.out.println("END");
        //System.out.println(+spots.get(2)+" le dernier");


        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("lastSpotName", lastSpotName);
        model.addObject("lastSpotDescription", lastSpotDescription);
        //model.addObject("test",spots);
        model.setViewName("user/test");
        return model;
    }
    @RequestMapping(value= {"/home"}, method=RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();
        List<Topo> topos = topoService.findAllTopo();
        List<Spot> spots = spotService.findAllSpot();

        String lastSpotDescription = "";
        String lastSpotName="";
        for (Topo topo : topos) {
            for ( Spot spot : spots) {
                if(topo.getIdtopo() == spot.getIdtopo()) {
                    lastSpotName=spot.getNom();
                    lastSpotDescription = spot.getDescription();
                }
            }
        }
        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("lastSpotName", lastSpotName);
        model.addObject("lastSpotDescription", lastSpotDescription);
        //model.addObject("test",spots);
        model.setViewName("home/home");
        return model;
    }

}
