package com.progenia.incubatis01_03.views.main;

import com.progenia.incubatis01_03.data.business.ParametreSystemeBusiness;
import com.progenia.incubatis01_03.data.entity.ParametreSysteme;
import com.progenia.incubatis01_03.securities.services.SecurityService;
import com.progenia.incubatis01_03.securities.views.AdministrateurSystemeView;
import com.progenia.incubatis01_03.securities.views.ChangementCodeSecretView;
import com.progenia.incubatis01_03.securities.views.ParametrageSecuriteView;
import com.progenia.incubatis01_03.utilities.ApplicationConstanteHolder;
import com.progenia.incubatis01_03.utilities.MessageDialogHelper;
import com.progenia.incubatis01_03.views.consultations.ConsultationEvenementPorteurView;
import com.progenia.incubatis01_03.views.traitements.CloturePeriodeView;
import com.progenia.incubatis01_03.views.editions.LivreEvenementIncubationView;
import com.progenia.incubatis01_03.views.parametre.ParametreSystemeView;
import com.progenia.incubatis01_03.views.parametre.PreferenceUtilisateurView;
import com.progenia.incubatis01_03.views.empty.EmptyView;
import com.progenia.incubatis01_03.views.parametre.ParametreCentreIncubateurView;
import com.progenia.incubatis01_03.views.referentiel.ConfigurationServiceView;
import com.progenia.incubatis01_03.views.referentiel.CorpusCentreIncubateurView;
import com.progenia.incubatis01_03.views.referentiel.CorpusTiersView;
import com.progenia.incubatis01_03.views.referentiel.CorpusComptabiliteView;
import com.progenia.incubatis01_03.views.referentiel.CorpusDiversReferentielView;
import com.progenia.incubatis01_03.views.referentiel.CorpusFacturationView;
import com.progenia.incubatis01_03.views.referentiel.CorpusIndicateurView;
import com.progenia.incubatis01_03.views.referentiel.CorpusPorteurView;
import com.progenia.incubatis01_03.views.traitements.ClotureExerciceView;
import com.progenia.incubatis01_03.views.transactions.EvenementPorteurView;
import com.progenia.incubatis01_03.views.traitements.ContrepassationView;
import com.progenia.incubatis01_03.views.traitements.LettrageManuelView;
import com.progenia.incubatis01_03.views.traitements.RapprochementBancaireView;
import com.progenia.incubatis01_03.views.traitements.ValidationBrouillardView;
import com.progenia.incubatis01_03.views.transactions.ConsommationAbonnementView;
import com.progenia.incubatis01_03.views.transactions.ContratAccompagnementView;
import com.progenia.incubatis01_03.views.transactions.ContratLotView;
import com.progenia.incubatis01_03.views.transactions.EcritureUniverselleView;
import com.progenia.incubatis01_03.views.transactions.EvenementIncubationLotView;
import com.progenia.incubatis01_03.views.transactions.EvenementPreIncubationView;
import com.progenia.incubatis01_03.views.transactions.FacturationAbonnementView;
import com.progenia.incubatis01_03.views.transactions.FacturationActeView;
import com.progenia.incubatis01_03.views.transactions.LotEcritureView;
import com.progenia.incubatis01_03.views.transactions.MajAbonnementServiceView;
import com.progenia.incubatis01_03.views.transactions.MajSoldeOuvertureExerciceView;
import com.progenia.incubatis01_03.views.transactions.MesureIndicateurView;
import com.progenia.incubatis01_03.views.transactions.PrestationDemandeView;
import com.progenia.incubatis01_03.views.transactions.ReglementPorteurView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;
import java.util.Arrays;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.vaadin.spring.events.EventBus;
//import org.vaadin.spring.events.EventBus;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

