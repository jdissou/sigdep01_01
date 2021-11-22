/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.MouvementCompta;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.MouvementComptaRepository;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementComptaBusiness {
    @Autowired
    private final MouvementComptaRepository repository;

    public MouvementComptaBusiness(MouvementComptaRepository repository) {
        this.repository = repository;
    }

    public Optional<MouvementCompta> findById(Long noMouvement) {
        return this.repository.findById(noMouvement);
    }

    public List<MouvementCompta> findAll() {
        return this.repository.findAll();
    }

    public List<MouvementCompta> findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(String codeValidation, String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(codeValidation, codeCentreIncubateur);
    }
            
    public List<MouvementCompta> findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(String codeCentreIncubateur, Integer noExercice, String codeValidation) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(codeCentreIncubateur, noExercice, codeValidation);
    }
            
    public MouvementCompta save(MouvementCompta mouvementCompta) {
        return this.repository.save(mouvementCompta);
    }
            
    public void delete(MouvementCompta mouvementCompta) {
        this.repository.delete(mouvementCompta);
    }
    
    public List<MouvementCompta> getBrouillardData(String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur("B", codeCentreIncubateur);
        //return this.repository.getBrouillardData(codeCentreIncubateur);
    } 
    
    public List<MouvementCompta> getExtraComptableData(String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur("E", codeCentreIncubateur);
    } 
    
    public List<MouvementCompta> getValidatedData(String codeCentreIncubateur) {
        return this.repository.findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur("J", codeCentreIncubateur);
        //return this.repository.getValidatedData(codeCentreIncubateur);
    } 

    public List<MouvementCompta> getValidationBrouillardSourceListe(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(codeCentreIncubateur, noExercice, "B");
    }
            
    public List<MouvementCompta> getValidationBrouillardSourceListe(String codeCentreIncubateur, Integer noExercice, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidationAndDateMouvementBetween(codeCentreIncubateur, noExercice, "B", debutPeriode, finPeriode);
    }
            
    public List<MouvementCompta> getContrepassationSourceListe(String codeCentreIncubateur, Integer noExercice, String codeJournal, String codeOperation, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidationAndJournal_CodeJournalLikeAndOperationComptable_CodeOperationLikeAndDateMouvementBetween(codeCentreIncubateur, noExercice, "J", codeJournal, codeOperation, debutPeriode, finPeriode);
    }
            
    public List<MouvementCompta> getCloturePeriodeSourceListe(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(codeCentreIncubateur, noExercice, "B");
    }

    public List<MouvementCompta> getClotureExerciceSourceListe(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(codeCentreIncubateur, noExercice, "B");
    }

    @Transactional
    public void deleteByNoMouvement(Long noNouvementCompta) {
        this.repository.deleteByNoMouvement(noNouvementCompta);
    }
    
}
