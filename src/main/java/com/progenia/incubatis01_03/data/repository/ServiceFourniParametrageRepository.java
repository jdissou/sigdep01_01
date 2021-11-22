/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.ServiceFourniParametrage;
import com.progenia.incubatis01_03.data.entity.ServiceFourniParametrageId;
import com.progenia.incubatis01_03.data.entity.VariableService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ServiceFourniParametrageRepository extends JpaRepository<ServiceFourniParametrage, ServiceFourniParametrageId> {
        public List<ServiceFourniParametrage> findByServiceFourniParametrageIdCodeService(String codeService);
        public List<ServiceFourniParametrage> findByServiceFourniParametrageIdCodeVariable(String codeVariable);
}


