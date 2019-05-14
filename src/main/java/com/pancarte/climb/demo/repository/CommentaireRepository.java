package com.pancarte.climb.demo.repository;

import com.pancarte.climb.demo.model.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("commentaireRepository")
public interface CommentaireRepository extends JpaRepository<Commentaire,Integer> {
}
