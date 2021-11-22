/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.data.business;

import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.data.pojo.UtilisateurPojo;
import com.progenia.incubatis01_03.securities.data.repository.UtilisateurRepository;
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
public class UtilisateurBusiness {
    @Autowired
    private final UtilisateurRepository repository;

    public UtilisateurBusiness(UtilisateurRepository repository) {
        this.repository = repository;
    }

    public Optional<Utilisateur> findById(String codeUtilisateur) {
        return this.repository.findById(codeUtilisateur);
    }

    public List<Utilisateur> findByLogin(String login) {
        return this.repository.findByLogin(login);
    }
            
    public Utilisateur save(Utilisateur utilisateur) {
        return this.repository.save(utilisateur);
    }
            
    public void delete(Utilisateur utilisateur) {
        this.repository.delete(utilisateur);
    }
            
    public List<Utilisateur> findAll() {
        return this.repository.findAll();
    }

    public List<Utilisateur> findAll(int offset, int limit) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<Utilisateur> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    public Integer count() {
        return Math.toIntExact(this.repository.count());
    }

    public List<Utilisateur> fetch(int offset, int limit, String filterText) {
        int page = offset / limit;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.unsorted());
        List<Utilisateur> items = this.repository.findAll(pageRequest).getContent();
        return items.subList(offset%limit, items.size());
    }

    public Integer count(String filterText) {
        return Math.toIntExact(this.repository.count());
    }

    public List<UtilisateurPojo> getReportData() {
        return this.repository.getReportData();
   } //public List<Utilisateur> getReportData()

}
