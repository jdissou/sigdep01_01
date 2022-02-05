/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.repository;

import com.progenia.sigdep01_01.securities.data.entity.UtilisateurCodeSecret;
import com.progenia.sigdep01_01.securities.data.entity.UtilisateurCodeSecretId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @codeSecret Jamâl-Dine DISSOU
 */

@Repository
public interface UtilisateurCodeSecretRepository extends JpaRepository<UtilisateurCodeSecret, UtilisateurCodeSecretId> {
        public List<UtilisateurCodeSecret> findByUtilisateurCodeSecretIdCodeUtilisateur(String codeUtilisateur);
        public List<UtilisateurCodeSecret> findByUtilisateurCodeSecretIdCodeSecret(String codeSecret);
}
