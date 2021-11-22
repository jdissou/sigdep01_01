/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.ComptePojo;
import com.progenia.incubatis01_03.data.pojo.SoldeMouvementComptaPojo;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
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
    CompteRepositoryImpl extends CompteRepositoryCustom.
*/

public class CompteRepositoryImpl implements CompteRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<SoldeMouvementComptaPojo> getSoldeMouvementCompta(String codeCentreIncubateur, String noCompte, Integer noExercice) {
        List<SoldeMouvementComptaPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpSoldeMouvementCompta(:CodeCentreIncubateur, :NoCompte, :NoExercice)}", "SoldeMouvementComptaPojo")           
                .setParameter("CodeCentreIncubateur", codeCentreIncubateur)
                .setParameter("NoCompte", noCompte)
                .setParameter("NoExercice", noExercice)
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CompteRepositoryImpl.getSoldeMouvementCompta", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ComptePojo> getReportData() {
        List<ComptePojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpSoldeMouvementCompta()}", "ComptePojo")           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("CompteRepositoryImpl.getReportData", e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
