/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.views.base;

import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.utilities.ReportHelper;
import com.progenia.incubatis01_03.utilities.ReportViewerDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

public abstract class OngletReferentielBase extends VerticalLayout   {
 
    final private String INITIALES_UTILISATEUR = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getInitialesUtilisateur(); 

    //Factorisation
    //Boutons
    protected Button btnOverflow;
    protected Button btnAjouter;
    protected Button btnModifier;
    protected Button btnAfficher;
    protected Button btnSupprimer;
    protected Button btnActiver;
    protected Button btnDesactiver;
    protected Button btnImprimer;

    protected String strNomFormulaire;
    
    protected Map<Tab, Component> tabsToPages = new HashMap<>();
    protected Map<Tab, String> tabsToPageNames = new HashMap<>(); //Hold View names for security check

    protected Tabs tabs = new Tabs();
    protected HorizontalLayout pages = new HorizontalLayout();
    protected Tab selectedTab;
    protected Component selectedPage;
    
    protected HorizontalLayout topToolBar;
    protected ContextMenu menu;
    
    //Factorisation
    protected Boolean isAllowInsertItem;
    protected Boolean isAllowEditItem;
    protected Boolean isAllowDeleteItem;

    public OngletReferentielBase() {
        //Give the component a CSS class name to help with styling
        this.addClassName("fichier-list-view"); //Gives the component a CSS class name to help with styling.
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.

        this.setSizeFull(); //sets the size of the MainView Content
        this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
        this.setMargin(true); //sets the margin.            
   }

