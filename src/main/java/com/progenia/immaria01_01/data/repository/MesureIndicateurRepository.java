/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.entity.CentreIncubateur;
import com.progenia.immaria01_01.data.entity.DomaineActivite;
import com.progenia.immaria01_01.data.entity.MesureIndicateur;
import com.progenia.immaria01_01.data.entity.TableauCollecte;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Repository
public interface MesureIndicateurRepository extends JpaRepository<MesureIndicateur, Long> {
        public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateur(Boolean isSaisieValidee, String codeCentreIncubateur);
	public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDateMesureBetween(Boolean isSaisieValidee, String codeCentreIncubateur, LocalDate startDate, LocalDate endDate);

        public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurAndDomaineActiviteAndDateMesureBetween(Boolean isSaisieValidee, CentreIncubateur centreIncubateur, DomaineActivite domaineActivite, LocalDate startDate, LocalDate endDate);
	public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDomaineActiviteCodeDomaineActiviteAndDateMesureBetween(Boolean isSaisieValidee, String codeCentreIncubateur, String codeTableau, LocalDate startDate, LocalDate endDate);

        public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurAndTableauCollecteAndDateMesureBetween(Boolean isSaisieValidee, CentreIncubateur centreIncubateur, TableauCollecte tableauCollecte, LocalDate startDate, LocalDate endDate);
	public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndTableauCollecteCodeTableauAndDateMesureBetween(Boolean isSaisieValidee, String codeCentreIncubateur, String codeTableau, LocalDate startDate, LocalDate endDate);

        public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurAndDomaineActiviteAndTableauCollecteAndDateMesureBetween(Boolean isSaisieValidee, CentreIncubateur centreIncubateur, DomaineActivite domaineActivite, TableauCollecte tableauCollecte, LocalDate startDate, LocalDate endDate);
	public List<MesureIndicateur> findByIsSaisieValideeAndCentreIncubateurCodeCentreIncubateurAndDomaineActiviteCodeDomaineActiviteAndTableauCollecteCodeTableauAndDateMesureBetween(Boolean isSaisieValidee, String codeCentreIncubateur, String codeDomaineActivite, String codeTableau, LocalDate startDate, LocalDate endDate);
}


    
    /*
	[NoMesure] [int] IDENTITY(1,1) NOT NULL,
	[NoExercice] [smallint] NULL,
	[CodeUtilisateur] [nvarchar](10) NULL,
	[CodeCentreIncubateur] [nvarchar](10) NULL,
	[NoChrono] [nvarchar](10) NULL,
	[CodeDomaineActivite] [nvarchar](10) NULL,
	[CodeTableau] [nvarchar](10) NULL,
	[DateFinPeriode] [datetime] NULL,
	[LibelleMesure] [nvarchar](100) NULL,
	[DateDebutPeriode] [datetime] NULL,
	[DateFinPeriode] [datetime] NULL,
	[Observations] [nvarchar](50) NULL,
	[SaisieValidee] [bit] NOT NULL,
    */
