/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author DISSOU Jamâl-Dine
 */
public class TransactionReportInput {
   //Classe d’entrée de rapport contenant toutes les données nécessaires au rapport
   //Paramètres de rapport
   private String titreRapport;
   private String denomination;
   private String nomVersion;
   private String initiales;

   private String extraStringValue01;
   private String extraStringValue02;
   
   private String beanStringValue01;
   private String beanStringValue02;
   private String beanStringValue03;
   private String beanStringValue04;
   private String beanStringValue05;
   private String beanStringValue06;
   private String beanStringValue07;
   private String beanStringValue08;
   private String beanStringValue09;
   private String beanStringValue10;

   private String beanStringValue11;
   private String beanStringValue12;
   private String beanStringValue13;
   private String beanStringValue14;
   private String beanStringValue15;
   private String beanStringValue16;
   private String beanStringValue17;
   private String beanStringValue18;
   private String beanStringValue19;
   private String beanStringValue20;

   private Integer beanIntegerValue01;
   private Integer beanIntegerValue02;
   private Integer beanIntegerValue03;
   private Integer beanIntegerValue04;
   private Integer beanIntegerValue05;
   private Integer beanIntegerValue06;
   private Integer beanIntegerValue07;
   private Integer beanIntegerValue08;
   private Integer beanIntegerValue09;
   private Integer beanIntegerValue10;

   private Long beanLongValue01;
   private Long beanLongValue02;
   private Long beanLongValue03;
   private Long beanLongValue04;
   private Long beanLongValue05;
   private Long beanLongValue06;
   private Long beanLongValue07;
   private Long beanLongValue08;
   private Long beanLongValue09;
   private Long beanLongValue10;

   private Double beanDoubleValue01;
   private Double beanDoubleValue02;
   private Double beanDoubleValue03;
   private Double beanDoubleValue04;
   private Double beanDoubleValue05;
   private Double beanDoubleValue06;
   private Double beanDoubleValue07;
   private Double beanDoubleValue08;
   private Double beanDoubleValue09;
   private Double beanDoubleValue10;

   private Float beanFloatValue01;
   private Float beanFloatValue02;
   private Float beanFloatValue03;
   private Float beanFloatValue04;
   private Float beanFloatValue05;
   private Float beanFloatValue06;
   private Float beanFloatValue07;
   private Float beanFloatValue08;
   private Float beanFloatValue09;
   private Float beanFloatValue10;

   
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
 
