/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class ReportViewerDialog {
    /***
     * ReportViewerDialog is responsible for launch Report Viewer Dialog. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     */

     /*
        We make this view a reusable component that work in the same way as any Vaadin component : so, we can use it anywhere. 
        We configure the component by setting properties, and the component notifies us of events through listeners.
        Creating a reusable component is as simple as making sure it can be configured through : 
        setters, and that it fires events whenever something happens. 
        Using the component should not have side effects, for instance it shouldn’t change anything in the database by itself.
    */

    /**
     * The added bean
     */

    
    //Properties of API
    private StreamResource streamResource;

    /* Defines a new ReportEmbeddedPdfDocument. */
    private ReportEmbeddedPdfDocument viewer;
    
    //Define a Dialog
    private Dialog dialog = new Dialog();
    
    //Define a wrapper
    private VerticalLayout wrapper = new VerticalLayout();
    
    /* Action buttons */
    private Button btnFermer;
    private HorizontalLayout topToolBar;
    private Label lblInfoLabel;

    private ReportViewerDialog() 
    {
        super();
        this.setupComponents(); 
    }

    public static ReportViewerDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(ReportViewerDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(ReportViewerDialog.class, new ReportViewerDialog());
            }
            else {
                ((ReportViewerDialog)(VaadinSession.getCurrent().getAttribute(ReportViewerDialog.class))).wrapper.removeAll();
            }
            return (ReportViewerDialog)(VaadinSession.getCurrent().getAttribute(ReportViewerDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static ReportViewerDialog getInstance() {
    

    // Show Dialog
    public void showDialog(StreamResource streamResource) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.setStreamResource(streamResource);
            
            //2 - Configure the Viewer
            this.configureViewer();
            
            //3 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }
    
    private void setupComponents() {
        //Cette méthode contient les instructions pour créer les composants
        try 
        {
            //1- Mise à jour des propriétés du dialog
            this.dialog.setCloseOnEsc(false);
            this.dialog.setCloseOnOutsideClick(false);
            this.dialog.setModal(true);
            this.dialog.setSizeFull(); //sets the dialog size to fill the screen.
            this.dialog.setDraggable(false);
            this.dialog.setResizable(false);

            //2 - Setup the bottom toolbar
            this.customSetupTopToolBar();
            
            //3 - Setup the viewer
            //this.setupViewer(); 
            
            //4- Adds the bottom toolbar and the viewer to the layout
            this.wrapper.setPadding(false);
            this.wrapper.setSpacing(false);
            this.wrapper.setHeightFull();

            this.dialog.add(this.topToolBar, this.wrapper);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.setupComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void customSetupTopToolBar() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            
            //this.topToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("report-viewer-toolbar");
            
            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);

            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.btnFermer = new Button("Fermer", new Icon(VaadinIcon.CLOSE_CIRCLE_O));
            this.btnFermer.addClickListener(e -> customHandleFermerClick(e));

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.topToolBar.add(this.btnFermer, this.lblInfoLabel);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    private void configureViewer() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            this.viewer = new ReportEmbeddedPdfDocument(streamResource);
            /*
            this.viewer.setHeight("100%");
            this.viewer.setHeightFull();
            */
            this.viewer.setSizeFull();
            this.wrapper.add(this.viewer);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.configureViewer", e.toString());
            e.printStackTrace();
        }
    }    

    public void customHandleFermerClick(ClickEvent event) {
        try 
        {
            //Close the dialog
            this.dialog.close();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ReportViewerDialog.customHandleFermerClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleFermerClick() {

    // Creating setters 
    /* Start of the API - PROPERTIES IN */
    public void setStreamResource(StreamResource streamResource) {
        this.streamResource = streamResource;
    }
    
    /* End of the API - PROPERTIES IN */

    private void setFlexGrow(int i, VerticalLayout wrapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
