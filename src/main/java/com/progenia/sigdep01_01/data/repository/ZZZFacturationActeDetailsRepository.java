/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.repository;

import com.progenia.sigdep01_01.data.entity.ZZZFacturationActeDetails;
import com.progenia.sigdep01_01.data.entity.ZZZFacturationActeDetailsId;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface FacturationActeDetailsRepository extends JpaRepository<ZZZFacturationActeDetails, ZZZFacturationActeDetailsId> {
        public List<ZZZFacturationActeDetails> findByFacturationActeDetailsIdNoFacturation(Long noFacturation);
        public List<ZZZFacturationActeDetails> findByFacturationActeDetailsIdNoPrestation(Long noPrestation);
}
