package com.pancarte.climb.demo.controller;

import com.pancarte.climb.demo.model.Spot;
import com.pancarte.climb.demo.model.Topo;
import com.pancarte.climb.demo.model.User;
import com.pancarte.climb.demo.service.SpotService;
import com.pancarte.climb.demo.service.TopoService;
import com.pancarte.climb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class TopoController {
    @Autowired
    private TopoService topoService;
    @Autowired
    private SpotService spotService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    public ModelAndView lastTopo() {
        ModelAndView model = new ModelAndView();
        //todo: trouver l'id user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getId());
        List<Topo> topos = topoService.findAllTopo();
        model.addObject("topo", topos);
        model.addObject("userName", user);
        //model.setViewName("home/loggedHome");
/*
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
        */
        //model.addObject("test",spots);
        model.setViewName("user/inscriptionTopo");
        return model;
    }

    @RequestMapping(value = {"/test"}, method = RequestMethod.POST , headers = "content-type=multipart/*")
    public ModelAndView savePublication(@RequestParam("file") MultipartFile myFile) throws IOException {
        ModelAndView model = new ModelAndView();
        //create a temp file

        System.out.println("test "+myFile.getOriginalFilename());

         //String UPLOADED_FOLDER = "C:/Users/vivi/Desktop/x1/COURS/BTS/Java/first/src/main/resources/static/img/";



        Path currentRelativePath = Paths.get("");

        String s = (currentRelativePath.toAbsolutePath().toString()).concat("\\");

        byte[] bytes = myFile.getBytes();
        Path path = Paths.get(s +myFile.getOriginalFilename());
        Files.write(path, bytes);




       // System.out.println(spot.getLien()+" VOICI LE LIEN");
        // on récupère l'utilisation
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //User user = userService.findUserByEmail(auth.getName());
        //todo: ajouter l'id
            //user.getId();
        //todo:Insertion dans l'ordre suivante PUBLICATION , TOPO,spot,secteur,voie ,proprio, rent
        //todo: verifier l'insertion de la publication
        model.addObject("img",myFile.getOriginalFilename());
        model.setViewName("home/test");
        return model;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();
        List<Topo> topos = topoService.findAllTopo();
        List<Spot> spots = spotService.findAllSpot();

        String lastSpotDescription = "";
        String lastSpotName = "";
        for (Topo topo : topos) {
            for (Spot spot : spots) {
                if (topo.getIdtopo() == spot.getIdtopo()) {
                    lastSpotName = spot.getNom();
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
