/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.business;

import com.progenia.sigdep01_01.securities.data.entity.SystemeCommande;
import com.progenia.sigdep01_01.securities.data.pojo.SystemeMenuPojo;
import com.progenia.sigdep01_01.securities.data.repository.SystemeCommandeRepository;
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
public class SystemeCommandeBusiness {
    @Autowired
    private final SystemeCommandeRepository repository;

    public SystemeCommandeBusiness(SystemeCommandeRepository repository) {
        this.repository = repository;
    }

    public Optional<SystemeCommande> findById(String codeSystemeCommande) {
        return this.repository.findById(codeSystemeCommande);
    }

    public SystemeCommande save(SystemeCommande categorieUtilisateur) {
        return this.repository.save(categorieUtilisateur);
    }
            
    public void delete(SystemeCommande categorieUtilisateur) {
        this.repository.delete(categorieUtilisateur);
    }
            
    public List<SystemeMenuPojo> getMenuList() {
        return this.repository.getMenuList();
    }

    public List<SystemeCommande> findAll() {
        return this.repository.findAll();
    }

    public List<SystemeCommande> findAll(int offset, int limit) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<SystemeCommande> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    /*
    public List<SystemeCommande> findAll(int offset, int limit, Map<String, Boolean> sortOrders) {
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
        List<SystemeCommande> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }
    */

    public Integer count() {
        return Math.toIntExact(this.repository.count());
    }

    public List<SystemeCommande> fetch(int offset, int limit, String filterText) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<SystemeCommande> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    public Integer count(String filterText) {
        return Math.toIntExact(this.repository.count());
    }

    public List<SystemeCommande> getReportData() {
        return this.repository.findAll();
   } //public List<SystemeCommande> getReportData()
    
}
