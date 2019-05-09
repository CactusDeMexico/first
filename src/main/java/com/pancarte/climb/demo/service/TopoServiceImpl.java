package com.pancarte.climb.demo.service;

import com.pancarte.climb.demo.model.*;
import com.pancarte.climb.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("topoService")
public class TopoServiceImpl implements TopoService {

    @Autowired
    private TopoRepository topoRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private SpotRepository spotRepository;
    @Autowired
    private SecteurRepository secteurRepository;
    @Autowired
    private WayRepository wayRepository;

    @Override
    public List<Topo> findAllTopo() {

        return topoRepository.findAllTopo();
    }

    @Override
    public void savePublication(Publication publication, Topo topo, Spot spot, Secteur secteur, Way way, int IdUser,String imgSpot,String imgSecteur) {
        publication.setIduser(IdUser);
        //INSERTION PUBLICATION ____________________________________
        Date now = java.sql.Date.valueOf(LocalDate.now());
        publication.setCreationdate(now);
        publication.setUpdatedate(now);
        System.out.println("PUBLICATION "+publication.getName()+" "+publication.getIduser()+" "+publication.getCreationdate()+" "+publication.getUpdatedate());
        publicationRepository.save(publication);
        System.out.println("topo "+topo.getLieuTopo());
        //INSERTION TOPO ____________________________________
        topoRepository.save(topo);
        //INSERTION spot
        spot.setIdtopo(topoRepository.selectLastIdTopo());
        spot.setIdpublication(publicationRepository.selectLastIdPublication());
        spot.setLienSpot(imgSpot);
        System.out.println("spot "+spot.getIdtopo()+spot.getIdpublication()+spot.getNomSpot()+spot.getDescription()+spot.getLienSpot());
        spotRepository.save(spot);

        secteur.setLien(imgSecteur);
        secteur.setIdspot(spotRepository.selectLastIdspot());
        secteur.setIdpublication(publicationRepository.selectLastIdPublication());
        System.out.println("SECTEUR "+secteur.getIdspot()+secteur.getIdpublication()+secteur.getNomSecteur()+secteur.getType()+secteur.getLien()+secteur.getHauteur());
        secteurRepository.save(secteur);

        way.setIdsecteur(secteurRepository.selectLastIdSecteur());
        System.out.println("VOIE "+way.getIdsecteur()+way.getNomWay()+way.isEquipees()+way.getRelai()+way.getCotation());
        wayRepository.save(way);
    }
}
