/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.data.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Entity
@Table(name="UtilisateurCodeSecret")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class UtilisateurCodeSecret implements Serializable {
    @EmbeddedId
    private UtilisateurCodeSecretId utilisateurCodeSecretId;    
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeUtilisateur")
    @JoinColumn(name = "CodeUtilisateur")
    Utilisateur utilisateur;
    
    @Column(name="DateSaisieCodeSecret")
    private LocalDate dateSaisieCodeSecret;
    
    @Column(name="DateExpirationCodeSecret")
    private LocalDate dateExpirationCodeSecret;
    
    public UtilisateurCodeSecret(Utilisateur utilisateur) {
        this.utilisateurCodeSecretId = new UtilisateurCodeSecretId();
        this.utilisateurCodeSecretId.setCodeUtilisateur(utilisateur.getCodeUtilisateur()); 
        this.utilisateur = utilisateur;
    }

    public UtilisateurCodeSecret(String codeSecret) {
        this.utilisateurCodeSecretId = new UtilisateurCodeSecretId();
        this.utilisateurCodeSecretId.setCodeUtilisateur(codeSecret);
    }

    public String getCodeUtilisateur() {
        return this.utilisateurCodeSecretId.getCodeUtilisateur();
    }

    public String getCodeSecret() {
        return this.utilisateurCodeSecretId.getCodeSecret();
    }

    public void setCodeUtilisateur(String codeUtilisateur) {
        this.utilisateurCodeSecretId.setCodeUtilisateur(codeUtilisateur);
    }

    public void setCodeSecret(String codeSecret) {
        this.utilisateurCodeSecretId.setCodeSecret(codeSecret);
    }
}