    public void customSetupTopToolBar() {
        /* Responsive Toolbar in Flow
         This setup help make the application a bit more responsive across different sized viewports.
        Toolbars typically house a lot of actions. 
        On desktop we usually have more than enough room, but on mobile it’s a different story. 
        A common solution is to use a so-called overflow menu.
        We do by :
        1. Creating a simple toolbar by extending HorizontalLayout.
        2. Hiding buttons on small viewports
        3. Creating an overflow menu (context menu)
        4. Showing (on on small viewports narrower than 480 pixels)  & hiding (on desktop) the overflow menu
        To accomplish that we use  CSS file and set class for the toolbar and overflow buttonr. 
        */
        
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            
            //this.topToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-toolbar");

            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);

            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.btnOverflow = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_V));
            this.btnOverflow.addClassName("fichier-button-overflow");
            
            this.menu = new ContextMenu();
            this.menu.setTarget(this.btnOverflow); // 
            this.menu.setOpenOnClick(true);
            
            this.menu.addItem("Ajouter", (e -> workingHandleAjouterClick(e)));
            this.menu.addItem("Modifier", (e -> workingHandleModifierClick(e)));
            this.menu.addItem("Afficher", (e -> workingHandleAfficherClick(e)));
            this.menu.addItem("Supprimer", (e -> handleSupprimerClick(e)));
            this.menu.addItem("Activer", (e -> handleActiverClick(e)));
            this.menu.addItem("Désactiver", (e -> handleDesactiverClick(e)));
            this.menu.addItem("Imprimer", (e -> workingHandleImprimerClick(e)));
            
            Span title = new Span(" ");
            
            this.btnAjouter = new Button("Ajouter", new Icon(VaadinIcon.FILE_ADD)); 
            this.btnAjouter.addClickListener(e -> workingHandleAjouterClick(e));

            this.btnModifier = new Button("Modifier", new Icon(VaadinIcon.EDIT));
            this.btnModifier.addClickListener(e -> workingHandleModifierClick(e));

            this.btnAfficher = new Button("Afficher", new Icon(VaadinIcon.FORM));
            this.btnAfficher.addClickListener(e -> workingHandleAfficherClick(e));

            this.btnSupprimer = new Button(new Icon(VaadinIcon.TRASH));
            this.btnSupprimer.addClickListener(e -> handleSupprimerClick(e));
            
            this.btnActiver = new Button("Activer", new Icon(VaadinIcon.FLAG_O));
            this.btnActiver.addClickListener(e -> handleActiverClick(e));

            this.btnDesactiver = new Button("Désactiver", new Icon(VaadinIcon.FLAG));
            this.btnDesactiver.addClickListener(e -> handleDesactiverClick(e));

            this.btnImprimer = new Button(new Icon(VaadinIcon.PRINT)); 
            this.btnImprimer.addClickListener(e -> workingHandleImprimerClick(e));

            this.topToolBar.add(title, this.btnAjouter, this.btnModifier, this.btnAfficher, this.btnSupprimer, 
                    this.btnActiver, this.btnDesactiver, 
                    this.btnImprimer, this.btnOverflow);
            setFlexGrow(1, title);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("OngletReferentielBase.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    } //public void customSetupTopToolBar() {

    protected void workingHandleAjouterClick(ClickEvent event) {
    } //protected void workingHandleAjouterClick() {
    
    protected void workingHandleModifierClick(ClickEvent event) {
    } //protected void workingHandleModifierClick() {
    
    protected void workingHandleAfficherClick(ClickEvent event) {
    } //protected void workingHandleAfficherClick() {

    protected void handleSupprimerClick(ClickEvent event) {
    } //protected void handleSupprimerClick() {
    
    protected void handleActiverClick(ClickEvent event) {
    } //protected void handleActiverClick(ClickEvent event) {
    
    protected void handleDesactiverClick(ClickEvent event) {
    } //protected void handleDesactiverClick(ClickEvent event) {
    
    protected void workingHandleImprimerClick(ClickEvent event) {
    } //protected void workingHandleImprimerClick() {
    
    protected void execJasperReport(String reportName, String reportTitle, List reportData) {
        try 
        {
            
            final String report_resources_path = ApplicationConstanteHolder.getREPORT_RESOURCES_PATH();
            final String report_relative_path = ApplicationConstanteHolder.getREPORT_RELATIVE_PATH();

            final String compiledReportResourcesName = report_resources_path + reportName + ".jasper";
            final String compiledReportRelativeName = report_relative_path + reportName + ".jasper";
 
            final String sourceReportResourcesName = report_resources_path + reportName + ".jrxml";
            final String sourceReportRelativeName = report_relative_path + reportName + ".jrxml";

            final File compiledReportFile = new File(compiledReportRelativeName);
            final File sourceReportFile = new File(sourceReportRelativeName);
            
            JasperReport jasperReport;
            StreamResource streamResource;

            //A - Générer le rapport
            if(!compiledReportFile.exists() || compiledReportFile.lastModified() < sourceReportFile.lastModified()) { 
                //1 - Read jrxml file - Lire jrxml à partir du chemin donné comme objet de flux d'entrée
                InputStream inputStream = getClass().getResourceAsStream(sourceReportResourcesName);
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à getClass() qui n'existe pas dans une méthode statique

                //2 - Générer le rapport
                jasperReport = ReportHelper.generateCompiledJasperReport(inputStream);

                //3 - To avoid compiling it every time, we can save it to a file:
                JRSaver.saveObject(jasperReport, compiledReportRelativeName);
                //JRSaver.saveObject(jasperReport, "./src/main/resources/reports/" + reportName + ".jasper");
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRSaver.saveObject() qui ne renvoie pas le bon directory dans une méthode statique
            } 
            else {
                // The report is already compiled
                //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à JRLoader.loadObject() qui ne renvoie pas le bon directory dans une méthode statique
                jasperReport = (JasperReport) JRLoader.loadObject(compiledReportFile);
            }            

            //B - Générer le rapport
            //??? TO DO 
            streamResource = ReportHelper.generateReportStreamResource(reportTitle, this.INITIALES_UTILISATEUR, reportData, reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("OngletReferentielBase.execJasperReport", e.toString());
            e.printStackTrace();
        }
    } //protected void execJasperReport() {
    
    protected boolean reportFileExists(String fileName) {
        try 
        {
            //1 - Read the file - open your tthe file
            this.getClass().getResourceAsStream(fileName);
            //Ce code ne peut être exécuté dans ReportHelper parce qu'on a recours à getClass() qui n'existe pas dans une méthode statique
            
            return (true);
        } 
        catch (Exception e) 
        {
            return false;
        }
    } //protected void reportFileExists() {
    
    protected void execEmbeddedPdfDocument() {
        try 
        {
            // Widgets
            H1 heading = new H1( "Download PDF in browser tab" );

            String url = "https://www.fda.gov/media/76797/download";
            Anchor anchor = new Anchor( url , "Open a PDF document" );
            anchor.setTarget( "_blank" );  // Specify `_blank` to open in a new browser tab/window.

            // Arrange
            this.add(heading , anchor );
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("OngletReferentielBase.execEmbeddedPdfDocument", e.toString());
            e.printStackTrace();
        }
    } //protected void execEmbeddedPdfDocument() {
    
    protected void execPrintVaadinReport() {
        try 
        {
            UI.getCurrent().getPage().executeJs( "print();" );
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("OngletReferentielBase.execPrintVaadinReport", e.toString());
            e.printStackTrace();
        }
    } //protected void execPrintVaadinReport() {
    
protected void updateAndShowSelectedTab() {
        //Update and Show Selected Tab
        try 
        {
            if (SecurityService.getInstance().isAccessGranted(this.tabsToPageNames.get(this.tabs.getSelectedTab())) == true) {
                //Access is granted
                this.selectedTab = this.tabs.getSelectedTab();
                this.selectedPage = this.tabsToPages.get(this.selectedTab);

                this.tabsToPages.values().forEach(page -> page.setVisible(false));
                this.applySelectedTabChanged();
                this.customActivateMainToolBar();

                this.selectedPage.setVisible(true);
                //this.tabs.setSelectedTab(null);
            }
            else {
                //Access not granted
                this.tabs.setSelectedTab(this.selectedTab);
                MessageDialogHelper.showWarningDialog("Accès refusé", "Vous n'avez pas accès à cette fonctionnalité. Veuillez contacter l'Administrateur.");
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("OngletReferentielBase.updateAndShowSelectedTab", e.toString());
            e.printStackTrace();
        }
    } //protected void updateAndShowSelectedTab() {
   
    protected void applySelectedTabChanged()
    {
    }
        
    public void customActivateMainToolBar()
    {
    }
        
}
