/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.XXXServiceFourniTarification;
import com.progenia.sigdep01_01.data.entity.ServiceFourniTarificationId;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ServiceFourniTarificationRepository extends JpaRepository<XXXServiceFourniTarification, ServiceFourniTarificationId> {
        public List<XXXServiceFourniTarification> findByServiceFourniTarificationIdCodeServiceOrderByServiceFourniTarificationId_NoRubriqueAsc(String codeService);
        public List<XXXServiceFourniTarification> findByServiceFourniTarificationIdNoRubriqueOrderByServiceFourniTarificationId_CodeServiceAsc(String noRubrique);
}


