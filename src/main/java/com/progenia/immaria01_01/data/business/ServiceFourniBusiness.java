/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.pojo.ServiceFourniPojo;
import com.progenia.immaria01_01.data.entity.ServiceFourni;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ServiceFourniRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ServiceFourniBusiness {
    @Autowired
    private final ServiceFourniRepository repository;

    public ServiceFourniBusiness(ServiceFourniRepository repository) {
        this.repository = repository;
    }

    public Optional<ServiceFourni> findById(String codeService) {
        return this.repository.findById(codeService);
    }

    public List<ServiceFourni> findAll() {
        return this.repository.findAll();
    }

    public ServiceFourni save(ServiceFourni serviceFourni) {
        return this.repository.save(serviceFourni);
    }
            
    public void delete(ServiceFourni serviceFourni) {
        this.repository.delete(serviceFourni);
    }
        
        public List<ServiceFourniPojo> getReportData() {
        return this.repository.getReportData();
    }
}
