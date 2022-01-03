/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.data.repository;

import com.progenia.immaria01_01.securities.data.pojo.SystemeMenuPojo;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jamâl-Dine DISSOU
 */



public class SystemeCommandeRepositoryImpl implements SystemeCommandeRepositoryCustom {
    //Inject the EntityManager
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<SystemeMenuPojo> getMenuList() {
        List<SystemeMenuPojo> results = new ArrayList<>();
        
        try {
            //Solution 1 createStoredProcedureQuery - Option moins rapide
            //Solution 2 createNativeQuery - Alternative 1 : Paramètre nommée - Version abrégée - Map the result of a native query into a POJO with SQLResultSetMappings
            results = this.entityManager.createNativeQuery("{call ReqSpSystemeMenuCIF()}", "SystemeMenuPojo")           
               //.setParameter("Login", strLogin)           
               .getResultList();
            return results;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SystemeCommandeRepositoryImpl.getMenuList", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public List<SystemeMenuPojo> getMenuList() {
}
