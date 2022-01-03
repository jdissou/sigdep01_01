/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.IReglementPorteurDetailsSource;
import com.progenia.immaria01_01.data.entity.MouvementFacture;
import com.progenia.immaria01_01.data.entity.Porteur;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.MouvementFactureRepository;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementFactureBusiness {
    @Autowired
    private final MouvementFactureRepository repository;

    public MouvementFactureBusiness(MouvementFactureRepository repository) {
        this.repository = repository;
    }

    public Optional<MouvementFacture> findById(Long noMouvement) {
        return this.repository.findById(noMouvement);
    }

    public List<MouvementFacture> findAll() {
        return this.repository.findAll();
    }

    public List<MouvementFacture> getReglementPorteurDetailsSource(CentreIncubateur centreIncubateur, Porteur porteur, LocalDate dateEcheanceFacture) {
        return this.repository.findByCentreIncubateurAndPorteurAndDateMouvementLessThanEqual(centreIncubateur, porteur, dateEcheanceFacture);
    }

    /*
    public List<IReglementPorteurDetailsSource> getReglementPorteurDetailsSource(CentreIncubateur centreIncubateur, Porteur porteur, LocalDate dateEcheanceFacture) {
        return this.repository.getReglementPorteurDetailsSource(centreIncubateur, porteur, dateEcheanceFacture);
    }
    */

    public MouvementFacture save(MouvementFacture mouvementFacture) {
        return this.repository.save(mouvementFacture);
    }
            
    public void delete(MouvementFacture mouvementFacture) {
        this.repository.delete(mouvementFacture);
    }
}
