/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author DISSOU Jamâl-Dine
 */

@Embeddable
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
public class ParametreSystemeIndiceReferenceId implements Serializable {
    @NotNull
    @Column(name="CodeParametre")
    private String codeParametre;
    
    @NotNull
    @Column(name="CodeIndiceReference")
    private String codeIndiceReference;  
}
