/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.entity;

import com.progenia.sigdep01_01.securities.data.pojo.AutorisationUtilisateurPojo;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
The POJO to map the SP result - 
Where to place @SqlResultSetMapping in case of @ConstructorResult  ?
It varies from a persistence provider:
 - EclipseLink: put it at any class in the classpath
 - Hibernate: put it at any class annotated with @Entity; in fact I am able to reproduce the error when I put it elsewhere:
To make the application portable across JPA providers it would be the best to put SqlResultSetMapping at any entity class.
On Hibernate, if the POJO isn't an entity, persistence annotations are not picked up as it isn't part of the persistence unit. 
Put your annotation on an entity. 
*/
@SqlResultSetMapping(name = "AutorisationUtilisateurPojo", 
        classes = @ConstructorResult(targetClass = AutorisationUtilisateurPojo.class, 
                columns = { 
                    @ColumnResult(name = "autorisation", type = Boolean.class), 
                    @ColumnResult(name = "modification", type = Boolean.class), 
                    @ColumnResult(name = "suppression", type = Boolean.class), 
                    @ColumnResult(name = "ajout", type = Boolean.class)
}))


@Entity
@Table(name="SystemeAutorisation")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class SystemeAutorisation implements Serializable {
    @EmbeddedId
    private SystemeAutorisationId autorisationId;    
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCategorieUtilisateur")
    @JoinColumn(name = "CodeCategorieUtilisateur")
    CategorieUtilisateur categorieUtilisateur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCommande")
    @JoinColumn(name = "CodeCommande")
    SystemeCommande commande;

    @Column(name="Autorisation")
    private Boolean autorisation;

    @Column(name="Modification")
    private Boolean modification;

    @Column(name="Suppression")
    private Boolean suppression;

    @Column(name="Ajout")
    private Boolean ajout;
    
    public SystemeAutorisation() {
        //Initialisation des valeurs par défaut
        this.autorisation = false;
        this.modification = false;
        this.suppression = false;
        this.ajout = false;
    }

    public SystemeAutorisation(CategorieUtilisateur categorieUtilisateur) {
        this.autorisationId = new SystemeAutorisationId();
        this.autorisationId.setCodeCategorieUtilisateur(categorieUtilisateur.getCodeCategorieUtilisateur()); 
        this.categorieUtilisateur = categorieUtilisateur;
        
        //Initialisation des valeurs par défaut
        this.autorisation = false;
        this.modification = false;
        this.suppression = false;
        this.ajout = false;
    }

    public SystemeAutorisation(SystemeCommande commande) {
        this.autorisationId = new SystemeAutorisationId();
        this.autorisationId.setCodeCommande(commande.getCodeCommande()); 
        this.commande = commande;
        
        //Initialisation des valeurs par défaut
        this.autorisation = false;
        this.modification = false;
        this.suppression = false;
        this.ajout = false;
    }

    public String getCodeCategorieUtilisateur() {
        return this.autorisationId.getCodeCategorieUtilisateur();
    }

    public String getCodeCommande() {
        return this.autorisationId.getCodeCommande();
    }

    public void setCodeCategorieUtilisateur(String codeCategorieUtilisateur) {
        this.autorisationId.setCodeCategorieUtilisateur(codeCategorieUtilisateur);
    }

    public void setCodeCommande(String codeCommande) {
        this.autorisationId.setCodeCommande(codeCommande);
    }
}
