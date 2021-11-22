/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.incubatis01_03.utilities;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

/**
 *
 * @author Jamâl-Dine DISSOU
 */


@Tag("object")
public class ReportEmbeddedPdfDocument extends Component implements HasSize {

    public ReportEmbeddedPdfDocument(StreamResource resource) {
        this();
        getElement().setAttribute("data", resource);
    }

    public ReportEmbeddedPdfDocument(String url) {
        this();
        getElement().setAttribute("data", url);
    }

    protected ReportEmbeddedPdfDocument() {
        getElement().setAttribute("type", "application/pdf");
        setSizeFull();
    }
}