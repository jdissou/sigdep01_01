/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.sigdep01_01.dialogs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jamâl-Dine DISSOU
 */
public abstract class BaseEditerReferentielMaitreTabDialog<T extends Serializable> extends BaseEditerReferentielMaitreDialog<T>{
    
    /* Defines a new FormLayout and a Binder to bind data to the FormLayout. */
    protected Map<Tab, Component> tabsToPages = new HashMap<>();

    protected Tabs tabs = new Tabs();
    protected HorizontalLayout pages = new HorizontalLayout();

    public BaseEditerReferentielMaitreTabDialog() {
        super();
        //Adds the bottom toolbar and the form to the layout
        this.pages.setPadding(false);
        this.pages.setMargin(false);

        this.dialogTitleWrapper.setText(this.dialogTitle);
        this.wrapper.add(this.dialogTitleWrapper, this.topToolBar, this.tabs, this.pages, this.bottomToolBar);        
        this.dialog.add(this.wrapper);
    }
}
