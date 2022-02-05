/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.TrancheValeurDetails;
import com.progenia.sigdep01_01.data.entity.TrancheValeurDetailsId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.sigdep01_01.data.repository.TrancheValeurDetailsRepository;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TrancheValeurDetailsBusiness {
    @Autowired
    private final TrancheValeurDetailsRepository repository;

    public TrancheValeurDetailsBusiness(TrancheValeurDetailsRepository repository) {
        this.repository = repository;
    }

    public List<TrancheValeurDetails> getDetailsRelatedDataByCodeTranche(String codeTranche) {
        return this.repository.findByTrancheValeurDetailsIdCodeTrancheOrderByTrancheValeurDetailsId_ValeurTrancheSuperieureAsc(codeTranche);
    }
            
    public List<TrancheValeurDetails> getDetailsRelatedDataByValeurTrancheSuperieure(Long valeurTrancheSuperieure) {
        return this.repository.findByTrancheValeurDetailsIdValeurTrancheSuperieure(valeurTrancheSuperieure);
    }

    public List<TrancheValeurDetails> getTrancheApplicableDataByCodeTrancheAndValeurTrancheSuperieure(String codeTranche, Long valeurTrancheSuperieure) {
        return this.repository.findTop1ByTrancheValeurDetailsId_CodeTrancheAndTrancheValeurDetailsId_ValeurTrancheSuperieureIsGreaterThanEqualOrderByTrancheValeurDetailsId_ValeurTrancheSuperieureAsc(codeTranche, valeurTrancheSuperieure);
    }

    public List<TrancheValeurDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<TrancheValeurDetails> findById(TrancheValeurDetailsId trancheValeurDetailsId) {
        return this.repository.findById(trancheValeurDetailsId);
    }

    public TrancheValeurDetails save(TrancheValeurDetails trancheValeurDetails) {
        return this.repository.save(trancheValeurDetails);
    }
            
    public void delete(TrancheValeurDetails trancheValeurDetails) {
        this.repository.delete(trancheValeurDetails);
    }
}
