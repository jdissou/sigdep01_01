/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.parametre;

/*
import com.progenia.immaria01_01.securities.data.business.CentreIncubateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.CentreIncubateur;
*/
import com.progenia.immaria01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.immaria01_01.securities.data.entity.Utilisateur;
import com.progenia.immaria01_01.utilities.ApplicationConstanteHolder;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.progenia.immaria01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.dates.SuperDatePicker;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

/***
 * To enable navigating to the view, we annotate it with @Route. 
 * When no route is specified, the route will be the class name without “view”, in this case login.
 * Pour permettre la navigation vers la vue, nous l'annotons avec @Route. 
 * Lorsqu'aucune route n'est spécifiée, la route sera le nom de la classe sans «View», dans ce cas login.
 */

@Route(value = "preferences_utilisateur", layout = MainView.class)
@PageTitle(PreferenceUtilisateurView.PAGE_TITLE)
public class PreferenceUtilisateurView  extends VerticalLayout implements BeforeLeaveObserver {

    /**
     * The currently edited customer
     */

    /*
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CentreIncubateurBusiness centreIncubateurBusiness;
    private ArrayList<CentreIncubateur> centreIncubateurList = new ArrayList<CentreIncubateur>();
    private ListDataProvider<CentreIncubateur> centreIncubateurDataProvider; 
    */
    
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    
    private Utilisateur currentBean;
    private Utilisateur originalBean;
    private String viewTitle;
    private ModeFormulaireEnum modeFormulaire;

    private boolean isPermanentFieldReadOnly;
    private boolean isPrimaryKeyFieldReadOnly;
    private boolean isContextualFieldReadOnly;

    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    private FormLayout formLayout = new FormLayout();
    private BeanValidationBinder<Utilisateur> binder = new BeanValidationBinder<>(Utilisateur.class);
    /* BeanValidationBinder is a Binder that is aware of bean validation annotations. 
    By passing it in the Utilisateur.class, we define the type of object we are binding to. */
    //private Binder<Utilisateur> binder = new Binder<>(Utilisateur.class);
 
    /* Fields to edit properties in Utilisateur entity */
    private Checkbox chkRafraichissementAutomatique = new Checkbox();
    private Checkbox chkImpressionAutomatique = new Checkbox();
    private SuperDatePicker datDateDebutPlage = new SuperDatePicker();
    private SuperDatePicker datDateFinPlage = new SuperDatePicker();
    
    /*
    //private SuperTextField txtDenomination = new SuperTextField();
    ComboBox<CentreIncubateur> cboCodeCentreIncubateur = new ComboBox<>();
    */

    /* Action buttons */
    private Button btnModifier;
    private Button btnSauvegarder;
    private Button btnAbandonner;
    private Label lblInfoLabel;
    private HorizontalLayout topToolBar;
    private HorizontalLayout bottomToolBar;

    //Paramètres de Personnalisation ProGenia
    final static String PAGE_TITLE = "Préférences Utilisateur";
    final static String PANEL_FLEX_BASIS = "600px";
    final static String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    final static String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    final static String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    final static String DATEPICKER_LEFT_LABEL = "my-datepicker-left-label"; //TO DO
    final static String FORM_ITEM_LABEL_WIDTH250 = "250px";
    
    private String strNomFormulaire;
    private Boolean isAllowInsertItem;
    private Boolean isAllowEditItem;
    private Boolean isAllowDeleteItem;

    /***
     * It’s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommandé de différer l’initialisation du composant jusqu’à ce qu’il soit connecté. 
     * Cela évite d'exécuter du code potentiellement coûteux pour un composant qui n'est jamais affiché à l'utilisateur.
     */
    
    /***
     * We can do the initialization by overriding the onAttach method. 
     * To only run it once, we check if this is the first attach event, 
     * using the AttachEvent#isInitialAttach method.
     * Nous pouvons effectuer l'initialisation en redéfinissant la méthode onAttach. 
     * Pour ne l'exécuter qu'une seule fois, nous vérifions s'il s'agit du premier événement d'attachement, 
     * à l'aide de la méthode AttachEvent # isInitialAttach.
     */

