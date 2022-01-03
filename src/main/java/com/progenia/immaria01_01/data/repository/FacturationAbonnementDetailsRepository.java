/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.FacturationAbonnementDetails;
import com.progenia.immaria01_01.data.entity.FacturationAbonnementDetailsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationAbonnementDetailsRepository extends JpaRepository<FacturationAbonnementDetails, FacturationAbonnementDetailsId> {
        public List<FacturationAbonnementDetails> findByFacturationAbonnementDetailsIdNoFacturation(Long noFacturation);
        public List<FacturationAbonnementDetails> findByFacturationAbonnementDetailsIdNoPorteur(String noPorteur);
        public List<FacturationAbonnementDetails> findByFacturationAbonnementDetailsIdCodeService(String codeService);
}
