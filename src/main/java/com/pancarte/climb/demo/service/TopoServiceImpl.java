package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("topoService")
public class TopoServiceImpl implements TopoService {

//todo: page pret topo

    @Qualifier("topoRepository")
    @Autowired
    private TopoRepository topoRepository;
    @Qualifier("publicationRepository")
    @Autowired
    private PublicationRepository publicationRepository;
    @Qualifier("spotRepository")
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private SecteurRepository secteurRepository;
    @Qualifier("wayRepository")
    @Autowired
    private WayRepository wayRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Qualifier("commentaireRepository")
    @Autowired
    private CommentaireRepository commentaireRepository;

    @Qualifier("rentRepository")
    @Autowired
    private RentRepository rentRepository;

    @Override
    public void rentTopo(Rent rent) {
        rentRepository.save(rent);

    }
    @Override
    public List<Topo> findAllTopo() {

        return topoRepository.findAllTopo();
    }
    @Override
    public List<Proprietaire> findOwner(@Param("idtopo") int idtopo){
        return topoRepository.findOwner(idtopo);
    }
    @Override
    public List<Topo> findByLieu(@Param("nom") String nom){
        return topoRepository.findByLieu(nom);
    }
    @Override
    public List<Topo> findById(@Param("idtopo") int idtopo) {

        return topoRepository.findById(idtopo);
    }

    @Override
    public void saveCommentaire(Commentaire commentaire) {
        commentaireRepository.save(commentaire);
    }

    @Override
    public void savePublication(Publication publication, Topo topo, Spot spot, Secteur secteur, Way way, int IdUser, String imgSpot, String imgSecteur) {
        publication.setIduser(IdUser);
        //INSERTION PUBLICATION ____________________________________
        Date now = Date.valueOf(LocalDate.now());
        publication.setCreationdate(now);
        publication.setUpdatedate(now);
        System.out.println("PUBLICATION " + publication.getName() + " " + publication.getIduser() + " " + publication.getCreationdate() + " " + publication.getUpdatedate());
        publicationRepository.save(publication);
        System.out.println("topo " + topo.getLieuTopo());
        //INSERTION TOPO ____________________________________
        User userProprio = userRepository.findById(IdUser);
        topo.setUsers(new HashSet<User>(Arrays.asList(userProprio)));
        topoRepository.save(topo);
        //INSERTION spot
        spot.setIdtopo(topoRepository.selectLastIdTopo());
        spot.setIdpublication(publicationRepository.selectLastIdPublication());
        spot.setLienSpot(imgSpot);
        System.out.println("spot " + spot.getIdtopo() + spot.getIdpublication() + spot.getNomSpot() + spot.getDescription() + spot.getLienSpot());
        spotRepository.save(spot);

        secteur.setLien(imgSecteur);
        secteur.setIdspot(spotRepository.selectLastIdspot());
        secteur.setIdpublication(publicationRepository.selectLastIdPublication());
        System.out.println("SECTEUR " + secteur.getIdspot() + secteur.getIdpublication() + secteur.getNomSecteur() + secteur.getType() + secteur.getLien() + secteur.getHauteur());
        secteurRepository.save(secteur);

        way.setIdsecteur(secteurRepository.selectLastIdSecteur());
        System.out.println("VOIE " + way.getIdsecteur() + way.getNomWay() + way.isEquipees() + way.getRelai() + way.getCotation());

        wayRepository.save(way);

    }
}
