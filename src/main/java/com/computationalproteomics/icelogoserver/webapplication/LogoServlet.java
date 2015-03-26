package com.computationalproteomics.icelogoserver.webapplication;


import com.compomics.icelogo.core.aaindex.AAIndexMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexParameterMatrix;
import com.compomics.icelogo.core.aaindex.AAIndexSubstitutionMatrix;
import com.compomics.icelogo.core.data.MainInformationFeeder;
import com.compomics.icelogo.core.data.sequenceset.RawSequenceSet;
import com.compomics.icelogo.core.dbComposition.SwissProtComposition;
import com.compomics.icelogo.core.enumeration.*;
import com.compomics.icelogo.core.factory.AminoAcidStatisticsFactory;
import com.compomics.icelogo.core.interfaces.AminoAcidStatistics;
import com.compomics.icelogo.core.model.OneSampleMatrixDataModel;
import com.compomics.icelogo.gui.graph.*;
import com.compomics.icelogo.core.enumeration.ColorScheme;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.fop.svg.PDFTranscoder;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGDocument;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;


/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 20-okt-2008
 * Time: 16:52:43
 */

@WebServlet("/data/logo")
public class LogoServlet extends HttpServlet {
    /**
     * Boolean that indicates if a Swiss-Prot composition must be used
     */
    private boolean useSwissprot = false;
    /**
     * The scoretype
     */
    private ScoringTypeEnum iScoreType = ScoringTypeEnum.PERCENTAGE;
    /**
     * The colorscheme
     */
    private ColorScheme iColorScheme;
    /**
     * The p-value
     */
    private double iPvalue;
    /**
     * The startposition
     */
    private int iStartPosition;
    /**
     * The height of the y axis
     */
    private int iYaxis;
    /**
     * The experimental set
     */
    private String[] iPositiveSet;
    /**
     * The reference set
     */
    private String[] iNegativeSet;
    /**
     * The species scientific name
     */
    private String iSpeciesName;
    /**
     * The save location
     */
    private String iSaveLocation = null;
    /**
     * An instance of the MainInformationFeeder singleton
     */
    private MainInformationFeeder iInfoFeeder = MainInformationFeeder.getInstance();
    /**
     * An instance of the IceLogoWepAppSingelton
     */
    private IceLogoWepAppSingelton iWiceLogoSingelton = IceLogoWepAppSingelton.getInstance();

