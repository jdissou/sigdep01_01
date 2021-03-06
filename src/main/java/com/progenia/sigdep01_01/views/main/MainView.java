package com.progenia.sigdep01_01.views.main;

import com.progenia.sigdep01_01.data.business.ParametreSystemeBusiness;
import com.progenia.sigdep01_01.data.entity.ParametreSysteme;
import com.progenia.sigdep01_01.securities.services.SecurityService;
import com.progenia.sigdep01_01.securities.views.AdministrateurSystemeView;
import com.progenia.sigdep01_01.securities.views.ChangementCodeSecretView;
import com.progenia.sigdep01_01.securities.views.ParametrageSecuriteView;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.consultations.*;
import com.progenia.sigdep01_01.views.referentiel.*;
import com.progenia.sigdep01_01.views.traitements.CloturePeriodeView;
import com.progenia.sigdep01_01.views.editions.LivreEvenementIncubationView;
import com.progenia.sigdep01_01.views.parametre.ParametreSystemeView;
import com.progenia.sigdep01_01.views.parametre.PreferenceUtilisateurView;
import com.progenia.sigdep01_01.views.empty.EmptyView;
import com.progenia.sigdep01_01.views.parametre.ParametreCentreIncubateurView;
import com.progenia.sigdep01_01.views.referentiel.WorkplaceImmobilierView;
import com.progenia.sigdep01_01.views.traitements.ClotureExerciceView;
import com.progenia.sigdep01_01.views.transactions.EvenementInstrumentView;
import com.progenia.sigdep01_01.views.traitements.ContrepassationView;
import com.progenia.sigdep01_01.views.traitements.LettrageManuelView;
import com.progenia.sigdep01_01.views.traitements.RapprochementBancaireView;
import com.progenia.sigdep01_01.views.traitements.ValidationBrouillardView;
import com.progenia.sigdep01_01.views.transactions.ConsommationAbonnementView;
import com.progenia.sigdep01_01.views.transactions.ContratAccompagnementView;
import com.progenia.sigdep01_01.views.transactions.ContratLotView;
import com.progenia.sigdep01_01.views.transactions.EcritureUniverselleView;
import com.progenia.sigdep01_01.views.transactions.EvenementIncubationLotView;
import com.progenia.sigdep01_01.views.transactions.EvenementPreIncubationView;
import com.progenia.sigdep01_01.views.transactions.FacturationAbonnementView;
import com.progenia.sigdep01_01.views.transactions.FacturationActeView;
import com.progenia.sigdep01_01.views.transactions.LotEcritureView;
import com.progenia.sigdep01_01.views.transactions.MajAbonnementServiceView;
import com.progenia.sigdep01_01.views.transactions.MajSoldeOuvertureExerciceView;
import com.progenia.sigdep01_01.views.transactions.MesureIndicateurView;
import com.progenia.sigdep01_01.views.transactions.PrestationDemandeView;
import com.progenia.sigdep01_01.views.transactions.ReglementInstrumentView;
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
 * @author Jam??l-Dine DISSOU
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
    static final String APPLICATION_NAME = "sigdep 1.0";
    static final String APPLICATION_SHORT_NAME = "sigdep 1.0";

    // @Autowired annotation provides the automatic dependency injection.
    @Autowired
    private ParametreSystemeBusiness parametreSystemeBusiness;

    //Parm??tres de l'application
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
     * It???s good practice to defer component initialization until it is attached. 
     * This avoids potentially running costly code for a component that is never displayed to the user. 
     * Il est recommand?? de diff??rer l???initialisation du composant jusqu????? ce qu???il soit connect??. 
     * Cela ??vite d'ex??cuter du code potentiellement co??teux pour un composant qui n'est jamais affich?? ?? l'utilisateur.
     */
    
    MainView(EventBus.UIEventBus uiEventBus) {
        this.uiEventBus = uiEventBus;
    }
    
    
    /***
     * We can do the initialization by overriding the onAttach method. 
     * To only run it once, we check if this is the first attach event, 
     * using the AttachEvent#isInitialAttach method.
     * Nous pouvons effectuer l'initialisation en red??finissant la m??thode onAttach. 
     * Pour ne l'ex??cuter qu'une seule fois, nous v??rifions s'il s'agit du premier ??v??nement d'attachement, 
     * ?? l'aide de la m??thode AttachEvent # isInitialAttach.
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
            this.imgLogo.setHeight("44px"); //D??finition de la taille de l'image
            this.imgLogo.addClassName("logo");
            
            boolean touchOptimized = true;
             /*, les composants seront d??plac??s vers la zone inf??rieure 
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
        //M??thode Static pour mettre ?? jour des attributs de classe
        try 
        {
            nomApplication = "sigdep 1.0";
            descriptionApplication = "Progiciel de Gestion Locative Immobili??re";
            auteurApplication = "Application con??ue par Jam??l-Dine DISSOU";
            infoProGenia = "ProGenia, 06 BP 2580 Cotonou - B??nin, T??l: +229 21 33 37 31, Fax: +229 21 33 37 59, info@progeniaweb.com";
            infoCopyright = "Copyright ?? 2020-2021, ProGenia Sarl";
            nomVersion = "Edit?? par " + nomApplication; //version du logiciel figurant sur les ??tats
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("MainView.setApplicationParameter", e.toString());
            e.printStackTrace();
        }
    } //public static void setApplicationParameter()

    public void setParametreSystemeAttribute()
    {
        //Les param??tres syst??me
        ParametreSysteme parametreSysteme;

        try 
        {
            Optional<ParametreSysteme> parametreSystemeOptional = this.parametreSystemeBusiness.findById(ApplicationConstanteHolder.getVALEUR_CODE_PARAMETRE_SYSTEME());
            if (parametreSystemeOptional.isPresent()) { 
                //Stocker les param??tres Syst??me au niveau de l'application (Servlet ou Context - Servlet est pr??f??r?? au Context)
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DENOMINATION(), parametreSystemeOptional.get().getDenomination());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_ADMINISTRATEUR(), parametreSystemeOptional.get().getAdministrateur().getCodeUtilisateur());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_OPERATION_COMPTABLE(), parametreSystemeOptional.get().getOperationComptable().getCodeOperation());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_VALIDATION_COMPTA(), parametreSystemeOptional.get().getValidationCompta().getCodeValidation());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DATE_DEBUT_PLAGE(), parametreSystemeOptional.get().getDateDebutPlage());
                VaadinServlet.getCurrent().getServletContext().setAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_DATE_FIN_PLAGE(), parametreSystemeOptional.get().getDateFinPlage());
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
            topBarContentLayout.getThemeList().set("dark", true); //Th??me
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
            
            //D??j?? initialis?? lors de sa d??claration - this.viewTitle = new H4("");

            this.logOutButton = new Button("Log out");
            this.logOutButton.setIcon(VaadinIcon.SIGN_OUT.create());
            this.logOutButton.getStyle().set("margin-left", "auto"); //Aligner ?? doite
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
            //Composition du Menu Lat??ral gauche
            VerticalLayout lateralMenuContentLayout = new VerticalLayout();
            
            lateralMenuContentLayout.setSizeFull();
            lateralMenuContentLayout.setPadding(false);
            lateralMenuContentLayout.setSpacing(false);
            lateralMenuContentLayout.getThemeList().set("spacing-s", true); //Th??me
            lateralMenuContentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

            /* Code transf??r?? dans NavBar
            HorizontalLayout logoLayout = new HorizontalLayout();
            logoLayout.setId("logo");
            logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            logoLayout.add(this.imgLogo);
            logoLayout.add(new H4(MainView.APPLICATION_SHORT_NAME));
            */
            
            //this.lateralTabsMenu = this.createLateralTabsMenu();
            this.lateralAccordionMenu = this.createAccordionMenu();
            
            lateralMenuContentLayout.add(this.lateralAccordionMenu);
            //lateralMenuContentLayout.add(logoLayout, this.lateralAccordionMenu); : this.lateralAccordionMenu est transf??r?? dans NavBar
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
                    createDivAccordionItem("Ev??nements de Pr??-Incubation", EvenementPreIncubationView.class),
                    createDivAccordionItem("Ev??nements d'Incubation par Lot", EvenementIncubationLotView.class),
                    createDivAccordionItem("Ev??nements d'Incubation par Instrument", EvenementInstrumentView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Contrats de Service R??current par Lot", ContratLotView.class),
                    createDivAccordionItem("Contrats de Service R??current par Instrument", ContratAccompagnementView.class),
                    createDivAccordionItem("Mise ?? jour des Abonnements de Service", MajAbonnementServiceView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Prestations de Service Ponctuel", PrestationDemandeView.class),
                    createDivAccordionItem("Facturations de Service Ponctuel", FacturationActeView.class),
                    createDivAccordionItem("Consommations de Service R??current", ConsommationAbonnementView.class),
                    createDivAccordionItem("Facturations de Service R??current", FacturationAbonnementView.class),
                    createDivAccordionItem("R??glements de Facture", ReglementInstrumentView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Mouvements Comptables (Saisie libre)", EcritureUniverselleView.class),
                    createDivAccordionItem("Mouvements Comptables par Lot", LotEcritureView.class),
                    createDivAccordionItem("Mise ?? jour des Soldes d'ouverture", MajSoldeOuvertureExerciceView.class),
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
                    createDivAccordionItem("Cl??ture et Ouverture de P??riode", CloturePeriodeView.class),
                    createDivAccordionItem("Cl??ture Exercice", ClotureExerciceView.class)
                })
            );
            
            accordion.add(
                createAccordionPanel("Consultation des Transactions",
                new com.vaadin.flow.component.Component[]{    

                    createDivAccordionItem("Ev??nements de Pr??-Incubation", ConsultationEvenementPreIncubationView.class),
                    createDivAccordionItem("Ev??nements d'Incubation par Lot", ConsultationEvenementIncubationLotView.class),
                    createDivAccordionItem("Ev??nements d'Incubation par Instrument", ConsultationEvenementInstrumentView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Contrats de Service R??current par Lot", ConsultationContratLotView.class),
                    createDivAccordionItem("Contrats de Service R??current par Instrument", ConsultationContratAccompagnementView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Prestations de Service Ponctuel", ConsultationPrestationDemandeView.class),
                    createDivAccordionItem("Facturations de Service Ponctuel", ConsultationFacturationActeView.class),
                    createDivAccordionItem("R??glements de Facture", ConsultationReglementInstrumentView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Mouvements Comptables", ConsultationMouvementComptaView.class),
                    createDivAccordionItem("Rapports d'Indicateur de Suivi", ConsultationMesureIndicateurView.class)
                })
            );
            
            accordion.add(
                createAccordionPanel("Editions",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Liste des Instruments de Projet", EmptyView.class),
                    createDivAccordionItem("Liste des Programmes d'accompagnement", EmptyView.class),
                                                  
                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Ev??nements de Pr??-Incubation", EmptyView.class),
                    createDivAccordionItem("Livre des Ev??nements d'incubation", LivreEvenementIncubationView.class),
                    createDivAccordionItem("Livre des Rapports d'Indicateur de Suivi", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Contrats d'Accompagnement", EmptyView.class),
                    createDivAccordionItem("Livre des Consommations de Service R??current", EmptyView.class),
                    createDivAccordionItem("Livre des Facturations de Service R??current", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Livre des Prestations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Livre des Facturations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Livre des R??glements de Facture", EmptyView.class),
                                                  
                    createSeparatorItem(),
                    createDivAccordionItem("Abonnements aux Services", EmptyView.class),
                    createDivAccordionItem("Prestations de Service Ponctuel", EmptyView.class),
                    createDivAccordionItem("Analyse des Indicateurs de Suivi", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Plan Comptable", EmptyView.class),
                    createDivAccordionItem("ZZZJournal Comptable", EmptyView.class),
                    createDivAccordionItem("ZZZJournal Centralisateur", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("Grand Livre Comptable", EmptyView.class),
                    createDivAccordionItem("Balance Comptable", EmptyView.class),

                    createSeparatorItem(),
                    createDivAccordionItem("ZZZJournal du Lettrage", EmptyView.class),
                    createDivAccordionItem("ZZZJournal du Rapprochement Bancaire", EmptyView.class)
                })
            );
            
            
            accordion.add(createAccordionPanel("R??f??rentiels",
                new com.vaadin.flow.component.Component[]{
                    createDivAccordionItem("Workplace des  Parties Prenantes", WorkplacePartiePrenanteView.class),
                    createDivAccordionItem("Workplace Immobilier", WorkplaceImmobilierView.class),
                    createDivAccordionItem("Classification des Parties Prenantes", ClassificationPartiePrenanteView.class),
                    createDivAccordionItem("Classification Immobili??re", ClassificationImmobiliereView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Configuration des Services", ConfigurationServiceView.class),
                    createDivAccordionItem("Workplace de Facturation", WorkplaceFacturationView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Workplace de la Comptabilit??", WorkplaceComptabiliteView.class),
                    createDivAccordionItem("Divers R??f??rentiels", WorkplaceDiversReferentielView.class)
                })
            );
            
            accordion.add(createAccordionPanel("Param??tres",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Pr??f??rences Utilisateur", PreferenceUtilisateurView.class),
                    createDivAccordionItem("Param??tres Centre Incubateur", ParametreCentreIncubateurView.class),
                    createDivAccordionItem("Param??tres Syst??me", ParametreSystemeView.class)
                })
            );
            
            accordion.add(createAccordionPanel("S??curit??",
                new com.vaadin.flow.component.Component[]{    
                    createDivAccordionItem("Chagement Code Secret", ChangementCodeSecretView.class),
                    createSeparatorItem(),
                    createDivAccordionItem("Administrateur Syst??me", AdministrateurSystemeView.class),
                    createDivAccordionItem("Param??trage de la S??curit??", ParametrageSecuriteView.class)
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