@Route("")
@Theme(value = Lumo.class)
@PWA(name = MainView.APPLICATION_NAME, shortName = MainView.APPLICATION_SHORT_NAME, enableInstallPrompt = false)
@CssImport(value="./styles/base-textfield-styles.css", themeFor="vaadin-text-field")
@CssImport(value="./styles/base-combobox-styles.css", themeFor="vaadin-combo-box")
@CssImport(value="./styles/current-textarea-styles.css", themeFor="vaadin-text-area")
@CssImport(value="./styles/current-textfield-styles.css", themeFor="vaadin-text-field")
@CssImport(value="./styles/current-datepicker-styles.css", themeFor="vaadin-date-picker")
//@CssImport(value="./styles/current-checkbox-styles.css", themeFor="vaadin-checkbox")
@CssImport(value="./styles/current-combobox-styles.css", themeFor="vaadin-combo-box")
@CssImport(value="./styles/current-combobox-styles.css", themeFor="vaadin-combo-box-overlay")
//@CssImport("./styles/current-combobox-styles.css")
//@CssImport("./styles/current-textarea-styles.css")
//@CssImport("./styles/shared-styles.css")
@Component  @SessionScope//Allow to inject event bus for the session
public class MainView extends com.vaadin.flow.component.applayout.AppLayout {
    //private Tabs appTopBarTabs;
    //private Tabs appLeftMenuTabs;
    final static String APPLICATION_NAME = "Incubatis 1.0";
    final static String APPLICATION_SHORT_NAME = "Incubatis 1.0";

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ParametreSystemeBusiness parametreSystemeBusiness;

    //Parmètres de l'application
    private static String nomApplication;
    private static String descriptionApplication;
    private static String auteurApplication;
    private static String infoProGenia;
    private static String infoCopyright;
    private static String nomVersion;
        
    @Inject
    private EventBus.UIEventBus uiEventBus;
    
    private Image imgLogo;
    
    private Tabs lateralTabsMenu; //private final Tabs lateralTabsMenu;
    private Accordion lateralAccordionMenu; //private final Tabs lateralTabsMenu;
    private H4 viewTitle = new H4("");
    private Button logOutButton;

    /***
     * It’s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommandé de différer l’initialisation du composant jusqu’à ce qu’il soit connecté. 
     * Cela évite d'exécuter du code potentiellement coûteux pour un composant qui n'est jamais affiché à l'utilisateur.
     */
    
