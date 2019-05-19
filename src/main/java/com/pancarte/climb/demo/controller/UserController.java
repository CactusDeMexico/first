package com.pancarte.climb.demo.controller;


import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.CommentaireRepository;
import com.pancarte.climb.demo.repository.RentRepository;
import com.pancarte.climb.demo.repository.SecteurRepository;
import com.pancarte.climb.demo.repository.WayRepository;
import com.pancarte.climb.demo.service.PublicationService;
import com.pancarte.climb.demo.service.SpotService;
import com.pancarte.climb.demo.service.TopoService;
import com.pancarte.climb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    private final TopoService topoService;
    private final SpotService spotService;

    private final PublicationService publicationService;
    private final SecteurRepository secteurRepository;
    private final WayRepository wayRepository;
    private final CommentaireRepository commentaireRepository;
    private final RentRepository rentRepository;

    @Autowired
    public UserController(@Qualifier("commentaireRepository") CommentaireRepository commentaireRepository, UserService userService, TopoService topoService, SpotService spotService, PublicationService publicationService, SecteurRepository secteurRepository, @Qualifier("wayRepository") WayRepository wayRepository, @Qualifier("rentRepository") RentRepository rentRepository) {
        this.commentaireRepository = commentaireRepository;
        this.userService = userService;
        this.topoService = topoService;
        this.spotService = spotService;
        this.publicationService = publicationService;
        this.secteurRepository = secteurRepository;
        this.wayRepository = wayRepository;
        this.rentRepository = rentRepository;
    }

    @RequestMapping(value= {"/topo"}, method=RequestMethod.GET)
    public  ModelAndView accueil() {
        ModelAndView model = new ModelAndView();
        model.setViewName("home/topo");
        return model;
    }

    @RequestMapping(value= {"/login"}, method=RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("user/signup");

        return model;
    }

    @RequestMapping(value= {"/signup"}, method=RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());

        if(userExists != null) {
            bindingResult.rejectValue("email", "error.user", "This email already exists!");
        }
        if(bindingResult.hasErrors()) {
            model.setViewName("user/signup");
        } else {
            userService.saveUser(user);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());

            model.setViewName("user/signup");
        }

        return model;
    }

    @RequestMapping(value= {"/loggedhome"}, method=RequestMethod.GET)
    public ModelAndView loggedHome() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<Rent> rent = rentRepository.findAll();
        System.out.println(rent.size()+"___ rennt size");
        if(rent.size()>0){
            model.addObject("rent",rent);
        }else{
            model.addObject("rent",0);
        }
        List<Publication> publication = publicationService.findByIdUser(user.getId());

        List<Topo> topos = topoService.findAllTopo();
        List<Secteur> secteurs = secteurRepository.findAll();
        List<Spot> spots = spotService.findAllSpot();
        List<Way> ways = wayRepository.findAll();

        Commentaire commentaires = new Commentaire();




        model.addObject("UserId",user.getId());

        model.addObject("publication",publication);
        model.addObject("spot",spots);
        model.addObject("topo",topos);
        model.addObject("secteur",secteurs);
        model.addObject("way",ways);
        model.addObject("commentaire",commentaires);
        model.addObject("userName", user.getName() + " " + user.getLastname());
        model.setViewName("home/loggedHome");
        return model;
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public String logout(  )
    {

        return "redirect:home";
    }

    @RequestMapping(value= {"/access_denied"}, method=RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/access_denied");
        return model;
    }
    @RequestMapping(value= {"/error"}, method=RequestMethod.GET)
    public ModelAndView error() {
        ModelAndView model = new ModelAndView();
        model.setViewName("errors/error");
        return model;
    }


}
