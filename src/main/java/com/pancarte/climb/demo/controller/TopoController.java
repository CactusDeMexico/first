package com.pancarte.climb.demo.controller;

import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.*;
import com.pancarte.climb.demo.service.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Controller
public class TopoController {

    private final TopoService topoService;
     private final TopoRepository topoRepository;
    private final SpotService spotService;
    private final UserService userService;
    private final PublicationService publicationService;
    private final SecteurRepository secteurRepository;
    private final WayRepository wayRepository;
    private final ProprietaireRepository proprietaireRepository;
    private  final WayService wayService;

    private final CommentaireRepository commentaireRepository;
    private final RentRepository rentRepository;

    @Autowired
    public TopoController(TopoService topoService, @Qualifier("topoRepository") TopoRepository topoRepository, SpotService spotService, UserService userService, WayService wayService, PublicationService publicationService, SecteurRepository secteurRepository, @Qualifier("wayRepository") WayRepository wayRepository, @Qualifier("commentaireRepository") CommentaireRepository commentaireRepository, @Qualifier("rentRepository") RentRepository rentRepository, @Qualifier("proprietaireRepository") ProprietaireRepository proprietaireRepository) {
        this.topoService = topoService;
        this.topoRepository = topoRepository;
        this.spotService = spotService;
        this.userService = userService;
        this.wayService = wayService;
        this.publicationService = publicationService;
        this.secteurRepository = secteurRepository;
        this.wayRepository = wayRepository;
        this.commentaireRepository = commentaireRepository;
        this.rentRepository = rentRepository;
        this.proprietaireRepository = proprietaireRepository;
    }