    MainView(EventBus.UIEventBus uiEventBus) {
        this.uiEventBus = uiEventBus;
    }
    
    
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
            MessageDialogHelper.showAlertDialog("MainView.onAttach", e.toString());
            e.printStackTrace();
        }
    }

    /***
     * We can then create the initialization method, where we instantiate the MainView. 
     */
    private void initialize() {
        try 
        {
            this.setApplicationParameter();
            
            this.setPrimarySection(com.vaadin.flow.component.applayout.AppLayout.Section.NAVBAR);
            //this.setPrimarySection(com.vaadin.flow.component.applayout.AppLayout.Section.DRAWER);
            
            this.imgLogo = new Image("images/logo.png", "Logo ProGenia");
            this.imgLogo.setHeight("44px"); //Définition de la taille de l'image
            this.imgLogo.addClassName("logo");
            
            boolean touchOptimized = true;
             /*, les composants seront déplacés vers la zone inférieure 
             de la barre de navigation sur les appareils mobiles. */
            this.addToNavbar(touchOptimized, this.createTopBarContent());
            
            this.addToDrawer(this.createLateralMenuContent());
            
            this.setParametreSystemeAttribute();
            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.initialize", e.toString());
            e.printStackTrace();
        }
    }

    public static void setApplicationParameter()
    {
        //Méthode Static pour mettre à jour des attributs de classe
        try 
        {
            nomApplication = "Incubatis 1.0";
            descriptionApplication = "Progiciel de Gestion des IPHE";
            auteurApplication = "Application conçue par Jamâl-Dine DISSOU";
            infoProGenia = "ProGenia, 06 BP 2580 Cotonou - Bénin, Tél: +229 21 33 37 31, Fax: +229 21 33 37 59, info@progeniaweb.com";
            infoCopyright = "Copyright © 2020-2021, ProGenia Sarl";
            nomVersion = "Edité par " + nomApplication; //version du logiciel figurant sur les états
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.setApplicationParameter", e.toString());
            e.printStackTrace();
        }
    } //public static void setApplicationParameter()

    public void setParametreSystemeAttribute()
    {
        //Les paramètres système
        ParametreSysteme parametreSysteme;

        try 
        {
            Optional<ParametreSysteme> parametreSystemeOptional = this.parametreSystemeBusiness.findById(ApplicationConstanteHolder.getVALEUR_CODE_PARAMETRE_SYSTEME());
            if (parametreSystemeOptional.isPresent()) { 
                //Stocker les paramètres Système au niveau de l'application (Servlet ou Context - Servlet est préféré au Context)
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DENOMINATION(), parametreSystemeOptional.get().getDenomination());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_ADMINISTRATEUR(), parametreSystemeOptional.get().getAdministrateur().getCodeUtilisateur());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE(), parametreSystemeOptional.get().getOperationComptable().getCodeOperation());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA(), parametreSystemeOptional.get().getValidationCompta().getCodeValidation());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DATE_DEBUT_PAGE(), parametreSystemeOptional.get().getDateDebutPlage());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DATE_FIN_PAGE(), parametreSystemeOptional.get().getDateFinPlage());
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.setParametreSystemeAttribute", e.toString());
            e.printStackTrace();
        }
    } //public static void setParametreSystemeAttribute()

    private com.vaadin.flow.component.Component createTopBarContent() {
        try 
        {
            //Composition du Menu de la la barre de navigation horizontale
            HorizontalLayout topBarContentLayout = new HorizontalLayout();
            topBarContentLayout.getThemeList().set("dark", true); //Thème
            topBarContentLayout.setClassName("menu-navbar");
            
            topBarContentLayout.setWidthFull();
            topBarContentLayout.setSpacing(false);
            topBarContentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            
            HorizontalLayout logoLayout = new HorizontalLayout();
            logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            logoLayout.add(this.imgLogo);
            logoLayout.add(new H4(MainView.APPLICATION_SHORT_NAME));
            logoLayout.add(new H4 ("     ")); //5 spaces
            logoLayout.setClassName("logo");
            
            //Déjà initialisé lors de sa déclaration - this.viewTitle = new H4("");

            this.logOutButton = new Button("Log out");
            this.logOutButton.setIcon(VaadinIcon.SIGN_OUT.create());
            this.logOutButton.getStyle().set("margin-left", "auto"); //Aligner à doite
            this.logOutButton.addClickListener(e -> SecurityService.getInstance().logOut());

            topBarContentLayout.add(new DrawerToggle());
            topBarContentLayout.add(logoLayout);
            topBarContentLayout.add(this.viewTitle);
            //topBarContentLayout.add(new Avatar("Utilisateur"));
            topBarContentLayout.add(this.logOutButton);

            return topBarContentLayout;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTopBarContent", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private com.vaadin.flow.component.Component createLateralMenuContent() {
        try 
        {
            //Composition du Menu Latéral gauche
            VerticalLayout lateralMenuContentLayout = new VerticalLayout();
            
            lateralMenuContentLayout.setSizeFull();
            lateralMenuContentLayout.setPadding(false);
            lateralMenuContentLayout.setSpacing(false);
            lateralMenuContentLayout.getThemeList().set("spacing-s", true); //Thème
            lateralMenuContentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

            /* Code transféré dans NavBar
            HorizontalLayout logoLayout = new HorizontalLayout();
            logoLayout.setId("logo");
            logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            logoLayout.add(this.imgLogo);
            logoLayout.add(new H4(MainView.APPLICATION_SHORT_NAME));
            */
            
            //this.lateralTabsMenu = this.createLateralTabsMenu();
            this.lateralAccordionMenu = this.createAccordionMenu();
            
            lateralMenuContentLayout.add(this.lateralAccordionMenu);
            //lateralMenuContentLayout.add(logoLayout, this.lateralAccordionMenu); : this.lateralAccordionMenu est transféré dans NavBar
            //lateralMenuContentLayout.add(logoLayout, this.lateralAccordionMenu, this.lateralTabsMenu);
            return lateralMenuContentLayout;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createLateralMenuContent", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private Accordion createAccordionMenu() {
        try 
        {
            final Accordion accordion = new Accordion();
            
            accordion.setId("menu-accordion");
    
            accordion.add(createAccordionPanel("Saisie des Transactions",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Evénements de Pré-Incubation", EvenementPreIncubationView.class),
                    createDivAccordionItem("Evénements d'Incubation par Lot", EvenementIncubationLotView.class),
                    createDivAccordionItem("Evénements d'Incubation par Porteur", EvenementPorteurView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Contrats de Service Récurrent par Lot", ContratLotView.class),
                    createDivAccordionItem("Contrats de Service Récurrent par Porteur", ContratAccompagnementView.class),
                    createDivAccordionItem("Mise à jour des Abonnements de Service", MajAbonnementServiceView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Prestations de Service Ponctuel", PrestationDemandeView.class),
                    createDivAccordionItem("Facturations de Service Ponctuel", FacturationActeView.class),
                    createDivAccordionItem("Consommations de Service Récurrent", ConsommationAbonnementView.class),
                    createDivAccordionItem("Facturations de Service Récurrent", FacturationAbonnementView.class),
                    createDivAccordionItem("Règlements de Facture", ReglementPorteurView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Mouvements Comptables (Saisie libre)", EcritureUniverselleView.class),
                    createDivAccordionItem("Mouvements Comptables par Lot", LotEcritureView.class),
                    createDivAccordionItem("Mise à jour des Soldes d'ouverture", MajSoldeOuvertureExerciceView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Rapports d'Indicateur de Suivi", MesureIndicateurView.class)
                })
            );
            
            accordion.add(
                createAccordionPanel("Traitements Comptables",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Validation du Brouillard", ValidationBrouillardView.class),
                    createDivAccordionItem("Contrepassation des Mouvements", ContrepassationView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Lettrage Manuel des Ecritures", LettrageManuelView.class),
                    createDivAccordionItem("Rapprochement Bancaire des Ecritures", RapprochementBancaireView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Clôture et Ouverture de Période", CloturePeriodeView.class),
                    createDivAccordionItem("Clôture Exercice", ClotureExerciceView.class)
                })
            );
            
            accordion.add(
                createAccordionPanel("Consultation des Transactions",
                new com.vaadin.flow.component.Component[]{    

                    createDivAccordionItem("Evénements de Pré-Incubation", EmptyView.class),
                    createDivAccordionItem("Evénements d'Incubation par Lot", EmptyView.class),
                    createDivAccordionItem("Evénements d'Incubation par Porteur", ConsultationEvenementPorteurView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Contrats de Service Récurrent par Lot", EmptyView.class),
                    createDivAccordionItem("Contrats de Service Récurrent par Porteur", EmptyView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Prestations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Facturations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Consommations de Service Récurrent", EmptyView.class),
                    createDivAccordionItem("Facturations de Service Récurrent", EmptyView.class),
                    createDivAccordionItem("Règlements de Facture", EmptyView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Mouvements Comptables", EcritureUniverselleView.class),
                    createDivAccordionItem("Rapports d'Indicateur de Suivi", EmptyView.class)
                })
            );
            
            accordion.add(
                createAccordionPanel("Editions",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Liste des Porteurs de Projet", EmptyView.class),
                    createDivAccordionItem("Liste des Programmes d'accompagnement", EmptyView.class),
                                                  
                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Evénements de Pré-Incubation", EmptyView.class),
                    createDivAccordionItem("Livre des Evénements d'incubation", LivreEvenementIncubationView.class),
                    createDivAccordionItem("Livre des Rapports d'Indicateur de Suivi", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Contrats d'Accompagnement", EmptyView.class),
                    createDivAccordionItem("Livre des Consommations de Service Récurrent", EmptyView.class),
                    createDivAccordionItem("Livre des Facturations de Service Récurrent", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Prestations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Livre des Facturations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Livre des Règlements de Facture", EmptyView.class),
                                                  
                    createSeparatorItem(),
                    createDivAccordionItem("Abonnements aux Services", EmptyView.class),
                    createDivAccordionItem("Prestations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Analyse des Indicateurs de Suivi", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Plan Comptable", EmptyView.class),
                    createDivAccordionItem("Journal Comptable", EmptyView.class),
                    createDivAccordionItem("Journal Centralisateur", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Grand Livre Comptable", EmptyView.class),
                    createDivAccordionItem("Balance Comptable", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Journal du Lettrage", EmptyView.class),
                    createDivAccordionItem("Journal du Rapprochement Bancaire", EmptyView.class)
                })
            );
            
            
            accordion.add(createAccordionPanel("Référentiels",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Corpus des Porteurs de Projet", CorpusPorteurView.class),
                    createDivAccordionItem("Corpus des  Tiers", CorpusTiersView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Configuration des Services", ConfigurationServiceView.class),
                    createDivAccordionItem("Corpus de Facturation", CorpusFacturationView.class),
                    createDivAccordionItem("Corpus des Indicateurs", CorpusIndicateurView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Corpus de la Comptabilité", CorpusComptabiliteView.class),
                    createDivAccordionItem("Divers Référentiels", CorpusDiversReferentielView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Corpus des Centres Incubateurs", CorpusCentreIncubateurView.class)                    
                })
            );
            
            accordion.add(createAccordionPanel("Paramètres",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Préférences Utilisateur", PreferenceUtilisateurView.class),
                    createDivAccordionItem("Paramètres Centre Incubateur", ParametreCentreIncubateurView.class),
                    createDivAccordionItem("Paramètres Système", ParametreSystemeView.class)
                })
            );
            
            accordion.add(createAccordionPanel("Sécurité",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Chagement Code Secret", ChangementCodeSecretView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Administrateur Système", AdministrateurSystemeView.class),
                    createDivAccordionItem("Paramétrage de la Sécurité", ParametrageSecuriteView.class)
                })
            );
            
            return accordion;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createAccordionMenu", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //-----------------------------------------------------------------------------------------------------
    
    
    private AccordionPanel createAccordionPanel(String accordionPanelCaption, com.vaadin.flow.component.Component[] accordionItemsList) {
        try 
        {

            VerticalLayout panelLayout = new VerticalLayout();
            
            panelLayout.setClassName("menu-drawer");
            panelLayout.setMargin(true);
            panelLayout.setSpacing(false);
            panelLayout.setPadding(false);
            
            
            panelLayout.setAlignItems(FlexComponent.Alignment.START);
            panelLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
            
            panelLayout.add(accordionItemsList);

            AccordionPanel accordionPanel = new AccordionPanel(accordionPanelCaption, panelLayout);
            
            accordionPanel.getThemeNames().addAll(Arrays.asList("small", "reverse"));
            accordionPanel.setOpened(false);
            return accordionPanel;
            
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createAccordionPanel", e.toString());
            e.printStackTrace();
            return null;
        }
    }

//----------------------------------- createSeparatorItem
        private static Hr createSeparatorItem() {
        try 
        {
            final Hr separator = new Hr();
            
            return separator;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createSeparatorItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------- createSpanAccordionItem
    private static Span createSpanAccordionItem(String caption, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Span menu = new Span();
            
            menu.add(new RouterLink(caption, navigationTarget));
            ComponentUtil.setData(menu, Class.class, navigationTarget);
            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createSpanAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------- createDivAccordionItem
    private static Div createDivAccordionItem(String caption, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Div menu = new Div();
            
            menu.add(new RouterLink(caption, navigationTarget));
            //ComponentUtil.setData(menu, Class.class, navigationTarget);
            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createDivAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }
            
    //----------------------------------- createTabAccordionItem
    private static Tab createTabAccordionItem(String caption, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Tab menu = new Tab();
            
            menu.add(new RouterLink(caption, navigationTarget));
            ComponentUtil.setData(menu, Class.class, navigationTarget);
            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }
            
    private static Tab createTabAccordionItem(String caption, com.vaadin.flow.component.icon.Icon icon, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Tab menu = new Tab();
            
            menu.addComponentAsFirst(icon); //Set an icon on a Vaadin Flow Tab
            menu.add(new RouterLink(caption, navigationTarget));
            ComponentUtil.setData(menu, Class.class, navigationTarget);
            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }
            
    private static Tab createTabAccordionItem(String caption, DomEventListener listener) {
        try 
        {
            final Tab menu = new Tab();
            menu.getElement().addEventListener("click", listener);

            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }
            
    private static Tab createTabAccordionItem(String caption, com.vaadin.flow.component.icon.Icon icon, DomEventListener listener) {
        try 
        {
            final Tab menu = new Tab();
            
            menu.addComponentAsFirst(icon); //Set an icon on a Vaadin Flow Tab
            menu.getElement().addEventListener("click", listener);
            return menu;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabAccordionItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }
            
    /*
    private Tabs createLateralTabsMenu() {
        try 
        {
            final Tabs tabs = new Tabs();
            
            tabs.setOrientation(Tabs.Orientation.VERTICAL);
            tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
            tabs.setId("menu-tabs");
            
            tabs.add(createLateralTabsMenuItems());
            return tabs;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createLateralTabsMenu", e.toString());
            e.printStackTrace();
            return null;
        }
    }
    */
    
    /*
    private com.vaadin.flow.component.Component[] createLateralTabsMenuItems() {
        try 
        {
            return new Tab[]{createTabMenuRouterClassItem("Home", VaadinIcon.HOME.create(), EmptyView.class), 
                    createTabMenuRouterClassItem("Alarm", VaadinIcon.ALARM.create(), EmptyView.class), 
                    createTabMenuRouterClassItem("Connect", VaadinIcon.CONNECT.create(), EmptyView.class), 
                    createTabMenuClickableItem("Info", VaadinIcon.NOTEBOOK.create(), e -> MessageDialogHelper.showAlertDialog("MENU", "Info", "Fermer")), 
                    createTabMenuRouterClassItem("Empty", EmptyView.class)};
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createLateralTabsMenuItems", e.toString());
            e.printStackTrace();
            return null;
        }
    }
    */

    //----------------------------------- createTabMenuRouterClassItem
    private static Tab createTabMenuRouterClassItem(String caption, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Tab tab = new Tab();
      
            tab.add(new RouterLink(caption, navigationTarget));
            ComponentUtil.setData(tab, Class.class, navigationTarget);
            return tab;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabMenuRouterClassItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static Tab createTabMenuRouterClassItem(String caption, com.vaadin.flow.component.icon.Icon icon, Class<? extends com.vaadin.flow.component.Component> navigationTarget) {
        try 
        {
            final Tab tab = new Tab();
            
            tab.addComponentAsFirst(icon); //Set an icon on a Vaadin Flow Tab
            tab.add(new RouterLink(caption, navigationTarget));
            
            ComponentUtil.setData(tab, Class.class, navigationTarget);
            return tab;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabMenuRouterClassItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    //----------------------------------- createTabMenuRouterClassItem
    private static Tab createTabMenuClickableItem(String caption, DomEventListener listener) {
        try 
        {
            //com.vaadin.flow.component.ComponentEventListener<com.vaadin.flow.component.ClickEvent<?>> listener
            final Tab tab = new Tab();
            
            tab.getElement().addEventListener("click", listener);
            return tab;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabMenuClickableItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static Tab createTabMenuClickableItem(String caption, com.vaadin.flow.component.icon.Icon icon, DomEventListener listener) {
        try 
        {
            //com.vaadin.flow.component.ComponentEventListener<com.vaadin.flow.component.ClickEvent<?>> listener
            final Tab tab = new Tab();
            
            tab.addComponentAsFirst(icon); //Set an icon on a Vaadin Flow Tab
            
            tab.getElement().addEventListener("click", listener);

            return tab;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.createTabMenuClickableItem", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void afterNavigation() {
        try 
        {
            super.afterNavigation();
            //this.getTabForComponent(getContent()).ifPresent(this.lateralTabsMenu::setSelectedTab);
            this.viewTitle.setText(getCurrentPageTitle());
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.afterNavigation", e.toString());
            e.printStackTrace();
        }
    }
    
    
    /*
    private Optional<Tab> getTabForComponent(com.vaadin.flow.component.Component component) {
        try 
        {
             //java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "component" is null
            //return lateralTabsMenu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
            //        .findFirst().map(Tab.class::cast);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.getTabForComponent", e.toString());
            e.printStackTrace();
            return null;
        }
    }
    */
    
    private String getCurrentPageTitle() {
        try 
        {
            if (this.getContent() == null)
                return "";
            PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
            return title == null ? "" : title.value();
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.onAttach", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static String getNomApplication() {
        return nomApplication;
    }

    public static String getDescriptionApplication() {
        return descriptionApplication;
    }

    public static String getAuteurApplication() {
        return auteurApplication;
    }

    public static String getInfoProGenia() {
        return infoProGenia;
    }

    public static String getInfoCopyright() {
        return infoCopyright;
    }

    public static String getNomVersion() {
        return nomVersion;
    }
}
