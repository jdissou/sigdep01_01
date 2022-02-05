/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.MouvementFinancierDetails;
import com.progenia.sigdep01_01.data.entity.MouvementFinancierDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementFinancierDetailsRepository extends JpaRepository<MouvementFinancierDetails, MouvementFinancierDetailsId> {
        public List<MouvementFinancierDetails> findByMouvementFinancierDetailsIdNoMouvement(Long noMouvement);
        public List<MouvementFinancierDetails> findByMouvementFinancierDetailsIdNoEcheance(Integer noEcheance);
        public List<MouvementFinancierDetails> findByMouvementFinancierDetailsIdCodeCommission(String codeCommission);
}
