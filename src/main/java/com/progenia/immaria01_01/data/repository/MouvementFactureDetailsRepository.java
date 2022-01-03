/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.MouvementFactureDetails;
import com.progenia.immaria01_01.data.entity.MouvementFactureDetailsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MouvementFactureDetailsRepository extends JpaRepository<MouvementFactureDetails, MouvementFactureDetailsId> {
        public List<MouvementFactureDetails> findByMouvementFactureDetailsIdNoMouvement(Long noMouvement);
        public List<MouvementFactureDetails> findByMouvementFactureDetailsIdCodeService(String codeService);
}
