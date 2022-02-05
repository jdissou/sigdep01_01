/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.XXXServiceFourniParametrage;
import com.progenia.sigdep01_01.data.entity.ServiceFourniParametrageId;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ServiceFourniParametrageRepository extends JpaRepository<XXXServiceFourniParametrage, ServiceFourniParametrageId> {
        public List<XXXServiceFourniParametrage> findByServiceFourniParametrageIdCodeService(String codeService);
        public List<XXXServiceFourniParametrage> findByServiceFourniParametrageIdCodeVariable(String codeVariable);
}


