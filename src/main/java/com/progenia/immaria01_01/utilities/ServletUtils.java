/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progenia.immaria01_01.utilities;

import com.progenia.immaria01_01.Application;
import com.vaadin.flow.server.VaadinServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import javax.servlet.ServletContext;

/**
 *
 * @author Jamâl-Dine DISSOU
 */

public class ServletUtils {
    
    //LOGING - Add log statements
    private static final Logger LOGGER=LoggerFactory.getLogger(ServletUtils.class);
 
    public static File getBaseDirectory(VaadinServlet servlet) {
        final String realPath = getResourcePath(servlet.getServletContext(), "/");
        if (realPath == null) {
            return null;
        }
        return new File(realPath);
    }
    
    public static String getResourcePath(ServletContext servletContext,
            String path) {
        String resultPath = null;
        
        resultPath = servletContext.getRealPath(path);
        if (resultPath != null) {
            return resultPath;
        } else {
            try {
                final URL url = servletContext.getResource(path);
                resultPath = url.getFile();
            } catch (final Exception e) {
                // FIXME: Handle exception
                //LOGING - Add log statements
                LOGGER.info("Could not find resource path {}, {}", path, e);
            }
        }
        return resultPath;
    }    

    public static String getContextPath(ServletContext servletContext) {
        String resultPath = null;
        
        resultPath = servletContext.getContextPath();
        return resultPath;
    }    
}
