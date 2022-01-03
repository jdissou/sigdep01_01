/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.MouvementCompta;
import com.progenia.immaria01_01.data.entity.MouvementComptaDetails;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementComptaDetailsRepository extends JpaRepository<MouvementComptaDetails, Long> {
        public List<MouvementComptaDetails> findByMouvementCompta(MouvementCompta mouvementCompta);
        public List<MouvementComptaDetails> findByMouvementComptaNoMouvement(Long noMouvement);
        public List<MouvementComptaDetails> findByCompteNoCompte(String noCompte);
        public List<MouvementComptaDetails> findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidation(String codeCentreIncubateur, Integer noExercice, String codeValidation);
        public List<MouvementComptaDetails> findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_DateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, LocalDate startDate, LocalDate endDate);
        public List<MouvementComptaDetails> findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_Journal_CodeJournalLikeAndMouvementCompta_OperationComptable_CodeOperationLikeAndMouvementCompta_DateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, String codeJournal, String codeOperation, LocalDate startDate, LocalDate endDate);

        public List<MouvementComptaDetails> findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndMouvementCompta_OperationComptable_CodeOperationLikeAndMouvementCompta_DateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, String codeOperation, LocalDate startDate, LocalDate endDate);
        public List<MouvementComptaDetails> findByMouvementCompta_CentreIncubateur_CodeCentreIncubateurAndMouvementCompta_Exercice_NoExerciceAndMouvementCompta_Validation_CodeValidationAndCompte_NoCompteAndMouvementCompta_DateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, String noCompte, LocalDate startDate, LocalDate endDate);

        public List<MouvementComptaDetails> deleteByMouvementCompta_NoMouvement(Long noMouvement);
}


