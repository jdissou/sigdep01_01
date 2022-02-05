/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.views;

import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurCodeSecretBusiness;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.data.entity.UtilisateurCodeSecret;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.miki.superfields.text.SuperTextField;

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

@Route(value = "changement_code_secret", layout = MainView.class)
@PageTitle(ChangementCodeSecretView.PAGE_TITLE)
public class ChangementCodeSecretView  extends VerticalLayout {
//public class ChangementCodeSecretView  extends VerticalLayout implements BeforeLeaveObserver {

    /**
     * The currently edited customer
     */

    /*
    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private CompanyBusiness companyBusiness;
    private ArrayList<Company> companyList = new ArrayList<Company>();
    private ListDataProvider<Company> companyDataProvider; 
    */
    
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    
    @Autowired
    private UtilisateurCodeSecretBusiness utilisateurCodeSecretBusiness;
    private ArrayList<UtilisateurCodeSecret> utilisateurCodeSecretList = new ArrayList<UtilisateurCodeSecret>();

    private Utilisateur currentBean;
    private String viewTitle;

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
    private SuperTextField txtLibelleUtilisateur = new SuperTextField();
    private PasswordField txtActuelCodeSecret = new PasswordField();
    private PasswordField txtNouveauCodeSecret = new PasswordField();
    private PasswordField txtConfirmationCodeSecret = new PasswordField();
    //ComboBox<Company> cboCodeCompany = new ComboBox<>();

    /* Action buttons */
    private Button btnValider;
    private Button btnAbandonner;
    private Label lblInfoLabel;
    private HorizontalLayout bottomToolBar;