    @Override
    public void onAttach(AttachEvent event) {
        try 
        {
            if (event.isInitialAttach()) {
                this.initialize();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.onAttach", e.toString());
            e.printStackTrace();
        }
    }
    
    /***
     * We can then create the initialization method, where we instantiate the PreferenceUtilisateurView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.viewTitle = "Préférences Utilisateur";
            
            this.strNomFormulaire = "PreferenceUtilisateurView";
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;
            this.modeFormulaire = ModeFormulaireEnum.CONSULTATION; //Mode Consultation à l'ouverture

            //2- CIF
            /*
            this.centreIncubateurList = (ArrayList)this.centreIncubateurBusiness.findAll();
            this.centreIncubateurDataProvider = DataProvider.ofCollection(this.centreIncubateurList);
            // Make the dataProvider sorted by LibelleCentreIncubateur in ascending order
            this.centreIncubateurDataProvider.setSortOrder(CentreIncubateur::getLibelleCentreIncubateur, SortDirection.ASCENDING);
            */
            
            //3- Setup ReadOnly Field Mode
            this.customManageReadOnlyFieldMode();
                          
            //4 - Gives the component a CSS class name to help with styling
            this.addClassName("form-view"); //Gives the component a CSS class name to help with styling.

            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            

            //5 - Setup the bottom toolbar
            this.workingSetupToolBars();
            
            //6 - Setup the form
            this.configureComponents(); 
            
            //7- Adds the bottom toolbar and the form to the layout
            this.add(this.topToolBar, this.formLayout, this.bottomToolBar);

            //8- Activation de la barre d'outils
            this.workingManageToolBars();

            //9- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée après l'exécution de this.configureComponents()
            this.setCurrentBean();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.initialize", e.toString());
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
            this.chkRafraichissementAutomatique.setReadOnly(this.isContextualFieldReadOnly);
            
            this.chkImpressionAutomatique.setReadOnly(this.isContextualFieldReadOnly);

            this.datDateDebutPlage.setWidth(150, Unit.PIXELS);
            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateDebutPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateDebutPlage.setLocale(Locale.FRENCH);

            this.datDateFinPlage.setWidth(150, Unit.PIXELS);
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly); 
            this.datDateFinPlage.addClassName(DATEPICKER_LEFT_LABEL);
            this.datDateFinPlage.setLocale(Locale.FRENCH);

            /*
            this.txtDenomination.setWidth(400, Unit.PIXELS);
            this.txtDenomination.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtDenomination.addClassName(TEXTFIELD_LEFT_LABEL);
            */
            /*
            this.cboCodeCentreIncubateur.setWidth(400, Unit.PIXELS);
            this.cboCodeCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCentreIncubateur.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeCentreIncubateur.setDataProvider(this.centreIncubateurDataProvider);
            //this.cboCodeCentreIncubateur.setItems(this.centreIncubateurList);
            // Choose which property from CentreIncubateur is the presentation value
            this.cboCodeCentreIncubateur.setItemLabelGenerator(CentreIncubateur::getLibelleCentreIncubateur);
            this.cboCodeCentreIncubateur.setRequired(true);
            this.cboCodeCentreIncubateur.setRequiredIndicatorVisible(true);
            */
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            this.binder.forField(this.chkRafraichissementAutomatique)
                .bind(Utilisateur::getRafraichissementAutomatique, Utilisateur::setRafraichissementAutomatique); 
            
            this.binder.forField(this.chkImpressionAutomatique)
                .bind(Utilisateur::getImpressionAutomatique, Utilisateur::setImpressionAutomatique); 
            
