/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.MouvementCompta;
import com.progenia.immaria01_01.data.entity.MouvementComptaDetails;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.MouvementComptaDetailsRepository;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class MouvementComptaDetailsBusiness {
    @Autowired
    private final MouvementComptaDetailsRepository repository;

    public MouvementComptaDetailsBusiness(MouvementComptaDetailsRepository repository) {
        this.repository = repository;
    }

    public List<MouvementComptaDetails> getDetailsRelatedDataByMouvementCompta(MouvementCompta mouvementCompta) {
        return this.repository.findByMouvementCompta(mouvementCompta);
    }
            
    public List<MouvementComptaDetails> getDetailsRelatedDataByNoMouvement(Long noMouvement) {
        return this.repository.findByMouvementComptaNoMouvement(noMouvement);
    }
            
    public List<MouvementComptaDetails> getDetailsRelatedDataByNoCompte(String noCompte) {
        return this.repository.findByCompteNoCompte(noCompte);
    }
            
    public List<MouvementComptaDetails> findAll() {
        return this.repository.findAll();
    }

    public List<MouvementComptaDetails> getDetailsRelatedCodeCentreIncubateurAndNoExerciceAndCodeValidation(String codeCentreIncubateur, Integer noExercice, String codeValidation) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidation(codeCentreIncubateur, noExercice, codeValidation);    
    }
             
    public List<MouvementComptaDetails> getValidationBrouillardSourceDetails(String codeCentreIncubateur, Integer noExercice) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidation(codeCentreIncubateur, noExercice, "B");
    }

    public List<MouvementComptaDetails> getValidationBrouillardSourceDetails(String codeCentreIncubateur, Integer noExercice, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_DateMouvementBetween(codeCentreIncubateur, noExercice, "B", debutPeriode, finPeriode);
    }

    public List<MouvementComptaDetails> getContrepassationSourceDetails(String codeCentreIncubateur, Integer noExercice, String codeJournal, String codeOperation, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_Journal_CodeJournalLikeAndMouvementCompta_OperationComptable_CodeOperationLikeAndMouvementCompta_DateMouvementBetween(codeCentreIncubateur, noExercice, "J", codeJournal, codeOperation, debutPeriode, finPeriode);
    }

    public List<MouvementComptaDetails> getLettrageManuelSource(String codeCentreIncubateur, Integer noExercice, String codeOperation, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_OperationComptable_CodeOperationLikeAndMouvementCompta_DateMouvementBetween(codeCentreIncubateur, noExercice, "J", codeOperation, debutPeriode, finPeriode);
    }

    public List<MouvementComptaDetails> getRapprochementBancaireSource(String codeCentreIncubateur, Integer noExercice, String noCompte, LocalDate debutPeriode, LocalDate finPeriode) {
        return this.repository.findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndCompte_NoCompteAndMouvementCompta_DateMouvementBetween(codeCentreIncubateur, noExercice, "J", noCompte, debutPeriode, finPeriode);
    }

    public MouvementComptaDetails save(MouvementComptaDetails mouvementComptaDetails) {
        return this.repository.save(mouvementComptaDetails);
    }
            
    public void delete(MouvementComptaDetails mouvementComptaDetails) {
        this.repository.delete(mouvementComptaDetails);
    }

    @Transactional
    public void deleteByNoMouvement(Long noNouvementCompta) {
        this.repository.deleteByMouvementCompta_NoMouvement(noNouvementCompta);
    }
}
