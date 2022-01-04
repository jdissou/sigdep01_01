/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.data.repository;

import com.progenia.immaria01_01.data.pojo.ImmeublePojo;

import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


/*
   The key point is the custom implementation must end with “Custom”
   — unless overridden in Spring Data configuration
*/
public interface ImmeubleRepositoryCustom {
    List<ImmeublePojo> getReportData();
}