   public void setInitiales(String initiales) {
       this.initiales = initiales;
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
       parameters.put(ApplicationConstanteHolder.getCLE_BEAN_COLLECTION(), getJavaBeanDataSource());

       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_EXTRA_STRING01(), getExtraStringValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_EXTRA_STRING02(), getExtraStringValue02());
       
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING01(), getBeanStringValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING02(), getBeanStringValue02());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING03(), getBeanStringValue03());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING04(), getBeanStringValue04());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING05(), getBeanStringValue05());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING06(), getBeanStringValue06());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING07(), getBeanStringValue07());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING08(), getBeanStringValue08());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING09(), getBeanStringValue09());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING10(), getBeanStringValue10());

       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING11(), getBeanStringValue11());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING12(), getBeanStringValue12());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING13(), getBeanStringValue13());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING14(), getBeanStringValue14());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING15(), getBeanStringValue15());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING16(), getBeanStringValue16());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING17(), getBeanStringValue17());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING18(), getBeanStringValue18());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING19(), getBeanStringValue19());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_STRING20(), getBeanStringValue20());
       
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER01(), getBeanIntegerValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER02(), getBeanIntegerValue02());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER03(), getBeanIntegerValue03());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER04(), getBeanIntegerValue04());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER05(), getBeanIntegerValue05());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER06(), getBeanIntegerValue06());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER07(), getBeanIntegerValue07());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER08(), getBeanIntegerValue08());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER09(), getBeanIntegerValue09());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_INTEGER10(), getBeanIntegerValue10());

       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG01(), getBeanLongValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG02(), getBeanLongValue02());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG03(), getBeanLongValue03());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG04(), getBeanLongValue04());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG05(), getBeanLongValue05());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG06(), getBeanLongValue06());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG07(), getBeanLongValue07());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG08(), getBeanLongValue08());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG09(), getBeanLongValue09());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_LONG10(), getBeanLongValue10());

       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE01(), getBeanDoubleValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE02(), getBeanDoubleValue02());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE03(), getBeanDoubleValue03());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE04(), getBeanDoubleValue04());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE05(), getBeanDoubleValue05());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE06(), getBeanDoubleValue06());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE07(), getBeanDoubleValue07());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE08(), getBeanDoubleValue08());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE09(), getBeanDoubleValue09());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_DOUBLE10(), getBeanDoubleValue10());

       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT01(), getBeanFloatValue01());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT02(), getBeanFloatValue02());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT03(), getBeanFloatValue03());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT04(), getBeanFloatValue04());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT05(), getBeanFloatValue05());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT06(), getBeanFloatValue06());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT07(), getBeanFloatValue07());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT08(), getBeanFloatValue08());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT09(), getBeanFloatValue09());
       parameters.put(ApplicationConstanteHolder.getCLE_PARAM_BEAN_FLOAT10(), getBeanFloatValue10());
       
       return parameters;
   }

    public String getExtraStringValue01() {
        return extraStringValue01;
    }

    public void setExtraStringValue01(String extraStringValue01) {
        this.extraStringValue01 = extraStringValue01;
    }

    public String getExtraStringValue02() {
        return extraStringValue02;
    }

    public void setExtraStringValue02(String extraStringValue02) {
        this.extraStringValue02 = extraStringValue02;
    }

    
    public String getBeanStringValue01() {
        return beanStringValue01;
    }

    public void setBeanStringValue01(String beanStringValue01) {
        this.beanStringValue01 = beanStringValue01;
    }

    public String getBeanStringValue02() {
        return beanStringValue02;
    }

    public void setBeanStringValue02(String beanStringValue02) {
        this.beanStringValue02 = beanStringValue02;
    }

    public String getBeanStringValue03() {
        return beanStringValue03;
    }

    public void setBeanStringValue03(String beanStringValue03) {
        this.beanStringValue03 = beanStringValue03;
    }

    public String getBeanStringValue04() {
        return beanStringValue04;
    }

    public void setBeanStringValue04(String beanStringValue04) {
        this.beanStringValue04 = beanStringValue04;
    }

    public String getBeanStringValue05() {
        return beanStringValue05;
    }

    public void setBeanStringValue05(String beanStringValue05) {
        this.beanStringValue05 = beanStringValue05;
    }

    public String getBeanStringValue06() {
        return beanStringValue06;
    }

    public void setBeanStringValue06(String beanStringValue06) {
        this.beanStringValue06 = beanStringValue06;
    }

    public String getBeanStringValue07() {
        return beanStringValue07;
    }

    public void setBeanStringValue07(String beanStringValue07) {
        this.beanStringValue07 = beanStringValue07;
    }

    public String getBeanStringValue08() {
        return beanStringValue08;
    }

    public void setBeanStringValue08(String beanStringValue08) {
        this.beanStringValue08 = beanStringValue08;
    }

    public String getBeanStringValue09() {
        return beanStringValue09;
    }

    public void setBeanStringValue09(String beanStringValue09) {
        this.beanStringValue09 = beanStringValue09;
    }

    public String getBeanStringValue10() {
        return beanStringValue10;
    }

    public void setBeanStringValue10(String beanStringValue10) {
        this.beanStringValue10 = beanStringValue10;
    }

    public String getBeanStringValue11() {
        return beanStringValue11;
    }

    public void setBeanStringValue11(String beanStringValue11) {
        this.beanStringValue11 = beanStringValue11;
    }

    public String getBeanStringValue12() {
        return beanStringValue12;
    }

    public void setBeanStringValue12(String beanStringValue12) {
        this.beanStringValue12 = beanStringValue12;
    }

    public String getBeanStringValue13() {
        return beanStringValue13;
    }

    public void setBeanStringValue13(String beanStringValue13) {
        this.beanStringValue13 = beanStringValue13;
    }

    public String getBeanStringValue14() {
        return beanStringValue14;
    }

    public void setBeanStringValue14(String beanStringValue14) {
        this.beanStringValue14 = beanStringValue14;
    }

    public String getBeanStringValue15() {
        return beanStringValue15;
    }

    public void setBeanStringValue15(String beanStringValue15) {
        this.beanStringValue15 = beanStringValue15;
    }

    public String getBeanStringValue16() {
        return beanStringValue16;
    }

    public void setBeanStringValue16(String beanStringValue16) {
        this.beanStringValue16 = beanStringValue16;
    }

    public String getBeanStringValue17() {
        return beanStringValue17;
    }

    public void setBeanStringValue17(String beanStringValue17) {
        this.beanStringValue17 = beanStringValue17;
    }

    public String getBeanStringValue18() {
        return beanStringValue18;
    }

    public void setBeanStringValue18(String beanStringValue18) {
        this.beanStringValue18 = beanStringValue18;
    }

    public String getBeanStringValue19() {
        return beanStringValue19;
    }

    public void setBeanStringValue19(String beanStringValue19) {
        this.beanStringValue19 = beanStringValue19;
    }

    public String getBeanStringValue20() {
        return beanStringValue20;
    }

    public void setBeanStringValue20(String beanStringValue20) {
        this.beanStringValue20 = beanStringValue20;
    }

    public Integer getBeanIntegerValue01() {
        return beanIntegerValue01;
    }

    public void setBeanIntegerValue01(Integer beanIntegerValue01) {
        this.beanIntegerValue01 = beanIntegerValue01;
    }

    public Integer getBeanIntegerValue02() {
        return beanIntegerValue02;
    }

    public void setBeanIntegerValue02(Integer beanIntegerValue02) {
        this.beanIntegerValue02 = beanIntegerValue02;
    }

    public Integer getBeanIntegerValue03() {
        return beanIntegerValue03;
    }

    public void setBeanIntegerValue03(Integer beanIntegerValue03) {
        this.beanIntegerValue03 = beanIntegerValue03;
    }

    public Integer getBeanIntegerValue04() {
        return beanIntegerValue04;
    }

    public void setBeanIntegerValue04(Integer beanIntegerValue04) {
        this.beanIntegerValue04 = beanIntegerValue04;
    }

    public Integer getBeanIntegerValue05() {
        return beanIntegerValue05;
    }

    public void setBeanIntegerValue05(Integer beanIntegerValue05) {
        this.beanIntegerValue05 = beanIntegerValue05;
    }

    public Integer getBeanIntegerValue06() {
        return beanIntegerValue06;
    }

    public void setBeanIntegerValue06(Integer beanIntegerValue06) {
        this.beanIntegerValue06 = beanIntegerValue06;
    }

    public Integer getBeanIntegerValue07() {
        return beanIntegerValue07;
    }

    public void setBeanIntegerValue07(Integer beanIntegerValue07) {
        this.beanIntegerValue07 = beanIntegerValue07;
    }

    public Integer getBeanIntegerValue08() {
        return beanIntegerValue08;
    }

    public void setBeanIntegerValue08(Integer beanIntegerValue08) {
        this.beanIntegerValue08 = beanIntegerValue08;
    }

    public Integer getBeanIntegerValue09() {
        return beanIntegerValue09;
    }

    public void setBeanIntegerValue09(Integer beanIntegerValue09) {
        this.beanIntegerValue09 = beanIntegerValue09;
    }

    public Integer getBeanIntegerValue10() {
        return beanIntegerValue10;
    }

    public void setBeanIntegerValue10(Integer beanIntegerValue10) {
        this.beanIntegerValue10 = beanIntegerValue10;
    }

    public Long getBeanLongValue01() {
        return beanLongValue01;
    }

    public void setBeanLongValue01(Long beanLongValue01) {
        this.beanLongValue01 = beanLongValue01;
    }

    public Long getBeanLongValue02() {
        return beanLongValue02;
    }

    public void setBeanLongValue02(Long beanLongValue02) {
        this.beanLongValue02 = beanLongValue02;
    }

    public Long getBeanLongValue03() {
        return beanLongValue03;
    }

    public void setBeanLongValue03(Long beanLongValue03) {
        this.beanLongValue03 = beanLongValue03;
    }

    public Long getBeanLongValue04() {
        return beanLongValue04;
    }

    public void setBeanLongValue04(Long beanLongValue04) {
        this.beanLongValue04 = beanLongValue04;
    }

    public Long getBeanLongValue05() {
        return beanLongValue05;
    }

    public void setBeanLongValue05(Long beanLongValue05) {
        this.beanLongValue05 = beanLongValue05;
    }

    public Long getBeanLongValue06() {
        return beanLongValue06;
    }

    public void setBeanLongValue06(Long beanLongValue06) {
        this.beanLongValue06 = beanLongValue06;
    }

    public Long getBeanLongValue07() {
        return beanLongValue07;
    }

    public void setBeanLongValue07(Long beanLongValue07) {
        this.beanLongValue07 = beanLongValue07;
    }

    public Long getBeanLongValue08() {
        return beanLongValue08;
    }

    public void setBeanLongValue08(Long beanLongValue08) {
        this.beanLongValue08 = beanLongValue08;
    }

    public Long getBeanLongValue09() {
        return beanLongValue09;
    }

    public void setBeanLongValue09(Long beanLongValue09) {
        this.beanLongValue09 = beanLongValue09;
    }

    public Long getBeanLongValue10() {
        return beanLongValue10;
    }

    public void setBeanLongValue10(Long beanLongValue10) {
        this.beanLongValue10 = beanLongValue10;
    }

    public Double getBeanDoubleValue01() {
        return beanDoubleValue01;
    }

    public void setBeanDoubleValue01(Double beanDoubleValue01) {
        this.beanDoubleValue01 = beanDoubleValue01;
    }

    public Double getBeanDoubleValue02() {
        return beanDoubleValue02;
    }

    public void setBeanDoubleValue02(Double beanDoubleValue02) {
        this.beanDoubleValue02 = beanDoubleValue02;
    }

    public Double getBeanDoubleValue03() {
        return beanDoubleValue03;
    }

    public void setBeanDoubleValue03(Double beanDoubleValue03) {
        this.beanDoubleValue03 = beanDoubleValue03;
    }

    public Double getBeanDoubleValue04() {
        return beanDoubleValue04;
    }

    public void setBeanDoubleValue04(Double beanDoubleValue04) {
        this.beanDoubleValue04 = beanDoubleValue04;
    }

    public Double getBeanDoubleValue05() {
        return beanDoubleValue05;
    }

    public void setBeanDoubleValue05(Double beanDoubleValue05) {
        this.beanDoubleValue05 = beanDoubleValue05;
    }

    public Double getBeanDoubleValue06() {
        return beanDoubleValue06;
    }

    public void setBeanDoubleValue06(Double beanDoubleValue06) {
        this.beanDoubleValue06 = beanDoubleValue06;
    }

    public Double getBeanDoubleValue07() {
        return beanDoubleValue07;
    }

    public void setBeanDoubleValue07(Double beanDoubleValue07) {
        this.beanDoubleValue07 = beanDoubleValue07;
    }

    public Double getBeanDoubleValue08() {
        return beanDoubleValue08;
    }

    public void setBeanDoubleValue08(Double beanDoubleValue08) {
        this.beanDoubleValue08 = beanDoubleValue08;
    }

    public Double getBeanDoubleValue09() {
        return beanDoubleValue09;
    }

    public void setBeanDoubleValue09(Double beanDoubleValue09) {
        this.beanDoubleValue09 = beanDoubleValue09;
    }

    public Double getBeanDoubleValue10() {
        return beanDoubleValue10;
    }

    public void setBeanDoubleValue10(Double beanDoubleValue10) {
        this.beanDoubleValue10 = beanDoubleValue10;
    }

    public Float getBeanFloatValue01() {
        return beanFloatValue01;
    }

    public void setBeanFloatValue01(Float beanFloatValue01) {
        this.beanFloatValue01 = beanFloatValue01;
    }

    public Float getBeanFloatValue02() {
        return beanFloatValue02;
    }

    public void setBeanFloatValue02(Float beanFloatValue02) {
        this.beanFloatValue02 = beanFloatValue02;
    }

    public Float getBeanFloatValue03() {
        return beanFloatValue03;
    }

    public void setBeanFloatValue03(Float beanFloatValue03) {
        this.beanFloatValue03 = beanFloatValue03;
    }

    public Float getBeanFloatValue04() {
        return beanFloatValue04;
    }

    public void setBeanFloatValue04(Float beanFloatValue04) {
        this.beanFloatValue04 = beanFloatValue04;
    }

    public Float getBeanFloatValue05() {
        return beanFloatValue05;
    }

    public void setBeanFloatValue05(Float beanFloatValue05) {
        this.beanFloatValue05 = beanFloatValue05;
    }

    public Float getBeanFloatValue06() {
        return beanFloatValue06;
    }

    public void setBeanFloatValue06(Float beanFloatValue06) {
        this.beanFloatValue06 = beanFloatValue06;
    }

    public Float getBeanFloatValue07() {
        return beanFloatValue07;
    }

    public void setBeanFloatValue07(Float beanFloatValue07) {
        this.beanFloatValue07 = beanFloatValue07;
    }

    public Float getBeanFloatValue08() {
        return beanFloatValue08;
    }

    public void setBeanFloatValue08(Float beanFloatValue08) {
        this.beanFloatValue08 = beanFloatValue08;
    }

    public Float getBeanFloatValue09() {
        return beanFloatValue09;
    }

    public void setBeanFloatValue09(Float beanFloatValue09) {
        this.beanFloatValue09 = beanFloatValue09;
    }

    public Float getBeanFloatValue10() {
        return beanFloatValue10;
    }

    public void setBeanFloatValue10(Float beanFloatValue10) {
        this.beanFloatValue10 = beanFloatValue10;
    }
}
