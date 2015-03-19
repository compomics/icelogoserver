package com.computationalproteomics.icelogoserver.webapplication;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Iain on 12/03/2015.
 */
public class Controller extends HttpServlet {
    private static final String[] AMINOS = new String[] {"A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y"};
    private static final String[] COLORS = new String[] {"0000E5", "0E00E4", "1D00E3", "2B00E3", "3900E2", "4800E1", "5600E1", "6400E0", "7200E0", "8000DF", "8E00DE", "9B00DE", "A900DD", "B700DD", "C400DC", "D200DB", "DB00D6", "DA00C8", "DA00BA", "D900AB", "D8009D", "D8008F", "D70080", "D70072", "D60064", "D50056", "D50049", "D4003B", "D4002D", "D30020", "D20012", "D20005", "D10800", "D11500", "D02200", "CF3000", "CF3D00", "CE4A00", "CE5700", "CD6300", "CC7000", "CC7D00", "CB8900", "CB9600", "CAA200", "C9AF00", "C9BB00", "C8C700", "BCC800", "AEC700", "A1C600", "94C600", "87C500", "7AC500", "6DC400", "60C300", "54C300", "47C200", "3AC200", "2EC100", "21C000", "15C000", "09BF00", "00BF03"};
    private HashMap<String, Boolean> menu = new HashMap<>();

    /**
     * Process incoming requests for pages
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(7);    // gotta remove "/pages"
        HashMap<String, Object> data = new HashMap<>();

        if (path.equals("create")) {
            data.put("species", getDataList("/WEB-INF/speciesList.txt"));
            data.put("compositions", getDataList("/WEB-INF/substitutionList.txt"));
            data.put("aa", getDataList("/WEB-INF/aaList.txt"));
            data.put("aminos", AMINOS);
            data.put("colors", COLORS);

            path = "logo.html";
        } else if (path == null || path.length() == 0) {
            // weird olde occasion of "/"
            path = "index.html";
        } else {
            // catch any old links
            if (!path.contains(".html")) {
                path += ".html";
            }
        }

        InputStream template = getServletContext().getResourceAsStream("/" + path);
        InputStreamReader templateReader = new InputStreamReader(template);

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(templateReader, "body");

        menu.put("index", path.equals("index.html"));
        menu.put("create", path.equals("logo.html"));
        menu.put("manual", path.equals("manual.html"));
        menu.put("soap", path.equals("soap.html"));

        data.put("header", getPartial("header", menu));
        data.put("footer", getPartial("footer", null));

        mustache.execute(response.getWriter(), data).flush();
    }

    /**
     * Parse the data list files
     * @param file Path to file
     * @return List of strings
     * @throws IOException
     */
    private List<String> getDataList(String file) throws IOException {
        InputStream is = getServletContext().getResourceAsStream(file);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(reader);

        List<String> data = new ArrayList<>();
        String line = in.readLine();

        while (line != null) {
            data.add(line.substring(0, line.indexOf(" = ")));
            line = in.readLine();
        }

        return data;
    }

    /**
     * Create an HTML partial from a given template and any added data
     * @param name Template name (but not file name)
     * @param data Map of values to use in template (or null)
     * @return Compiled template as String
     * @throws IOException Likely if template file is not found
     */
    private String getPartial(String name, HashMap<String, Boolean> data) throws IOException {
        InputStream stream = getServletContext().getResourceAsStream("/" + name + ".html");
        InputStreamReader reader = new InputStreamReader(stream);

        StringWriter writer = new StringWriter();

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache partial = mf.compile(reader, name);
        partial.execute(writer, data).flush();

        return writer.toString();
    }
}
