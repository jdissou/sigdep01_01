/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.JournalPojo;
import com.progenia.incubatis01_03.data.entity.Journal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface JournalRepository extends JpaRepository<Journal, String>, JournalRepositoryCustom {
        public List<Journal> findByCompteIsNotNull();        
	public List<JournalPojo> getReportData();        
}


