/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.Balance;
import com.progenia.immaria01_01.data.entity.BalanceId;
import com.progenia.immaria01_01.data.pojo.BalanceSourceMajPojo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface BalanceRepository extends JpaRepository<Balance, BalanceId>, BalanceRepositoryCustom {
        public List<Balance> findByBalanceIdCodeCentreIncubateurAndBalanceIdNoExercice(String codeCentreIncubateur, Integer noExercice);
        public List<Balance> findByBalanceIdCodeCentreIncubateur(String codeCentreIncubateur);
        public List<Balance> findByBalanceIdNoExercice(Integer noExercice);
        public List<Balance> findByBalanceIdNoCompte(String noCompte);

        public List<Balance> deleteByBalanceId_CodeCentreIncubateurAndBalanceId_NoExercice(String codeCentreIncubateur, Integer noExercice);

        public List<BalanceSourceMajPojo> getMajBalanceSourceData(String codeCentreIncubateur, Integer noExercice, String brouillard, LocalDate finPeriode);
}
