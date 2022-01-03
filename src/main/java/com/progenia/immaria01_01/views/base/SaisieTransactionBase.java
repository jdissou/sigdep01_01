/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.utilities.ReportHelper;
import com.progenia.immaria01_01.utilities.ReportViewerDialog;
import com.progenia.immaria01_01.utilities.TransactionReportInput;
import com.progenia.immaria01_01.views.main.MainView;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.vaadin.miki.superfields.dates.SuperDatePicker;
import org.vaadin.miki.superfields.dates.SuperDateTimePicker;
import org.vaadin.miki.superfields.numbers.SuperBigDecimalField;
import org.vaadin.miki.superfields.numbers.SuperDoubleField;
import org.vaadin.miki.superfields.numbers.SuperIntegerField;
import org.vaadin.miki.superfields.numbers.SuperLongField;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class SaisieTransactionBase<T extends Serializable> extends VerticalLayout   {
    protected Long noTransactionCourante;

    protected ArrayList<T> referenceBeanList;
    protected T currentBean;
    protected ArrayList<T> targetBeanList;
    protected T originalBean;
    protected String newComboValue;

    protected boolean isPermanentFieldReadOnly;
    protected boolean isContextualFieldReadOnly;

    private boolean isButtonAfficherVisible = true;
    protected Boolean isButtonOptionnel01Visible = false;
    protected Boolean isButtonOptionnel02Visible = false;
    private boolean isButtonImprimerVisible = false;

    protected boolean isImpressionAutomatique = false;
    protected boolean isRafraichissementAutomatique = false;
    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    protected ListDataProvider<T> dataProvider; 

    /* Defines a Binder to bind data to the FormLayout. */
    protected BeanValidationBinder<T> binder;
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the T.class, we define the type of object we are binding to. */

    protected Boolean isTransactionSaisieValidee = false;
    
    protected TransactionReportInput reportInput;
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
    protected Button btnAfficher;
    protected Button btnDetails;
    protected Button btnValider;
    protected Button btnRafraichir;
    protected Button btnOptionnel01;
    protected Button btnOptionnel02;
    protected Button btnImprimer;

    private String btnDetailsText = "Détails";

    protected String btnOptionnel01Text = "Optionnel01";
    protected String btnOptionnel02Text = "Optionnel02";
    protected Component btnOptionnel01Icon;
    protected Component btnOptionnel02Icon;
    
    //Paramètres de Personnalisation ProGenia
    protected final static String PANEL_FLEX_BASIS = "600px";
    protected final static String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    protected final static String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    protected final static String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    protected final static String DATEPICKER_LEFT_LABEL = "my-datepicker-left-label"; 
    protected final static String FORM_ITEM_LABEL_WIDTH250 = "250px";
    protected final static String FORM_ITEM_LABEL_WIDTH200 = "200px";
    protected final static String FORM_ITEM_LABEL_WIDTH150 = "150px";

    public SaisieTransactionBase() {
        //1 - Give the component a CSS class name to help with styling
        this.addClassName("transaction"); //Gives the component a CSS class name to help with styling.
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.

        this.setSizeFull(); //sets the size of the MainView Content
        this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
        this.setMargin(true); //sets the margin.            
        
        this.noTransactionCourante = 0L;
    }
    
    public void customSetButtonAfficherVisible(Boolean isVisible) {
        try 
        {
            this.isButtonAfficherVisible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonAfficherVisible", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonImprimerVisible(Boolean isVisible) {
        try 
        {
            this.isButtonImprimerVisible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonImprimerVisible", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customLoadNewBean() {
        try 
        {
            this.customClearInputFields();
   
            //this.binder.addStatusChangeListener(e -> this.btnValider.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            this.binder.addStatusChangeListener(event -> {
                //boolean isValid = event.getBinder().isValid();
                boolean hasChanges = event.getBinder().hasChanges();

                this.btnValider.setEnabled(hasChanges && this.workingCheckBeforeEnableValider());
                //this.btnValider.setEnabled(hasChanges && isValid);
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customLoadNewBean", e.toString());
            e.printStackTrace();
        }
    }

    public void customClearInputFields()
    {
        try 
        {
            this.isTransactionSaisieValidee = false;

            this.noTransactionCourante = 0L;
            this.currentBean = this.workingCreateNewBeanInstance();
            this.originalBean = this.currentBean; //Save a reference to the targetBean so we can save the form values back into it later.
            
            //This setter ensure to handle data once the fields are injected
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the targetBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */

            this.workingSetFieldsInitValues();
            this.workingExecuteOnCurrent();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customClearInputFields", e.toString());
            e.printStackTrace();
        }
    } //void customClearInputFields()
    
    public void customManageReadOnlyFieldMode()
    {
        try 
        {
            this.isPermanentFieldReadOnly = true;

            if (this.isTransactionSaisieValidee == false) {
                this.isContextualFieldReadOnly = false;
            }
            else {
                this.isContextualFieldReadOnly = true;
            } //if (this.isTransactionSaisieValidee == false) {
                
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //public void customManageReadOnlyFieldMode()

    protected T workingCreateNewBeanInstance()
    {
        //Must be ovverided
        T element;
        element = null; //Initialisation bidon
        return (element);
    }

    public void customSetNewComboValue(String newComboValue) {
        this.newComboValue = newComboValue;
    }

    public boolean customBinderHasChanges() {
        return this.binder.hasChanges();
    }    

    public void customSetReferenceBeanList(ArrayList<T> referenceBeanList) {
        this.referenceBeanList = referenceBeanList;
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
            
            if (this.isButtonAfficherVisible) {
                this.menu.addItem("Afficher", (e -> workingHandleAfficherClick(e)));
            }
            this.menu.addItem("Valider", (e -> handleValiderClick(e)));
            this.menu.addItem("Rafraîchir", (e -> handleRafraichirClick(e)));
            if (this.isButtonOptionnel01Visible) {
                this.menu.addItem(this.btnOptionnel01Text, (e -> workingHandleButtonOptionnel01Click(e)));
            }
            if (this.isButtonOptionnel02Visible) {
                this.menu.addItem(this.btnOptionnel02Text, (e -> workingHandleButtonOptionnel02Click(e)));
            }
            if (this.isButtonImprimerVisible) {
                this.menu.addItem("Imprimer", (e -> customHandleImprimerClick(e)));
            }
            
            Span title = new Span(" ");
            
            this.btnAfficher = new Button("Afficher", new Icon(VaadinIcon.FORM));
            this.btnAfficher.addClickListener(e -> workingHandleAfficherClick(e));
            this.btnAfficher.setVisible(this.isButtonAfficherVisible);

            this.btnDetails = new Button(this.btnDetailsText, new Icon(VaadinIcon.FILE_TABLE)); 
            this.btnDetails.addClickListener(e -> workingHandleDetailsClick(e));

            this.btnValider = new Button("Valider", new Icon(VaadinIcon.CHECK));
            this.btnValider.addClickListener(e -> handleValiderClick(e));

            this.btnRafraichir = new Button("Rafraîchir", new Icon(VaadinIcon.FILE_REFRESH));
            this.btnRafraichir.addClickListener(e -> handleRafraichirClick(e));
            
            this.btnOptionnel01 = new Button(this.btnOptionnel01Text);
            this.btnOptionnel01.addClickListener(e -> workingHandleButtonOptionnel01Click(e));
            this.btnOptionnel01.setVisible(this.isButtonOptionnel01Visible);
            if (this.btnOptionnel01Icon != null) {
                this.btnOptionnel01.setIcon(this.btnOptionnel01Icon);
            }
            
            this.btnOptionnel02 = new Button(this.btnOptionnel02Text);
            this.btnOptionnel02.addClickListener(e -> workingHandleButtonOptionnel02Click(e));
            this.btnOptionnel02.setVisible(this.isButtonOptionnel02Visible);
            if (this.btnOptionnel02Icon != null) {
                this.btnOptionnel02.setIcon(this.btnOptionnel02Icon);
            }

            this.btnImprimer = new Button(new Icon(VaadinIcon.PRINT)); 
            this.btnImprimer.addClickListener(e -> customHandleImprimerClick(e));
            this.btnImprimer.setVisible(this.isButtonImprimerVisible);
            
            this.topToolBar.add(title, this.btnAfficher, this.btnDetails, this.btnValider, this.btnRafraichir, 
                    this.btnOptionnel01, this.btnOptionnel02, this.btnImprimer, this.btnOverflow);
            setFlexGrow(1, title);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    private void handleValiderClick(ClickEvent event) {
        //Valider la transaction courante 

        try 
        {
            if (this.currentBean == null)
            {
                MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner ou saisir une transaction.");
            }
            else 
            {
                this.workingExecuteValider();
            } //if (this.currentBean == null)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.handleValiderClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleValiderClick(ClickEvent event) {
    
    public void customShowBeanExtraCheckValidationErrorMessage () {
        try 
        {
            //Afficher les erreurs
            BinderValidationStatus<T> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
            String errorText = validate.getFieldValidationStatuses()
                .stream().filter(BindingValidationStatus::isError)
                .map(BindingValidationStatus::getMessage)
                .map(Optional::get).distinct()
                .collect(Collectors.joining(", "));

            MessageDialogHelper.showWarningDialog("Validation de la Saisie de Transaction", errorText);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customShowBeanExtraCheckValidationErrorMessage ", e.toString());
            e.printStackTrace();
        }
    }

    public void customExecuteAfterValiderSucceed() {
        try 
        {
            if ((this.btnImprimer.isVisible() == true) && (this.isImpressionAutomatique == true)) {
                this.customExecJasperReport();
            } //if ((this.btnImprimer.isVisible() == true) && (this.isImpressionAutomatique == true)) {

            if ((this.btnRafraichir.isVisible() == true) && (this.isRafraichissementAutomatique == true)) {
                this.dataProvider.refreshAll();
                this.customClearInputFields();
            } //if ((this.btnRafraichir.isVisible() == true) && (this.isRafraichissementAutomatique == true)) {
            
            this.customManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customExecuteAfterValiderSucceed", e.toString());
            e.printStackTrace();
        }
    }

    private void handleRafraichirClick(ClickEvent event) {
        //Rafraichir la transaction courante 
        try 
        {
            if (this.currentBean == null)
            {
                MessageDialogHelper.showWarningDialog("Rafraîchissement de transaction", "Aucune transaction n'est sélectionnée. Veuillez d'abord sélectionner ou saisir une transaction.");
            }
            else
            {
                this.dataProvider.refreshAll();
                this.customClearInputFields();
                this.customManageToolBars();
                this.customManageReadOnlyFieldMode();
                this.workingConfigureReadOnlyField();
            } //if (this.currentBean == null)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.handleRafraichirClick", e.toString());
            e.printStackTrace();
        }
    } //private void handleRafraichirClick(ClickEvent event) {
    
    public void customSetButtonOptionnel01Visible(Boolean isVisible) {
        try 
        {
            this.isButtonOptionnel01Visible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel01Visible", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel02Visible(Boolean isVisible) {
        try 
        {
            this.isButtonOptionnel02Visible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel02Visible", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel01Text(String label) {
        try 
        {
            this.isButtonOptionnel01Visible = true;
            this.btnOptionnel01Text = label;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel01Text", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel02Text(String label) {
        try 
        {
            this.isButtonOptionnel02Visible = true;
            this.btnOptionnel02Text = label;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel02Text", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel01Icon(Component icon) {
        try 
        {
            this.btnOptionnel01Icon = icon;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel01Icon", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel02Icon(Component icon) {
        try 
        {
            this.btnOptionnel02Icon = icon;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonOptionnel02Icon", e.toString());
            e.printStackTrace();
        }
    }

    public void customSetValue(TextField textField, String value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperIntegerField textField, Integer value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperLongField textField, Long value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperDoubleField textField, Double value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperBigDecimalField textField, BigDecimal value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (textField.isReadOnly()) {
                textField.setReadOnly(false);
                textField.setValue(value);
                textField.setReadOnly(true);
            } else {
                textField.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetValue(SuperDatePicker datePicker, LocalDate value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (datePicker.isReadOnly()) {
                datePicker.setReadOnly(false);
                datePicker.setValue(value);
                datePicker.setReadOnly(true);
            } else {
                datePicker.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     

    public void customSetValue(SuperDateTimePicker dateTimePicker, LocalDateTime value) {
        //Programmatically set a TextField's value while in ReadOnly mode?
        try 
        {
            if (dateTimePicker.isReadOnly()) {
                dateTimePicker.setReadOnly(false);
                dateTimePicker.setValue(value);
                dateTimePicker.setReadOnly(true);
            } else {
                dateTimePicker.setValue(value);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    public void customSetButtonDetailsText(String label) {
        try 
        {
            this.btnDetailsText = label;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customSetButtonDetailsText", e.toString());
            e.printStackTrace();
        }
    }

    protected ArrayList<T> workingFetchItems() {
        return (new ArrayList<T> ());
    } //protected ArrayList<T> workingFetchItems() {
    
    protected void workingExecuteValider() {
        //execute Valider current Bean
    } //protected void workingExecuteValider() {

    protected void workingHandleAfficherClick(ClickEvent event) {
    } //protected void workingHandleAfficherClick() {
    
    protected void workingHandleDetailsClick(ClickEvent event) {
    } //protected void workingHandleDetailsClick() {
    
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
    } //protected void workingHandleButtonOptionnel01Click() {
    
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
    } //protected void workingHandleButtonOptionnel02Click() {
    
    protected void workingBeanDataAssemble()
    {
    }
            
    protected void workingExecuteOnCurrent() {
        //execute Before Display current Bean
    }

    protected Boolean workingCheckBeforeEnableValider() {
        //check Before Enable Valider Button
        return (true);
    }

    protected Boolean workingCheckBeforeEnableButtonOptionnel01() {
        //check Before Enable Optionnel01 Button
        return (true);
    }

    protected Boolean workingCheckBeforeEnableButtonOptionnel02() {
        //check Before Enable Optionnel02 Button
        return (true);
    }

    protected void customManageToolBars() {
    } //public void customManageToolBars() {
    
    protected void workingSetFieldsInitValues()
    {
    }
    
    protected void workingConfigureReadOnlyField()
    {
    }
    
    protected void customHandleImprimerClick(ClickEvent event) {
        try 
        {
            this.customExecJasperReport();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customHandleImprimerClick", e.toString());
            e.printStackTrace();
        }
    } //protected void customHandleImprimerClick() {
    
     protected void customExecJasperReport() {
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
            
            //i - Assembler les paramètres du rapport
            String denomination = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DENOMINATION());
            String nomVersion = MainView.getNomVersion();
            if (this.reportInput == null) {
                this.reportInput = ReportHelper.basicDataAssemble(this.reportTitle, denomination, nomVersion, initialesUtilisateur);
            }

            //ii - Compléter les paramètres issue du bean
            this.workingBeanDataAssemble();
            
            //iii - Générer le le StreamResource
            streamResource = ReportHelper.generateTransactionReportStreamResource(this.reportInput, this.reportName, jasperReport);
            
            //C - Ouvrir l'instance du Dialog ReportViewerDialog.
            ReportViewerDialog.getInstance().showDialog(streamResource); 
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.customExecJasperReport", e.toString());
            e.printStackTrace();
        }
    } //protected void customExecJasperReport() {
    
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
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.execEmbeddedPdfDocument", e.toString());
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
            MessageDialogHelper.showAlertDialog("SaisieTransactionBase.execPrintVaadinReport", e.toString());
            e.printStackTrace();
        }
    } //private void execPrintVaadinReport() {
}
