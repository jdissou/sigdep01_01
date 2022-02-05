/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="ZZZImmeuble")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class ZZZImmeuble implements Serializable {
    @Id
    @Column(name="CodeImmeuble")
    private String codeImmeuble;
    
    @Column(name="LibelleImmeuble")
    private String libelleImmeuble;
    
    @Column(name="LibelleCourtImmeuble")
    private String libelleCourtImmeuble;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeTypeImmeuble", nullable=false)
    private TypeImmeuble typeImmeuble;

    @Column(name="Superficie")
    private Double superficie;

    @Column(name="Adresse")
    private String adresse;

    @Column(name="Ville")
    private String ville;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeLocalisation")
    private ZZZLocalisation localisation;

    @Column(name="NoDossierTFU")
    private String noDossierTFU;

    @Column(name="NoPoliceAssurance")
    private String noPoliceAssurance;

    @Column(name="CompagnieAssurance")
    private String compagnieAssurance;

    @Column(name="DebutValiditeAssurance")
    private LocalDate debutValiditeAssurance;

    @Column(name="FinValiditeAssurance")
    private LocalDate finValiditeAssurance;

    @Column(name="NoContratAssurance")
    private Long noContratAssurance;

    @Column(name="CompteurExterne01")
    private Long compteurExterneExterne01;
    
    @Column(name="CompteurExterne02")
    private Long compteurExterneExterne02;
    
    @Column(name="CompteurExterne03")
    private Long compteurExterneExterne03;
    
    @Column(name="CompteurExterne04")
    private Long compteurExterneExterne04;
    
    @Column(name="CompteurExterne05")
    private Long compteurExterneExterne05;
    
    @Column(name="CompteurExterne06")
    private Long compteurExterneExterne06;
    
    @Column(name="CompteurExterne07")
    private Long compteurExterneExterne07;
    
    @Column(name="CompteurExterne08")
    private Long compteurExterneExterne08;
    
    @Column(name="CompteurExterne09")
    private Long compteurExterneExterne09;
    
    @Column(name="CompteurExterne10")
    private Long compteurExterneExterne10;

	@Column(name="Cede")
    private boolean isCede;

    @Column(name="DateCession")
    private LocalDate dateCession;

    @Column(name="Inactif")
    private boolean isInactif;
}
