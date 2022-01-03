/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author DISSOU Jamâl-Dine
 */

public class LocalDateHelper {
    //Factory Classe utilisée pour générer le rapport Jasper Report

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");

    public static String localDateToString(LocalDate localDate) {
        try 
        {
            return localDate.format(dateFormatter);
        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LocalDateHelper.localDateToString", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static String localDateToString(LocalDate localDate) {

    public static String localDateTimeToString(LocalDate localDate) {
        try 
        {
            return localDate.format(dateTimeFormatter);
        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LocalDateHelper.localDateTimeToString", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static String localDateTimeToString(LocalDate localDate) {

}