    //Paramètres de Personnalisation ProGenia
    static final String PAGE_TITLE = "Change de Code Secret";
    static final String PANEL_FLEX_BASIS = "600px";
    static final String TEXTFIELD_LEFT_LABEL = "my-textfield-left-label";
    static final String TEXTFIELD_CENTER_ALIGN = "my-textfield-center-align";
    static final String COMBOBOX_LEFT_LABEL = "my-combobox-left-label";
    static final String FORM_ITEM_LABEL_WIDTH250 = "200px";
    
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
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.onAttach", e.toString());
            e.printStackTrace();
        }
    }
    
    /***
     * We can then create the initialization method, where we instantiate the ChangementCodeSecretView. 
     */
    private void initialize() {
        try 
        {
            //1- Mise à jour des propriétés du formulaire
            this.viewTitle = "Changement de Code Secret";
            
            this.strNomFormulaire = "ChangementCodeSecretView";
            this.isAllowInsertItem = false;
            this.isAllowEditItem = true;
            this.isAllowDeleteItem = false;

            //2- CIF
            //Néant
            /* 
            this.companyList = (ArrayList)this.companyBusiness.findAll();
            this.companyDataProvider = DataProvider.ofCollection(this.companyList);
            // Make the dataProvider sorted by LibelleCompany in ascending order
            this.companyDataProvider.setSortOrder(Company::getLibelleCompany, SortDirection.ASCENDING);
            */
            
            //3- Setup ReadOnly Field Mode
            this.isPermanentFieldReadOnly = true;
            this.isPrimaryKeyFieldReadOnly = true;
            this.isContextualFieldReadOnly = false;
                          
            //4 - Gives the component a CSS class name to help with styling
            this.addClassName("form-view"); //Gives the component a CSS class name to help with styling.

            this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.
            
            this.setSizeFull(); //sets the size of the MainView Content
            this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
            this.setMargin(true); //sets the margin.            

            //5 - Setup the bottom toolbar
            this.setupBottomToolBar();
            
            //6 - Setup the form
            this.configureComponents(); 
            
            //7- Adds the bottom toolbar and the form to the layout
            this.add(this.formLayout, this.bottomToolBar);        

            //8- Set up Target Bean - TargetBeanSet : cette instruction doit être exécutée après l'exécution de this.configureComponents()
            this.setCurrentBean();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.initialize", e.toString());
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
            this.txtLibelleUtilisateur.setWidth(400, Unit.PIXELS);
            this.txtLibelleUtilisateur.setReadOnly(this.isPermanentFieldReadOnly); 
            this.txtLibelleUtilisateur.addClassName(TEXTFIELD_LEFT_LABEL);
            
            this.txtActuelCodeSecret.setWidth(400, Unit.PIXELS);
            this.txtActuelCodeSecret.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtActuelCodeSecret.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtActuelCodeSecret.setRequired(true);
            this.txtActuelCodeSecret.setRequiredIndicatorVisible(true);

            this.txtNouveauCodeSecret.setWidth(400, Unit.PIXELS);
            this.txtNouveauCodeSecret.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtNouveauCodeSecret.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtNouveauCodeSecret.setRequired(true);
            this.txtNouveauCodeSecret.setRequiredIndicatorVisible(true);

            this.txtConfirmationCodeSecret.setWidth(400, Unit.PIXELS);
            this.txtConfirmationCodeSecret.setReadOnly(this.isContextualFieldReadOnly); 
            this.txtConfirmationCodeSecret.addClassName(TEXTFIELD_LEFT_LABEL);
            this.txtConfirmationCodeSecret.setRequired(true);
            this.txtConfirmationCodeSecret.setRequiredIndicatorVisible(true);
            
            /*
            this.cboCodeCompany.setWidth(400, Unit.PIXELS);
            this.cboCodeCompany.setReadOnly(this.isContextualFieldReadOnly); 
            this.cboCodeCompany.addClassName(COMBOBOX_LEFT_LABEL);
            this.cboCodeCompany.setDataProvider(this.companyDataProvider);
            //this.cboCodeCompany.setItems(this.companyList);
            // Choose which property from Company is the presentation value
            this.cboCodeCompany.setItemLabelGenerator(Company::getLibelleCompany);
            this.cboCodeCompany.setRequired(true);
            this.cboCodeCompany.setRequiredIndicatorVisible(true);
            */
            
            //3 - Bind Fields instances to use (Manual Data Binding)
            // Easily bind forms to beans and manage validation and buffering
            //To bind a component to read-only data, use a null value for the setter.
            Label lblLibelleUtilisateurValidationStatus = new Label();
            this.binder.forField(this.txtLibelleUtilisateur)
                .withValidator(text -> text.length() <= 50, "Nom Utilisateur ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblLibelleUtilisateurValidationStatus.setText(status.getMessage().orElse(""));       
                         lblLibelleUtilisateurValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getLibelleUtilisateur, Utilisateur::setLibelleUtilisateur); 
            
            /* Pas de binding
            Label lblActuelCodeSecretValidationStatus = new Label();
            this.binder.forField(this.txtActuelCodeSecret)
                .withValidator(text -> text.length() <= 50, "Code Secret ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblActuelCodeSecretValidationStatus.setText(status.getMessage().orElse(""));       
                         lblActuelCodeSecretValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getActuelCodeSecret, Utilisateur::setActuelCodeSecret); 
            */
            
            /* Pas de binding
            Label lblNouveauCodeSecretValidationStatus = new Label();
            this.binder.forField(this.txtNouveauCodeSecret)
                .withValidator(text -> text.length() <= 50, "Code Secret ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblNouveauCodeSecretValidationStatus.setText(status.getMessage().orElse(""));       
                         lblNouveauCodeSecretValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getNouveauCodeSecret, Utilisateur::setNouveauCodeSecret); 
            */
            
            /* Pas de binding
            Label lblConfirmationCodeSecretValidationStatus = new Label();
            this.binder.forField(this.txtConfirmationCodeSecret)
                .withValidator(text -> text.length() <= 50, "Code Secret ne peut contenir au plus 50 caractères.")
                .withValidationStatusHandler(status -> {lblConfirmationCodeSecretValidationStatus.setText(status.getMessage().orElse(""));       
                         lblConfirmationCodeSecretValidationStatus.setVisible(status.isError());})
                .bind(Utilisateur::getConfirmationCodeSecret, Utilisateur::setConfirmationCodeSecret); 
            */
            
            /*
            Label lblCompanyValidationStatus = new Label();
            binder.forField(this.cboCodeCompany)
                .asRequired("La Saisie de l'Company est requise. Veuillez sélectionner un Company")
                .bind(Utilisateur::getCompany, Utilisateur::setCompany); 
            */
            
            /* 3 - Alternative : Bind Fields instances that need validators manually and then bind all remaining fields using the bindInstanceFields method
            this.binder.bindInstanceFields(this.formLayout); //Automatic Data Binding
            //bindInstanceFields matches fields in Utilisateur and UtilisateurView based on their names.
            */

            //4 - Add input fields to formLayout - We don't use .setLabel since we will use addFormItem instead of add to add items to the form - addFormItem allows us to set SuperTextField with on a FormaLayout when add doesn't
            this.formLayout.addFormItem(this.txtLibelleUtilisateur, "Utilisateur :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtActuelCodeSecret, "Code Secret Actuel :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtNouveauCodeSecret, "Nouveau Code Secret :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            this.formLayout.addFormItem(this.txtConfirmationCodeSecret, "Confirmation du Code Secret :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            //this.formLayout.addFormItem(this.cboCodeCompany, "Company :").getStyle().set("--vaadin-form-item-label-width", FORM_ITEM_LABEL_WIDTH250);
            
            //5 - Making the Layout Responsive : Custom responsive layouting
            //breakpoint at 600px, with the label to the side. At resolutions lower than 600px, the label will be at the top. In both cases there is only 1 column.
            this.formLayout.setResponsiveSteps(new ResponsiveStep("0", 1, LabelsPosition.TOP), 
                    new ResponsiveStep(PANEL_FLEX_BASIS, 1, LabelsPosition.ASIDE));
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.configureComponents", e.toString());
            e.printStackTrace();
        }
    }    

    public void setCurrentBean() {
        //Setup the bean that contains the item components and configure the binder
        try 
        {
            String strCodeUtilisateurCourant = (String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE());

            this.utilisateurCodeSecretList = (ArrayList)this.utilisateurCodeSecretBusiness.findByUtilisateurCodeSecretIdCodeUtilisateur(strCodeUtilisateurCourant);

            //1 - Set up Current Bean
            Optional<Utilisateur> utilisateurOptional = this.utilisateurBusiness.findById((String)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE()));
            if (utilisateurOptional.isPresent()) {
                this.currentBean = utilisateurOptional.get();
            }
            else {
                this.currentBean = new Utilisateur();
            }
            
            //2 - Biend bind the values from the currentBean to the UI fields. 
            this.binder.readBean(this.currentBean); //use the readBean method to Manually read values from a business object instance into the UI components
            /* Calls binder.readBean to bind the values from the currentBean to the UI fields. 
            readBean copies the values from the Bean to an internal model, that way we don’t overwrite values if we cancel editing. */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.setCurrentBean", e.toString());
            e.printStackTrace();
        }
    }

    private boolean isFormInputFieldsValidated()
    {
        //TEST à effectuer avant la mise à jour
        //Vérification de la validité de l'enregistrement courant
        Boolean blnCheckOk = false;

        try
        {
            //txtActuelCodeSecret, txtNouveauCodeSecret et txtConfirmationCodeSecret sont contrôlés ici parce qu'ils ne sont pas liés au binder
            if (StringUtils.isBlank(this.txtActuelCodeSecret.getValue()) == true)
            {
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de l'Actuel Code Secret est Obligatoire. Veuillez saisir l'Actuel Code Secret.");
                this.txtActuelCodeSecret.focus();
            }
            else if (StringUtils.isBlank(this.txtNouveauCodeSecret.getValue()) == true)
            {
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie du Nouveau Code Secret est Obligatoire. Veuillez saisir l'Actuel Code Secret.");
                this.txtNouveauCodeSecret.focus();
            }
            else if (StringUtils.isBlank(this.txtConfirmationCodeSecret.getValue()) == true)
            {
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "La Saisie de la Confirmation du Nouveau Code Secret est Obligatoire. Veuillez saisir la Confirmation du Nouveau Code Secret.");
                this.txtConfirmationCodeSecret.focus();
            }
            else if (this.currentBean.getCodeSecret().equals(this.txtActuelCodeSecret.getValue()) == false) //Ce code est utilisé pour vérifier la concordance (Case sensibility) exacte du login entré
            {
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "L'Actuel Code Secret est erroné. Veuillez entrer correctement l'Actuel Code Secret.");
                this.txtActuelCodeSecret.focus();
            }
            else if (this.txtNouveauCodeSecret.getValue().equals(this.txtConfirmationCodeSecret.getValue()) == false) //Ce code est utilisé pour vérifier la concordance (Case sensibility) exacte du login entré
            {
                blnCheckOk = false;
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Nouveau Code Secret est différent de sa Confirmation. Veuillez entrer correctement le Nouveau Code Secret, puis confirmer à l'identique ce nouveau Code Secret.");
                this.txtConfirmationCodeSecret.focus();
            }
            else if (this.utilisateurCodeSecretList.stream()
                    .anyMatch(p -> (p.getUtilisateurCodeSecretId().getCodeSecret()
                            .equals(this.txtNouveauCodeSecret.getValue())))) {
                blnCheckOk = false;
                this.txtNouveauCodeSecret.focus();
                MessageDialogHelper.showWarningDialog("Erreur de Saisie", "Le Nouveau Code Secret a été utilisé une fois déjà. Veuillez entrer un Nouveau Code Secret, puis confirmer à l'identique ce nouveau Code Secret.");
            }
            else
                blnCheckOk = true;
        }
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.isFormInputFieldsValidated", e.toString());
            e.printStackTrace();
        }

        return (blnCheckOk);
    }//private boolean isFormInputFieldsValidated()
    
    private void execUpdateCodeSecret()
    {
        try 
        {
            //1 - Get PeriodiciteChangementCodeSecret
            int intPeriodiciteChangementCodeSecret = ((Utilisateur)VaadinSession.getCurrent()
                    .getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE()))
                    .getCategorieUtilisateur().getPeriodiciteChangementCodeSecret();

            LocalDate datDateExpirationCodeSecret = LocalDate.now().plusDays(intPeriodiciteChangementCodeSecret);
            
            
            //2 - Update CodeSecret, DateSaisieCodeSecret and DateExpiration of currentBean
            this.currentBean.setCodeSecret(this.txtNouveauCodeSecret.getValue());
            this.currentBean.setDateSaisieCodeSecret(LocalDate.now());
            this.currentBean.setDateExpirationCodeSecret(datDateExpirationCodeSecret);

            //3 - Save currentBean to the backend
            this.utilisateurBusiness.save(this.currentBean);
            
            //4 - Keep trace of new CodeSecret Save it to the backend
            UtilisateurCodeSecret utilisateurCodeSecret  = new UtilisateurCodeSecret(this.currentBean);
            utilisateurCodeSecret.setCodeSecret(this.txtNouveauCodeSecret.getValue());
            
            utilisateurCodeSecret.setDateSaisieCodeSecret(LocalDate.now());
            utilisateurCodeSecret.setDateExpirationCodeSecret(datDateExpirationCodeSecret);
            this.utilisateurCodeSecretBusiness.save(utilisateurCodeSecret);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.execUpdateCodeSecret", e.toString());
            e.printStackTrace();
        }
    } //private void execUpdateCodeSecret()

    
