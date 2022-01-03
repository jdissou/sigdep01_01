/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.dialogs;

import com.progenia.immaria01_01.data.business.IndicateurSuiviBusiness;
import com.progenia.immaria01_01.data.business.NatureIndicateurBusiness;
import com.progenia.immaria01_01.data.business.TableauCollecteDetailsBusiness;
import com.progenia.immaria01_01.data.business.UniteOeuvreBusiness;
import com.progenia.immaria01_01.data.entity.IndicateurSuivi;
import com.progenia.immaria01_01.data.entity.TableauCollecte;
import com.progenia.immaria01_01.data.entity.TableauCollecteDetails;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.FORM_ITEM_LABEL_WIDTH250;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.PANEL_FLEX_BASIS;
import static com.progenia.immaria01_01.dialogs.BaseEditerReferentielDialog.TEXTFIELD_LEFT_LABEL;
import com.progenia.immaria01_01.dialogs.EditerTableauCollecteDetailsDialog.TableauCollecteDetailsAddEvent;
import com.progenia.immaria01_01.dialogs.EditerTableauCollecteDetailsDialog.TableauCollecteDetailsRefreshEvent;
import com.progenia.immaria01_01.dialogs.EditerTableauCollecteDetailsDialog.TableauCollecteDetailsUpdateEvent;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ModeFormulaireEditerEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
public class EditerTableauCollecteDialog extends BaseEditerReferentielMaitreFormGridDialog<TableauCollecte, TableauCollecteDetails> {
    /***
     * EditerTableauCollecteDialog is responsible for launch Dialog. 
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
    private TableauCollecteDetailsBusiness tableauCollecteDetailsBusiness;
    
    private NatureIndicateurBusiness natureIndicateurBusiness;
    private UniteOeuvreBusiness uniteOeuvreBusiness;

    //CIF Details
    private IndicateurSuiviBusiness indicateurSuiviBusiness;
    private ArrayList<IndicateurSuivi> indicateurSuiviList = new ArrayList<IndicateurSuivi>();
    private ListDataProvider<IndicateurSuivi> indicateurSuiviDataProvider; 
    
    /* Fields to edit properties in TableauCollecte entity */
    private SuperTextField txtCodeTableau = new SuperTextField();
    private SuperTextField txtLibelleTableau = new SuperTextField();
    private SuperTextField txtLibelleCourtTableau = new SuperTextField();
    private Checkbox chkInactif = new Checkbox();

    public EditerTableauCollecteDialog() {
        super();
        this.binder = new BeanValidationBinder<>(TableauCollecte.class);
        
        this.gridBinder = new Binder<>(TableauCollecteDetails.class);

        this.referenceBeanDetailsList = new ArrayList<TableauCollecteDetails>();
        this.detailsBeanList = new ArrayList<TableauCollecteDetails>();
        
        this.configureComponents(); 
                        
        this.configureGrid(); 
    }

