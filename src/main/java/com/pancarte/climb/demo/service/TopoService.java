package com.pancarte.climb.demo.service;
import com.pancarte.climb.demo.model.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopoService {
    public List<Topo> findAllTopo();
    public List<Topo> findById(@Param("idtopo") int idtopo);
    void saveCommentaire(Commentaire commentaire);

    public void savePublication(Publication publication, Topo topo, Spot spot, Secteur secteur, Way way, int IdUser, String imgSpot, String imgSecteur);

}
