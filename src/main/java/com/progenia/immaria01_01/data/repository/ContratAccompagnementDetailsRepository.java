/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.ContratAccompagnementDetails;
import com.progenia.immaria01_01.data.entity.ContratAccompagnementDetailsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface ContratAccompagnementDetailsRepository extends JpaRepository<ContratAccompagnementDetails, ContratAccompagnementDetailsId> {
        public List<ContratAccompagnementDetails> findByContratAccompagnementDetailsIdNoContrat(Long noContrat);
        public List<ContratAccompagnementDetails> findByContratAccompagnementDetailsIdCodeService(String codeService);
}
