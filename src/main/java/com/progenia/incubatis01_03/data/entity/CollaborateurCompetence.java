/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import java.io.Serializable;
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
@Table(name="CollaborateurCompetence")
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
public class CollaborateurCompetence implements Serializable {
    @EmbeddedId
    private CollaborateurCompetenceId collaborateurCompetenceId;    

    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCollaborateur")
    @JoinColumn(name = "CodeCollaborateur")
    private Collaborateur collaborateur;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeCompetence")
    @JoinColumn(name = "CodeCompetence")
    private Competence competence;

    public CollaborateurCompetence(Collaborateur collaborateur, Competence competence) {
        this.collaborateurCompetenceId = new CollaborateurCompetenceId();
        this.collaborateurCompetenceId.setCodeCollaborateur(collaborateur.getCodeCollaborateur());
        this.collaborateurCompetenceId.setCodeCompetence(competence.getCodeCompetence());
        this.collaborateur = collaborateur;
        this.competence = competence;
    }

    public CollaborateurCompetence(Collaborateur collaborateur) {
        this.collaborateurCompetenceId = new CollaborateurCompetenceId();
        this.collaborateurCompetenceId.setCodeCollaborateur(collaborateur.getCodeCollaborateur());
        this.collaborateur = collaborateur;
    }

    public CollaborateurCompetence(Competence competence) {
        this.collaborateurCompetenceId = new CollaborateurCompetenceId();
        this.collaborateurCompetenceId.setCodeCompetence(competence.getCodeCompetence());
        this.competence = competence;
    }

    public String getCodeCollaborateur() {
        return this.getCollaborateur().getCodeCollaborateur();
        //return this.collaborateurCompetenceId.getCodeCollaborateur();
    }

    public String getCodeCompetence() {
        return this.getCompetence().getCodeCompetence();
        //return this.collaborateurCompetenceId.getCodeCompetence();
    }

    public void setCodeCollaborateur(String codeCollaborateur) {
        this.collaborateurCompetenceId.setCodeCollaborateur(codeCollaborateur);
    }

    public void setCodeCompetence(String codeCompetence) {
        this.collaborateurCompetenceId.setCodeCompetence(codeCompetence);
    }
}
