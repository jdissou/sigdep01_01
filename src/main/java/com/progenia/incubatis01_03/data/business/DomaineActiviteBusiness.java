/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.business;

import com.progenia.incubatis01_03.data.pojo.SecteurActivitePojo;
import com.progenia.incubatis01_03.data.entity.DomaineActivite;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.incubatis01_03.data.repository.DomaineActiviteRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class DomaineActiviteBusiness {
    @Autowired
    private final DomaineActiviteRepository repository;

    public DomaineActiviteBusiness(DomaineActiviteRepository repository) {
        this.repository = repository;
    }

    public Optional<DomaineActivite> findById(String codeDomaineActivite) {
        return this.repository.findById(codeDomaineActivite);
    }

    public List<DomaineActivite> findAll() {
        return this.repository.findAll();
    }

    public List<DomaineActivite> findBySecteurActiviteCodeSecteurActivite(String codeSecteurActivite) {
        return this.repository.findBySecteurActiviteCodeSecteurActivite(codeSecteurActivite);
    }
            
    public DomaineActivite save(DomaineActivite domaineActivite) {
        return this.repository.save(domaineActivite);
    }
            
    public void delete(DomaineActivite domaineActivite) {
        this.repository.delete(domaineActivite);
    }
    
    public List<SecteurActivitePojo> getReportData() {
        return this.repository.getReportData();
    }

}
