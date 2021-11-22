/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.utilities;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author DISSOU Jamâl-Dine
 */
public class ReportInput {
   //Classe d’entrée de rapport contenant toutes les données nécessaires au rapport
   //Paramètres de rapport
   private String titreRapport;
   private String denomination;
   private String nomVersion;
   private String initiales;

   private String paramPeriode;
   private String paramSerie;
   private String paramSerie1;
   private String paramSerie2;
   
   //DataSet de rapport
   private JRBeanCollectionDataSource javaBeanDataSource;

    public String getTitreRapport() {
        return titreRapport;
    }

    public void setTitreRapport(String titreRapport) {
        this.titreRapport = titreRapport;
    }
   
   public String getDenomination() {
       return denomination;
   }
 
   public void setDenomination(String denomination) {
       this.denomination = denomination;
   }
   public String getNomVersion() {
       return nomVersion;
   }
 
   public void setNomVersion(String nomVersion) {
       this.nomVersion = nomVersion;
   }

   public String getInitiales() {
       return initiales;
   }
 
   public String getParamPeriode() {
       return paramPeriode;
   }
 
   public String getParamSerie() {
       return paramSerie;
   }
 
   public String getParamSerie1() {
       return paramSerie1;
   }
 
   public String getParamSerie2() {
       return paramSerie2;
   }
 
   public void setInitiales(String initiales) {
       this.initiales = initiales;
   }

   public void setParamPeriode(String paramPeriode) {
       this.paramPeriode = paramPeriode;
   }

   public void setParamSerie(String paramSerie) {
       this.paramSerie = paramSerie;
   }

   public void setParamSerie1(String paramSerie1) {
       this.paramSerie1 = paramSerie1;
   }

   public void setParamSerie2(String paramSerie2) {
       this.paramSerie2 = paramSerie2;
   }

   public JRBeanCollectionDataSource getJavaBeanDataSource() {
       return javaBeanDataSource;
   }
   
   public void setJavaBeanDataSource(JRBeanCollectionDataSource javaBeanDataSource) {
       this.javaBeanDataSource = javaBeanDataSource;
   } 
   
    public Map<String, Object> getParameters() {
        //Créer une Map qui contiendra les paramètres. 
        //Ces paramètres seront utilisés dans le rapport jasper (jrxml)
       Map<String,Object> parameters = new HashMap<>();
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_TITRE_RAPPORT(), getTitreRapport());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_DENOMINATION(), getDenomination());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_VERSION(), getNomVersion());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_INITIALES(), getInitiales());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_PERIODE(), getParamPeriode());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_SERIE(), getParamSerie());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_SERIE1(), getParamSerie1());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_SERIE2(), getParamSerie2());
       parameters.put(ApplicationConstanteHolder.getCLE_BEAN_COLLECTION(), getJavaBeanDataSource());

       return parameters;
   }
}
