/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.data.pojo;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


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

public class PreferenceUtilisateurPojo {
    //The returned column types have to match the constructor  parameter types
    private Boolean impressionAutomatique;
    private Boolean rafraichissementAutomatique;
    private Boolean impressionAutomatiqueOperationCaisse;
    private Boolean selectionEnCours;



    /*
    	[CodeParametre] [nvarchar](2) NOT NULL,
	[CodeUtilisateur] [nvarchar](10) NOT NULL,
	[ImpressionAutomatique] [bit] NOT NULL,
	[RafraichissementAutomatique] [bit] NOT NULL,
	[ImpressionAutomatiqueOperationCaisse] [bit] NOT NULL,
	[SelectionEnCours] [bit] NOT NULL,
	[NoExercice] [smallint] NULL,
	[DateDebutPlage] [datetime] NULL,
	[DateFinPlage] [datetime] NULL,

    */
}
