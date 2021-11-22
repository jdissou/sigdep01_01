/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
* Custom annotation with the goal of Security Check on a View.
*/

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RequiresSecurityCheck {
    
}