    /**
     * This method will load all SwissProtCompositions
     */
    public void loadCompositions() {
        try {
            //get the species list
            InputStream is = getServletContext().getResourceAsStream("/WEB-INF/speciesList.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(reader);
            String strLine;
            Vector<SwissProtComposition> lAllSpecies = new Vector<SwissProtComposition>();
            Vector<SwissProtComposition> lDoneAllSpecies = new Vector<SwissProtComposition>();
            while ((strLine = in.readLine()) != null) {
                lAllSpecies.add(new SwissProtComposition(strLine.substring(strLine.indexOf("= ") + 2), strLine.substring(0, strLine.indexOf(" = "))));
            }

            //read the composition file
            //and link all the compositions to one species from the species list
            InputStream is2 = getServletContext().getResourceAsStream("/WEB-INF/compositions.txt");
            InputStreamReader reader2 = new InputStreamReader(is2);
            BufferedReader in2 = new BufferedReader(reader2);
            String strLine2;
            String aComposition = "";
            boolean aInAComposition = false;
            SwissProtComposition lComposition = null;
            while ((strLine2 = in2.readLine()) != null) {
                if (aInAComposition) {
                    aComposition = aComposition + strLine2 + "\n";
                    if (strLine2.startsWith("total")) {
                        aInAComposition = false;
                        lComposition.setComposition(aComposition);
                        lDoneAllSpecies.add(lComposition);
                        aComposition = "";
                    }
                } else {
                    for (int i = 0; i < lAllSpecies.size(); i++) {
                        if (strLine2.equalsIgnoreCase("//" + lAllSpecies.get(i).getSpecieLink())) {
                            lComposition = lAllSpecies.get(i);
                            lAllSpecies.remove(lComposition);
                            aInAComposition = true;
                            i = lAllSpecies.size();
                        }
                    }
                }
            }
            iWiceLogoSingelton.setCompositions(lDoneAllSpecies);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * This post method gets data from the client and makes a logo for this data
     *
     * @param req The servlet request.
     * @param res The servlet response.
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //Load the save location if necessary
        File saveFolder = new File(this.getRootLocation() + "/generated_images");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        iSaveLocation = saveFolder.getAbsolutePath();
        //Delete old images from the server
        new ImageDeleter(iSaveLocation).start();
        //set the SwissProtCompositions if necessary
        if (!iWiceLogoSingelton.speciesSet()) {
            this.loadCompositions();
        }
        //get start position from request
        iStartPosition = Integer.valueOf(req.getParameter("start"));
        //get p value from request
        iPvalue = Double.valueOf(req.getParameter("pValue"));
        // check if we must use swissprot as a negative set or the negative sequences
        if (req.getParameter("reference").equalsIgnoreCase("swiss-prot")) {
            //use swissprot as negative set
            useSwissprot = true;
            iSpeciesName = req.getParameter("species");

        } else {
            //use the given negative sequences as a negative set and remove any fasta headers and empty lines
            String neg = req.getParameter("negativeSequences");
            neg = neg.replace("\r\n", "\n");
            iNegativeSet = neg.split("\n");
            if (iNegativeSet.length == 1) {
                iNegativeSet = neg.split("\r");
            }
            Arrays.stream(iNegativeSet).filter(e -> !e.startsWith(">") || !e.isEmpty()).collect(Collectors.toList()).toArray(iNegativeSet);

            useSwissprot = false;
        }

        //get the positive sequences and remove any fasta headers and empty lines
        String pos = req.getParameter("positiveSequences");
        pos = pos.replace("\r\n", "\n");
        iPositiveSet = pos.split("\n");
        if (iPositiveSet.length == 1) {
            iPositiveSet = pos.split("\r");
        }
        iPositiveSet = Arrays.stream(iPositiveSet).filter(e -> !e.startsWith(">") || !e.isEmpty()).collect(Collectors.toList()).toArray(new String[iPositiveSet.length]);


        //check scoring type
        if (req.getParameter("scoringSystem").equalsIgnoreCase("foldChange")) {
            iScoreType = ScoringTypeEnum.FOLDCHANGE;
        } else {
            iScoreType = ScoringTypeEnum.PERCENTAGE;
        }

        List<String> tempList = Arrays.stream(req.getParameterValues("colors[]")).map(e -> "0x" + e).collect(Collectors.toList());

        iColorScheme = new ColorScheme(tempList.toArray(new String[21]));

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (String anIPositiveSet : iPositiveSet) {
            lRawPositiveSequenceSet.add(anIPositiveSet);
        }
        AminoAcidStatistics[] lPositiveStatistics;
        // always use the smallest set to do the statistics
        if (req.getParameter("reference").equalsIgnoreCase("reference_set")) {
            if (iPositiveSet.length < iNegativeSet.length) {
                lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, iPositiveSet[0].length(), iPositiveSet.length);
            } else {
                lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, iPositiveSet[0].length(), iNegativeSet.length);
            }
        } else {
            lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, iPositiveSet[0].length(), iPositiveSet.length);
        }

