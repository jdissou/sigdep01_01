/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ServiceFourniParametrage;
import com.progenia.immaria01_01.data.entity.ServiceFourniParametrageId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ServiceFourniParametrageRepository;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ServiceFourniParametrageBusiness {
    @Autowired
    private final ServiceFourniParametrageRepository repository;

    public ServiceFourniParametrageBusiness(ServiceFourniParametrageRepository repository) {
        this.repository = repository;
    }

    public List<ServiceFourniParametrage> getRelatedDataByCodeService(String codeService) {
        return this.repository.findByServiceFourniParametrageIdCodeService(codeService);
    }
            
    public List<ServiceFourniParametrage> getRelatedDataByCodeVariable(String codeVariable) {
        return this.repository.findByServiceFourniParametrageIdCodeVariable(codeVariable);
    }
            
    public List<ServiceFourniParametrage> findAll() {
        return this.repository.findAll();
    }

    public Optional<ServiceFourniParametrage> findById(ServiceFourniParametrageId serviceFourniParametrageId) {
        return this.repository.findById(serviceFourniParametrageId);
    }

    public ServiceFourniParametrage save(ServiceFourniParametrage ServiceFourniParametrage) {
        return this.repository.save(ServiceFourniParametrage);
    }
            
    public void delete(ServiceFourniParametrage ServiceFourniParametrage) {
        this.repository.delete(ServiceFourniParametrage);
    }
}
