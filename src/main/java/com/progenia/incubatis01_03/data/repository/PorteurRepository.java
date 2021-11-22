/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.pojo.PorteurPojo;
import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.Cohorte;
import com.progenia.incubatis01_03.data.entity.Porteur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    
/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface PorteurRepository extends JpaRepository<Porteur, String>, PorteurRepositoryCustom {
    public List<PorteurPojo> getReportData();        

    public List<Porteur> findByCentreIncubateur(CentreIncubateur centreIncubateur);
    public List<Porteur> findByCentreIncubateurAndCohorte(CentreIncubateur centreIncubateur, Cohorte cohorte);
}