   @RequestMapping(value = {"/content"}, method = RequestMethod.GET)
    public ModelAndView content(@RequestParam("idtopo") int idtopo) {
        ModelAndView model = new ModelAndView();
        Rent rent = new Rent();
        User user = new User();
        System.out.println(user.getEmail());
        System.out.println(user.getId());
        Publication pub = new Publication();
        Topo topo = new Topo();
        Spot spot = new Spot();
        Secteur secteur = new Secteur();
        List<Way> way = wayRepository.findAll();
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
        model.setViewName("fragment/content");

        return model;
    }
    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        model.addObject("view", "home");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/borrow"}, method = RequestMethod.GET)
    public ModelAndView borrowTopo(@RequestParam("idtopo") int idtopo) {
        ModelAndView model = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Topo> topo = topoService.findById(idtopo);
        List<Spot> spot = spotService.findAllSpot();
        Rent rent = rentRepository.getOne(idtopo);

        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.addObject("topo", topo);
        model.addObject("spot", spot);
        model.addObject("rent", rent);
        model.addObject("view", "borrow");
       // model.setViewName("user/borrow");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/validateDele"}, method = RequestMethod.GET)
    public String CancelRent(@RequestParam("idtopo") int idtopo) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Rent rent =rentRepository.getOne(idtopo);
        rent.setSeen(true);
        rent.setBorrow(false);
        Proprietaire proprietaire = proprietaireRepository.getOne(rent.getIdtopo());
        rent.setIduser(proprietaire.getIduser());
        rentRepository.save(rent);


        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/borrow"}, method = RequestMethod.POST)
    public String borrowIt(@RequestParam("idtopo") int idtopo, Rent rent) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        rent = rentRepository.getOne(idtopo);
        rent.setIsloan(true);
        rent.setIduser(user.getId());
        rent.setIdtopo(idtopo);
        rent.setBorrow(true);
        rentRepository.save(rent);
        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/borrowed"}, method = RequestMethod.GET)
    public ModelAndView borrowed() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Rent> rents = rentRepository.findAll();
        List<Proprietaire> proprietaires = proprietaireRepository.findAll();

        model.addObject("userName", user.getName() + " " + user.getLastname());
        List<Topo> topo = topoService.findAllTopo();
        List<Spot> spot = spotService.findAllSpot();
        model.addObject("proprietaire", proprietaires);
        model.addObject("user", user);
        model.addObject("topo", topo);
        model.addObject("spot", spot);
        model.addObject("rent", rents);
        model.addObject("view", "borrowed");
       // model.setViewName("user/borrowed");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/rent"}, method = RequestMethod.GET)
    public ModelAndView rentTopo(@RequestParam("idtopo") int idtopo) {
        ModelAndView model = new ModelAndView();
        Rent rent = new Rent();
        List<Topo> topo = topoService.findById(idtopo);
        List<Spot> spot = spotService.findAllSpot();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.addObject("topo", topo);
        model.addObject("spot", spot);
        model.addObject("rent", rent);
        model.addObject("view", "rent");
        //model.setViewName("user/rent");
        model.setViewName("index");

        return model;
    }
//todo:add topo
    //todo:uptopo

@RequestMapping(value = {"/addspot"}, method = RequestMethod.POST)
public String addSpot(@RequestParam("idtopo") int idtopo,  Spot spot, Secteur secteur, Way way) {
    //Date creationDate, Date returnDate
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.findUserByEmail(auth.getName());

    return "redirect:/loggedhome";
}
    @RequestMapping(value = {"/addSecteur"}, method = RequestMethod.POST)
    public String addSecteur(@RequestParam("idspot") int idspot, Secteur secteur, Way way) {
        //Date creationDate, Date returnDate
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/updateVoie"}, method = RequestMethod.GET)
    public ModelAndView updateVoie(@RequestParam("idsecteur") int idsecteur,@RequestParam("idway") int idway) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Way way = wayService.findAllByIdWay(idway);
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.addObject("view", "updateVoie");
        model.addObject("way", way);
        model.setViewName("index");
        return model;
    }
    @RequestMapping(value = {"/updateVoie"}, method = RequestMethod.POST)
    public String  updatedVoie(Way way) {
        System.out.println("VOIE " + way.getIdsecteur() + way.getNomWay() + way.isEquipees() + way.getRelai() + way.getCotation()+way.getIdvoie());

        wayRepository.save(way);
        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/addVoie"}, method = RequestMethod.GET)
    public ModelAndView addVoie(@RequestParam("idsecteur") int idsecteur, Way way) {
        ModelAndView model = new ModelAndView();
        way.setIdsecteur(idsecteur);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addObject("view", "addVoie");
        model.addObject("way", way);
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.setViewName("index");
        return model;
    }
    @RequestMapping(value = {"/addVoie"}, method = RequestMethod.POST)
    public String addedVoie( Way way) {
        //Date creationDate, Date returnDate
        wayRepository.save(way);
        return "redirect:/loggedhome";
    }
    @RequestMapping(value = {"/rent"}, method = RequestMethod.POST)
    public String rentIt(@RequestParam("idtopo") int idtopo, Rent rent) {
        //Date creationDate, Date returnDate
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        rent.setIsloan(true);
        rent.setIdtopo(idtopo);
        rent.setIduser(user.getId());
        rent.setBorrow(false);
        rent.setSeen(true);
        topoService.rentTopo(rent);
        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/unrent"}, method = RequestMethod.GET)
    public String unrentIt(@RequestParam("idtopo") int idtopo, Rent rent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        rent = rentRepository.getOne(idtopo);
        if (rent.isIsloan() && rent.isBorrow()) {
            rent.setSeen(false);
            rent.setIsloan(false);
            rent.setBorrow(false);
        }
        if ((rent.isIsloan()) && (!rent.isBorrow())) {
            rent.setIsloan(false);
        }
        rentRepository.save(rent);

        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/deleCom"}, method = RequestMethod.GET)
    public String deleteCom(@RequestParam("idcommentaire") int idcommentaire) {
        Commentaire com = commentaireRepository.getOne(idcommentaire);
        com.setHidden(true);
        commentaireRepository.save(com);
        return "redirect:/loggedhome";
    }

    @RequestMapping(value = {"/deleTopo"}, method = RequestMethod.GET)
    public String deleteTopo(@RequestParam("idtopo") int idtopo) {
        Rent rent = rentRepository.getOne(idtopo);
        Topo topo = topoService.findOneById(idtopo);

        topo.setHidden(true);
        System.out.println("je suis la");
        if (rent.isBorrow() && rent.isIsloan()) {

            rent.setIsloan(false);

            rent.setSeen(false);

            rentRepository.save(rent);
            topoRepository.save(topo);

        }

        return "redirect:/loggedhome";
    }


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
        model.addObject("view", "home");
        //model.setViewName("home/home");
        model.setViewName("index");

        return model;
    }

    @RequestMapping(value = {"/publication/"}, method = RequestMethod.GET)
    public ModelAndView publication(@RequestParam("idtopo") int idtopo, @RequestParam("idspot") int idspot) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Topo> topos = topoService.findById(idtopo);
        List<Proprietaire> Proprietaire = proprietaireRepository.findAllPro();
        List<User> userL = userService.findAll();
        System.out.println(" voici l'id topo " + idspot + " id spot" + idspot);
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastname());
            model.addObject("userConnectedId", user.getId());
            model.addObject("UserId", user.getId());
        } else {
            model.addObject("userName", "0");
        }
        List<Rent> rent = rentRepository.findAll();
        String rentContain = "";
        if (rent.size() > 0) {
            model.addObject("rent", rent);
            for (Rent rents : rent) {
                rentContain += rents.getIdtopo();
            }
        } else {
            Rent dummy = new Rent();
            Date now = Date.valueOf(LocalDate.now());
            dummy.setCreationDate(now);
            dummy.setReturnDate(now);
            dummy.setIsloan(false);
            dummy.setIdtopo(0);
            dummy.setIduser(0);
            rent.add(dummy);
            model.addObject("rent", rent);
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
            model.addObject("rentContain", rentContain);
        }
        // List<User> users = userService.f
        List<Commentaire> comment = commentaireRepository.findAllByIdPublication(idPub);
        Publication pub = publicationService.findAllById(idPub);
        System.out.println(pub.getIdpublication());

        model.addObject("user", userL);
        model.addObject("view", "publication");
        model.addObject("proprietaire", Proprietaire);
        model.addObject("comment", comment);
        model.addObject("publication", pub);
        model.addObject("spot", spots);
        model.addObject("topo", topos);
        model.addObject("secteur", secteurs);
        model.addObject("way", ways);
        model.addObject("commentaire", commentaires);
        //model.setViewName("home/publication");
        model.setViewName("index");
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
       // model.setViewName("user/inscriptionTopo");
        model.addObject("view", "inscriptionTopo");
        model.setViewName("index");
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
        model.addObject("view", "home");
        model.addObject("topo", topos);
        model.addObject("lastSpotName", lastSpotName);
        model.addObject("lastSpotDescription", lastSpotDescription);

        //model.setViewName("home/home");
        model.setViewName("index");
        return model;
    }
}