        AminoAcidStatistics[] lNegativeStatistics;
        if (useSwissprot) {
            //create a SwissProtComposition for the used species
            SwissProtComposition swComp = iWiceLogoSingelton.getComposition(iSpeciesName);
            AminoAcidStatistics lNegative = null;
            if (swComp != null) {
                lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(swComp, iPositiveSet.length);
            }

            lNegativeStatistics = new AminoAcidStatistics[iPositiveSet[0].length()];
            for (int i = 0; i < iPositiveSet[0].length(); i++) {
                lNegativeStatistics[i] = lNegative;
            }
        } else {
            //Create a raw sequence set for the negative sequences.
            RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
            for (String anINegativeSet : iNegativeSet) {
                lRawNegativeSequenceSet.add(anINegativeSet);
            }
            // always use the smallest set to do the statistics
            if (iPositiveSet.length < iNegativeSet.length) {
                lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, iPositiveSet[0].length(), iPositiveSet.length);
            } else {
                lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, iPositiveSet[0].length(), iNegativeSet.length);
            }

        }


        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        iInfoFeeder.setStartPosition(iStartPosition);
        iInfoFeeder.setPvalue(iPvalue);
        iInfoFeeder.setColorScheme(iColorScheme);
        iInfoFeeder.setYaxisValue(iYaxis);
        iInfoFeeder.setScoringType(iScoreType);


        SVGDocument lSvg = null;
        boolean lGotSVG = false;
        int lTries = 0;
        //try to get the svg
        while (!lGotSVG && lTries < 5) {
            try {
                lSvg = this.buildSVG(req, dataModel);
                if (lSvg != null) {
                    lGotSVG = true;
                }
            } catch (DOMException e) {
                lGotSVG = false;
            }
            lTries = lTries + 1;
        }


        if (lGotSVG) {

            // save the logo
            try {

                //work around to generate less errors
                //1. create svg string
                TranscoderInput inputFirstSvg = new TranscoderInput(lSvg);
                // Create the transcoder output.
                Transcoder lFirstSVGTranscoder = new SVGTranscoder();
                lFirstSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                lFirstSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
                lFirstSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
                StringWriter lStringWriter = new StringWriter();
                TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
                lFirstSVGTranscoder.transcode(inputFirstSvg, lSVGoutput);
                String lResultSVG = lStringWriter.toString();

                //2. create a svg from this string
                String parser = XMLResourceDescriptor.getXMLParserClassName();
                SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
                InputStream is = null;
                is = new ByteArrayInputStream(lResultSVG.getBytes("UTF-8"));
                SVGDocument lSVGDocument = (SVGDocument) f.createDocument(null, is);

                //3. now write this newly generated svg to the different file types
                String fileLocation = iSaveLocation;
                //The name of the picture will be a time stamp. By this, the filename will always be unique
                String fileName = String.valueOf(System.currentTimeMillis());
                fileLocation = fileLocation + "\\" + fileName;
                //Create the transcoder
                Transcoder lJPEGTranscoder = new JPEGTranscoder();
                // Set the transcoding hints.
                lJPEGTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1.0));
                Transcoder lPNGTranscoder = new PNGTranscoder();
                //300dpi
                lPNGTranscoder.addTranscodingHint(PNGTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));

                Transcoder lTIFFTranscoder = new TIFFTranscoder();
                //300dpi
                lTIFFTranscoder.addTranscodingHint(TIFFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));
                lTIFFTranscoder.addTranscodingHint(TIFFTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);
                Transcoder lPDFTranscoder = new PDFTranscoder();
                //300dpi
                lPDFTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));

                Transcoder lSVGTranscoder = new SVGTranscoder();
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);

                //get input
                TranscoderInput input = new TranscoderInput(lSVGDocument);
                // Create the transcoder output.
                OutputStream lJPEGoutstream = new FileOutputStream(fileLocation + ".jpeg");
                OutputStream lPNGoutstream = new FileOutputStream(fileLocation + ".png");
                OutputStream lTIFFoutstream = new FileOutputStream(fileLocation + ".tiff");
                OutputStream lPDFoutstream = new FileOutputStream(fileLocation + ".pdf");
                FileOutputStream gz = new FileOutputStream(fileLocation + ".svg");

                TranscoderOutput output = new TranscoderOutput(new OutputStreamWriter(gz, "UTF-8"));
                TranscoderOutput lJPEGoutput = new TranscoderOutput(lJPEGoutstream);
                TranscoderOutput lPNGoutput = new TranscoderOutput(lPNGoutstream);
                TranscoderOutput lTIFFoutput = new TranscoderOutput(lTIFFoutstream);
                TranscoderOutput lPDFoutput = new TranscoderOutput(lPDFoutstream);
                // Save the image.
                lJPEGTranscoder.transcode(input, lJPEGoutput);
                // Flush and close the stream.
                lJPEGoutstream.flush();
                lJPEGoutstream.close();

                // set up the response
                res.setContentType("text/xml");
                res.setHeader("Cache-Control", "no-cache");
                // write out the response string to the client
                res.getWriter().write(fileName);

                // Save the image.
                lPNGTranscoder.transcode(input, lPNGoutput);
                // Flush and close the stream.
                lPNGoutstream.flush();
                lPNGoutstream.close();
                // Save the image.
                lTIFFTranscoder.transcode(input, lTIFFoutput);
                // Flush and close the stream.
                lTIFFoutstream.flush();
                lTIFFoutstream.close();
                // Save the image.
                lPDFTranscoder.transcode(input, lPDFoutput);
                // Flush and close the stream.
                lPDFoutstream.flush();
                lPDFoutstream.close();
                // Save the image.
                lSVGTranscoder.transcode(input, output);
                // Flush and close the stream.
                gz.flush();
                gz.close();


            } catch (TranscoderException | IOException e) {
                e.printStackTrace();
            }
        } else {
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            // write out the response string to the client
            res.getWriter().write("Problem creating your logo!");
        }


    }

    /**
     * This method will generate the SVGDocument
     *
     * @param req       The servelt request
     * @param dataModel The icelogo datamodel
     * @return SVGDocument
     */
    public SVGDocument buildSVG(HttpServletRequest req, OneSampleMatrixDataModel dataModel) {
        SVGDocument lSVG = null;
        //set the image size
        iInfoFeeder.setGraphableHeight(Integer.valueOf(req.getParameter("height")));
        iInfoFeeder.setGraphableWidth(Integer.valueOf(req.getParameter("width")));
        //create the SVGDocument depending on the request
        if (req.getParameter("visualisationType").equalsIgnoreCase("iceLogo")) {
            IceLogoComponent logo = new IceLogoComponent(dataModel, false);
            lSVG = logo.getSVG();
        } else if (req.getParameter("visualisationType").equalsIgnoreCase("heatMap")) {
            HeatMapComponent lHeatmap = new HeatMapComponent(dataModel);
            lSVG = lHeatmap.getSVG();
        } else if (req.getParameter("visualisationType").equalsIgnoreCase("filledLogo") || req.getParameter("visualisationType").equalsIgnoreCase("sequenceLogo") ) {
            if (req.getParameter("visualisationType").equalsIgnoreCase("sequenceLogo")) {
                iInfoFeeder.setWeblogoNegativeCorrection(true);
                iInfoFeeder.setScoringType(ScoringTypeEnum.FREQUENCY);
                iInfoFeeder.setFillWeblogo(false);
            } else {
                iInfoFeeder.setWeblogoNegativeCorrection(false);
                iInfoFeeder.setFillWeblogo(true);
            }
            SequenceLogoComponent lSeq = new SequenceLogoComponent(dataModel);
            lSVG = lSeq.getSVG();
        } else if (req.getParameter("visualisationType").equalsIgnoreCase("aaParameter")) {
            String laaParam = req.getParameter("aaMatrix");
            Vector<AAIndexMatrix> lAaParamMatrices = iInfoFeeder.getAaParameterMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                if (lAaParamMatrices.get(i).getTitle().equalsIgnoreCase(laaParam)) {
                    iInfoFeeder.setSelectedAaParameterMatrix((AAIndexParameterMatrix) lAaParamMatrices.get(i));
                    i = lAaParamMatrices.size();
                }
            }
            AAIndexComponent lAaParamGraph = new AAIndexComponent(dataModel);
            lSVG = lAaParamGraph.getSVG();
        } else if (req.getParameter("visualisationType").equalsIgnoreCase("correlationLine")) {
            String lSubMatrix = req.getParameter("substitutionMatrix");
            lSubMatrix = lSubMatrix.replace("*", "'");
            Vector<AAIndexMatrix> lSubMatrices = iInfoFeeder.getSubstitutionMatrixes();
            for (int i = 0; i < lSubMatrices.size(); i++) {
                if (lSubMatrices.get(i).getTitle().equalsIgnoreCase(lSubMatrix)) {
                    iInfoFeeder.setSubstitutionMatrix((AAIndexSubstitutionMatrix) lSubMatrices.get(i));
                    i = lSubMatrices.size();
                }
            }
            ConservationComponent lCorrLine = new ConservationComponent(dataModel);
            lSVG = lCorrLine.getSVG();
        }
        return lSVG;
    }

    public String getRootLocation() {
        return getServletContext().getRealPath("/");
    }
}

