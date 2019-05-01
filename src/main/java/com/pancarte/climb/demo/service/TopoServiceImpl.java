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
    private PublicationRepository publicationRepository;
    private SpotRepository spotRepository;
    private SecteurRepository secteurRepository;
    private WayRepository wayRepository;

    @Override
    public List<Topo> findAllTopo() {

        return topoRepository.findAllTopo();
    }

    //@Override
    public void savePublication(Publication publication, Topo topo, Spot spot, Secteur secteur, Way way, int IdUser) {
        publication.setIduser(IdUser);
        //INSERTION PUBLICATION ____________________________________
        Date now = java.sql.Date.valueOf(LocalDate.now());
        publication.setCreationdate(now);
        publication.setUpdatedate(now);
        publicationRepository.save(publication);
        //INSERTION TOPO ____________________________________
        topoRepository.save(topo);
        //INSERTION spot
        spot.setIdtopo(topoRepository.selectLastIdTopo());
        spot.setIdpublication(publicationRepository.selectLastIdPublication());

        spotRepository.save(spot);
        secteur.setIdspot(spotRepository.selectLastIdspot());
        secteur.setIdpublication(publicationRepository.selectLastIdPublication());
        secteurRepository.save(secteur);
        way.setIdsecteur(secteurRepository.selectLastIdSecteur());
        wayRepository.save(way);
        //publication
        //topo
        //spot
        //secteur
        //voie
        //propriétaire
        //rent
    }
}