    public static EditerTableauCollecteDialog getInstance() {
        try 
        {
            if (VaadinSession.getCurrent().getAttribute(EditerTableauCollecteDialog.class) ==  null) {
                //Register an instance - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(EditerTableauCollecteDialog.class, new EditerTableauCollecteDialog());
            }
            return (EditerTableauCollecteDialog)(VaadinSession.getCurrent().getAttribute(EditerTableauCollecteDialog.class));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    } //public static EditerTableauCollecteDialog getInstance() {
    

    // Show Dialog
    public void showDialog(String dialogTitle, ModeFormulaireEditerEnum modeFormulaireEditerEnum, ArrayList<TableauCollecte> targetBeanList, ArrayList<TableauCollecte> referenceBeanList, String newComboValue, EventBus.UIEventBus uiEventBus, TableauCollecteDetailsBusiness tableauCollecteDetailsBusiness, IndicateurSuiviBusiness indicateurSuiviBusiness, NatureIndicateurBusiness natureIndicateurBusiness, UniteOeuvreBusiness uniteOeuvreBusiness) {
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
            
            this.uiEventBus.subscribe(this, false);//Use false as the second constructor parameter to indicate that the event does not come from the client

            this.tableauCollecteDetailsBusiness = tableauCollecteDetailsBusiness;
            this.indicateurSuiviBusiness = indicateurSuiviBusiness;
            
            this.natureIndicateurBusiness = natureIndicateurBusiness;
            this.uniteOeuvreBusiness = uniteOeuvreBusiness;
            
            //2- CIF
            //CIF Details
            this.indicateurSuiviList = (ArrayList)this.indicateurSuiviBusiness.findAll();
            this.indicateurSuiviDataProvider = DataProvider.ofCollection(this.indicateurSuiviList);
            // Make the dataProvider sorted by LibelleIndicateurSuivi in ascending order
            this.indicateurSuiviDataProvider.setSortOrder(IndicateurSuivi::getLibelleIndicateur, SortDirection.ASCENDING);
        
            //3- Setup ReadOnly Field Mode - Configure ReadOnly Field Set ComboBox DataProvider - Manage ToolBars
            this.customManageReadOnlyFieldMode();
            this.configureReadOnlyField();
            this.setComboBoxDataProvider();
            this.customManageToolBars();
        
            //4- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée avant l'exécution de Collections.sort(this.targetBeanList.....
            this.targetBeanList = targetBeanList;

            //5 - Make the this.targetBeanList sorted by LibelleTableau in ascending order
            Collections.sort(this.targetBeanList, Comparator.comparing(TableauCollecte::getCodeTableau));

            //6- LoadFirstBean : cette instruction doit être exécutée après l'exécution de this.configureComponents() de façon à s'assurer de traiter les données une fois que les champs sont injectés
            this.customLoadFirstBean();

            //7 - Open the dialog
            this.dialog.open();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.showDialog", e.toString());
            e.printStackTrace();
        }
    }

