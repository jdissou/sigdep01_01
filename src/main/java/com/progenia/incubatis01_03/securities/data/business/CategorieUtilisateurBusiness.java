/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.data.business;

import com.progenia.incubatis01_03.securities.data.entity.CategorieUtilisateur;
import com.progenia.incubatis01_03.securities.data.repository.CategorieUtilisateurRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class CategorieUtilisateurBusiness {
    @Autowired
    private final CategorieUtilisateurRepository repository;

    public CategorieUtilisateurBusiness(CategorieUtilisateurRepository repository) {
        this.repository = repository;
    }

    public Optional<CategorieUtilisateur> findById(String codeCategorieUtilisateur) {
        return this.repository.findById(codeCategorieUtilisateur);
    }

    public CategorieUtilisateur save(CategorieUtilisateur categorieUtilisateur) {
        return this.repository.save(categorieUtilisateur);
    }
            
    public void delete(CategorieUtilisateur categorieUtilisateur) {
        this.repository.delete(categorieUtilisateur);
    }
            
    public List<CategorieUtilisateur> findAll() {
        return this.repository.findAll();
    }

    public List<CategorieUtilisateur> findAll(int offset, int limit) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<CategorieUtilisateur> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    /*
    public List<CategorieUtilisateur> findAll(int offset, int limit, Map<String, Boolean> sortOrders) {
        int page = offset / limit;
        List<Sort.Order> orders = sortOrders.entrySet().stream()
                .map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
                .collect(Collectors.toList());

        Sort sort;
        if (orders.isEmpty())
            sort = Sort.unsorted();
        else
            sort = new Sort(orders);
        PageRequest pageRequest;
        pageRequest = PageRequest.of(page, limit, orders.isEmpty() ? Sort.unsorted() : new Sort(orders));
        List<CategorieUtilisateur> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }
    */

    public Integer count() {
        return Math.toIntExact(this.repository.count());
    }

    public List<CategorieUtilisateur> fetch(int offset, int limit, String filterText) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<CategorieUtilisateur> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    public Integer count(String filterText) {
        return Math.toIntExact(this.repository.count());
    }

    public List<CategorieUtilisateur> getReportData() {
        return this.repository.findAll();
   } //public List<CategorieUtilisateur> getReportData()
    
}
