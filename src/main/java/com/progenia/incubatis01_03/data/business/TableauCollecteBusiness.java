/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.TableauCollectePojo;
import com.progenia.incubatis01_03.data.entity.TableauCollecte;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.TableauCollecteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TableauCollecteBusiness {
    @Autowired
    private final TableauCollecteRepository repository;

    public TableauCollecteBusiness(TableauCollecteRepository repository) {
        this.repository = repository;
    }

    public Optional<TableauCollecte> findById(String codeTableauCollecte) {
        return this.repository.findById(codeTableauCollecte);
    }

    public List<TableauCollecte> findAll() {
        return this.repository.findAll();
    }

    public TableauCollecte save(TableauCollecte compteTresorerie) {
        return this.repository.save(compteTresorerie);
    }
            
    public void delete(TableauCollecte compteTresorerie) {
        this.repository.delete(compteTresorerie);
    }

    public List<TableauCollectePojo> getReportData() {
        return this.repository.getReportData();
    }    
}
