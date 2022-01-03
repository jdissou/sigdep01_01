/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.Balance;
import com.progenia.immaria01_01.data.pojo.BalanceSourceMajPojo;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
   The key point is the custom implementation must end with “Custom”
   — unless overridden in Spring Data configuration
*/
public interface BalanceRepositoryCustom {
    List<BalanceSourceMajPojo> getMajBalanceSourceData(String codeCentreIncubateur, Integer noExercice, String brouillard, LocalDate finPeriode);
    List<Balance> getBalanceCompteDebiteurData(String codeCentreIncubateur, Integer noExercice);
    List<Balance> getBalanceCompteCrediteurData(String codeCentreIncubateur, Integer noExercice);
    List<Balance> getBalanceRazCompteExploitationData(String codeCentreIncubateur, Integer noExercice);
}
