/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.utilities;

import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServlet;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 *
 * @author DISSOU Jamâl-Dine
 */
public class ReportHelper {
    //Factory Classe utilisée pour générer le rapport Jasper Report

    public static JasperReport generateCompiledJasperReport(InputStream inputStream) {
        try 
        {
            //1 - create jasper design obbject - Créer un objet Jasper Design à l'aide du fichier Jrxml.
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            
            //JRXML files need to be compiled so the report engine can fill them with data. - Let's perform this operation with the help of the JasperCompilerManager class:
            //2 - Compiler jrxml avec l'aide de Jasper Compile Manager et obtenir l'objet Jasper Report
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            return (jasperReport);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.generateCompiledJasperReport", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static JasperReport generateCompiledJasperReport(InputStream inputStream) {

    public static StreamResource generateReportStreamResource(String titreRapport, String strInitialesUtilisateur, List javaBeanList, String reportName, JasperReport jasperReport) {
        try 
        {
            String denomination = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DENOMINATION());
            String nomVersion = MainView.getNomVersion();

            //1 - Assembler les paramètres du rapport
            ReportInput reportInput = dataAssemble(titreRapport, denomination, nomVersion, strInitialesUtilisateur, javaBeanList);
            
            //2 - Use JasperRunManager to fill a report and return byte array object containing the report in PDF format. The intermediate JasperPrint object is not saved on disk.
            byte[] byteArray = null;
            byteArray = JasperRunManager.runReportToPdf(jasperReport, reportInput.getParameters(), new JREmptyDataSource());

            //3 - 
            InputStream is = new ByteArrayInputStream(byteArray);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            //SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            StreamResource streamResource = new StreamResource(reportName + "-" + df.format(new Date())+".pdf", () -> is);
            //StreamResource streamResource = new StreamResource("Report-" + UUID.randomUUID().toString().replace("-", "") + ".pdf", () -> is);

            return (streamResource);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.generateReportStreamResource", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static StreamResource generateReportStreamResource(String titreRapport, String strInitialesUtilisateur, List javaBeanList, String reportName, JasperReport jasperReport) {

   
    public static StreamResource generateReportStreamResource(String titreRapport, String strInitialesUtilisateur, String strParamPeriode, String strParamSerie, String strParamSerie1, String strParamSerie2, List javaBeanList, String reportName, JasperReport jasperReport) {
        try 
        {
            String denomination = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DENOMINATION());
            String nomVersion = MainView.getNomVersion();

            //1 - Assembler les paramètres du rapport
            ReportInput reportInput = dataAssemble(titreRapport, denomination, nomVersion, strInitialesUtilisateur, strParamPeriode, strParamSerie, strParamSerie1, strParamSerie2, javaBeanList);
            
            //2 - Use JasperRunManager to fill a report and return byte array object containing the report in PDF format. The intermediate JasperPrint object is not saved on disk.
            byte[] byteArray = null;
            byteArray = JasperRunManager.runReportToPdf(jasperReport, reportInput.getParameters(), new JREmptyDataSource());

            //3 - 
            InputStream is = new ByteArrayInputStream(byteArray);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            //SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            StreamResource streamResource = new StreamResource(reportName + "-" + df.format(new Date())+".pdf", () -> is);
            //StreamResource streamResource = new StreamResource("Report-" + UUID.randomUUID().toString().replace("-", "") + ".pdf", () -> is);
            
            return (streamResource);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.generateReportStreamResource", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static StreamResource generateReportStreamResource(String titreRapport, String strInitialesUtilisateur, List javaBeanList, String reportName, JasperReport jasperReport) {


    public static StreamResource generateTransactionReportStreamResource(TransactionReportInput reportInput, String reportName, JasperReport jasperReport) {
        try 
        {
            //1 - Use JasperRunManager to fill a report and return byte array object containing the report in PDF format. The intermediate JasperPrint object is not saved on disk.
            byte[] byteArray = null;
            byteArray = JasperRunManager.runReportToPdf(jasperReport, reportInput.getParameters(), new JREmptyDataSource());

            //2 - 
            InputStream is = new ByteArrayInputStream(byteArray);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            //SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            StreamResource streamResource = new StreamResource(reportName + "-" + df.format(new Date())+".pdf", () -> is);
            //StreamResource streamResource = new StreamResource("Report-" + UUID.randomUUID().toString().replace("-", "") + ".pdf", () -> is);
            
            return (streamResource);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.generateTransactionReportStreamResource", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static StreamResource generateTransactionReportStreamResource(TransactionReportInput reportInput, String reportName, JasperReport jasperReport) {

    public static ReportInput dataAssemble(String strTitreRapport, String strDenomination, String strNomVersion, String strInitiales, List javaBeanList) {
        //Créer, remplir une instance de la classe d’entrée (ReportInput) de rapport avec les données
        try 
        {
            ReportInput reportInput = new ReportInput();
            reportInput.setTitreRapport(strTitreRapport);
            reportInput.setDenomination(strDenomination);
            reportInput.setNomVersion(strNomVersion);
            reportInput.setInitiales(strInitiales);

            //Convertir la liste de beans en JRBeanCollectionDataSource
            JRBeanCollectionDataSource javaBeanDataSource = new JRBeanCollectionDataSource(javaBeanList);
            reportInput.setJavaBeanDataSource(javaBeanDataSource);

            return reportInput;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.dataAssemble", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static ReportInput dataAssemble(String strTitre, String strDenomination, String strNomVersion, String strInitiales, List javaBeanList)
   
    public static ReportInput dataAssemble(String strTitreRapport, String strDenomination, String strNomVersion, String strInitiales, String strParamPeriode, String strParamSerie, String strParamSerie1, String strParamSerie2, List javaBeanList) {
        //Créer, remplir une instance de la classe d’entrée (ReportInput) de rapport avec les données
        try 
        {
            ReportInput reportInput = new ReportInput();
            reportInput.setTitreRapport(strTitreRapport);
            reportInput.setDenomination(strDenomination);
            reportInput.setNomVersion(strNomVersion);
            reportInput.setInitiales(strInitiales);

            reportInput.setParamPeriode(strParamPeriode);
            reportInput.setParamSerie(strParamSerie);
            reportInput.setParamSerie1(strParamSerie1);
            reportInput.setParamSerie2(strParamSerie2);

            //Convertir la liste de beans en JRBeanCollectionDataSource
            JRBeanCollectionDataSource javaBeanDataSource = new JRBeanCollectionDataSource(javaBeanList);
            reportInput.setJavaBeanDataSource(javaBeanDataSource);
            
            return reportInput;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.dataAssemble", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static ReportInput dataAssemble(String strTitre, String strDenomination, String strNomVersion, String strInitiales, List javaBeanList)

    public static TransactionReportInput basicDataAssemble(String strTitreRapport, String strDenomination, String strNomVersion, String strInitiales) {
        //Créer, remplir une instance de la classe d’entrée (TransactionReportInput) de rapport avec les données
        try 
        {
            TransactionReportInput reportInput = new TransactionReportInput();
            reportInput.setTitreRapport(strTitreRapport);
            reportInput.setDenomination(strDenomination);
            reportInput.setNomVersion(strNomVersion);
            reportInput.setInitiales(strInitiales);

            //Convertir la liste de beans en JRBeanCollectionDataSource
            JRBeanCollectionDataSource javaBeanDataSource = new JRBeanCollectionDataSource(new ArrayList());
            reportInput.setJavaBeanDataSource(javaBeanDataSource);
            
            return reportInput;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportHelper.basicDataAssemble", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static TransactionReportInput basicDataAssemble(String strTitreRapport, String strDenomination, String strNomVersion, String strInitiales) {
}