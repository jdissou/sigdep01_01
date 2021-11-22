/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.repository;

import com.progenia.incubatis01_03.data.entity.CentreIncubateur;
import com.progenia.incubatis01_03.data.entity.DomaineActivite;
import com.progenia.incubatis01_03.data.entity.MesureIndicateurDetails;
import com.progenia.incubatis01_03.data.entity.MesureIndicateurDetailsId;
import com.progenia.incubatis01_03.data.entity.TableauCollecte;
import com.progenia.incubatis01_03.data.entity.IndicateurSuivi;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MesureIndicateurDetailsRepository extends JpaRepository<MesureIndicateurDetails, MesureIndicateurDetailsId> {
        public List<MesureIndicateurDetails> findByMesureIndicateurDetailsIdNoMesure(Long noMesure);
        public List<MesureIndicateurDetails> findByMesureIndicateurDetailsIdCodeIndicateur(String codeIndicateur);
        
	public List<MesureIndicateurDetails> findByMesureIndicateur_IsSaisieValideeAndMesureIndicateur_CentreIncubateur_CodeCentreIncubateurAndMesureIndicateur_DomaineActivite_CodeDomaineActiviteAndMesureIndicateur_TableauCollecte_CodeTableauAndIndicateurSuivi_CodeIndicateurAndMesureIndicateur_DateMesureBetween(Boolean isSaisieValidee, String codeCentreIncubateur, String noDomaineActivite, String codeTableau, String codeIndicateur, LocalDate startDate, LocalDate endDate);
	public List<MesureIndicateurDetails> findByMesureIndicateur_IsSaisieValideeAndMesureIndicateur_CentreIncubateurAndMesureIndicateur_DomaineActiviteAndMesureIndicateur_TableauCollecteAndIndicateurSuiviAndMesureIndicateur_DateMesureBetween(Boolean isSaisieValidee, CentreIncubateur centreIncubateur, DomaineActivite domaineActivite, TableauCollecte tableauCollecte, IndicateurSuivi variableService, LocalDate startDate, LocalDate endDate);
}

