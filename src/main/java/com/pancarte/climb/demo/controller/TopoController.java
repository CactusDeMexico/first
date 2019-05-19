package com.pancarte.climb.demo.controller;

import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.*;
import com.pancarte.climb.demo.service.PublicationService;
import com.pancarte.climb.demo.service.SpotService;
import com.pancarte.climb.demo.service.TopoService;
import com.pancarte.climb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Random;


@Controller
public class TopoController {

    private final TopoService topoService;
    private final SpotService spotService;
    private final UserService userService;
    private final PublicationService publicationService;
    private final SecteurRepository secteurRepository;
    private final WayRepository wayRepository;

    private final CommentaireRepository commentaireRepository;

    private final RentRepository rentRepository;

    @Autowired
    public TopoController(TopoService topoService, SpotService spotService, UserService userService, PublicationService publicationService, SecteurRepository secteurRepository, @Qualifier("wayRepository") WayRepository wayRepository, @Qualifier("commentaireRepository") CommentaireRepository commentaireRepository, @Qualifier("rentRepository") RentRepository rentRepository) {
        this.topoService = topoService;
        this.spotService = spotService;
        this.userService = userService;
        this.publicationService = publicationService;
        this.secteurRepository = secteurRepository;
        this.wayRepository = wayRepository;
        this.commentaireRepository = commentaireRepository;
        this.rentRepository = rentRepository;
    }

    @RequestMapping(value = {"/rent"}, method = RequestMethod.GET)
    public ModelAndView rentTopo(@RequestParam("idtopo") int idtopo) {
        ModelAndView model = new ModelAndView();
        Rent rent= new Rent();
        List<Topo> topo = topoService.findById(idtopo);
        List<Spot> spot = spotService.findAllSpot();
        model.addObject("topo", topo);
        model.addObject("spot", spot);
        model.addObject("rent", rent);
        model.setViewName("user/rent");
        return model;
    }

    @RequestMapping(value = {"/rent"}, method = RequestMethod.POST)
    public String rentIt(@RequestParam("idtopo") int idtopo, Rent rent) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println("_______________"+rent.getReturnDate());
        rent.setIsloan(false);
        rent.setIdtopo(idtopo);
        rent.setIduser(user.getId());
        topoService.rentTopo(rent);
        return "redirect:/home/loggedhome";
    }

    //todo: logout
    //todo: pret topo

    @RequestMapping(value = {"/com"}, method = RequestMethod.POST)
    public String comment(Commentaire commentaire) {
        topoService.saveCommentaire(commentaire);
        return "redirect:home";
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public ModelAndView search(String search) {
        ModelAndView model = new ModelAndView();
        System.out.println(search + " la recherche ");
        List<Spot> spots = spotService.findAllSpot();
        List<Topo> topos = topoService.findAllTopo();

        String lastSpotDescription = "";
        String lastSpotName = "";
        for (Topo topo : topos) {
            for (Spot spot : spots) {
                if (topo.getIdtopo() == spot.getIdtopo()) {
                    lastSpotName = spot.getNomSpot();
                    lastSpotDescription = spot.getDescription();
                }
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        spots = spotService.findByName(search);
        topos = topoService.findByLieu(search);
        for (Spot spot : spots) {

            System.out.println(" le topo " + spot.getNomSpot());
            if (!topos.containsAll(topoService.findById(spot.getIdtopo()))) {
                topos.addAll(topoService.findById(spot.getIdtopo()));
            }
        }

        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastname());
        } else {
            model.addObject("userName", "0");
        }
        spots = spotService.findAllSpot();

        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("lastSpotName", lastSpotName);
        model.addObject("lastSpotDescription", lastSpotDescription);
        model.setViewName("home/home");
        return model;
    }

    @RequestMapping(value = {"/publication/"}, method = RequestMethod.GET)
    public ModelAndView publication(@RequestParam("idtopo") int idtopo, @RequestParam("idspot") int idspot) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Topo> topos = topoService.findById(idtopo);

        for (Topo topo : topos) {

            System.out.println(topo.getLieuTopo() + " ____");
        }
        System.out.println(" voici l'id topo " + idspot + " id spot" + idspot);
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastname());
            model.addObject("userConnectedId", user.getId());
        } else {
            model.addObject("userName", "0");
        }

        List<Secteur> secteurs = secteurRepository.findByIdSpot(idspot);
        List<Spot> spots = spotService.findByIdtopo(idtopo);
        List<Way> ways = wayRepository.findAll();
        int idPub = 0;
        Commentaire commentaires = new Commentaire();
        for (Spot spot : spots) {

            System.out.println(spot.getIdpublication() + " ____");
            idPub = spot.getIdpublication();
            Publication pub = publicationService.findAllById(spot.getIdpublication());
            System.out.println(pub.getName());
        }
        // List<User> users = userService.f
        List<Commentaire> comment = commentaireRepository.findAllByIdPublication(idPub);
        Publication pub = publicationService.findAllById(idPub);
        System.out.println(pub.getIdpublication());
        String search = "";
        model.addObject("search", search);
        model.addObject("comment", comment);
        model.addObject("publication", pub);
        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("secteur", secteurs);
        model.addObject("way", ways);
        model.addObject("commentaire", commentaires);
        model.setViewName("home/publication");
        return model;
    }

    @RequestMapping(value = {"/insertPublication"}, method = RequestMethod.GET)
    public ModelAndView insertPublication() {
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
        String search = "";
        model.addObject("search", search);
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
    public String savePublication(@RequestParam("file") MultipartFile imgSpot, @RequestParam("fileSecteur") MultipartFile imgSecteur, Publication publication, Topo topo, Spot spot, Secteur secteur, Way way) throws IOException {
        //ModelAndView model = new ModelAndView();
        System.out.println("test " + imgSpot.getOriginalFilename());
        Path currentRelativePath = Paths.get("");
        System.out.println("PATH /" + currentRelativePath.toAbsolutePath());
        String path = "\\src\\main\\resources\\static\\img\\";

        String s = (currentRelativePath.toAbsolutePath().toString()) + path;
        System.out.println("____________________XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        byte[] bytesSpot = imgSpot.getBytes();
        byte[] bytesSecteur = imgSecteur.getBytes();
        Random random = new Random();
        int numb;
        Path pathSpot = Paths.get(s + imgSpot.getOriginalFilename());
        Path pathSector = Paths.get(s + imgSecteur.getOriginalFilename());

        while (pathSpot.toFile().exists() || pathSector.toFile().exists() || pathSector == pathSpot) {
            numb = random.nextInt(10);
            System.out.println();
            if (pathSpot.toFile().exists()) {
                pathSpot = Paths.get(s + numb + imgSpot.getOriginalFilename());
            }
            if (pathSector.toFile().exists()) {
                pathSector = Paths.get(s + numb + imgSpot.getOriginalFilename());
            }
            if (pathSector == pathSpot) {
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
        // model.setViewName("home/home");
        return "redirect:home";
    }

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView model = new ModelAndView();
        List<Topo> topos = topoService.findAllTopo();
        List<Spot> spots = spotService.findAllSpot();
        String lastSpotDescription = "";
        String lastSpotName = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        for (Topo topo : topos) {
            for (Spot spot : spots) {
                if (topo.getIdtopo() == spot.getIdtopo()) {
                    lastSpotName = spot.getNomSpot();
                    lastSpotDescription = spot.getDescription();
                }
            }
        }
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastname());
        } else {
            model.addObject("userName", "0");
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
