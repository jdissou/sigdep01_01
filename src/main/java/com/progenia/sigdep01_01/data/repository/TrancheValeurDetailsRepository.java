/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.TrancheValeurDetails;
import com.progenia.sigdep01_01.data.entity.TrancheValeurDetailsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface TrancheValeurDetailsRepository extends JpaRepository<TrancheValeurDetails, TrancheValeurDetailsId> {
        public List<TrancheValeurDetails> findByTrancheValeurDetailsIdCodeTrancheOrderByTrancheValeurDetailsId_ValeurTrancheSuperieureAsc(String codeTranche);
        public List<TrancheValeurDetails> findByTrancheValeurDetailsIdValeurTrancheSuperieure(Long valeurTrancheSuperieure);
        public List<TrancheValeurDetails> findTop1ByTrancheValeurDetailsId_CodeTrancheAndTrancheValeurDetailsId_ValeurTrancheSuperieureIsGreaterThanEqualOrderByTrancheValeurDetailsId_ValeurTrancheSuperieureAsc(String codeTranche, Long valeurTrancheSuperieure);
}


