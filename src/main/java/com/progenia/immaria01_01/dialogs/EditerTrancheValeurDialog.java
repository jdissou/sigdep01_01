/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.TrancheValeurDetailsBusiness;
import com.progenia.immaria01_01.data.entity.TrancheValeur;
import com.progenia.immaria01_01.data.entity.TrancheValeurDetails;
import com.progenia.immaria01_01.dialogs.EditerTrancheValeurDetailsDialog.TrancheValeurDetailsAddEvent;
import com.progenia.immaria01_01.dialogs.EditerTrancheValeurDetailsDialog.TrancheValeurDetailsRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerTrancheValeurDetailsDialog.TrancheValeurDetailsUpdateEvent;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.server.VaadinSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import org.vaadin.miki.superfields.text.SuperTextField;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public class EditerTrancheValeurDialog extends BaseEditerReferentielMaitreFormGridDialog<TrancheValeur, TrancheValeurDetails> {
    /***
     * EditerTrancheValeurDialog is responsible for launch Dialog. 
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

    //Details
    private TrancheValeurDetailsBusiness trancheValeurDetailsBusiness;
    
    /* Fields to edit properties in TrancheValeur entity */
    private SuperTextField txtCodeTranche = new SuperTextField();
    private SuperTextField txtLibelleTranche = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();

    public EditerTrancheValeurDialog() {
        super();
        this.binder = new BeanValidationBinder<>(TrancheValeur.class);
        
        this.gridBinder = new Binder<>(TrancheValeurDetails.class);

        this.referenceBeanDetailsList = new ArrayList<TrancheValeurDetails>();
        this.detailsBeanList = new ArrayList<TrancheValeurDetails>();
        
        this.configureComponents(); 
                        
        this.configureGrid(); 
    }

    public static EditerTrancheValeurDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerTrancheValeurDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerTrancheValeurDialog.class, new EditerTrancheValeurDialog());
            }
            return (EditerTrancheValeurDialog)(VaadinSession.getCurrent().getAttribute(EditerTrancheValeurDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerTrancheValeurDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<TrancheValeur> targetBeanList, ArrayList<TrancheValeur> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TrancheValeurDetailsBusiness trancheValeurDetailsBusiness) {
        //Cette méthode contient les instructions ad hoc
        try 
        {
            //1- Initialisation des paramètres passés
            this.customSetDialogTitle(dialogTitle);
            this.customSetModeFormulaireEditer(modeFormulaireEditerEnum);
            this.customSetReferenceBeanList(referenceBeanList);

            if (this.modeFormulaireEditer == ModeFormulaireEditerEnum.AJOUTERCIF) {
                this.customSetNewComboValue(newComboValue);
            }
            
            this.uiEventBus = uiEventBus;
            
            this.trancheValeurDetailsBusiness = trancheValeurDetailsBusiness;
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            //2- CIF
            //Néant
            
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;
            
            //5 - Make the this.targetBeanList sorted by LibelleTranche in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(TrancheValeur::getCodeTranche));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.trancheValeurDetailsBusiness.getDetailsRelatedDataByCodeTranche(this.currentBean.getCodeTranche());
            }
            else {
                this.detailsBeanList = (ArrayList)this.trancheValeurDetailsBusiness.getDetailsRelatedDataByCodeTranche("");
            } //if (this.currentBean != null) {

            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by ValeurTrancheSuperieure in ascending order
            this.detailsDataProvider.setSortOrder(TrancheValeurDetails::getValeurTrancheSuperieure, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.setGridBeanList", e.toString());
            e.printStackTrace();
        }
    }
    
    private void configureComponents() {
        //Associate the data with the formLayout columns and load the data. 
        
        try 
        {
            //1 - Set properties of the form
            this.formLayout.addClassName("fichier-form");
            this.formLayout.setSizeFull(); //sets the form size to fill the screen.
            
            //2 - Define the Fields instances to use - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.txtCodeTranche.setWidth(100, Unit.PIXELS);
            this.txtCodeTranche.setRequired(true);
            this.txtCodeTranche.setRequiredIndicatorVisible(true);
            this.txtCodeTranche.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleTranche.setWidth(400, Unit.PIXELS);
            this.txtLibelleTranche.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeTrancheValidationStatus = new Label();
            this.binder.forField(this.txtCodeTranche)
                .asRequired("La Saisie du Code Tranche est Obligatoire. Veuillez saisir le Code Tranche.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Tranche ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeTrancheValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeTrancheValidationStatus.setVisible(status.isError());})
                .bind(TrancheValeur::getCodeTranche, TrancheValeur::setCodeTranche); 
            
            Label lblLibelleTrancheValidationStatus = new Label();
            this.binder.forField(this.txtLibelleTranche)
                .withValidator(text -> text.length() <= 50, "Libellé Tranche ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleTrancheValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleTrancheValidationStatus.setVisible(status.isError());})
                .bind(TrancheValeur::getLibelleTranche, TrancheValeur::setLibelleTranche); 
            
            this.binder.forField(this.chkInactif)
                .bind(TrancheValeur::isInactif, TrancheValeur::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in TrancheValeur and TrancheValeurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeTranche, this.txtLibelleTranche, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeTranche, "Code Tranche :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleTranche, "Libellé Tranche :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeTranche.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleTranche.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.configureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    }    

    private void setComboBoxDataProvider() {
        //Set Combo Box DataProvider
        try 
        {
            //Néant
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.setComboBoxDataProvider", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureGrid() {
        //Associate the data with the grid columns and load the data. 
        try 
        {
            //1 - Set properties of the grid
            this.grid.addClassName("fichier-grid");
            this.grid.getThemeNames().addAll(Arrays.asList("compact", "column-borders", "row-stripes", "wrap-cell-content"));
            
            this.grid.setSizeFull(); //sets the grid size to fill the screen.
            
            //this.grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            this.grid.setSelectionMode(Grid.SelectionMode.MULTI);
            
            Grid.Column<TrancheValeurDetails> valeurTrancheSuperieureColumn = this.grid.addColumn(new NumberRenderer<>(TrancheValeurDetails::getValeurTrancheSuperieure, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("ValeurTrancheSuperieure").setHeader("Valeur Tranche Supérieure").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("500px"); // fixed column
            Grid.Column<TrancheValeurDetails> valeurApplicableColumn = this.grid.addColumn(new NumberRenderer<>(TrancheValeurDetails::getValeurApplicable, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("ValeurApplicable").setHeader("Valeur Applicable").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("300px"); // fixed column
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerTrancheValeurDetailsDialog.
            EditerTrancheValeurDetailsDialog.getInstance().showDialog("Détails Tranche de Valeur", this.currentBean, this.detailsBeanList, this.referenceBeanDetailsList, this.uiEventBus);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingHandleDetailsClick", e.toString());
            e.printStackTrace();
        }
    } //protected void workingHandleDetailsClick() {
    @Override

    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
        try 
        {
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingExecuteOnCurrent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeUpdate() {
        //execute Before Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingExecuteBeforeUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteBeforeAddNew() {
        //execute Before Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingExecuteBeforeAddNew", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterAddNew() {
        //execute After Add New Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void workingExecuteAfterUpdate() {
        //execute After Update current Bean
        try 
        {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new TrancheValeurRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.publishRefreshEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
    {
        //TEST à effectuer avant la mise à jour ou l'ajout du nouvel enregistrement courant
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            if (this.referenceBeanList.stream()
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeTranche()
                            .equals(this.txtCodeTranche.getValue())))) {
                blnCheckOk = false;
                this.txtCodeTranche.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Tranche.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
  
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(TrancheValeurDetailsAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TrancheValeurDetails newInstance = this.trancheValeurDetailsBusiness.save(event.getTrancheValeurDetails());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(TrancheValeurDetailsUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TrancheValeurDetails updateInstance = this.trancheValeurDetailsBusiness.save(event.getTrancheValeurDetails());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(TrancheValeurDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.detailsDataProvider.refreshAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTrancheValeurDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public TrancheValeur workingCreateNewBeanInstance()
    {
        return (new TrancheValeur());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleTranche.setValue(this.newComboValue);
        this.txtLibelleTranche.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerTrancheValeurDialogEvent extends ComponentEvent<Dialog> {
        private TrancheValeur trancheValeur;

        protected EditerTrancheValeurDialogEvent(Dialog source, TrancheValeur argTrancheValeur) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.trancheValeur = argTrancheValeur;
        }

        public TrancheValeur getTrancheValeur() {
            return trancheValeur;
        }
    }

    public static class TrancheValeurAddEvent extends EditerTrancheValeurDialogEvent {
        public TrancheValeurAddEvent(Dialog source, TrancheValeur trancheValeur) {
            super(source, trancheValeur);
        }
    }

    public static class TrancheValeurUpdateEvent extends EditerTrancheValeurDialogEvent {
        public TrancheValeurUpdateEvent(Dialog source, TrancheValeur trancheValeur) {
            super(source, trancheValeur);
        }
    }

    public static class TrancheValeurRefreshEvent extends EditerTrancheValeurDialogEvent {
        public TrancheValeurRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */
}
