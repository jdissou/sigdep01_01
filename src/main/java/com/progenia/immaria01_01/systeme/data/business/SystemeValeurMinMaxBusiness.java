/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.business;

import com.progenia.immaria01_01.systeme.data.entity.SystemeValeurMinMax;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.progenia.immaria01_01.systeme.data.repository.SystemeValeurMinMaxRepository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class SystemeValeurMinMaxBusiness {
    @Autowired
    private final SystemeValeurMinMaxRepository repository;

    public SystemeValeurMinMaxBusiness(SystemeValeurMinMaxRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeValeurMinMax> findById(String codeValeur) {
        return this.repository.findById(codeValeur);
    }

    public List<SystemeValeurMinMax> findAll() {
        return this.repository.findAll();
    }

    public SystemeValeurMinMax save(SystemeValeurMinMax typeValeurMinMax) {
        return this.repository.save(typeValeurMinMax);
    }
            
    public void delete(SystemeValeurMinMax typeValeurMinMax) {
        this.repository.delete(typeValeurMinMax);
    }
    
}
