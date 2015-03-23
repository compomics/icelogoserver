package com.computationalproteomics.icelogoserver.webapplication;

import com.compomics.icelogo.core.enumeration.ColorScheme;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Davy Maddelein on 16/03/2015.
 */
@WebServlet("/data/colors")
public class ColorSchemeServlet extends HttpServlet {

    static Map<String, Integer[]> presets = new HashMap<String, Integer[]>() {{
        this.put("Standard", new Integer[]{0, 4, 6, 8, 12, 14, 16, 20, 22, 24, 26, 30, 32, 36, 38, 40, 44, 48, 48, 50, 52});
        this.put("Hydrofobicity at pH 7", new Integer[]{24, 20, 63, 53, 40, 37, 0, 1, 50, 1, 10, 52, 60, 44, 46, 33, 32, 9, 1, 0, 13});
    }};


    private static final ColorScheme colorScheme = new ColorScheme();

    public static String[] getPresets() {
        return presets.keySet().toArray(new String[2]);
    }

    public static List<Color> getAllColors() {
        return colorScheme.getColors();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameterMap().containsKey("preset")) {
            resp.setHeader("Context-Type","application/json");
            resp.getWriter().write(ColorSchemeServlet.getColorsFor(req.getParameter("preset")));
        }
    }

    protected static String getColorsFor(String colorSchemeName) {
        return Arrays.stream(presets.get(colorSchemeName))
                .map(e -> {
                    Color aColor = colorScheme.getColors().get(e);
                    return String.format("#%02x%02x%02x", aColor.getRed(), aColor.getGreen(), aColor.getBlue());
                })
                .collect(Collectors.joining(","));
    }
}

