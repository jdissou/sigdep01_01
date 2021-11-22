/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.entity.ServiceFourni;
import com.progenia.incubatis01_03.data.pojo.VariableServicePojo;
import com.progenia.incubatis01_03.data.entity.VariableService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.VariableServiceRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class VariableServiceBusiness {
    @Autowired
    private final VariableServiceRepository repository;

    public VariableServiceBusiness(VariableServiceRepository repository) {
        this.repository = repository;
    }

    public Optional<VariableService> findById(String codeVariable) {
        return this.repository.findById(codeVariable);
    }

    public List<VariableService> findAll() {
        return this.repository.findAll();
    }

    public List<VariableService> findByCodeService(String codeService) {
        return this.repository.findByCodeService(codeService);
    }

    public VariableService save(VariableService variableService) {
        return this.repository.save(variableService);
    }
            
    public void delete(VariableService variableService) {
        this.repository.delete(variableService);
    }
        
        public List<VariableServicePojo> getReportData() {
        return this.repository.getReportData();
    }
}
