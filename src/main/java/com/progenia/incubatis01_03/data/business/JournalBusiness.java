/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.JournalPojo;
import com.progenia.incubatis01_03.data.entity.Journal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.JournalRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class JournalBusiness {
    @Autowired
    private final JournalRepository repository;

    public JournalBusiness(JournalRepository repository) {
        this.repository = repository;
    }

    public Optional<Journal> findById(String codeJournal) {
        return this.repository.findById(codeJournal);
    }

    public List<Journal> findAll() {
        return this.repository.findAll();
    }

    public List<Journal> findByCompteIsNotNull() {
        return this.repository.findByCompteIsNotNull();
    }

    public Journal save(Journal journal) {
        return this.repository.save(journal);
    }
            
    public void delete(Journal journal) {
        this.repository.delete(journal);
    }
        
        public List<JournalPojo> getReportData() {
        return this.repository.getReportData();
    }
}
