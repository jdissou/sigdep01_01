/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.securities.views;

import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.data.entity.Utilisateur;
import com.progenia.incubatis01_03.securities.data.business.UtilisateurBusiness;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
@Route("login")
@CssImport("./styles/login-view.css")
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    
    @Autowired
    private UtilisateurBusiness utilisateurBusiness;
    
    Utilisateur utilisateur;
    List<Utilisateur> utilisateurList = new ArrayList();

    LocalDate datDateExpirationCodeSecret = LocalDate.now();
    String strLogin = "";
    String strCodeSecret = "";
    Boolean blnisInactif = true;

    
    VerticalLayout loginWrapper;

    H2 lblNomApplication;
    H4 lblDescriptionApplication;        
    
    H3 lblTitreConnexion;
    TextField txtLogin;
    PasswordField txtCodeSecret;
    Button loginButton;
    
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
                initialize();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LoginView.onAttach", e.toString());
            e.printStackTrace();
        }
    } //public void onAttach(AttachEvent event) {
    
    /***
     * We can then create the initialization method, where we instantiate the login form. 
     * We align the form in the center, both horizontally and vertically, 
     * and then hide the Forgot txtCodeSecret? option from the login form before adding it to the layout.
     * Nous pouvons ensuite créer la méthode d'initialisation, 
     * où nous instancions le formulaire de connexion. 
     * Nous alignons le formulaire au centre, à la fois horizontalement et verticalement, 
     * puis masquons le mot de passe oublié? option du formulaire de connexion 
     * avant de l'ajouter à la mise en page.
     */
    private void initialize() {
        try 
        {
            this.setAlignItems(FlexComponent.Alignment.CENTER);
            this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            this.setSizeFull();
            
            //this.setLocale(UI.getCurrent().getLocale());
            this.setSpacing(false);
            this.setMargin(false);

            this.loginWrapper = new VerticalLayout();

            this.loginWrapper.setWidth(350, Unit.PIXELS);
            this.loginWrapper.setSpacing(true);
            this.loginWrapper.addClassName("login-wrapper"); // add CSS classes to the components
            this.loginWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
            this.loginWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

            this.lblNomApplication = new H2("Incubatis 1.0");
            this.lblNomApplication.addClassName("nom-application"); // add CSS classes to the components

            this.lblDescriptionApplication = new H4("Progiciel de Gestion des IPHE");   
            this.lblDescriptionApplication.addClassName("description-application"); // add CSS classes to the components
            
            this.lblTitreConnexion = new H3("Connexion au Système");
            
            this.txtLogin = new TextField();
            this.txtLogin.setWidth(100, Unit.PERCENTAGE);
            this.txtLogin.setPlaceholder("Nom de Connexion");
            this.txtLogin.setPrefixComponent(VaadinIcon.USER.create());
            this.txtLogin.getThemeNames().addAll(Arrays.asList("align-left", "small", "helper-below-field")); //Defining Component Theme Variants
            this.txtLogin.focus();

            this.txtCodeSecret = new PasswordField();
            this.txtCodeSecret.setWidth(100, Unit.PERCENTAGE);
            this.txtCodeSecret.setPlaceholder("Code Secret");
            this.txtCodeSecret.getThemeNames().addAll(Arrays.asList("align-left", "small", "helper-below-field")); //Defining Component Theme Variants
            this.txtCodeSecret.setPrefixComponent(VaadinIcon.LOCK.create());

            this.loginButton = new Button("Login");
            this.loginButton.setIcon(VaadinIcon.SIGN_IN.create());
            this.loginButton.setWidth(100, Unit.PERCENTAGE);
            this.loginButton.getThemeNames().addAll(Arrays.asList("primary")); //Defining Component Theme Variants
            //this.loginButton.getThemeNames().addAll(Arrays.asList("primary", "small")); //Defining Component Theme Variants
            //this.loginButton.getThemeNames().addAll(Arrays.asList("primary", "success", "large")); //Defining Component Theme Variants
            this.loginButton.addClickListener(e -> {this.handleLoginClick();});

            this.loginWrapper.add(this.lblTitreConnexion, this.txtLogin, this.txtCodeSecret, this.loginButton);

            this.add(this.lblNomApplication, this.lblDescriptionApplication, this.loginWrapper);
            
            //Extra - Set the focus on the LoginForm component to the Username field?
            //UI.getCurrent().getPage().executeJavaScript("document.getElementById(\"vaadinLoginUsername\").focus();");
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LoginView.initialize", e.toString());
            e.printStackTrace();
        }
    } //private void initialize() {

    public void handleLoginClick() {
        /***
         * We call the method authenticate, and only set an error if the method returns false. 
         * Otherwise, we redirect the user to the MainView. 
         * As we have now hardcoded the value MainView.class twice in the project, 
         * we should add a method in the security service that returns the default view, and change the prior uses to use this new method.
         */
        try 
        {
            if (StringUtils.isBlank(this.txtLogin.getValue()) == true)
            {
                MessageDialogHelper.showWarningDialog("Connexion au Système", "La saisie du Nom de Connexion est requise.");
                this.txtLogin.focus();
            }
            else
            {
                if (StringUtils.isBlank(this.txtCodeSecret.getValue()))
                {
                        MessageDialogHelper.showWarningDialog("Connexion au Système", "La saisie du Code Secret est requise.");
                        this.txtCodeSecret.focus();
                }
                else
                {
                    //LONG OPERATIONS TASKS 
                    //Change the Cursor for Long Operations / Tasks.
                    //Change the cursor to a wait cursor, perform the long task, then change the cursor back to its default.

                    // BEGIN - Do some stuff
                    this.utilisateurList = this.utilisateurBusiness.findByLogin(this.txtLogin.getValue());
                    //TRAITEMENT - utilisateurList contient au maximum un élément
                    if (this.utilisateurList.isEmpty() == true)
                    {
                        //Pas d'utilisateur - Utilisateur non trouvé
                        MessageDialogHelper.showWarningDialog("Connexion au Système", "Accès réfusé. Vérifier l'Accès à la Base de Données ou le référencement de l'utilisateur dans la Base de Données.");

                        //Clear the controls
                        this.txtLogin.clear();
                        this.txtLogin.setPlaceholder("Nom de Connexion");
                        this.txtCodeSecret.clear();
                        this.txtCodeSecret.setPlaceholder("Code Secret");
                    }
                    else
                    {
                        //Utilisateur trouvé - Get User
                        this.utilisateur = this.utilisateurList.get(0);

                        this.datDateExpirationCodeSecret = this.utilisateur.getDateExpirationCodeSecret();
                        this.strLogin = this.utilisateur.getLogin();
                        this.strCodeSecret = this.utilisateur.getCodeSecret();
                        this.blnisInactif = this.utilisateur.isInactif();

                        if (this.blnisInactif == true)
                        {
                            //Utilisateur - trouvé, mais isInactif
                            MessageDialogHelper.showWarningDialog("Connexion au Système", "Compte d'Utilisateur désactivé. Veuillez contacter votre Administrateur.");
                            this.txtLogin.focus();
                        }
                        else
                        {
                            //Utilisateur - trouvé
                            if ((this.strLogin.equals(this.txtLogin.getValue()) == false) || (this.strCodeSecret.equals(this.txtCodeSecret.getValue()) == false)) //Ce code est utilisé pour vérifier la concordance (Case sensibility) exacte du login entré
                            //if (this.strCodeSecret.equals(this.txtCodeSecret.getValue()) == false)
                            {
                                //Utilisateur - trouvé, mais Non Concordance de Code Secret
                                MessageDialogHelper.showWarningDialog("Connexion au Système", "Vérifiez que vous avez entré le nom d'utilisateur et le mot de passe corrects et réessayez.");
                                //Clear the controls
                                this.txtLogin.clear();
                                this.txtLogin.setPlaceholder("Nom de Connexion");
                                this.txtCodeSecret.clear();
                                this.txtCodeSecret.setPlaceholder("Code Secret");
                                //this.txtCodeSecret.focus();
                            }
                            else
                            {
                                //Utilisateur - trouvé avec Concordance de Code Secret
                                if (this.datDateExpirationCodeSecret.isBefore(LocalDate.now()) == true)
                                {
                                    //Utilisateur - trouvé avec Concordance de Code Secret, mais Code Secret expiré
                                    MessageDialogHelper.showWarningDialog("Connexion au Système", "Votre Code Secret a expiré. Veuillez contacter votre Administrateur.");
                                    this.txtLogin.focus();
                                }
                                else
                                {
                                    //OK
                                    //Utilisateur - trouvé avec Concordance de Code Secret - tout correct pour une Autorisation de connexion
                                    if (this.datDateExpirationCodeSecret.minusDays(7).isBefore(LocalDate.now()) == true)
                                    {
                                        //Utilisateur - trouvé avec Concordance de Code Secret - tout correct pour une Autorisation de connexion, mais Code Secreyt expire dans moins de 7 jours
                                        MessageDialogHelper.showWarningDialog("Connexion au Système", "Votre Code Secret expire dans moins d'une semaine. Veuillez changer votre otre Code Secret.");
                                        this.txtLogin.focus();
                                    }
                                    
                                    //Positionner les informations de l'utilisateur courant de l'application
                                    SecurityService.getInstance().registerUserForAuthentication(this.txtLogin.getValue(), this.utilisateur);
                                    //Afficher l'écran MainView
                                    UI.getCurrent().navigate(SecurityService.getInstance().getDefaultView());
                                }
                            } //if (strCodeSecret.equals(this.txtCodeSecret.getValue()) == false)
                        } // if (blnisInactif == true)  
                    } //if (utilisateurList.isEmpty() == true)
                    //FIN TRAITEMENT
                    
                    // END - Do some stuff
                } //if (StringUtils.isBlank(this.txtCodeSecret.getValue()))
            } //if (StringUtils.isBlank(this.txtLogin.getValue()) == true)
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("LoginView.handleLoginClick()", e.toString());
            e.printStackTrace();
        }
    } //public void loginFormEvent() {
    
} //public class LoginView extends VerticalLayout {
