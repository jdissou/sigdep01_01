/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.entity;


import com.progenia.sigdep01_01.data.entity.ZZZCentreIncubateur;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="Utilisateur")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @ToString @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class Utilisateur implements Serializable {
    @Id
    @Column(name="CodeUtilisateur")
    private String codeUtilisateur;
    
    @Column(name="Login")
    private String login;
    
    @Column(name="CodeSecret")
    private String codeSecret;
    
    @Column(name="LibelleUtilisateur")
    private String libelleUtilisateur;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCategorieUtilisateur", nullable=false)
    private CategorieUtilisateur categorieUtilisateur;

    @Column(name="InitialesUtilisateur")
    private String initialesUtilisateur;
    
    @Column(name="DateSaisieCodeSecret")
    private LocalDate dateSaisieCodeSecret;
    
    @Column(name="DateExpirationCodeSecret")
    private LocalDate dateExpirationCodeSecret;

    @Column(name="ImpressionAutomatique")
    private Boolean impressionAutomatique;

    @Column(name="RafraichissementAutomatique")
    private Boolean rafraichissementAutomatique;

    @Column(name="DateDebutPlage")
    private LocalDate dateDebutPlage;
    
    @Column(name="DateFinPlage")
    private LocalDate dateFinPlage;
    
    /* Spéciciques */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeCentreIncubateur")
    private ZZZCentreIncubateur centreIncubateur;
    
    @Column(name="SelectionEnCours")
    private boolean selectionEnCours;
    
    @Column(name="Inactif")
    private boolean isInactif;

    public Utilisateur() {
        //Initialisation des valeurs par défaut
        this.selectionEnCours = false;
        this.impressionAutomatique = false;
        this.rafraichissementAutomatique = false;
    }
}
