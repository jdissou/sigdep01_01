/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.pojo.IndicateurSuiviPojo;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
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
    IndicateurSuiviRepositoryImpl extends IndicateurSuiviRepositoryCustom.
*/

public class IndicateurSuiviRepositoryImpl implements IndicateurSuiviRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<IndicateurSuiviPojo> getReportData() {
        List<IndicateurSuiviPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpEtatIndicateurSuivi()}", "IndicateurSuiviPojo")           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("IndicateurSuiviRepositoryImpl.getReportData", e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
