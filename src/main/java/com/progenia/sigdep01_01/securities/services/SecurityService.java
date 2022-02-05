/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.securities.services;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

import com.progenia.sigdep01_01.securities.data.business.SystemeAutorisationBusiness;
import com.progenia.sigdep01_01.securities.data.business.UtilisateurBusiness;
import com.progenia.sigdep01_01.securities.data.entity.CategorieUtilisateur;
import com.progenia.sigdep01_01.securities.data.entity.SystemeAutorisation;
import com.progenia.sigdep01_01.securities.data.entity.Utilisateur;
import com.progenia.sigdep01_01.securities.data.pojo.AutorisationUtilisateurPojo;
import com.progenia.sigdep01_01.securities.views.LoginView;
import com.progenia.sigdep01_01.utilities.ApplicationConstanteHolder;
import com.progenia.sigdep01_01.utilities.MessageDialogHelper;
import com.progenia.sigdep01_01.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

public class SecurityService {
    /***
     * SecurityService is responsible for authenticated. 
     * We make this a singleton class by creating a private constructor, 
     * and returning a static instance in a getInstance() method.
     * SecurityService est chargé de vérifier si l'utilisateur est authentifié. 
     * Nous en faisons une classe singleton en créant un constructeur privé 
     * et en retournant une instance statique dans une méthode getInstance ().
     */
    
    /***
     * When the user logs in, we store the username as a session attribute under an arbitrary key. 
     * As such, we can check if the user is logged in by checking if that attribute is set.
     */
    
    private static SecurityService instance;
    //private static final ApplicationInformationHolder INSTANCE = new ApplicationInformationHolder();

    @Autowired
    private SystemeAutorisationBusiness autorisationUtilisateurBusiness;

    @Autowired
    private UtilisateurBusiness utilisateurBusiness;


    private SecurityService() {}