            Label lblDateDebutPlageValidationStatus = new Label();
            this.binder.forField(this.datDateDebutPlage)
                .withValidationStatusHandler(status -> {lblDateDebutPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateDebutPlageValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getDateDebutPlage, Utilisateur::setDateDebutPlage); 
            
            Label lblDateFinPlageValidationStatus = new Label();
            // Store DateFinPlage binding so we can revalidate it later
            Binder.Binding<Utilisateur, LocalDate> dateFinPlageBinding =this.binder.forField(this.datDateFinPlage)
                .withValidator(dateFinPlage -> !(dateFinPlage.isBefore(this.datDateDebutPlage.getValue())), "La date de Fin de plage ne peut précéder la date de début de plage.")
                .withValidationStatusHandler(status -> {lblDateFinPlageValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDateFinPlageValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getDateFinPlage, Utilisateur::setDateFinPlage); 
            
            // Revalidate DateFinPlage when DateDebutPlage changes
            this.datDateDebutPlage.addValueChangeListener(event -> dateFinPlageBinding.validate());

            /*
            Label lblDenominationValidationStatus = new Label();
            this.binder.forField(this.txtDenomination)
                .withValidator(text -> text.length() <= 6, "Dénomination ne peut contenir au plus 6 caractères.")
                .withValidationStatusHandler(status -> {lblDenominationValidationStatus.setText(status.getMessage().orElse(""));       
                         lblDenominationValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getDenomination, Utilisateur::setDenomination); 
            */
            
            /*
            Label lblCentreIncubateurValidationStatus = new Label();
            this.binder.forField(this.cboCodeCentreIncubateur)
                .asRequired("La Saisie du Centre Incubateur est requise. Veuillez sélectionner un Centre Incubateur")
                .bind(Utilisateur::getCentreIncubateur, Utilisateur::setCentreIncubateur); 
            */
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Utilisateur and PreferenceUtilisateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.formLayout.addFormItem(this.chkRafraichissementAutomatique, "Rafraîchissement Automatique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.chkImpressionAutomatique, "Impression Automatique :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.datDateDebutPlage, "Date Début Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.datDateFinPlage, "Date Fin Plage :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            //this.formLayout.addFormItem(this.txtDenomination, "Dénomination :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            /*
            this.formLayout.addFormItem(this.cboCodeCentreIncubateur, "CentreIncubateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            */
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    public void setCurrentBean() {
        //Setup the bean that contains the item components and configure the binder
        try 
        {
            //1 - Set up Current Bean
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.currentBean = utilisateurOptional.get();
            }
            else {
                this.currentBean = new Utilisateur();
            }
            this.originalBean = this.currentBean; //Save a reference to the currentBean so we can save the form values back into it later.
            
            //2 - Biend bind the values from the currentBean to the UI fields. 
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the currentBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.setCurrentBean", e.toString());
            e.printStackTrace();
        }
    }

    public void handleModifierClick(ClickEvent event) {
        try 
        {
            this.modeFormulaire = ModeFormulaireEnum.SAISIE ;
            this.customManageReadOnlyFieldMode();
            this.reConfigureReadOnlyField();
            this.workingManageToolBars();
            this.lblInfoLabel.setText("");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.handleModifierClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleModifierClick() {
    
    public void customHandleSauvegarderClick(ClickEvent event) {
        try 
        {
            //Validate and Write the form contents back to the original contact.
            if (binder.writeBeanIfValid(this.currentBean)) {
                this.utilisateurBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                this.lblInfoLabel.setText("");
                this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                this.customManageReadOnlyFieldMode();
                this.reConfigureReadOnlyField();
                this.workingManageToolBars();
            } 
            else {
                BinderValidationStatus<Utilisateur> validate = this.binder.validate(); //The validate() call ensures that bean-level validators are checked when saving automatically
                String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
                this.lblInfoLabel.setText("Il y a des erreurs : " + errorText);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.customHandleSauvegarderClick", e.toString());
            e.printStackTrace();
        }
    } //public void customHandleSauvegarderClick() {
    
    public void handleAbandonnerClick(ClickEvent event) {
        try 
        {
            // clear fields by setting to this.originalBean
            binder.readBean(this.originalBean);
            this.lblInfoLabel.setText("");

            this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
            this.customManageReadOnlyFieldMode();
            this.reConfigureReadOnlyField();
            this.workingManageToolBars();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.handleAbandonnerClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleAbandonnerClick() {
    
    private void workingSetupToolBars() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.topToolBar = new HorizontalLayout();
            this.bottomToolBar = new HorizontalLayout();

            //this.topToolBar.getThemeList().set("dark", true); //Thème
            this.topToolBar.addClassName("fichier-editer-toolbar");
            this.bottomToolBar.addClassName("fichier-editer-toolbar");
            
            this.topToolBar.setWidthFull();
            this.topToolBar.setSpacing(false);
            
            this.bottomToolBar.setWidthFull();
            this.bottomToolBar.setSpacing(false);

            this.topToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.topToolBar.setPadding(true);

            this.bottomToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.bottomToolBar.setPadding(true);

            //this.btnModifier = new Button("Modifier"); 
            this.btnModifier = new Button("Modifier", new Icon(VaadinIcon.EDIT)); 
            this.btnModifier.getStyle().set("marginRight", "10px");
            this.btnModifier.addClickListener(e -> handleModifierClick(e));

            //this.btnSauvegarder = new Button("Sauvegarder"); 
            this.btnSauvegarder = new Button("Enregistrer", new Icon(VaadinIcon.DOWNLOAD)); 
            //this.btnSauvegarder.getStyle().set("marginRight", "10px");
            this.btnSauvegarder.addClickListener(e -> customHandleSauvegarderClick(e));
            
            //this.btnAbandonner = new Button("Abandonner");
            this.btnAbandonner = new Button("Abandonner", new Icon(VaadinIcon.REPLY));
            //this.btnAbandonner.getStyle().set("marginRight", "10px");
            this.btnAbandonner.addClickListener(e -> handleAbandonnerClick(e));

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.topToolBar.add(this.btnModifier);
            this.bottomToolBar.add(this.btnSauvegarder, this.btnAbandonner, this.lblInfoLabel);
   
            //this.binder.addStatusChangeListener(e -> this.btnSauvegarder.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            this.binder.addStatusChangeListener(event -> {
                //boolean isValid = event.getBinder().isValid();
                boolean hasChanges = event.getBinder().hasChanges();

                this.btnSauvegarder.setEnabled(hasChanges);
                //this.btnSauvegarder.setEnabled(hasChanges && isValid);
            });
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.workingSetupToolBars", e.toString());
            e.printStackTrace();
        }
    }

    private void customManageReadOnlyFieldMode()
    {
        try 
        {
            switch (this.modeFormulaire) {
                case SAISIE:
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = false;
                        this.isContextualFieldReadOnly = false;
                    }    
                    break;
                case CONSULTATION:   
                    {
                        this.isPermanentFieldReadOnly = true;
                        this.isPrimaryKeyFieldReadOnly = true;
                        this.isContextualFieldReadOnly = true;
                    }    
                    break;
            } //switch (modeFormulaire) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.customManageReadOnlyFieldMode", e.toString());
            e.printStackTrace();
        }
    } //private void customManageReadOnlyFieldMode()

    private void reConfigureReadOnlyField()
    {
        try 
        {
            this.chkRafraichissementAutomatique.setReadOnly(this.isContextualFieldReadOnly);
            this.chkImpressionAutomatique.setReadOnly(this.isContextualFieldReadOnly);
            this.datDateDebutPlage.setReadOnly(this.isContextualFieldReadOnly);
            this.datDateFinPlage.setReadOnly(this.isContextualFieldReadOnly);
            //this.txtDenomination.setReadOnly(this.isContextualFieldReadOnly);
            /*
            this.cboCodeCentreIncubateur.setReadOnly(this.isContextualFieldReadOnly);
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.reConfigureReadOnlyField", e.toString());
            e.printStackTrace();
        }
    } //private void reConfigureReadOnlyField()

    private void workingManageToolBars()
    {
        try 
        {
            switch (this.modeFormulaire) {
                case SAISIE:
                    {
                        this.btnModifier.setVisible(false);
                        this.btnSauvegarder.setVisible(true);
                        this.btnAbandonner.setVisible(true);
                    }    
                    break;
                case CONSULTATION:   
                    {
                        this.btnModifier.setVisible(true);
                        this.btnSauvegarder.setVisible(false);
                        this.btnAbandonner.setVisible(false);
                    }    
                    break;
            } //switch (modeFormulaire) {
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("PreferenceUtilisateurView.workingManageToolBars", e.toString());
            e.printStackTrace();
        }
    } //private void workingManageToolBars()

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if (this.hasChanges()) {
            ContinueNavigationAction action = event.postpone();

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> noClickListener = ev -> {
                //Ne pas enregistrer avant de quitter cette page
                this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                this.customManageReadOnlyFieldMode();
                this.reConfigureReadOnlyField();
                this.workingManageToolBars();

                //Proceed Page Leave Action
                action.proceed();
            };

            //Java Lambda Implementation of ComponentEventListener<ClickEvent<Button>>
            ComponentEventListener<ClickEvent<Button>> yesClickListener = ev -> {
                //Enregistrer avant de quitter cette page
                if (binder.writeBeanIfValid(this.currentBean)) {
                    this.utilisateurBusiness.save(this.currentBean); //Sauvegarder la modification dans le backend
                    this.lblInfoLabel.setText("");
                    this.modeFormulaire = ModeFormulaireEnum.CONSULTATION ;
                    this.customManageReadOnlyFieldMode();
                    this.reConfigureReadOnlyField();
                    this.workingManageToolBars();

                    //Proceed Page Leave Action
                    action.proceed();
                } 
            };

            MessageDialogHelper.showYesNoDialog("Vous n'avez pas enregistré la saisie en cours.", "Désirez-vous enregistrer la saisie avant de quitter cette page?. Cliquez sur Oui pour enregistrer avant de quitter cette page.", yesClickListener, noClickListener);
        } //if (this.hasChanges()) {
    }
   
    private boolean hasChanges() {
        return this.binder.hasChanges();
    }    
    
    private enum ModeFormulaireEnum {
        SAISIE, CONSULTATION
    }
}
