/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.OperationComptable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.OperationComptableRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class OperationComptableBusiness {
    @Autowired
    private final OperationComptableRepository repository;

    public OperationComptableBusiness(OperationComptableRepository repository) {
        this.repository = repository;
    }

    public Optional<OperationComptable> findById(String codeOperation) {
        return this.repository.findById(codeOperation);
    }

    public List<OperationComptable> findAll() {
        return this.repository.findAll();
    }

    public OperationComptable save(OperationComptable operationComptable) {
        return this.repository.save(operationComptable);
    }
            
    public void delete(OperationComptable operationComptable) {
        this.repository.delete(operationComptable);
    }
    
    public List<OperationComptable> getReportData() {
        return this.repository.findAll();
    }  
}
