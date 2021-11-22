/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.systeme.data.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name="SystemeCategoriePorteur")
/*
By adding the @Getter and @Setter annotations we told Lombok to, 
well, generate these for all the fields of the class. 
@NoArgsConstructor will lead to an empty constructor generation.
@AllArgsConstructor will lead to an constructor generation.
@ToString: will generate a toString() method including all class attributes. 
@EqualsAndHashCode: will generate both equals() and hashCode() methods by default considering all relevant fields, 
and according to very well though semantics.
*/
@Getter @ToString @EqualsAndHashCode
public class SystemeCategoriePorteur  implements Serializable {
    @Id
    @Column(name="CodeCategoriePorteur")
    private String codeCategoriePorteur;
     
    @Column(name="LibelleCategoriePorteur")
    private String libelleCategoriePorteur;
     
    @Column(name="LibelleCourtCategoriePorteur")
    private String libelleCourtCategoriePorteur;
}
