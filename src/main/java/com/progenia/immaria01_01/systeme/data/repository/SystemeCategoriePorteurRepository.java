/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.systeme.data.repository;

import com.progenia.immaria01_01.systeme.data.entity.SystemeCategoriePorteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface SystemeCategoriePorteurRepository extends JpaRepository<SystemeCategoriePorteur, String> {
    
}