    public static SecurityService getInstance() {
        try 
        {
            if (instance == null) {
                instance = new SecurityService();
            }
            return instance;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.getInstance", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /***
     * This method is called before entering any view. 
     * For this, we utilize a BeforeEnterListener added to every UI. 
     * We can use a VaadinServiceInitListener to add it whenever a UI is created.
     *  
     */
    public boolean isAuthenticated() {
        try 
        {
            return VaadinSession.getCurrent() != null  &&
                    VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getLOGIN_ATTRIBUTE()) != null;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isAuthenticated", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAccessGranted(String codeCommande, Class <?> classCommande) {
        Boolean blnResult = false;
        
        try 
        {
            // Allow if Security Check is NOT required.
            RequiresSecurityCheck secured = AnnotationUtils.findAnnotation(classCommande, RequiresSecurityCheck.class);
            if (secured == null) {
                return true; // 
            }
            else {
                return isAccessGranted(codeCommande);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isAccessGranted", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAccessGranted(String codeCommande) {
        Boolean blnResult = false;
        AutorisationUtilisateurPojo autorisationUtilisateurPojo;
        
        try 
        {
            String codeAdministrateur = (String)VaadinServlet.getCurrent().getServletContext().getAttribute(ApplicationConstanteHolder.getPARAMETRE_SYSTEME_CODE_ADMINISTRATEUR());
            String codeUtilisateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getCodeUtilisateur();
            
            if (codeAdministrateur.equals(codeUtilisateur))
            {
                //Administrateur
                autorisationUtilisateurPojo = new AutorisationUtilisateurPojo(true, true, true, true);
                blnResult =  true;
            }
            else
            {
                
                CategorieUtilisateur categorieUtilisateur = ((Utilisateur)VaadinSession.getCurrent().getAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE())).getCategorieUtilisateur();
                List<AutorisationUtilisateurPojo> autorisationUtilisateurPojoList = new ArrayList<AutorisationUtilisateurPojo>();
                
                Optional<SystemeAutorisation> optionalSystemeAutorisation = this.autorisationUtilisateurBusiness.getSystemeAutorisation(categorieUtilisateur.getCodeCategorieUtilisateur(), codeCommande);
                if (optionalSystemeAutorisation.isPresent())
                {
                    //Autorisation trouvée
                    autorisationUtilisateurPojo = new AutorisationUtilisateurPojo();
                    autorisationUtilisateurPojo.setAjout(optionalSystemeAutorisation.get().getAjout());
                    autorisationUtilisateurPojo.setAutorisation(optionalSystemeAutorisation.get().getAutorisation());
                    autorisationUtilisateurPojo.setModification(optionalSystemeAutorisation.get().getModification());
                    autorisationUtilisateurPojo.setSuppression(optionalSystemeAutorisation.get().getSuppression());
                    
                    blnResult =  autorisationUtilisateurPojoList.get(0).getAutorisation();
                }
                else
                {
                    //Pas d'info Utilisateur - Utilisateur non trouvé
                    autorisationUtilisateurPojo = new AutorisationUtilisateurPojo(false, false, false, false);
                    blnResult =  false;
                } //if (optionalSystemeAutorisation.isPresent())
            } //if (codeAdministrateur.equals(codeUtilisateur))
            
            if (VaadinSession.getCurrent().getAttribute(codeCommande) ==  null) {
                //Register User Security Grants - We use this registre as a CACHE, i.e we store it only once
                VaadinSession.getCurrent().setAttribute(codeCommande, autorisationUtilisateurPojo);
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isAccessGranted", e.toString());
            e.printStackTrace();
            return false;
        }
        return (blnResult);
    }

    public void registerUserForAuthentication(String login, Utilisateur utilisateur) {
        /***
         * In the login view, we call this method after the user is authenticated.
         */
        try 
        {
            VaadinSession.getCurrent().setAttribute(ApplicationConstanteHolder.getLOGIN_ATTRIBUTE(), login);
            VaadinSession.getCurrent().setAttribute(ApplicationConstanteHolder.getCODE_UTILISATEUR_ATTRIBUTE(), utilisateur.getCodeUtilisateur());
            VaadinSession.getCurrent().setAttribute(ApplicationConstanteHolder.getUSER_ATTRIBUTE(), utilisateur);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.registerUserForAuthentication", e.toString());
            e.printStackTrace();
        }
    }

    public void logOut() {
        /***
         * This method does two things:
         *  1. It invalidates the wrapped HttpSession, closing all VaadinSession instances that it contains. 
         *      This causes a new VaadinSession to be created upon the next navigation, 
         *      effectively clearing the stored user.
         * 2. It redirects the user to the login view.
         */
        try 
        {
            VaadinSession.getCurrent().close();
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate(LoginView.class);
            /***
             * All servlets in a WAR-file share the same HttpSession, 
             * but have their own VaadinSession instances. 
             * If you only want to close a particular Vaadin session, 
             * call VaadinSession.getCurrent().close().
             */
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.logOut", e.toString());
            e.printStackTrace();
        }
    }

    public Class<? extends Component> getDefaultView() {
        /***
         * Class<? extends Component> is equivalent to any class tha extends Component class
         * Returns the default view to avoid have hardcoded the value MainView.class several times in the project
         */
        try 
        {
            return MainView.class;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.getDefaultView", e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isModificationAutorisee(String strViewName) {
        try 
        {
            if (((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)) == null) {
                return (false);
            }
            else {
                return ((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)).getModification();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isModificationAutorisee", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSuppressionAutorisee(String strViewName) {
        try 
        {
            if (((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)) == null) {
                return (false);
            }
            else {
                return ((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)).getSuppression();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isSuppressionAutorisee", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAjoutAutorise(String strViewName) {
        try 
        {
            if (((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)) == null) {
                return (false);
            }
            else {
                return ((AutorisationUtilisateurPojo)VaadinSession.getCurrent().getAttribute(strViewName)).getAjout();
            }
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("SecurityService.isAjoutAutorise", e.toString());
            e.printStackTrace();
            return false;
        }
    }
}
