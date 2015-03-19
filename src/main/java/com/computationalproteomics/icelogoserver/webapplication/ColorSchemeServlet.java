package com.computationalproteomics.icelogoserver.webapplication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Davy Maddelein on 16/03/2015.
 */
public class ColorSchemeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Content-Type","application/json");

        resp.getWriter().write("[\"Standard\",\"Hydrofobicity at pH 7\",\"Size\",\"Charge at pH 7\"]");

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameterMap().containsKey("colorscheme")){
            resp.getWriter().write(ColorSchemeServlet.getColorsFor(req.getParameter("colorscheme")));
        }
    }

    protected static String getColorsFor(String colorscheme) {

        if(colorscheme.equals("Standard")){
            return "[0,4,6,8,12,14,16,20,22,24,26,30,32,36,38,40,44,48,48,50]";
        }
         else if (colorscheme.equals("Hydrofobicity at pH 7")){
            return "[24,20,64,53,40,37,0,1,50,1,10,52,60,44,46,33,32,9,1,13]";
        }
        else if (colorscheme.equals("Size")){
            return",,,,,,,,,,,,,,,,,,,,,,,,,,,";
        } else if (colorscheme.equals("Charge at pH 7")){
            return",,,,,,,,,,,,,,,,,,,,";
        }
        else{
            return"invalid color option";
        }
    }
}
