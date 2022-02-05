/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.utilities;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class ApplicationConstanteHolder {

    // SECURITY - LOGIN
    private static final String LOGIN_ATTRIBUTE = "LoginAttribute";
    private static final String CODE_UTILISATEUR_ATTRIBUTE = "CodeUtilisateurAttribute";
    private static final String USER_ATTRIBUTE = "UserAttribute";
    private static final String ADMIN_ATTRIBUTE = "AdminAttribute";
    
    // PARAMETRE SYSTEME
    private static final String VALEUR_CODE_PARAMETRE_SYSTEME = "PG";

    private static final String PARAMETRE_SYSTEME_DENOMINATION = "ParametreSystemeDenomination";
    private static final String PARAMETRE_SYSTEME_CODE_ADMINISTRATEUR = "ParametreSystemeCodeAdministrateur";
    private static final String PARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE = "ParametreSystemeCodeOperationComptable";
    private static final String PARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA = "ParametreSystemeCodeValidationCompta";
    private static final String PARAMETRE_SYSTEME_CODE_MODE_AFFICHAGE_IMAGE = "ParametreSystemeCodeModeAffichageImage";
    private static final String PARAMETRE_SYSTEME_CODE_PAYS = "ParametreSystemeCodePays";
    private static final String PARAMETRE_SYSTEME_PRELEVEMENT_ZAKAT = "ParametreSystemePrelevementZakat";
    private static final String PARAMETRE_SYSTEME_DATE_DEBUT_PAGE = "ParametreSystemeDateDebutPlage";
    private static final String PARAMETRE_SYSTEME_DATE_FIN_PAGE = "ParametreSystemeDateFinPlage";

    //CONSTANTES DIVERS
    static final String REPORT_RELATIVE_PATH = "./src/main/resources/reports/";
    static final String REPORT_RESOURCES_PATH = "/reports/";
    static final String REPORT_COMPONENT = "ReportComponent";
    static final String REPORT_STREAM_RESOURCE = "ReportStreamResource";
    
    //CONSTANTES CLES du dictionnaire (MAP) de passage de paramètres de formulaire Editer
    static final String VIEW_TITLE = "ViewTitle";
    static final String MODE_FORMULAIRE_EDITER = "ModeFormulaireEditer";
    static final String BEANS_TARGET_LIST = "BeansTargetSet";
    static final String BEANS_REFRENCE_LIST = "BeansArrayList";
    static final String CALLER_COMPONENT = "CallerComponent";
    static final String BEANS_KEY_VALUE1 = "BeansKeyValue1";
    static final String BEANS_KEY_VALUE2 = "BeansKeyValue2";
    static final String BEANS_KEY_VALUE3 = "BeansKeyValue3";
    
    //CONSTANTES CLES du dictionnaire (MAP) de passage de paramètres de Report
    private static  final String CLE_PARAM_TITRE_RAPPORT = "PARAM_TITRE_RAPPORT";
    private static  final String CLE_PARAM_DENOMINATION = "PARAM_DENOMINATION";
    private static  final String CLE_PARAM_VERSION = "PARAM_VERSION";
    private static  final String CLE_PARAM_INITIALES = "PARAM_INITIALES";
    private static  final String CLE_PARAM_PERIODE = "PARAM_PERIODE";
    private static  final String CLE_PARAM_SERIE = "PARAM_SERIE";
    private static  final String CLE_PARAM_SERIE1 = "PARAM_SERIE1";
    private static  final String CLE_PARAM_SERIE2 = "PARAM_SERIE2";
    private static  final String CLE_BEAN_COLLECTION = "BEAN_COLLECTION";

    //CONSTANTES CLES du dictionnaire (MAP) de passage de paramètres de Report TRANSACTION
    private static  final String CLE_PARAM_EXTRA_STRING01 = "PARAM_EXTRA_STRING01";
    private static  final String CLE_PARAM_EXTRA_STRING02 = "PARAM_EXTRA_STRING02";
    
    private static  final String CLE_PARAM_BEAN_STRING01 = "PARAM_BEAN_STRING01";
    private static  final String CLE_PARAM_BEAN_STRING02 = "PARAM_BEAN_STRING02";
    private static  final String CLE_PARAM_BEAN_STRING03 = "PARAM_BEAN_STRING03";
    private static  final String CLE_PARAM_BEAN_STRING04 = "PARAM_BEAN_STRING04";
    private static  final String CLE_PARAM_BEAN_STRING05 = "PARAM_BEAN_STRING05";
    private static  final String CLE_PARAM_BEAN_STRING06 = "PARAM_BEAN_STRING06";
    private static  final String CLE_PARAM_BEAN_STRING07 = "PARAM_BEAN_STRING07";
    private static  final String CLE_PARAM_BEAN_STRING08 = "PARAM_BEAN_STRING08";
    private static  final String CLE_PARAM_BEAN_STRING09 = "PARAM_BEAN_STRING09";
    private static  final String CLE_PARAM_BEAN_STRING10 = "PARAM_BEAN_STRING10";

    private static  final String CLE_PARAM_BEAN_STRING11 = "PARAM_BEAN_STRING11";
    private static  final String CLE_PARAM_BEAN_STRING12 = "PARAM_BEAN_STRING12";
    private static  final String CLE_PARAM_BEAN_STRING13 = "PARAM_BEAN_STRING13";
    private static  final String CLE_PARAM_BEAN_STRING14 = "PARAM_BEAN_STRING14";
    private static  final String CLE_PARAM_BEAN_STRING15 = "PARAM_BEAN_STRING15";
    private static  final String CLE_PARAM_BEAN_STRING16 = "PARAM_BEAN_STRING16";
    private static  final String CLE_PARAM_BEAN_STRING17 = "PARAM_BEAN_STRING17";
    private static  final String CLE_PARAM_BEAN_STRING18 = "PARAM_BEAN_STRING18";
    private static  final String CLE_PARAM_BEAN_STRING19 = "PARAM_BEAN_STRING19";
    private static  final String CLE_PARAM_BEAN_STRING20 = "PARAM_BEAN_STRING20";
    
    private static  final String CLE_PARAM_BEAN_INTEGER01 = "PARAM_BEAN_INTEGER01";
    private static  final String CLE_PARAM_BEAN_INTEGER02 = "PARAM_BEAN_INTEGER02";
    private static  final String CLE_PARAM_BEAN_INTEGER03 = "PARAM_BEAN_INTEGER03";
    private static  final String CLE_PARAM_BEAN_INTEGER04 = "PARAM_BEAN_INTEGER04";
    private static  final String CLE_PARAM_BEAN_INTEGER05 = "PARAM_BEAN_INTEGER05";
    private static  final String CLE_PARAM_BEAN_INTEGER06 = "PARAM_BEAN_INTEGER06";
    private static  final String CLE_PARAM_BEAN_INTEGER07 = "PARAM_BEAN_INTEGER07";
    private static  final String CLE_PARAM_BEAN_INTEGER08 = "PARAM_BEAN_INTEGER08";
    private static  final String CLE_PARAM_BEAN_INTEGER09 = "PARAM_BEAN_INTEGER09";
    private static  final String CLE_PARAM_BEAN_INTEGER10 = "PARAM_BEAN_INTEGER10";
    
    private static  final String CLE_PARAM_BEAN_LONG01 = "PARAM_BEAN_LONG01";
    private static  final String CLE_PARAM_BEAN_LONG02 = "PARAM_BEAN_LONG02";
    private static  final String CLE_PARAM_BEAN_LONG03 = "PARAM_BEAN_LONG03";
    private static  final String CLE_PARAM_BEAN_LONG04 = "PARAM_BEAN_LONG04";
    private static  final String CLE_PARAM_BEAN_LONG05 = "PARAM_BEAN_LONG05";
    private static  final String CLE_PARAM_BEAN_LONG06 = "PARAM_BEAN_LONG06";
    private static  final String CLE_PARAM_BEAN_LONG07 = "PARAM_BEAN_LONG07";
    private static  final String CLE_PARAM_BEAN_LONG08 = "PARAM_BEAN_LONG08";
    private static  final String CLE_PARAM_BEAN_LONG09 = "PARAM_BEAN_LONG09";
    private static  final String CLE_PARAM_BEAN_LONG10 = "PARAM_BEAN_LONG10";
    
    private static  final String CLE_PARAM_BEAN_DOUBLE01 = "PARAM_BEAN_DOUBLE01";
    private static  final String CLE_PARAM_BEAN_DOUBLE02 = "PARAM_BEAN_DOUBLE02";
    private static  final String CLE_PARAM_BEAN_DOUBLE03 = "PARAM_BEAN_DOUBLE03";
    private static  final String CLE_PARAM_BEAN_DOUBLE04 = "PARAM_BEAN_DOUBLE04";
    private static  final String CLE_PARAM_BEAN_DOUBLE05 = "PARAM_BEAN_DOUBLE05";
    private static  final String CLE_PARAM_BEAN_DOUBLE06 = "PARAM_BEAN_DOUBLE06";
    private static  final String CLE_PARAM_BEAN_DOUBLE07 = "PARAM_BEAN_DOUBLE07";
    private static  final String CLE_PARAM_BEAN_DOUBLE08 = "PARAM_BEAN_DOUBLE08";
    private static  final String CLE_PARAM_BEAN_DOUBLE09 = "PARAM_BEAN_DOUBLE09";
    private static  final String CLE_PARAM_BEAN_DOUBLE10 = "PARAM_BEAN_DOUBLE10";
    
    private static  final String CLE_PARAM_BEAN_FLOAT01 = "PARAM_BEAN_FLOAT01";
    private static  final String CLE_PARAM_BEAN_FLOAT02 = "PARAM_BEAN_FLOAT02";
    private static  final String CLE_PARAM_BEAN_FLOAT03 = "PARAM_BEAN_FLOAT03";
    private static  final String CLE_PARAM_BEAN_FLOAT04 = "PARAM_BEAN_FLOAT04";
    private static  final String CLE_PARAM_BEAN_FLOAT05 = "PARAM_BEAN_FLOAT05";
    private static  final String CLE_PARAM_BEAN_FLOAT06 = "PARAM_BEAN_FLOAT06";
    private static  final String CLE_PARAM_BEAN_FLOAT07 = "PARAM_BEAN_FLOAT07";
    private static  final String CLE_PARAM_BEAN_FLOAT08 = "PARAM_BEAN_FLOAT08";
    private static  final String CLE_PARAM_BEAN_FLOAT09 = "PARAM_BEAN_FLOAT09";
    private static  final String CLE_PARAM_BEAN_FLOAT10 = "PARAM_BEAN_FLOAT10";
    
    public static String getLOGIN_ATTRIBUTE() {
        return LOGIN_ATTRIBUTE;
    }

    public static String getUSER_ATTRIBUTE() {
        return USER_ATTRIBUTE;
    }
    
    public static String getADMIN_ATTRIBUTE() {
        return ADMIN_ATTRIBUTE;
    }
    
    public static String getREPORT_RELATIVE_PATH() {
        return REPORT_RELATIVE_PATH;
    }
    
    public static String getREPORT_RESOURCES_PATH() {
        return REPORT_RESOURCES_PATH;
    }
    
    public static String getVIEW_TITLE() {
        return VIEW_TITLE;
    }

    public static String getBEANS_REFRENCE_LIST() {
        return BEANS_REFRENCE_LIST;
    }

    public static String getBEANS_TARGET_LIST() {
        return BEANS_TARGET_LIST;
    }

    public static String getMODE_FORMULAIRE_EDITER() {
        return MODE_FORMULAIRE_EDITER;
    }

    public static String getCALLER_COMPONENT() {
        return CALLER_COMPONENT;
    }

    public static String getREPORT_COMPONENT() {
        return REPORT_COMPONENT;
    }

    public static String getCLE_PARAM_TITRE_RAPPORT() {
        return CLE_PARAM_TITRE_RAPPORT;
    }

    public static String getCLE_PARAM_DENOMINATION() {
        return CLE_PARAM_DENOMINATION;
    }

    public static String getCLE_PARAM_VERSION() {
        return CLE_PARAM_VERSION;
    }

    public static String getCLE_PARAM_INITIALES() {
        return CLE_PARAM_INITIALES;
    }

    public static String getCLE_PARAM_PERIODE() {
        return CLE_PARAM_PERIODE;
    }

    public static String getCLE_PARAM_SERIE() {
        return CLE_PARAM_SERIE;
    }

    public static String getCLE_PARAM_SERIE1() {
        return CLE_PARAM_SERIE1;
    }

    public static String getCLE_PARAM_SERIE2() {
        return CLE_PARAM_SERIE2;
    }

    public static String getVALEUR_CODE_PARAMETRE_SYSTEME() {
        return VALEUR_CODE_PARAMETRE_SYSTEME;
    }

    public static String getCLE_BEAN_COLLECTION() {
        return CLE_BEAN_COLLECTION;
    }

    public static String getREPORT_STREAM_RESOURCE() {
        return REPORT_STREAM_RESOURCE;
    }

    public static String getPARAMETRE_SYSTEME_DENOMINATION() {
        return PARAMETRE_SYSTEME_DENOMINATION;
    }

    public static String getPARAMETRE_SYSTEME_CODE_ADMINISTRATEUR() {
        return PARAMETRE_SYSTEME_CODE_ADMINISTRATEUR;
    }

    public static String getPARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE() {
        return PARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE;
    }

    public static String getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA() {
        return PARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA;
    }

    public static String getPARAMETRE_SYSTEME_CODE_MODE_AFFICHAGE_IMAGE() {
        return PARAMETRE_SYSTEME_CODE_MODE_AFFICHAGE_IMAGE;
    }

    public static String getPARAMETRE_SYSTEME_CODE_PAYS() {
        return PARAMETRE_SYSTEME_CODE_PAYS;
    }

    public static String getPARAMETRE_SYSTEME_PRELEVEMENT_ZAKAT() {
        return PARAMETRE_SYSTEME_PRELEVEMENT_ZAKAT;
    }

    public static String getPARAMETRE_SYSTEME_DATE_DEBUT_PAGE() {
        return PARAMETRE_SYSTEME_DATE_DEBUT_PAGE;
    }

    public static String getPARAMETRE_SYSTEME_DATE_FIN_PAGE() {
        return PARAMETRE_SYSTEME_DATE_FIN_PAGE;
    }

    public static String getCODE_UTILISATEUR_ATTRIBUTE() {
        return CODE_UTILISATEUR_ATTRIBUTE;
    }

    public static String getBEANS_KEY_VALUE1() {
        return BEANS_KEY_VALUE1;
    }

    public static String getBEANS_KEY_VALUE2() {
        return BEANS_KEY_VALUE2;
    }

    public static String getBEANS_KEY_VALUE3() {
        return BEANS_KEY_VALUE3;
    }

    
    public static String getCLE_PARAM_EXTRA_STRING01() {
        return CLE_PARAM_EXTRA_STRING01;
    }

    public static String getCLE_PARAM_EXTRA_STRING02() {
        return CLE_PARAM_EXTRA_STRING02;
    }


    public static String getCLE_PARAM_BEAN_STRING01() {
        return CLE_PARAM_BEAN_STRING01;
    }

    public static String getCLE_PARAM_BEAN_STRING02() {
        return CLE_PARAM_BEAN_STRING02;
    }

    public static String getCLE_PARAM_BEAN_STRING03() {
        return CLE_PARAM_BEAN_STRING03;
    }

    public static String getCLE_PARAM_BEAN_STRING04() {
        return CLE_PARAM_BEAN_STRING04;
    }

    public static String getCLE_PARAM_BEAN_STRING05() {
        return CLE_PARAM_BEAN_STRING05;
    }

    public static String getCLE_PARAM_BEAN_STRING06() {
        return CLE_PARAM_BEAN_STRING06;
    }

    public static String getCLE_PARAM_BEAN_STRING07() {
        return CLE_PARAM_BEAN_STRING07;
    }

    public static String getCLE_PARAM_BEAN_STRING08() {
        return CLE_PARAM_BEAN_STRING08;
    }

    public static String getCLE_PARAM_BEAN_STRING09() {
        return CLE_PARAM_BEAN_STRING09;
    }

    public static String getCLE_PARAM_BEAN_STRING10() {
        return CLE_PARAM_BEAN_STRING10;
    }



    public static String getCLE_PARAM_BEAN_STRING11() {
        return CLE_PARAM_BEAN_STRING11;
    }

    public static String getCLE_PARAM_BEAN_STRING12() {
        return CLE_PARAM_BEAN_STRING12;
    }

    public static String getCLE_PARAM_BEAN_STRING13() {
        return CLE_PARAM_BEAN_STRING13;
    }

    public static String getCLE_PARAM_BEAN_STRING14() {
        return CLE_PARAM_BEAN_STRING14;
    }

    public static String getCLE_PARAM_BEAN_STRING15() {
        return CLE_PARAM_BEAN_STRING15;
    }

    public static String getCLE_PARAM_BEAN_STRING16() {
        return CLE_PARAM_BEAN_STRING16;
    }

    public static String getCLE_PARAM_BEAN_STRING17() {
        return CLE_PARAM_BEAN_STRING17;
    }

    public static String getCLE_PARAM_BEAN_STRING18() {
        return CLE_PARAM_BEAN_STRING18;
    }

    public static String getCLE_PARAM_BEAN_STRING19() {
        return CLE_PARAM_BEAN_STRING19;
    }

    public static String getCLE_PARAM_BEAN_STRING20() {
        return CLE_PARAM_BEAN_STRING20;
    }
    
    public static String getCLE_PARAM_BEAN_INTEGER01() {
        return CLE_PARAM_BEAN_INTEGER01;
    }

    public static String getCLE_PARAM_BEAN_INTEGER02() {
        return CLE_PARAM_BEAN_INTEGER02;
    }

    public static String getCLE_PARAM_BEAN_INTEGER03() {
        return CLE_PARAM_BEAN_INTEGER03;
    }

    public static String getCLE_PARAM_BEAN_INTEGER04() {
        return CLE_PARAM_BEAN_INTEGER04;
    }

    public static String getCLE_PARAM_BEAN_INTEGER05() {
        return CLE_PARAM_BEAN_INTEGER05;
    }

    public static String getCLE_PARAM_BEAN_INTEGER06() {
        return CLE_PARAM_BEAN_INTEGER06;
    }

    public static String getCLE_PARAM_BEAN_INTEGER07() {
        return CLE_PARAM_BEAN_INTEGER07;
    }

    public static String getCLE_PARAM_BEAN_INTEGER08() {
        return CLE_PARAM_BEAN_INTEGER08;
    }

    public static String getCLE_PARAM_BEAN_INTEGER09() {
        return CLE_PARAM_BEAN_INTEGER09;
    }

    public static String getCLE_PARAM_BEAN_INTEGER10() {
        return CLE_PARAM_BEAN_INTEGER10;
    }
    
    public static String getCLE_PARAM_BEAN_LONG01() {
        return CLE_PARAM_BEAN_LONG01;
    }

    public static String getCLE_PARAM_BEAN_LONG02() {
        return CLE_PARAM_BEAN_LONG02;
    }

    public static String getCLE_PARAM_BEAN_LONG03() {
        return CLE_PARAM_BEAN_LONG03;
    }

    public static String getCLE_PARAM_BEAN_LONG04() {
        return CLE_PARAM_BEAN_LONG04;
    }

    public static String getCLE_PARAM_BEAN_LONG05() {
        return CLE_PARAM_BEAN_LONG05;
    }

    public static String getCLE_PARAM_BEAN_LONG06() {
        return CLE_PARAM_BEAN_LONG06;
    }

    public static String getCLE_PARAM_BEAN_LONG07() {
        return CLE_PARAM_BEAN_LONG07;
    }

    public static String getCLE_PARAM_BEAN_LONG08() {
        return CLE_PARAM_BEAN_LONG08;
    }

    public static String getCLE_PARAM_BEAN_LONG09() {
        return CLE_PARAM_BEAN_LONG09;
    }

    public static String getCLE_PARAM_BEAN_LONG10() {
        return CLE_PARAM_BEAN_LONG10;
    }
    
    public static String getCLE_PARAM_BEAN_DOUBLE01() {
        return CLE_PARAM_BEAN_DOUBLE01;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE02() {
        return CLE_PARAM_BEAN_DOUBLE02;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE03() {
        return CLE_PARAM_BEAN_DOUBLE03;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE04() {
        return CLE_PARAM_BEAN_DOUBLE04;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE05() {
        return CLE_PARAM_BEAN_DOUBLE05;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE06() {
        return CLE_PARAM_BEAN_DOUBLE06;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE07() {
        return CLE_PARAM_BEAN_DOUBLE07;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE08() {
        return CLE_PARAM_BEAN_DOUBLE08;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE09() {
        return CLE_PARAM_BEAN_DOUBLE09;
    }

    public static String getCLE_PARAM_BEAN_DOUBLE10() {
        return CLE_PARAM_BEAN_DOUBLE10;
    }
    
    public static String getCLE_PARAM_BEAN_FLOAT01() {
        return CLE_PARAM_BEAN_FLOAT01;
    }

    public static String getCLE_PARAM_BEAN_FLOAT02() {
        return CLE_PARAM_BEAN_FLOAT02;
    }

    public static String getCLE_PARAM_BEAN_FLOAT03() {
        return CLE_PARAM_BEAN_FLOAT03;
    }

    public static String getCLE_PARAM_BEAN_FLOAT04() {
        return CLE_PARAM_BEAN_FLOAT04;
    }

    public static String getCLE_PARAM_BEAN_FLOAT05() {
        return CLE_PARAM_BEAN_FLOAT05;
    }

    public static String getCLE_PARAM_BEAN_FLOAT06() {
        return CLE_PARAM_BEAN_FLOAT06;
    }

    public static String getCLE_PARAM_BEAN_FLOAT07() {
        return CLE_PARAM_BEAN_FLOAT07;
    }

    public static String getCLE_PARAM_BEAN_FLOAT08() {
        return CLE_PARAM_BEAN_FLOAT08;
    }

    public static String getCLE_PARAM_BEAN_FLOAT09() {
        return CLE_PARAM_BEAN_FLOAT09;
    }

    public static String getCLE_PARAM_BEAN_FLOAT10() {
        return CLE_PARAM_BEAN_FLOAT10;
    }
}
