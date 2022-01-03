/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ReportHelper;
import com.progenia.immaria01_01.utilities.ReportViewerDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class FichierBase<T extends Serializable> extends VerticalLayout   {
    
    protected ArrayList<T> targetBeanList;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 

    protected String reportName;
    protected String reportTitle;
    protected String strNomFormulaire;
    protected Boolean isAllowInsertItem;
    protected Boolean isAllowEditItem;
    protected Boolean isAllowDeleteItem;

    protected HorizontalLayout topToolBar;
    protected ContextMenu menu;
    
    //Boutons
    protected Button btnOverflow;
    protected Button btnAjouter;
    protected Button btnModifier;
    protected Button btnAfficher;
    protected Button btnSupprimer;
    protected Button btnActiver;
    protected Button btnDesactiver;
    protected Button btnImprimer;

    //Defines a new field grid and instantiates it to a Grid of type T.
    protected Grid<T> grid = new Grid<>(); //Manually defining columns

    public FichierBase() {
        //1 - Give the component a CSS class name to help with styling
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
            MessageDialogHelper.showAlertDialog("FichierBase.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    public void customActivateMainToolBar()
    {
        try 
        {
            this.btnAjouter.setEnabled((this.isAllowInsertItem && SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire)));

            //int fullSize = dataProvider.getItems().size(); // this is how you get the size of all items
            int filteredSize = dataProvider.size(new Query<>(dataProvider.getFilter()));
    
            if (filteredSize == 0) //if (this.targetBeanList.size() == 0)
            {
                this.btnModifier.setEnabled(false);
                this.btnSupprimer.setEnabled(false);

                this.btnAfficher.setEnabled(false);

                this.btnActiver.setEnabled(false);
                this.btnDesactiver.setEnabled(false);

                this.btnImprimer.setEnabled(false);
            }
            else
            {
                this.btnModifier.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));
                this.btnSupprimer.setEnabled((this.isAllowDeleteItem && SecurityService.getInstance().isSuppressionAutorisee(this.strNomFormulaire)));

                this.btnAfficher.setEnabled(true);

                this.btnActiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));
                this.btnDesactiver.setEnabled((this.isAllowEditItem && SecurityService.getInstance().isModificationAutorisee(this.strNomFormulaire)));

                this.btnImprimer.setEnabled(true);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.customActivateMainToolBar", e.toString());
            e.printStackTrace();
        }
    } //public void customActivateMainToolBar()

    public void customRefreshGrid()
    {
        /* Run this both when first creating the grid, and again after the new item is saved.
           This time you don't need to call refreshAll()
        */
        try 
        {
            //1 - Fetch the items again
            this.targetBeanList = this.workingFetchItems();
            
            //2 - Set a new data provider. 
            this.dataProvider = DataProvider.ofCollection(this.targetBeanList);
            
            //3- Set the data provider for this grid. 
            this.grid.setDataProvider(this.dataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.customRefreshGrid", e.toString());
            e.printStackTrace();
        }
    } //public void customRefreshGrid()

        
    protected ArrayList<T> workingFetchItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchItems() {
    
    protected List workingGetReportData() {
        return (new ArrayList ());
    } //protected List workingGetReportData() {
    
    protected void workingSaveItem(T item) {
    } //protected void workingSaveItem() {
    
    protected void workingDeleteItem(T item) {
    } //protected void workingDeleteItem() {
    
    protected void workingSetInactif(T item, boolean isInactif) {
    } //protected void workingSetInactif(T item, boolean isInactif) {
    
    protected void workingHandleAjouterClick(ClickEvent event) {
    } //protected void workingHandleAjouterClick() {
    
    protected void workingHandleModifierClick(ClickEvent event) {
    } //protected void workingHandleModifierClick() {
    
    protected void workingHandleAfficherClick(ClickEvent event) {
    } //protected void workingHandleAfficherClick() {
    
    private void handleSupprimerClick(ClickEvent event) {
        //Supprimer l'enregistrement courant 
        try 
        {
            Set<T> selected = this.grid.getSelectedItems();

            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Suppression d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                    //Abandonner la suppression
                    //Rien à faire
                };

                //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
                ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                    //Confirmer la suppression
                    //1 - Iterate Set Using For-Each Loop
                    for(T item : selected) {
                        this.workingDeleteItem(item);
                    }            

                    //2 - Actualiser la liste
                    this.customRefreshGrid();

                    //3 - Activation de la barre d'outils
                    this.customActivateMainToolBar();
                };

                MessageDialogHelper.showYesNoDialog("Suppression d'un Enregistrement de référence", "Désirez-vous effacer les enregistrements de manière permanente?. Cliquez sur Oui pour effacer cet enregistrement de manière permanente. Vous ne pourrez plus annuler ce changement.", yesClickListener, noClickListener);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.handleSupprimerClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleSupprimerClick() {
    
    private void handleActiverClick(ClickEvent event) {
        //Activer l'enregistrement courant 
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.grid.getSelectedItems();

            //2 - Iterate Set Using For-Each Loop
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Activation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                for(T item : selected) {
                    //Mise à jour
                    this.workingSetInactif(item, false);

                    //Enregistrer les mofdifications dans le backend
                    this.workingSaveItem(item);
                }   //for(T item : selected) {

                //3- Retrieving targetBeanList from the database
                this.customRefreshGrid();

                //4 - Annulation des sélections - indispensable
                //this.grid.deselectAll();
            } //if (selected.isEmpty() == true)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.handleActiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleActiverClick(ClickEvent event) {
    
    private void handleDesactiverClick(ClickEvent event) {
        //Desactiver l'enregistrement courant 
        try 
        {
            //1 - Get selected rows
            Set<T> selected = this.grid.getSelectedItems();

            //2 - Iterate Set Using For-Each Loop
            if (selected.isEmpty() == true)
            {
                MessageDialogHelper.showWarningDialog("Désactivation d'enregistrement", "Aucune ligne n'est sélectionnée. Veuillez d'abord sélectionner une ligne dans le tableau.");
            }
            else
            {
                for(T item : selected) {
                    //Mise à jour
                    this.workingSetInactif(item, true);

                    //Enregistrer les mofdifications dans le backend
                    this.workingSaveItem(item);
                }  //for(T item : selected) {

                //3- Retrieving targetBeanList from the database
                this.customRefreshGrid();
                
                //4 - Annulation des sélections - indispensable
                //this.grid.deselectAll();
            } //if (selected.isEmpty() == true)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.handleDesactiverClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleDesactiverClick(ClickEvent event) {
    
    protected void workingHandleImprimerClick(ClickEvent event) {
        try 
        {
            this.execJasperReport();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.workingHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleImprimerClick() {
    
     private void execJasperReport() {
        try 
        {
            /*
            org.springframework.core.convert.ConversionFailedException: Failed to convert from type [java.lang.Object[]] to type [com.progenia.immaria01_01.securities.data.entity.UtilisateurEtat] for value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; nested exception is org.springframework.core.convert.ConverterNotFoundException: No converter found capable of converting from type [java.lang.String] to type [com.progenia.immaria01_01.securities.data.entity.UtilisateurEtat]
            */
            final String initialesUtilisateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getInitialesUtilisateur(); 
            
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
            //org.springframework.core.convert.ConversionFailedException: 
            // Failed to convert from type [java.lang.Object[]] to type [com.progenia.immaria01_01.securities.data.entity.UtilisateurEtat] for
            //value '{Admin, Admin, ok, Admin, 01, AD, 2021-04-21 00:00:00.0, 2022-04-21 00:00:00.0, false, Administrateur}'; 
            //nested exception is org.springframework.core.convert.ConverterNotFoundException: 
            //No converter found capable of converting from type [java.lang.String] to type [com.progenia.immaria01_01.securities.data.entity.UtilisateurEtat]
            
            streamResource = ReportHelper.generateReportStreamResource(this.reportTitle, initialesUtilisateur, this.workingGetReportData(), this.reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.execJasperReport", e.toString());
            e.printStackTrace();
        }
    } //private void execJasperReport() {
    
    //Factorisation
    private boolean reportFileExists(String fileName) {
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
    } //private void reportFileExists() {
    
    private void execEmbeddedPdfDocument() {
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
            MessageDialogHelper.showAlertDialog("FichierBase.execEmbeddedPdfDocument", e.toString());
            e.printStackTrace();
        }
    } //private void execEmbeddedPdfDocument() {
    
    private void execPrintVaadinReport() {
        try 
        {
            UI.getCurrent().getPage().executeJs( "print();" );
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("FichierBase.execPrintVaadinReport", e.toString());
            e.printStackTrace();
        }
    } //private void execPrintVaadinReport() {
}
