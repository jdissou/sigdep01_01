/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.entity;

import java.io.Serializable;
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
@Table(name="ServiceTarification")
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
public class ServiceFourniTarification implements Serializable {
    @EmbeddedId
    private ServiceFourniTarificationId serviceFourniTarificationId;    
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codeService")
    @JoinColumn(name = "CodeService")
    private ServiceFourni serviceFourni;
    
    // Use @MapsId to specify the correspondent primary key defined in the @Embeddable composite primary key class
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noRubrique")
    @JoinColumn(name = "NoRubrique")
    private Rubrique rubrique;

    @Column(name="NoOrdre")
    private Integer noOrdre;

    public ServiceFourniTarification(ServiceFourni serviceFourni, Rubrique rubrique) {
        this.serviceFourniTarificationId = new ServiceFourniTarificationId();
        this.serviceFourniTarificationId.setCodeService(serviceFourni.getCodeService()); 
        this.serviceFourniTarificationId.setNoRubrique(rubrique.getNoRubrique()); 

        this.serviceFourni = serviceFourni;
        this.rubrique = rubrique;
    }

    public ServiceFourniTarification(ServiceFourni serviceFourni) {
        this.serviceFourniTarificationId = new ServiceFourniTarificationId();
        this.serviceFourniTarificationId.setCodeService(serviceFourni.getCodeService()); 
        this.serviceFourni = serviceFourni;
    }

    public ServiceFourniTarification(Rubrique rubrique) {
        this.serviceFourniTarificationId = new ServiceFourniTarificationId();
        this.serviceFourniTarificationId.setNoRubrique(rubrique.getNoRubrique()); 
        this.rubrique = rubrique;
    }

    public String getCodeService() {
        return this.serviceFourniTarificationId.getCodeService();
    }

    public String getNoRubrique() {
        return this.serviceFourniTarificationId.getNoRubrique();
    }

    public void setCodeService(String codeService) {
        this.serviceFourniTarificationId.setCodeService(codeService);
    }

    public void setNoRubrique(String noRubrique) {
        this.serviceFourniTarificationId.setNoRubrique(noRubrique);
    }
}
