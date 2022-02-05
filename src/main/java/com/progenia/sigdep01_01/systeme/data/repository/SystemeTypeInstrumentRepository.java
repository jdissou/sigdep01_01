/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.systeme.data.repository;

import com.progenia.sigdep01_01.systeme.data.entity.SystemeTypeInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface SystemeTypeInstrumentRepository extends JpaRepository<SystemeTypeInstrument, String> {
    
}
