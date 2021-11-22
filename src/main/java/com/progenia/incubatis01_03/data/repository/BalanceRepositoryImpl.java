/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.Balance;
import com.progenia.incubatis01_03.data.pojo.BalanceSourceMajPojo;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
    We must end our implementation name with “Impl”.
    BalanceRepositoryImpl extends BalanceRepositoryCustom.
*/

public class BalanceRepositoryImpl implements BalanceRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<BalanceSourceMajPojo> getMajBalanceSourceData(String codeCentreIncubateur, Integer noExercice, String brouillard, LocalDate finPeriode) {
        List<BalanceSourceMajPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpSourceMajBalance(:CodeCentreIncubateur, :NoExercice, :Brouillard, :FinPeriode)}", "BalanceSourceMajPojo")           
                .setParameter("CodeCentreIncubateur", codeCentreIncubateur)
                .setParameter("NoExercice", noExercice)
                .setParameter("Brouillard", brouillard)
                .setParameter("FinPeriode", finPeriode)
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BalanceRepositoryImpl.getMajBalanceSourceData", e.toString());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Balance> getBalanceCompteDebiteurData(String codeCentreIncubateur, Integer noExercice) {
        List<Balance> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a managed entity
            results = this.entityManager.createNativeQuery("{call ReqSpBalanceCompteDebiteur(:CodeCentreIncubateur, :NoExercice)}", Balance.class)           
                    .setParameter("CodeCentreIncubateur", codeCentreIncubateur)
                    .setParameter("NoExercice", noExercice)
                    .getResultList();

            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BalanceRepositoryImpl.getBalanceCompteDebiteurData", e.toString());
            e.printStackTrace();
            return null;
        }
    }    

    @Override
    public List<Balance> getBalanceCompteCrediteurData(String codeCentreIncubateur, Integer noExercice) {
        List<Balance> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a managed entity
            results = this.entityManager.createNativeQuery("{call ReqSpBalanceCompteCrediteur(:CodeCentreIncubateur, :NoExercice)}", Balance.class)           
                    .setParameter("CodeCentreIncubateur", codeCentreIncubateur)
                    .setParameter("NoExercice", noExercice)
                    .getResultList();

            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BalanceRepositoryImpl.getBalanceCompteCrediteurData", e.toString());
            e.printStackTrace();
            return null;
        }
    }    
    
    @Override
    public List<Balance> getBalanceRazCompteExploitationData(String codeCentreIncubateur, Integer noExercice) {
        List<Balance> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a managed entity
            results = this.entityManager.createNativeQuery("{call ReqSpBalanceRazCompteExploitation(:CodeCentreIncubateur, :NoExercice)}", Balance.class)           
                    .setParameter("CodeCentreIncubateur", codeCentreIncubateur)
                    .setParameter("NoExercice", noExercice)
                    .getResultList();

            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("BalanceRepositoryImpl.getBalanceRazCompteExploitationData", e.toString());
            e.printStackTrace();
            return null;
        }
    }    
    
}
