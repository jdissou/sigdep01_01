/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.business;

import com.progenia.immaria01_01.data.entity.ServiceFourniTarification;
import com.progenia.immaria01_01.data.entity.ServiceFourniTarificationId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.data.repository.ServiceFourniTarificationRepository;
import java.util.Optional;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class ServiceFourniTarificationBusiness {
    @Autowired
    private final ServiceFourniTarificationRepository repository;

    public ServiceFourniTarificationBusiness(ServiceFourniTarificationRepository repository) {
        this.repository = repository;
    }

    public List<ServiceFourniTarification> getRelatedDataByCodeService(String codeService) {
        return this.repository.findByServiceFourniTarificationIdCodeServiceOrderByServiceFourniTarificationId_NoRubriqueAsc(codeService);
    }
            
    public List<ServiceFourniTarification> getRelatedDataByNoRubrique(String noRubrique) {
        return this.repository.findByServiceFourniTarificationIdNoRubriqueOrderByServiceFourniTarificationId_CodeServiceAsc(noRubrique);
    }
            
    public List<ServiceFourniTarification> findAll() {
        return this.repository.findAll();
    }

    public Optional<ServiceFourniTarification> findById(ServiceFourniTarificationId serviceFourniTarificationId) {
        return this.repository.findById(serviceFourniTarificationId);
    }

    public ServiceFourniTarification save(ServiceFourniTarification ServiceFourniTarification) {
        return this.repository.save(ServiceFourniTarification);
    }
            
    public void delete(ServiceFourniTarification ServiceFourniTarification) {
        this.repository.delete(ServiceFourniTarification);
    }
}
