package com.progenia.incubatis01_03.views.empty;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.progenia.incubatis01_03.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "empty", layout = MainView.class)
//@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Empty")
//@CssImport("./views/empty/empty-view.css")
public class EmptyView extends Div {

    public EmptyView() {
        addClassName("empty-view");
        add(new Text("Content placeholder"));
    }

}
