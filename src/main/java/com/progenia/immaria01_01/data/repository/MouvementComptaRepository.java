/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.MouvementCompta;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementComptaRepository extends JpaRepository<MouvementCompta, Long> {
        public List<MouvementCompta> findByValidation_CodeValidationAndCentreIncubateur_CodeCentreIncubateur(String codeValidation, String codeCentreIncubateur);
        public List<MouvementCompta> findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidation(String codeCentreIncubateur, Integer noExercice, String codeValidation);
        
        public List<MouvementCompta> findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidationAndDateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, LocalDate startDate, LocalDate endDate);
        //Base de Connaissances - public List<MouvementCompta> findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidationAndDateMouvementGreaterThanEqualAndDateMouvementLessThanEqual(String codeCentreIncubateur, Integer noExercice, String codeValidation, LocalDate startDate, LocalDate endDate);

        public List<MouvementCompta> findByCentreIncubateur_CodeCentreIncubateurAndExercice_NoExerciceAndValidation_CodeValidationAndJournal_CodeJournalLikeAndOperationComptable_CodeOperationLikeAndDateMouvementBetween(String codeCentreIncubateur, Integer noExercice, String codeValidation, String codeJournal, String codeOperation, LocalDate startDate, LocalDate endDate);

        public List<MouvementCompta> deleteByNoMouvement(Long noMouvement);
}