    private void setGridBeanList() {
        try 
        {
            //1 - Fetch the items
            if (this.currentBean != null) {
                this.detailsBeanList = (ArrayList)this.tableauCollecteDetailsBusiness.getDetailsRelatedDataByCodeTableau(this.currentBean.getCodeTableau());
            }
            else {
                this.detailsBeanList = (ArrayList)this.tableauCollecteDetailsBusiness.getDetailsRelatedDataByCodeTableau("");
            } //if (this.currentBean != null) {

            //2 - Set a new data provider. 
            this.detailsDataProvider = DataProvider.ofCollection(this.detailsBeanList);

            //3 - Make the detailsDataProvider sorted by LibelleIndicateur in ascending order
            this.detailsDataProvider.setSortOrder(TableauCollecteDetails::getCodeIndicateur, SortDirection.ASCENDING);
            
            //4 - Set the data provider for this grid. The data provider is queried for displayed items as needed.
            this.grid.setDataProvider(this.detailsDataProvider);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.setGridBeanList", e.toString());
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
            this.txtCodeTableau.setWidth(100, Unit.PIXELS);
            this.txtCodeTableau.setRequired(true);
            this.txtCodeTableau.setRequiredIndicatorVisible(true);
            this.txtCodeTableau.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleTableau.setWidth(400, Unit.PIXELS);
            this.txtLibelleTableau.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtLibelleCourtTableau.setWidth(400, Unit.PIXELS);
            this.txtLibelleCourtTableau.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.chkInactif.setAutofocus(false); //Sepecific for isInactif
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblCodeTableauValidationStatus = new Label();
            this.binder.forField(this.txtCodeTableau)
                .asRequired("La Saisie du Code Tableau est Obligatoire. Veuillez saisir le Code Tableau.")
                .withValidator(text -> text != null && text.length() <= 10, "Code Tableau ne peut contenir au plus 10 caractères")
                .withValidationStatusHandler(status -> {lblCodeTableauValidationStatus.setText(status.getMessage().orElse(""));       
                         lblCodeTableauValidationStatus.setVisible(status.isError());})
                .bind(TableauCollecte::getCodeTableau, TableauCollecte::setCodeTableau); 
            
            Label lblLibelleTableauValidationStatus = new Label();
            this.binder.forField(this.txtLibelleTableau)
                .withValidator(text -> text.length() <= 50, "Libellé Tableau ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleTableauValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleTableauValidationStatus.setVisible(status.isError());})
                .bind(TableauCollecte::getLibelleTableau, TableauCollecte::setLibelleTableau); 
            
            Label lblLibelleCourtTableauValidationStatus = new Label();
            this.binder.forField(this.txtLibelleCourtTableau)
                .withValidator(text -> text.length() <= 20, "Libellé Abrégé Tableau ne peut contenir au plus 20 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleCourtTableauValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleCourtTableauValidationStatus.setVisible(status.isError());})
                .bind(TableauCollecte::getLibelleCourtTableau, TableauCollecte::setLibelleCourtTableau); 
            
            this.binder.forField(this.chkInactif)
                .bind(TableauCollecte::isInactif, TableauCollecte::setInactif); 
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in TableauCollecte and TableauCollecteView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set TextField with on a FormaLayout when add doesn't
            //this.formLayout.add(this.txtCodeTableau, this.txtLibelleTableau, this.txtLibelleCourtTableau, this.chkInactif);
            //4 - Alternative
            this.formLayout.addFormItem(this.txtCodeTableau, "Code Tableau :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleTableau, "Libellé Tableau :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtLibelleCourtTableau, "Libellé Abrégé Tableau :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkInactif, "Inactif :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            

            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), 
                    new FormLayout.ResponsiveStep(PANEL_FLEX_BASIS, 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    private void configureReadOnlyField() {
        //Configure Read Only Fields
        try 
        {
            this.txtCodeTableau.setReadOnly(this.isPrimaryKeyFieldReadOnly); 
            this.txtLibelleTableau.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtLibelleCourtTableau.setReadOnly(this.isContextualFieldReadOnly); 
            this.chkInactif.setReadOnly(true); //Sepecific for isInactif
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.configureReadOnlyField", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.setComboBoxDataProvider", e.toString());
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
            
            Grid.Column<TableauCollecteDetails> noOrdreColumn = this.grid.addColumn(new NumberRenderer<>(TableauCollecteDetails::getNoOrdre, NumberFormat.getIntegerInstance(Locale.FRENCH))).setKey("NoOrdre").setHeader("N° Ordre").setTextAlign(ColumnTextAlign.CENTER).setFlexGrow(0).setWidth("250px"); // fixed column

            Grid.Column<TableauCollecteDetails> indicateurColumn = this.grid.addColumn(new ComponentRenderer<>(
                        tableauCollecteDetails -> {
                            //ComboBox comboBox = new ComboBox();
                            ComboBox<IndicateurSuivi> comboBox = new ComboBox<>();
                            comboBox.setDataProvider(this.indicateurSuiviDataProvider);
                            // Choose which property from IndicateurSuivi is the presentation value
                            comboBox.setItemLabelGenerator(IndicateurSuivi::getLibelleIndicateur);
                            comboBox.setValue(tableauCollecteDetails.getIndicateurSuivi());
                            comboBox.getElement().setAttribute("theme", "widepopup");
                            
                            //comboBox.setRequired(true);
                            //comboBox.setRequiredIndicatorVisible(true);

                            return comboBox;
                        }
                    )
            ).setKey("IndicateurSuivi").setHeader("Indicateur Suivi").setTextAlign(ColumnTextAlign.START).setFlexGrow(0).setWidth("175px"); // fixed column
      } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.configureGrid", e.toString());
            e.printStackTrace();
        }
    }    

    @Override
    protected void workingHandleDetailsClick(ClickEvent event) {
        try 
        {
            //Ouvre l'instance du Dialog EditerTableauCollecteDetailsDialog.
            EditerTableauCollecteDetailsDialog.getInstance().showDialog("Détails Tableau de Collecte", this.currentBean, this.detailsBeanList, this.referenceBeanDetailsList, this.uiEventBus, this.indicateurSuiviBusiness, this.natureIndicateurBusiness, this.uniteOeuvreBusiness);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingHandleDetailsClick", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingExecuteOnCurrent", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingExecuteBeforeUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingExecuteBeforeAddNew", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingExecuteAfterUpdate", e.toString());
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
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingExecuteAfterUpdate", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishAddEvent() {
        //Publish Add Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteAddEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.publishAddEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishUpdateEvent() {
        //Publish Update Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteUpdateEvent(this.dialog, this.currentBean));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.publishUpdateEvent", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishRefreshEvent() {
        //Publish Refresh Event
        try 
        {
            this.uiEventBus.publish(this, new TableauCollecteRefreshEvent(this.dialog));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.publishRefreshEvent", e.toString());
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
                    .anyMatch(p -> (p != this.currentBean) && (p.getCodeTableau()
                            .equals(this.txtCodeTableau.getValue())))) {
                blnCheckOk = false;
                this.txtCodeTableau.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Risque de Doublons dans champ clé principale. Veuillez en saisir un autre Code Tableau.");
                
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.workingIsPrimaryKeyAndBeanExtraCheckValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//protected boolean workingIsPrimaryKeyAndBeanExtraCheckValidated()
  
    @EventBusListenerMethod
    private void handleAddEventFromEditorView(TableauCollecteDetailsAddEvent event) {
        //Handle Add Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TableauCollecteDetails newInstance = this.tableauCollecteDetailsBusiness.save(event.getTableauCollecteDetails());

            //2 - Actualiser la liste
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.handleAddEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleUpdateEventFromEditorView(TableauCollecteDetailsUpdateEvent event) {
        //Handle Udpdate Event received from EditorView
        try 
        {
            //1 - Sauvegarder la modification dans le backend
            TableauCollecteDetails updateInstance = this.tableauCollecteDetailsBusiness.save(event.getTableauCollecteDetails());

            //2- Retrieving targetBeanList from the database
            this.setGridBeanList();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.handleUpdateEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @EventBusListenerMethod
    private void handleRefreshEventFromEditorView(TableauCollecteDetailsRefreshEvent event) {
        //Handle Refresh Event received from EditorView
        try 
        {
            //1 - Actualiser l'affichage du grid
            this.detailsDataProvider.refreshAll();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("EditerTableauCollecteDialog.handleRefreshEventFromEditorView", e.toString());
            e.printStackTrace();
        }
    }
    
    @Override
    public TableauCollecte workingCreateNewBeanInstance()
    {
        return (new TableauCollecte());
    }
    
    @Override
    protected void workingSetFieldsInitValues() {
        //Set default value - Code à exécuter après this.binder.readBean(this.currentBean) 
        this.txtLibelleTableau.setValue(this.newComboValue);
        this.txtLibelleTableau.focus();
    }
    
    //Setting Up Events
    /* Start of the API - EVENTS OUT */
    public static abstract class EditerTableauCollecteDialogEvent extends ComponentEvent<Dialog> {
        private TableauCollecte tableauCollecte;

        protected EditerTableauCollecteDialogEvent(Dialog source, TableauCollecte argTableauCollecte) { 
            /* The second constructor parameter determines whether the event is triggered 
            by a DOM event in the browser 
            or through the component’s server-side API. */
            super(source, false); //Use false as the second constructor parameter to indicate that the event does not come from the client
            this.tableauCollecte = argTableauCollecte;
        }

        public TableauCollecte getTableauCollecte() {
            return tableauCollecte;
        }
    }

    public static class TableauCollecteAddEvent extends EditerTableauCollecteDialogEvent {
        public TableauCollecteAddEvent(Dialog source, TableauCollecte tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class TableauCollecteUpdateEvent extends EditerTableauCollecteDialogEvent {
        public TableauCollecteUpdateEvent(Dialog source, TableauCollecte tableauCollecte) {
            super(source, tableauCollecte);
        }
    }

    public static class TableauCollecteRefreshEvent extends EditerTableauCollecteDialogEvent {
        public TableauCollecteRefreshEvent(Dialog source) {
            super(source, null);
        }
    }
    /* End of the API - EVENTS OUT */

    private class SortByCodeTableau implements Comparator<TableauCollecte> {
        // Used for sorting in ascending order of
        // getCodeTableau()
        public int compare(TableauCollecte a, TableauCollecte b)
        {
            return a.getCodeTableau().compareTo(b.getCodeTableau());
        }
    }
}
