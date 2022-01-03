/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.SerializablePredicate;
import java.util.stream.Stream;



/**
 *
 * @author DISSOU Jamâl-Dine
 */

public class DataProviderHelper {
    //Factory Classe utilisée pour générer le rapport Jasper Report

    /*
    // heck how many items now are displayed
    int filteredSize = dataProvider.size(new Query<>(dataProvider.getFilter()));

    // get the size of all items
    int fullSize = dataProvider.getItems().size(); // this is how you 
    */
    
    /*
ListDataProvider.getFilteredStream(...)
                private Stream<T> getFilteredStream(
                    Query<T, SerializablePredicate<T>> query) {
                  Stream<T> stream = backend.stream();
                  // Apply our own filters first so that query filters never see the items
                  // that would already have been filtered out
                  if (filter != null) {
                    stream = stream.filter(filter);
                  }
                  stream = query.getFilter().map(stream::filter).orElse(stream);
                  return stream;
                }     
    */
    
    /*
    public static <T> Stream<T> getFilteredStream(Query<T, SerializablePredicate<T>> query) {
        try 
        {
                  Stream<T> stream = backend.stream();
                  // Apply our own filters first so that query filters never see the items
                  // that would already have been filtered out
                  if (filter != null) {
                    stream = stream.filter(filter);
                  }
                  stream = query.getFilter().map(stream::filter).orElse(stream);
                  return stream;
        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("DataProviderHelper.getFilteredStream", e.toString());
            e.printStackTrace();
            return null;
        }
   } //public static String getFilteredStream(DataProvider localDate) {
    */
    /*
// everytime after resetting the filter, check how many items now are displayed
    int filteredSize = dataProvider.size(new Query<>(dataProvider.getFilter()));

    int fullSize = dataProvider.getItems().size(); // this is how you get the size of all items    
    */
    public static <T> int fullSize(ListDataProvider<T> dataProvider) {
        try 
        {
            return dataProvider.getItems().size();
        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("DataProviderHelper.fullSize", e.toString());
            e.printStackTrace();
            return 0;
        }
   } //public static int fullSize(DataProvider localDate) {

    public static <T> int filteredSize(ListDataProvider<T> dataProvider) {
        try 
        {
            return dataProvider.size(new Query<>(dataProvider.getFilter()));
        
        } 
        catch (Exception e) 
        {
            MessageDialogHelper.showAlertDialog("DataProviderHelper.filteredSize", e.toString());
            e.printStackTrace();
            return 0;
        }
   } //public static int filteredSize(DataProvider localDate) {

}