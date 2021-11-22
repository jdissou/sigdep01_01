/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.TypePorteurPojo;
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
    TypePorteurRepositoryImpl extends TypePorteurRepositoryCustom.
*/

public class TypePorteurRepositoryImpl implements TypePorteurRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<TypePorteurPojo> getReportData() {
        List<TypePorteurPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpEtatTypePorteur()}", "TypePorteurPojo")           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("TypePorteurRepositoryImpl.getReportData", e.toString());
            e.printStackTrace();
            return null;
        }
    }
}