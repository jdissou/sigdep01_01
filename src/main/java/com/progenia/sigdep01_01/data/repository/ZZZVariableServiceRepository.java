/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.pojo.VariableServicePojo;
import com.progenia.sigdep01_01.data.entity.VariableService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface VariableServiceRepository extends JpaRepository<VariableService, String>, VariableServiceRepositoryCustom {
    //Query method - Using @Query annotation - JPA query
    @Query("SELECT variableService from XXXServiceFourniParametrage serviceFourniParametrage JOIN serviceFourniParametrage.variableService variableService where serviceFourniParametrage.serviceFourniParametrageId.codeService = :codeService")
    List<VariableService> findByCodeService(String codeService);	

    public List<VariableServicePojo> getReportData();        
}


