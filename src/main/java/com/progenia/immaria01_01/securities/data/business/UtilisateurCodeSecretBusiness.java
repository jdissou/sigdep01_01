/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.securities.data.business;

import com.progenia.immaria01_01.securities.data.entity.UtilisateurCodeSecret;
import com.progenia.immaria01_01.securities.data.repository.UtilisateurCodeSecretRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Service
public class UtilisateurCodeSecretBusiness {
    @Autowired
    private final UtilisateurCodeSecretRepository repository;

    public UtilisateurCodeSecretBusiness(UtilisateurCodeSecretRepository repository) {
        this.repository = repository;
    }

    public List<UtilisateurCodeSecret> findAll() {
        return this.repository.findAll();
    }

        public List<UtilisateurCodeSecret> findByUtilisateurCodeSecretIdCodeUtilisateur(String codeUtilisateur) {
        return this.repository.findByUtilisateurCodeSecretIdCodeUtilisateur(codeUtilisateur);
    }
            
    public List<UtilisateurCodeSecret> findByUtilisateurCodeSecretIdCodeSecret(String codeSecret) {
        return this.repository.findByUtilisateurCodeSecretIdCodeSecret(codeSecret);
    }
    
    public UtilisateurCodeSecret save(UtilisateurCodeSecret utilisateurCodeSecret) {
        return this.repository.save(utilisateurCodeSecret);
    }
            
    public void delete(UtilisateurCodeSecret utilisateurCodeSecret) {
        this.repository.delete(utilisateurCodeSecret);
    }
            
}
