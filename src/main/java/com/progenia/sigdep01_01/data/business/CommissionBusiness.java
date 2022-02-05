/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.business;

import com.progenia.sigdep01_01.data.entity.Commission;
import com.progenia.sigdep01_01.data.pojo.CommissionPojo;
import com.progenia.sigdep01_01.data.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CommissionBusiness {
    @Autowired
    private final CommissionRepository repository;

    public CommissionBusiness(CommissionRepository repository) {
        this.repository = repository;
    }

    public Optional<Commission> findById(String codeCommission) {
        return this.repository.findById(codeCommission);
    }

    public List<Commission> findAll() {
        return this.repository.findAll();
    }

    public Commission save(Commission commission) {
        return this.repository.save(commission);
    }
            
    public void delete(Commission commission) {
        this.repository.delete(commission);
    }
        
    public List<CommissionPojo> getReportData() {
        return this.repository.getReportData();
    }
}
