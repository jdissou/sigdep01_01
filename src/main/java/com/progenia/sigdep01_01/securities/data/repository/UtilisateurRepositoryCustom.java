/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.data.repository;

import com.progenia.sigdep01_01.securities.data.pojo.UtilisateurPojo;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

/*
   The key point is the custom implementation must end with “Custom”
   — unless overridden in Spring Data configuration
*/
public interface UtilisateurRepositoryCustom {
    List<UtilisateurPojo> getReportData();
}
