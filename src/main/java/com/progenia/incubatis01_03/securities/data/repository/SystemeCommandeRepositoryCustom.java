/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.data.repository;

import com.progenia.incubatis01_03.securities.data.pojo.SystemeMenuPojo;
import java.util.List;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public interface SystemeCommandeRepositoryCustom {
    List<SystemeMenuPojo> getMenuList();
}
