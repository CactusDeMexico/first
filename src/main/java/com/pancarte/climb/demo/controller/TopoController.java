package com.pancarte.climb.demo.controller;

import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.PublicationRepository;
import com.pancarte.climb.demo.repository.SecteurRepository;
import com.pancarte.climb.demo.repository.SpotRepository;
import com.pancarte.climb.demo.repository.WayRepository;
import com.pancarte.climb.demo.service.PublicationService;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
public class TopoController {
    @Autowired
    private TopoService topoService;
    @Autowired
    private SpotService spotService;
    @Autowired
    private UserService userService;
    @Autowired
    private PublicationService publicationService;

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    public ModelAndView lastTopo()
    {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Publication> publication = publicationService.findByIdUser(user.getId());
        for (Publication publications : publication)
        {
            System.out.println("_______________");
            System.out.println(publications.getIdpublication());
            System.out.println(publications.getName());
            System.out.println(publications.getCreationdate());
        }
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.setViewName("home/loggedHome");
        return model;
    }
    @RequestMapping(value = {"/insertPublication"}, method = RequestMethod.GET)
    public ModelAndView insertPublication()
    {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getId());
        Publication pub = new Publication();
        Topo topo = new Topo();
        Spot spot = new Spot();
        Secteur secteur = new Secteur();
        Way way = new Way();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        List<Topo> topos = topoService.findAllTopo();
        model.addObject("topos", topos);
        model.addObject("userName", user);
        model.addObject("publication", pub);
        model.addObject("topo", topo);
        model.addObject("spot", spot);
        model.addObject("secteur", secteur);
        model.addObject("way", way);
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.setViewName("user/inscriptionTopo");
        return model;
    }

    @RequestMapping(value = {"/insertPublication"}, method = RequestMethod.POST, headers = "content-type=multipart/*")
    public ModelAndView savePublication(@RequestParam("file") MultipartFile imgSpot, @RequestParam("fileSecteur") MultipartFile imgSecteur, Publication publication, Topo topo, Spot spot, Secteur secteur, Way way) throws IOException
    {
        ModelAndView model = new ModelAndView();
        System.out.println("test " + imgSpot.getOriginalFilename());
        Path currentRelativePath = Paths.get("");
        System.out.println("PATH /" + currentRelativePath.toAbsolutePath());
        String path="\\src\\main\\resources\\static\\img\\";

        String s = (currentRelativePath.toAbsolutePath().toString())+path;

        byte[] bytesSpot = imgSpot.getBytes();
        byte[] bytesSecteur = imgSecteur.getBytes();
        Random random = new Random();
        int numb;
        Path pathSpot = Paths.get(s + imgSpot.getOriginalFilename());
        Path pathSector = Paths.get(s + imgSecteur.getOriginalFilename());

        while (pathSpot.toFile().exists()||pathSector.toFile().exists()||pathSector==pathSpot)
        {
            numb = random.nextInt(10);
            System.out.println();
            if(pathSpot.toFile().exists())
            {
                pathSpot = Paths.get(s + numb + imgSpot.getOriginalFilename());
            }
            if(pathSector.toFile().exists())
            {
                pathSector = Paths.get(s + numb + imgSpot.getOriginalFilename());
            }
            if(pathSector==pathSpot)
            {
                pathSector = Paths.get(s + numb + imgSpot.getOriginalFilename());
            }
        }

        // enregistrement img
        System.out.println(pathSpot);
        Files.write(pathSpot, bytesSpot);
        Files.write(pathSector, bytesSecteur);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        topoService.savePublication(publication, topo, spot, secteur, way, user.getId(), imgSpot.getOriginalFilename(), imgSecteur.getOriginalFilename());
        model.setViewName("home/home");
        return model;
    }

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public ModelAndView home()
    {
        ModelAndView model = new ModelAndView();
        List<Topo> topos = topoService.findAllTopo();
        List<Spot> spots = spotService.findAllSpot();
        String lastSpotDescription = "";
        String lastSpotName = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        for (Topo topo : topos)
        {
            for (Spot spot : spots)
            {
                if (topo.getIdtopo() == spot.getIdtopo())
                {
                    lastSpotName = spot.getNomSpot();
                    lastSpotDescription = spot.getDescription();
                }
            }
        }
        if(!auth.getName().equals("anonymousUser"))
        {
            model.addObject("userName", user.getName() + " " + user.getLastname());
        }
        else
        {
            model.addObject("userName","0");
        }
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getName().isEmpty());
        System.out.println(auth.getName());





        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("lastSpotName", lastSpotName);
        model.addObject("lastSpotDescription", lastSpotDescription);
        model.setViewName("home/home");
        return model;
    }
}
