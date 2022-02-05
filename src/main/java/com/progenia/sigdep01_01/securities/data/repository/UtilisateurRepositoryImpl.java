/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.repository;

import com.progenia.sigdep01_01.securities.data.pojo.UtilisateurPojo;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
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
    UtilisateurRepositoryImpl extends UtilisateurRepositoryCustom.
*/

public class UtilisateurRepositoryImpl implements UtilisateurRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<UtilisateurPojo> getReportData() {
        List<UtilisateurPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpEtatUtilisateur()}", "UtilisateurPojo")           
               //.setParameter("Login", strLogin)           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("UtilisateurRepositoryImpl.getReportData", e.toString());
            e.printStackTrace();
            return null;
        }
    }
    
    /*
    @Override
    public List<UtilisateurPojo> getReportData() {
        StoredProcedureQuery findByYearProcedure =
              em.createNamedStoredProcedureQuery("getAllUtilisateurs");
        return findByYearProcedure.getResultList();
    }
    */
}
