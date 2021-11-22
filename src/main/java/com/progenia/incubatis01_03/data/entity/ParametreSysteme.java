/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;


import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.systeme.data.entity.SystemeValidation;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Entity
@Table(name="ParametreSysteme")
/* A la difference de ParametreSystemePojo, cette classe contient des pointeurs (@ManyToOne(fetch = FetchType.LAZY) @JoinColumn) vers les CIF  */

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
public class ParametreSysteme implements Serializable {
    @Id
    @Column(name="CodeParametre")
    private String codeParametre;
    
    @Column(name="Denomination")
    private String denomination;
    
    @Column(name="Dirigeant")
    private String dirigeant;
    
    @Column(name="Adresse")
    private String adresse;
    
    @Column(name="Ville")
    private String ville;
    
    @Column(name="Pays")
    private String pays;
    
    @Column(name="NoTelephone")
    private String noTelephone;
    
    @Column(name="NoTelecopie")
    private String noTelecopie;

    @Column(name="RC")
    private String rC;
    
    @Column(name="NoINSAE")
    private String noINSAE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeAdministrateur")
    private Utilisateur administrateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeOperationComptable")
    private OperationComptable operationComptable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CodeValidationCompta")
    private SystemeValidation validationCompta;

    @Column(name="DateDebutPlage")
    private LocalDate dateDebutPlage;
    
    @Column(name="DateFinPlage")
    private LocalDate dateFinPlage;
}
