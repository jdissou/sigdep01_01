/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.data.repository;

import com.progenia.immaria01_01.securities.data.pojo.AutorisationUtilisateurPojo;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class SystemeAutorisationRepositoryImpl implements SystemeAutorisationRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<AutorisationUtilisateurPojo> getAutorisationUtilisateur(String codeUtilisateur, String codeCommande) {
        List<AutorisationUtilisateurPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpAutorisationUtilisateur()}", "AutorisationUtilisateurPojo")           
               .setParameter("CodeUtilisateur", codeUtilisateur).setParameter("CodeCommande", codeCommande)           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SystemeAutorisationRepositoryImpl.getAutorisationUtilisateur", e.toString());
            e.printStackTrace();
            return null;
        }
    }    
}
