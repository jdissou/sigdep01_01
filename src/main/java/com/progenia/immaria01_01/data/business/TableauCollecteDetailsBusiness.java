/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.TableauCollecte;
import com.progenia.immaria01_01.data.entity.TableauCollecteDetails;
import com.progenia.immaria01_01.data.entity.TableauCollecteDetailsId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.TableauCollecteDetailsRepository;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class TableauCollecteDetailsBusiness {
    @Autowired
    private final TableauCollecteDetailsRepository repository;

    public TableauCollecteDetailsBusiness(TableauCollecteDetailsRepository repository) {
        this.repository = repository;
    }

    public List<TableauCollecteDetails> getDetailsRelatedDataByCodeTableau(String codeTableau) {
        return this.repository.findByTableauCollecteDetailsIdCodeTableau(codeTableau);
    }
            
    public List<TableauCollecteDetails> getDetailsRelatedDataByCodeIndicateur(String codeIndicateur) {
        return this.repository.findByTableauCollecteDetailsIdCodeIndicateur(codeIndicateur);
    }

    public List<TableauCollecteDetails> getDetailsRelatedDataByTableauCollecteOrderByNoOrdre(TableauCollecte tableauCollecte) {
        return this.repository.findByTableauCollecteOrderByNoOrdre(tableauCollecte);
    }

    public List<TableauCollecteDetails> findAll() {
        return this.repository.findAll();
    }

    public Optional<TableauCollecteDetails> findById(TableauCollecteDetailsId tableauCollecteDetailsId) {
        return this.repository.findById(tableauCollecteDetailsId);
    }

    public TableauCollecteDetails save(TableauCollecteDetails tableauCollecteDetails) {
        return this.repository.save(tableauCollecteDetails);
    }
            
    public void delete(TableauCollecteDetails tableauCollecteDetails) {
        this.repository.delete(tableauCollecteDetails);
    }
}