public void handleValiderClick(ClickEvent event) {
        try 
        {
            //Validate and Write the form contents back to the original contact.
            if (isFormInputFieldsValidated() && binder.writeBeanIfValid(this.currentBean)) {
                this.lblInfoLabel.setText("");
                this.execUpdateCodeSecret();
                UI.getCurrent().navigate(SecurityService.getInstance().getDefaultView());
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
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.handleValiderClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleValiderClick() {
    
    public void handleAbandonnerClick(ClickEvent event) {
        try 
        {
            this.lblInfoLabel.setText("");

            UI.getCurrent().navigate(SecurityService.getInstance().getDefaultView());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.handleAbandonnerClick", e.toString());
            e.printStackTrace();
        }
    } //public void handleAbandonnerClick() {
    
    private void setupBottomToolBar() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            this.bottomToolBar = new HorizontalLayout();
            
            //this.bottomToolBar.getThemeList().set("dark", true); //Thème
            this.bottomToolBar.addClassName("fichier-editer-toolbar");
            
            this.bottomToolBar.setWidthFull();
            this.bottomToolBar.setSpacing(false);

            this.bottomToolBar.setAlignItems(FlexComponent.Alignment.CENTER);
            //this.setAlignItems(Alignment.CENTER);
            this.bottomToolBar.setPadding(true);

            //this.btnValider = new Button("Valider"); 
            this.btnValider = new Button("Valider", new Icon(VaadinIcon.DOWNLOAD)); 
            //this.btnValider.getStyle().set("marginRight", "10px");
            this.btnValider.addClickListener(e -> handleValiderClick(e));
            this.btnValider.setVisible(true);
            
            //this.btnAbandonner = new Button("Abandonner");
            this.btnAbandonner = new Button("Abandonner", new Icon(VaadinIcon.REPLY));
            //this.btnAbandonner.getStyle().set("marginRight", "10px");
            this.btnAbandonner.addClickListener(e -> handleAbandonnerClick(e));
            this.btnAbandonner.setVisible(true);

            this.lblInfoLabel = new Label();
            this.lblInfoLabel.getStyle().set("marginLeft", "10px");
            
            this.bottomToolBar.add(this.btnValider, this.btnAbandonner, this.lblInfoLabel);
   
            //this.binder.addStatusChangeListener(e -> this.btnValider.setEnabled(binder.isValid())); //Validates the form every time it changes. If it is invalid, it disables the save button to avoid invalid submissions.
            /*
            this.binder.addStatusChangeListener(event -> {
                //boolean isValid = event.getBinder().isValid();
                boolean hasChanges = event.getBinder().hasChanges();

                this.btnSauvegarder.setEnabled(hasChanges);
                //this.btnSauvegarder.setEnabled(hasChanges && isValid);
            });
            */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("ChangementCodeSecretView.setupBottomToolBar", e.toString());
            e.printStackTrace();
        }
    }
}
