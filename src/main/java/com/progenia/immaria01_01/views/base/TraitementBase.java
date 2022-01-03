/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.views.base;

import com.progenia.immaria01_01.securities.services.SecurityService;
import com.progenia.immaria01_01.utilities.MessageDialogHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public abstract class TraitementBase extends VerticalLayout   {
    
    protected boolean isPermanentFieldReadOnly = true;
    protected boolean isContextualFieldReadOnly = false;
    
    protected Boolean isButtonExecuterVisible = true;
    protected Boolean isButtonOptionnel01Visible = false;
    protected Boolean isButtonOptionnel02Visible = false;

    //For Lazy Loading
    //DataProvider<Utilisateur, Void> dataProvider; //The second DataProvider type parameter defines how the provider can be filtered. In the example, the filter type is Void
    //For ListDataProvider for Advanced In-memory Data Handling
    //Toto- protected ListDataProvider<T> dataProvider; 

    protected String strNomFormulaire;

    protected HorizontalLayout topToolBar;
    protected ContextMenu menu;
    
    //Boutons
    protected Button btnOverflow;
    protected Button btnExecuter;
    protected Button btnOptionnel01;
    protected Button btnOptionnel02;
    
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

    public TraitementBase() {
        //1 - Give the component a CSS class name to help with styling
        this.addClassName("traitement"); //Gives the component a CSS class name to help with styling.
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);  //Centers the contents of the layout.

        this.setSizeFull(); //sets the size of the MainView Content
        this.setSpacing(true); //for clarity we set spaces between the rows of the layout.
        this.setMargin(true); //sets the margin.            
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
            
            if (this.isButtonExecuterVisible) {
                this.menu.addItem("Exécuter", (e -> workingHandleExecuterClick(e)));
            }
            if (this.isButtonOptionnel01Visible) {
                this.menu.addItem(this.btnOptionnel01Text, (e -> workingHandleButtonOptionnel01Click(e)));
            }
            if (this.isButtonOptionnel02Visible) {
                this.menu.addItem(this.btnOptionnel02Text, (e -> workingHandleButtonOptionnel02Click(e)));
            }
        
            Span title = new Span(" ");
            
            this.btnExecuter = new Button("Exécuter", new Icon(VaadinIcon.TASKS)); 
            this.btnExecuter.setEnabled(SecurityService.getInstance().isAjoutAutorise(this.strNomFormulaire));
            this.btnExecuter.addClickListener(e -> workingHandleExecuterClick(e));
            
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

            this.topToolBar.add(title, this.btnExecuter, this.btnOptionnel01, this.btnOptionnel02, this.btnOverflow);
            setFlexGrow(1, title);
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetupTopToolBar", e.toString());
            e.printStackTrace();
        }
    }

    public void customSetButtonExecuterVisible(Boolean isVisible) {
        try 
        {
            this.isButtonExecuterVisible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonExecuterVisible", e.toString());
            e.printStackTrace();
        }
    }
    
    public void customSetButtonOptionnel01Visible(Boolean isVisible) {
        try 
        {
            this.isButtonOptionnel01Visible = isVisible;
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel01Visible", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel02Visible", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel01Text", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel02Text", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel01Icon", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.customSetButtonOptionnel02Icon", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
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
            MessageDialogHelper.showAlertDialog("TraitementBase.setValue", e.toString());
            e.printStackTrace();
        }
    }     
    
    protected void workingHandleExecuterClick(ClickEvent event) {
    } //protected void workingHandleExecuterClick() {
    
    protected void workingHandleButtonOptionnel01Click(ClickEvent event) {
    } //protected void workingHandleButtonOptionnel01Click() {
    
    protected void workingHandleButtonOptionnel02Click(ClickEvent event) {
    } //protected void workingHandleButtonOptionnel02Click() {
}
