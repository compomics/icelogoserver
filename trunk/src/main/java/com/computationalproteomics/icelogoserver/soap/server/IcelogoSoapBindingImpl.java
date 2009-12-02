/**
 * IcelogoSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.computationalproteomics.icelogoserver.soap.server;

import be.proteomics.logo.core.aaindex.AAIndexMatrix;
import be.proteomics.logo.core.aaindex.AAIndexParameterMatrix;
import be.proteomics.logo.core.aaindex.AAIndexSubstitutionMatrix;
import be.proteomics.logo.core.data.MainInformationFeeder;
import be.proteomics.logo.core.data.MatrixAminoAcidStatistics;
import be.proteomics.logo.core.data.sequenceset.FastaSequenceSet;
import be.proteomics.logo.core.data.sequenceset.RawSequenceSet;
import be.proteomics.logo.core.data.sequenceset.RegionalFastaSequenceSet;
import be.proteomics.logo.core.dbComposition.SwissProtComposition;
import be.proteomics.logo.core.enumeration.*;
import be.proteomics.logo.core.factory.AminoAcidStatisticsFactory;
import be.proteomics.logo.core.interfaces.AminoAcidStatistics;
import be.proteomics.logo.core.interfaces.ISequenceSet;
import be.proteomics.logo.core.interfaces.MatrixDataModel;
import be.proteomics.logo.core.model.OneSampleMatrixDataModel;
import be.proteomics.logo.core.model.TwoSampleMatrixDataModel;
import be.proteomics.logo.gui.graph.*;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.w3c.dom.svg.SVGDocument;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public class IcelogoSoapBindingImpl implements com.computationalproteomics.icelogoserver.soap.server.IceLogo{
    private Vector<SwissProtComposition> iAllSpecies = null;
    private String iLocation = null;
    private SoapServerSingelton iSoapServerSingleton = SoapServerSingelton.getInstance();


    public String[] getAvailableAaParameters(){
        Vector<String> lResult = new Vector<String>();
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        Vector<AAIndexMatrix> lAaParamMatrices = lInfoFeeder.getAaParameterMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                lResult.add(lAaParamMatrices.get(i).getTitle());
            }
        String[] lResultArray = new String[lResult.size()];
        lResult.toArray(lResultArray);
        return lResultArray;
    }

    public String[] getAvailableSubstitutionMatrices(){
        Vector<String> lResult = new Vector<String>();
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        Vector<AAIndexMatrix> lAaParamMatrices = lInfoFeeder.getSubstitutionMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                lResult.add(lAaParamMatrices.get(i).getTitle());
            }
        String[] lResultArray = new String[lResult.size()];
        lResult.toArray(lResultArray);
        return lResultArray;
    }

    public String testService(String lTestString){
        return "The service is running, your test string was : " + lTestString;
    }

    private void readProperties() throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("soap.properties");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(reader);
        String strLine;
        while ((strLine = in.readLine()) != null) {
            if (strLine.startsWith("location")) {
                iLocation = strLine.substring(strLine.indexOf("=") + 2);
            }
        }
    }

    public String getSamplingIceLogo(String[] lExperimentalSet, String[] lExperimentalSet2, String lSpecies, String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth, String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, String lSamplingDirection) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        int lLength;
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        int lLength2;
        boolean lUseTwoSets = false;
        if (lExperimentalSet2 != null && lExperimentalSet2.length > 0) {
            lUseTwoSets = true;
            lLength2 = lExperimentalSet2[0].length();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                if (lLength2 != lExperimentalSet2[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet2[i] + " in the second experimental set has an incorrect length (" + lExperimentalSet2[i].length() + " != " + lLength2 + ")!");
                }
            }
            if (lLength != lLength2) {
                throw new Exception( "The sequences in the first sequence set were not of the same length as the sequences in the second experimental sequence set!");
            }
        }
        //check the sampling type
        if (lSamplingType != null) {
            if (lSamplingType.equalsIgnoreCase("terminal") || lSamplingType.equalsIgnoreCase("regional") || lSamplingType.equalsIgnoreCase("random")) {
                //recognized
            } else {
                throw new Exception( "The sampling type \"" + lSamplingType + "\" was not recognized (it must be \"terminal\", \"regional\" or \"random\")!");
            }
        } else {
            throw new Exception( "The sampling type was null!");
        }
        //check sampling position
        if (lSamplingType.equalsIgnoreCase("regional")) {
            if (lSamplingPosition < 0 || lSamplingPosition >= lLength) {
                throw new Exception( "The sampling position " + lSamplingPosition + " was not between 0 and " + lLength + " !");
            }
        }
        //check anchor position
        if (lSamplingType.equalsIgnoreCase("terminal")) {
            if (lAnchorPosition <= 1) {
                throw new Exception( "The anchor position " + lAnchorPosition + " must be larger than 0 !");
            }
            //check the sampling direction
            if (lSamplingDirection != null) {
                if (!lSamplingDirection.equalsIgnoreCase("NtoC") || !lSamplingDirection.equalsIgnoreCase("CtoN")) {
                    throw new Exception( "The sampling direction \"" + lSamplingType + "\" was not recognized (it must be \"NtoC\" or \"CtoN\")!");
                }
            } else {
                throw new Exception( "The sampling type was null!");
            }
        }

        //check sampling iterations
        if (lSamplingIterations <= 0) {
            throw new Exception( "The sampling iterations " + lSamplingIterations + " must be larger than 0 !");
        }

        //check the Y axis dimension
        if (lYaxis < 1) {
            throw new Exception( "The Y axis dimension cannot be smaller than one!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }
        //check the scoring type
        if (!lScoringType.equalsIgnoreCase("foldchange") && !lScoringType.equalsIgnoreCase("percentage")) {
            throw new Exception( "The scoring type must be \"foldchange\" or \"percentage\" !");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        //find or create the fastafile
        if (iLocation == null) {
            try {
                this.readProperties();
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }
        File fasta = new File(iLocation + lSpecies + ".fasta");

        if (!fasta.exists()) {
            //it does not exists
            //we have to create one

            try {
                System.out.println("\nCreating database " + lSpecies + ".fasta\n");
                //check if we can find uniprot
                File lUniprotfasta = new File(iLocation + "uniprot_sprot.fasta");
                if(!lUniprotfasta.exists()){
                    //check if the server is writting a Database
                    if(iSoapServerSingleton.isWritingDatabase()){
                        throw new Exception("The iceLogoserver is downloading and saving a new uniprot database, please retry in a few minutes!");
                    } else {
                        //we could not find uniprot we will download it from the ebi ftp server
                        try {
                            iSoapServerSingleton.setWritingDatabase(true);
                            String lUniprotFileName = iLocation + "uniprot_sprot.fasta";

                            //create the writer
                            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(lUniprotFileName)));
                            //create the correct url connection
                            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");

                            URLConnection urlcDbDownload = urlDbDownload.openConnection();
                            InputStream isDbDownload = urlcDbDownload.getInputStream();
                            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);

                            System.out.println("Downloading and unzipping the database");

                            int lProteinCounter = 0;
                            int entry;
                            String lHeader = "";
                            String lSequence = "";
                            boolean lParsingHeader = false;
                            boolean lParsingSequence = false;
                            while ((entry = lZippedInputstream.read()) != -1) {
                                if ((char) entry == '\n') {
                                    if (lParsingHeader) {
                                        lParsingHeader = false;
                                        lParsingSequence = true;
                                    }
                                } else if ((char) entry == '>') {
                                    if (lParsingSequence) {
                                        lParsingHeader = true;
                                        lParsingSequence = false;
                                        lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                        lHeader = "";
                                        lSequence = "";
                                        lProteinCounter++;
                                        if (lProteinCounter % 500 == 0) {
                                            System.out.print(".");
                                            lDbOutputWriter.flush();
                                        }
                                        if (lProteinCounter % 40000 == 0) {
                                            System.out.print("\n");
                                            lDbOutputWriter.flush();
                                        }
                                    }
                                    lParsingSequence = false;
                                    lParsingHeader = true;
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingHeader) {
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingSequence) {
                                    lSequence = lSequence + (char) entry;
                                }
                            }
                            //flush and close the db
                            if(lHeader.length() != 0 && lSequence.length() != 0){
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                            }
                            lDbOutputWriter.flush();
                            lDbOutputWriter.close();
                            System.out.println("\nDone downloading and unzipping");
                            iSoapServerSingleton.setWritingDatabase(false);
                        } catch (MalformedURLException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (IOException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (Exception e){
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                        }
                    }
                }

                //create the reader
                InputStream is = new FileInputStream(new File(iLocation + "uniprot_sprot.fasta"));
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(reader);
                //create the writer
                BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iLocation + lSpecies + ".fasta")));

                String line = "";
                String lHeader = "";
                String lSequence = "";
                int lCounter = 0;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith(">")) {
                        lHeader = line;
                        //write the sequence if it is the correct species
                        if (lSequence.length() > 0) {
                            boolean lUse = false;
                            String lParsedTaxonomy;
                            if (lHeader.indexOf(" GN=") > 0) {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" GN="));
                            } else {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" PE="));
                            }
                            if (lParsedTaxonomy.equalsIgnoreCase(lSpecies)) {
                                lUse = true;
                            } else {
                                lUse = false;
                            }
                            if (lUse) {
                                lCounter = lCounter + 1;
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                if (lCounter % 100 == 0) {
                                    System.out.print(".");
                                }
                                if (lCounter % 2000 == 0) {
                                    System.out.print("\n");
                                }
                            }
                        }
                        lSequence = "";
                    } else {
                        lSequence = lSequence + line;
                    }
                }
                lDbOutputWriter.flush();
                lDbOutputWriter.close();
                fasta = new File(iLocation + lSpecies + ".fasta");

            } catch (FileNotFoundException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }

        //check if the file exists
        //now we have the correct fasta file
        FastaSequenceSet lSequenceSet;
        lSequenceSet = new FastaSequenceSet(fasta.getAbsolutePath(), fasta.getName());
        if (lSequenceSet.test(fasta)) {
            lInfoFeeder.getInstance().setActiveReferenceSet(lSequenceSet);
        } else {
            throw new Exception( "There was a problem reading and writing the correct fasta file!");
        }
        lInfoFeeder.setIterationSize(lSamplingIterations);

        SamplingTypeEnum lSampling = null;
        if (lSamplingType.equalsIgnoreCase("regional")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.REGIONAL);
            lInfoFeeder.setExperimentAnchorValue(lSamplingPosition);
            lSampling = SamplingTypeEnum.REGIONAL;
        } else if (lSamplingType.equalsIgnoreCase("random")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.RANDOM);
            lSampling = SamplingTypeEnum.RANDOM;
        } else if (lSamplingType.equalsIgnoreCase("terminal")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.TERMINAL);
            lSampling = SamplingTypeEnum.TERMINAL;
            lInfoFeeder.setAnchorStartPosition(lAnchorPosition);
            if (lSamplingDirection.equalsIgnoreCase("NtoC")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.NtermToCterm);
            } else if (lSamplingDirection.equalsIgnoreCase("CtoN")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.CtermToNterm);
            }
        }

        RawSequenceSet lPositiveSet = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        lPositiveSet.clearContent();
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lPositiveSet.add(lExperimentalSet[i]);
        }
        RawSequenceSet lPositiveSet2 = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        if (lUseTwoSets) {
            lPositiveSet2.clearContent();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                lPositiveSet2.add(lExperimentalSet2[i]);
            }
            lInfoFeeder.setPositionSampleSize((lPositiveSet.getNumberOfSequences() + lPositiveSet2.getNumberOfSequences()) / 2);
            lInfoFeeder.setTwoExperiment(true);
        } else {
            lInfoFeeder.setPositionSampleSize(lPositiveSet.getNumberOfSequences());
        }


        // The final lModel to be created.
        String lReferenceID = lInfoFeeder.getActiveReferenceSet().getID();
        AminoAcidStatistics[] lPositionStatistics = createPositionStatistics();
        AminoAcidStatistics[] lPositionTwoStatistics = new AminoAcidStatistics[0];
        if (lUseTwoSets) {
            lPositionTwoStatistics = createPositionTwoStatistics();
        }
        AminoAcidStatistics[] lReferenceStatistics = null;
        lReferenceStatistics = createReferenceStatistics(lReferenceStatistics, lSampling);

        //create the datamodel
        // Create a OneSampleMatrixDataModel
        MatrixDataModel lModel;
        if (lUseTwoSets) {
            lModel = new TwoSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lPositionTwoStatistics, lReferenceID);
        } else {
            lModel = new OneSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lReferenceID);
        }

        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);
        lInfoFeeder.setYaxisValue(lYaxis);
        ScoringTypeEnum lScoreType;
        if (lScoringType.equalsIgnoreCase("foldchange")) {
            lScoreType = ScoringTypeEnum.FOLDCHANGE;
        } else {
            lScoreType = ScoringTypeEnum.PERCENTAGE;
        }
        lInfoFeeder.setScoringType(lScoreType);

        IceLogoComponent logo = new IceLogoComponent(lModel, false);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getSamplingHeatMap(String[] lExperimentalSet, String[] lExperimentalSet2, String lSpecies, int lStartPosition, double lPvalue, int lHeight, int lWidth, String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, String lSamplingDirection) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        int lLength;
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        int lLength2;
        boolean lUseTwoSets = false;
        if (lExperimentalSet2 != null && lExperimentalSet2.length > 0) {
            lUseTwoSets = true;
            lLength2 = lExperimentalSet2[0].length();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                if (lLength2 != lExperimentalSet2[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet2[i] + " in the second experimental set has an incorrect length (" + lExperimentalSet2[i].length() + " != " + lLength2 + ")!");
                }
            }
            if (lLength != lLength2) {
                throw new Exception( "The sequences in the first sequence set were not of the same length as the sequences in the second experimental sequence set!");
            }
        }
        //check the sampling type
        if (lSamplingType != null) {
            if (lSamplingType.equalsIgnoreCase("terminal") || lSamplingType.equalsIgnoreCase("regional") || lSamplingType.equalsIgnoreCase("random")) {
                //recognized
            } else {
                throw new Exception( "The sampling type \"" + lSamplingType + "\" was not recognized (it must be \"terminal\", \"regional\" or \"random\")!");
            }
        } else {
            throw new Exception( "The sampling type was null!");
        }
        //check sampling position
        if (lSamplingType.equalsIgnoreCase("regional")) {
            if (lSamplingPosition < 0 || lSamplingPosition >= lLength) {
                throw new Exception( "The sampling position " + lSamplingPosition + " was not between 0 and " + lLength + " !");
            }
        }
        //check anchor position
        if (lSamplingType.equalsIgnoreCase("terminal")) {
            if (lAnchorPosition <= 1) {
                throw new Exception( "The anchor position " + lAnchorPosition + " must be larger than 0 !");
            }
            //check the sampling direction
            if (lSamplingDirection != null) {
                if (!lSamplingDirection.equalsIgnoreCase("NtoC") || !lSamplingDirection.equalsIgnoreCase("CtoN")) {
                    throw new Exception( "The sampling direction \"" + lSamplingType + "\" was not recognized (it must be \"NtoC\" or \"CtoN\")!");
                }
            } else {
                throw new Exception( "The sampling type was null!");
            }
        }

        //check sampling iterations
        if (lSamplingIterations <= 0) {
            throw new Exception( "The sampling iterations " + lSamplingIterations + " must be larger than 0 !");
        }

        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        //find or create the fastafile
        if (iLocation == null) {
            try {
                this.readProperties();
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }
        File fasta = new File(iLocation + lSpecies + ".fasta");

        if (!fasta.exists()) {
            //it does not exists
            //we have to create one

            try {
                System.out.println("\nCreating database " + lSpecies + ".fasta\n");
                //check if we can find uniprot
                File lUniprotfasta = new File(iLocation + "uniprot_sprot.fasta");
                if(!lUniprotfasta.exists()){
                    //check if the server is writting a Database
                    if(iSoapServerSingleton.isWritingDatabase()){
                        throw new Exception("The iceLogoserver is downloading and saving a new uniprot database, please retry in a few minutes!");
                    } else {
                        //we could not find uniprot we will download it from the ebi ftp server
                        try {
                            iSoapServerSingleton.setWritingDatabase(true);
                            String lUniprotFileName = iLocation + "uniprot_sprot.fasta";

                            //create the writer
                            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(lUniprotFileName)));
                            //create the correct url connection
                            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");

                            URLConnection urlcDbDownload = urlDbDownload.openConnection();
                            InputStream isDbDownload = urlcDbDownload.getInputStream();
                            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);

                            System.out.println("Downloading and unzipping the database");

                            int lProteinCounter = 0;
                            int entry;
                            String lHeader = "";
                            String lSequence = "";
                            boolean lParsingHeader = false;
                            boolean lParsingSequence = false;
                            while ((entry = lZippedInputstream.read()) != -1) {
                                if ((char) entry == '\n') {
                                    if (lParsingHeader) {
                                        lParsingHeader = false;
                                        lParsingSequence = true;
                                    }
                                } else if ((char) entry == '>') {
                                    if (lParsingSequence) {
                                        lParsingHeader = true;
                                        lParsingSequence = false;
                                        lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                        lHeader = "";
                                        lSequence = "";
                                        lProteinCounter++;
                                        if (lProteinCounter % 500 == 0) {
                                            System.out.print(".");
                                            lDbOutputWriter.flush();
                                        }
                                        if (lProteinCounter % 40000 == 0) {
                                            System.out.print("\n");
                                            lDbOutputWriter.flush();
                                        }
                                    }
                                    lParsingSequence = false;
                                    lParsingHeader = true;
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingHeader) {
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingSequence) {
                                    lSequence = lSequence + (char) entry;
                                }
                            }
                            //flush and close the db
                            if(lHeader.length() != 0 && lSequence.length() != 0){
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                            }
                            lDbOutputWriter.flush();
                            lDbOutputWriter.close();
                            System.out.println("\nDone downloading and unzipping");
                            iSoapServerSingleton.setWritingDatabase(false);
                        } catch (MalformedURLException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (IOException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (Exception e){
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                        }
                    }
                }
                //create the reader
                InputStream is = new FileInputStream(new File(iLocation + "uniprot_sprot.fasta"));
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(reader);
                //create the writer
                BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iLocation + lSpecies + ".fasta")));

                String line = "";
                String lHeader = "";
                String lSequence = "";
                int lCounter = 0;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith(">")) {
                        lHeader = line;
                        //write the sequence if it is the correct species
                        if (lSequence.length() > 0) {
                            boolean lUse = false;
                            String lParsedTaxonomy;
                            if (lHeader.indexOf(" GN=") > 0) {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" GN="));
                            } else {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" PE="));
                            }
                            if (lParsedTaxonomy.equalsIgnoreCase(lSpecies)) {
                                lUse = true;
                            } else {
                                lUse = false;
                            }
                            if (lUse) {
                                lCounter = lCounter + 1;
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                if (lCounter % 100 == 0) {
                                    System.out.print(".");
                                }
                                if (lCounter % 2000 == 0) {
                                    System.out.print("\n");
                                }
                            }
                        }
                        lSequence = "";
                    } else {
                        lSequence = lSequence + line;
                    }
                }
                lDbOutputWriter.flush();
                lDbOutputWriter.close();
                fasta = new File(iLocation + lSpecies + ".fasta");

            } catch (FileNotFoundException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }

        //check if the file exists
        //now we have the correct fasta file
        FastaSequenceSet lSequenceSet;
        lSequenceSet = new FastaSequenceSet(fasta.getAbsolutePath(), fasta.getName());
        if (lSequenceSet.test(fasta)) {
            lInfoFeeder.getInstance().setActiveReferenceSet(lSequenceSet);
        } else {
            throw new Exception( "There was a problem reading and writing the correct fasta file!");
        }
        lInfoFeeder.setIterationSize(lSamplingIterations);

        SamplingTypeEnum lSampling = null;
        if (lSamplingType.equalsIgnoreCase("regional")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.REGIONAL);
            lInfoFeeder.setExperimentAnchorValue(lSamplingPosition);
            lSampling = SamplingTypeEnum.REGIONAL;

        } else if (lSamplingType.equalsIgnoreCase("random")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.RANDOM);
            lSampling = SamplingTypeEnum.RANDOM;
        } else if (lSamplingType.equalsIgnoreCase("terminal")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.TERMINAL);
            lSampling = SamplingTypeEnum.TERMINAL;
            lInfoFeeder.setAnchorStartPosition(lAnchorPosition);
            if (lSamplingDirection.equalsIgnoreCase("NtoC")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.NtermToCterm);
            } else if (lSamplingDirection.equalsIgnoreCase("CtoN")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.CtermToNterm);
            }
        }

        RawSequenceSet lPositiveSet = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        lPositiveSet.clearContent();
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lPositiveSet.add(lExperimentalSet[i]);
        }
        RawSequenceSet lPositiveSet2 = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        if (lUseTwoSets) {
            lPositiveSet2.clearContent();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                lPositiveSet2.add(lExperimentalSet2[i]);
            }
            lInfoFeeder.setPositionSampleSize((lPositiveSet.getNumberOfSequences() + lPositiveSet2.getNumberOfSequences()) / 2);
            lInfoFeeder.setTwoExperiment(true);
        } else {
            lInfoFeeder.setPositionSampleSize(lPositiveSet.getNumberOfSequences());
        }


        // The final lModel to be created.
        String lReferenceID = lInfoFeeder.getActiveReferenceSet().getID();
        AminoAcidStatistics[] lPositionStatistics = createPositionStatistics();
        AminoAcidStatistics[] lPositionTwoStatistics = new AminoAcidStatistics[0];
        if (lUseTwoSets) {
            lPositionTwoStatistics = createPositionTwoStatistics();
        }
        AminoAcidStatistics[] lReferenceStatistics = null;
        lReferenceStatistics = createReferenceStatistics(lReferenceStatistics, lSampling);

        //create the datamodel
        // Create a OneSampleMatrixDataModel
        MatrixDataModel lModel;
        if (lUseTwoSets) {
            lModel = new TwoSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lPositionTwoStatistics, lReferenceID);
        } else {
            lModel = new OneSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lReferenceID);
        }

        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        HeatMapComponent logo = new HeatMapComponent(lModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getSamplingAaParameterGraph(String[] lExperimentalSet, String[] lExperimentalSet2, String lSpecies, String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth, String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, String lSamplingDirection) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        int lLength;
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        int lLength2;
        boolean lUseTwoSets = false;
        if (lExperimentalSet2 != null && lExperimentalSet2.length > 0) {
            lUseTwoSets = true;
            lLength2 = lExperimentalSet2[0].length();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                if (lLength2 != lExperimentalSet2[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet2[i] + " in the second experimental set has an incorrect length (" + lExperimentalSet2[i].length() + " != " + lLength2 + ")!");
                }
            }
            if (lLength != lLength2) {
                throw new Exception( "The sequences in the first sequence set were not of the same length as the sequences in the second experimental sequence set!");
            }
        }
        //check the sampling type
        if (lSamplingType != null) {
            if (lSamplingType.equalsIgnoreCase("terminal") || lSamplingType.equalsIgnoreCase("regional") || lSamplingType.equalsIgnoreCase("random")) {
                //recognized
            } else {
                throw new Exception( "The sampling type \"" + lSamplingType + "\" was not recognized (it must be \"terminal\", \"regional\" or \"random\")!");
            }
        } else {
            throw new Exception( "The sampling type was null!");
        }
        //check sampling position
        if (lSamplingType.equalsIgnoreCase("regional")) {
            if (lSamplingPosition < 0 || lSamplingPosition >= lLength) {
                throw new Exception( "The sampling position " + lSamplingPosition + " was not between 0 and " + lLength + " !");
            }
        }
        //check anchor position
        if (lSamplingType.equalsIgnoreCase("terminal")) {
            if (lAnchorPosition <= 1) {
                throw new Exception( "The anchor position " + lAnchorPosition + " must be larger than 0 !");
            }
            //check the sampling direction
            if (lSamplingDirection != null) {
                if (!lSamplingDirection.equalsIgnoreCase("NtoC") || !lSamplingDirection.equalsIgnoreCase("CtoN")) {
                    throw new Exception( "The sampling direction \"" + lSamplingType + "\" was not recognized (it must be \"NtoC\" or \"CtoN\")!");
                }
            } else {
                throw new Exception( "The sampling type was null!");
            }
        }

        //check sampling iterations
        if (lSamplingIterations <= 0) {
            throw new Exception( "The sampling iterations " + lSamplingIterations + " must be larger than 0 !");
        }

        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }


        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        //check the aaparam title
        boolean lParamSet = false;
        if (lAaMatrixTitle != null) {
            Vector<AAIndexMatrix> lAaParamMatrices = lInfoFeeder.getAaParameterMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                if (lAaParamMatrices.get(i).getTitle().equalsIgnoreCase(lAaMatrixTitle)) {
                    lInfoFeeder.setSelectedAaParameterMatrix((AAIndexParameterMatrix) lAaParamMatrices.get(i));
                    i = lAaParamMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The amino acid parameter matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The amino acid parameter matrix \"" + lAaMatrixTitle + "\" could not be recognized!");
        }

        //find or create the fastafile
        if (iLocation == null) {
            try {
                this.readProperties();
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }
        File fasta = new File(iLocation + lSpecies + ".fasta");

        if (!fasta.exists()) {
            //it does not exists
            //we have to create one

            try {
                System.out.println("\nCreating database " + lSpecies + ".fasta\n");
                //check if we can find uniprot
                File lUniprotfasta = new File(iLocation + "uniprot_sprot.fasta");
                if(!lUniprotfasta.exists()){
                    //check if the server is writting a Database
                    if(iSoapServerSingleton.isWritingDatabase()){
                        throw new Exception("The iceLogoserver is downloading and saving a new uniprot database, please retry in a few minutes!");
                    } else {
                        //we could not find uniprot we will download it from the ebi ftp server
                        try {
                            iSoapServerSingleton.setWritingDatabase(true);
                            String lUniprotFileName = iLocation + "uniprot_sprot.fasta";

                            //create the writer
                            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(lUniprotFileName)));
                            //create the correct url connection
                            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");

                            URLConnection urlcDbDownload = urlDbDownload.openConnection();
                            InputStream isDbDownload = urlcDbDownload.getInputStream();
                            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);

                            System.out.println("Downloading and unzipping the database");

                            int lProteinCounter = 0;
                            int entry;
                            String lHeader = "";
                            String lSequence = "";
                            boolean lParsingHeader = false;
                            boolean lParsingSequence = false;
                            while ((entry = lZippedInputstream.read()) != -1) {
                                if ((char) entry == '\n') {
                                    if (lParsingHeader) {
                                        lParsingHeader = false;
                                        lParsingSequence = true;
                                    }
                                } else if ((char) entry == '>') {
                                    if (lParsingSequence) {
                                        lParsingHeader = true;
                                        lParsingSequence = false;
                                        lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                        lHeader = "";
                                        lSequence = "";
                                        lProteinCounter++;
                                        if (lProteinCounter % 500 == 0) {
                                            System.out.print(".");
                                            lDbOutputWriter.flush();
                                        }
                                        if (lProteinCounter % 40000 == 0) {
                                            System.out.print("\n");
                                            lDbOutputWriter.flush();
                                        }
                                    }
                                    lParsingSequence = false;
                                    lParsingHeader = true;
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingHeader) {
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingSequence) {
                                    lSequence = lSequence + (char) entry;
                                }
                            }
                            //flush and close the db
                            if(lHeader.length() != 0 && lSequence.length() != 0){
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                            }
                            lDbOutputWriter.flush();
                            lDbOutputWriter.close();
                            System.out.println("\nDone downloading and unzipping");
                            iSoapServerSingleton.setWritingDatabase(false);
                        } catch (MalformedURLException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (IOException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (Exception e){
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                        }
                    }
                }
                //create the reader
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("uniprot_sprot.fasta");
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(reader);
                //create the writer
                BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iLocation + lSpecies + ".fasta")));

                String line = "";
                String lHeader = "";
                String lSequence = "";
                int lCounter = 0;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith(">")) {
                        lHeader = line;
                        //write the sequence if it is the correct species
                        if (lSequence.length() > 0) {
                            boolean lUse = false;
                            String lParsedTaxonomy;
                            if (lHeader.indexOf(" GN=") > 0) {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" GN="));
                            } else {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" PE="));
                            }
                            if (lParsedTaxonomy.equalsIgnoreCase(lSpecies)) {
                                lUse = true;
                            } else {
                                lUse = false;
                            }
                            if (lUse) {
                                lCounter = lCounter + 1;
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                if (lCounter % 100 == 0) {
                                    System.out.print(".");
                                }
                                if (lCounter % 2000 == 0) {
                                    System.out.print("\n");
                                }
                            }
                        }
                        lSequence = "";
                    } else {
                        lSequence = lSequence + line;
                    }
                }
                lDbOutputWriter.flush();
                lDbOutputWriter.close();
                fasta = new File(iLocation + lSpecies + ".fasta");

            } catch (FileNotFoundException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }

        //check if the file exists
        //now we have the correct fasta file
        FastaSequenceSet lSequenceSet;
        lSequenceSet = new FastaSequenceSet(fasta.getAbsolutePath(), fasta.getName());
        if (lSequenceSet.test(fasta)) {
            lInfoFeeder.getInstance().setActiveReferenceSet(lSequenceSet);
        } else {
            throw new Exception( "There was a problem reading and writing the correct fasta file!");
        }
        lInfoFeeder.setIterationSize(lSamplingIterations);

        SamplingTypeEnum lSampling = null;
        if (lSamplingType.equalsIgnoreCase("regional")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.REGIONAL);
            lInfoFeeder.setExperimentAnchorValue(lSamplingPosition);
            lSampling = SamplingTypeEnum.REGIONAL;
        } else if (lSamplingType.equalsIgnoreCase("random")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.RANDOM);
            lSampling = SamplingTypeEnum.RANDOM;
        } else if (lSamplingType.equalsIgnoreCase("terminal")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.TERMINAL);
            lSampling = SamplingTypeEnum.TERMINAL;
            lInfoFeeder.setAnchorStartPosition(lAnchorPosition);
            if (lSamplingDirection.equalsIgnoreCase("NtoC")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.NtermToCterm);
            } else if (lSamplingDirection.equalsIgnoreCase("CtoN")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.CtermToNterm);
            }
        }

        RawSequenceSet lPositiveSet = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        lPositiveSet.clearContent();
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lPositiveSet.add(lExperimentalSet[i]);
        }
        RawSequenceSet lPositiveSet2 = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        if (lUseTwoSets) {
            lPositiveSet2.clearContent();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                lPositiveSet2.add(lExperimentalSet2[i]);
            }
            lInfoFeeder.setPositionSampleSize((lPositiveSet.getNumberOfSequences() + lPositiveSet2.getNumberOfSequences()) / 2);
            lInfoFeeder.setTwoExperiment(true);
        } else {
            lInfoFeeder.setPositionSampleSize(lPositiveSet.getNumberOfSequences());
        }


        // The final lModel to be created.
        String lReferenceID = lInfoFeeder.getActiveReferenceSet().getID();
        AminoAcidStatistics[] lPositionStatistics = createPositionStatistics();
        AminoAcidStatistics[] lPositionTwoStatistics = new AminoAcidStatistics[0];
        if (lUseTwoSets) {
            lPositionTwoStatistics = createPositionTwoStatistics();
        }
        AminoAcidStatistics[] lReferenceStatistics = null;
        lReferenceStatistics = createReferenceStatistics(lReferenceStatistics, lSampling);

        //create the datamodel
        // Create a OneSampleMatrixDataModel
        MatrixDataModel lModel;
        if (lUseTwoSets) {
            lModel = new TwoSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lPositionTwoStatistics, lReferenceID);
        } else {
            lModel = new OneSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lReferenceID);
        }

        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        AAIndexComponent logo = new AAIndexComponent(lModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getSamplingConservationLine(String[] lExperimentalSet, String[] lExperimentalSet2, String lSpecies, String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth, String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, String lSamplingDirection) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        int lLength;
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        int lLength2;
        boolean lUseTwoSets = false;
        if (lExperimentalSet2 != null && lExperimentalSet2.length > 0) {
            lUseTwoSets = true;
            lLength2 = lExperimentalSet2[0].length();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                if (lLength2 != lExperimentalSet2[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet2[i] + " in the second experimental set has an incorrect length (" + lExperimentalSet2[i].length() + " != " + lLength2 + ")!");
                }
            }
            if (lLength != lLength2) {
                throw new Exception( "The sequences in the first sequence set were not of the same length as the sequences in the second experimental sequence set!");
            }
        }
        //check the sampling type
        if (lSamplingType != null) {
            if (lSamplingType.equalsIgnoreCase("terminal") || lSamplingType.equalsIgnoreCase("regional") || lSamplingType.equalsIgnoreCase("random")) {
                //recognized
            } else {
                throw new Exception( "The sampling type \"" + lSamplingType + "\" was not recognized (it must be \"terminal\", \"regional\" or \"random\")!");
            }
        } else {
            throw new Exception( "The sampling type was null!");
        }
        //check sampling position
        if (lSamplingType.equalsIgnoreCase("regional")) {
            if (lSamplingPosition < 0 || lSamplingPosition >= lLength) {
                throw new Exception( "The sampling position " + lSamplingPosition + " was not between 0 and " + lLength + " !");
            }
        }
        //check anchor position
        if (lSamplingType.equalsIgnoreCase("terminal")) {
            if (lAnchorPosition <= 1) {
                throw new Exception( "The anchor position " + lAnchorPosition + " must be larger than 0 !");
            }
            //check the sampling direction
            if (lSamplingDirection != null) {
                if (!lSamplingDirection.equalsIgnoreCase("NtoC") || !lSamplingDirection.equalsIgnoreCase("CtoN")) {
                    throw new Exception( "The sampling direction \"" + lSamplingType + "\" was not recognized (it must be \"NtoC\" or \"CtoN\")!");
                }
            } else {
                throw new Exception( "The sampling type was null!");
            }
        }

        //check sampling iterations
        if (lSamplingIterations <= 0) {
            throw new Exception( "The sampling iterations " + lSamplingIterations + " must be larger than 0 !");
        }

        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }


        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        //check the substitution matrix title
        boolean lParamSet = false;
        if (lSubstitutionMatrixTitle != null) {
            Vector<AAIndexMatrix> lSubstitutionMatrices = lInfoFeeder.getSubstitutionMatrixes();
            for (int i = 0; i < lSubstitutionMatrices.size(); i++) {
                if (lSubstitutionMatrices.get(i).getTitle().equalsIgnoreCase(lSubstitutionMatrixTitle)) {
                    lInfoFeeder.setSubstitutionMatrix((AAIndexSubstitutionMatrix) lSubstitutionMatrices.get(i));
                    i = lSubstitutionMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The substitution matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The substitution matrix \"" + lSubstitutionMatrixTitle + "\" could not be recognized!");
        }

        //find or create the fastafile
        if (iLocation == null) {
            try {
                this.readProperties();
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }
        File fasta = new File(iLocation + lSpecies + ".fasta");

        if (!fasta.exists()) {
            //it does not exists
            //we have to create one

            try {
                System.out.println("\nCreating database " + lSpecies + ".fasta\n");
                //check if we can find uniprot
                File lUniprotfasta = new File(iLocation + "uniprot_sprot.fasta");
                if(!lUniprotfasta.exists()){
                    //check if the server is writting a Database
                    if(iSoapServerSingleton.isWritingDatabase()){
                        throw new Exception("The iceLogoserver is downloading and saving a new uniprot database, please retry in a few minutes!");
                    } else {
                        //we could not find uniprot we will download it from the ebi ftp server
                        try {
                            iSoapServerSingleton.setWritingDatabase(true);
                            String lUniprotFileName = iLocation + "uniprot_sprot.fasta";

                            //create the writer
                            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(lUniprotFileName)));
                            //create the correct url connection
                            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");

                            URLConnection urlcDbDownload = urlDbDownload.openConnection();
                            InputStream isDbDownload = urlcDbDownload.getInputStream();
                            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);

                            System.out.println("Downloading and unzipping the database");

                            int lProteinCounter = 0;
                            int entry;
                            String lHeader = "";
                            String lSequence = "";
                            boolean lParsingHeader = false;
                            boolean lParsingSequence = false;
                            while ((entry = lZippedInputstream.read()) != -1) {
                                if ((char) entry == '\n') {
                                    if (lParsingHeader) {
                                        lParsingHeader = false;
                                        lParsingSequence = true;
                                    }
                                } else if ((char) entry == '>') {
                                    if (lParsingSequence) {
                                        lParsingHeader = true;
                                        lParsingSequence = false;
                                        lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                        lHeader = "";
                                        lSequence = "";
                                        lProteinCounter++;
                                        if (lProteinCounter % 500 == 0) {
                                            System.out.print(".");
                                            lDbOutputWriter.flush();
                                        }
                                        if (lProteinCounter % 40000 == 0) {
                                            System.out.print("\n");
                                            lDbOutputWriter.flush();
                                        }
                                    }
                                    lParsingSequence = false;
                                    lParsingHeader = true;
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingHeader) {
                                    lHeader = lHeader + (char) entry;
                                } else if (lParsingSequence) {
                                    lSequence = lSequence + (char) entry;
                                }
                            }
                            //flush and close the db
                            if(lHeader.length() != 0 && lSequence.length() != 0){
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                            }
                            lDbOutputWriter.flush();
                            lDbOutputWriter.close();
                            System.out.println("\nDone downloading and unzipping");
                            iSoapServerSingleton.setWritingDatabase(false);
                        } catch (MalformedURLException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (IOException e) {
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                            e.printStackTrace();
                        } catch (Exception e){
                            //we encountered an error while creating the database, find the incorrect database and delete it
                            iSoapServerSingleton.setWritingDatabase(false);
                            File lIncorrect = new File(iLocation + "uniprot_sprot.fasta");
                            if(lIncorrect.exists()){
                                lIncorrect.delete();
                            }
                        }
                    }
                }
                //create the reader
                InputStream is = new FileInputStream(new File(iLocation + "uniprot_sprot.fasta"));
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader in = new BufferedReader(reader);
                //create the writer
                BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iLocation + lSpecies + ".fasta")));

                String line = "";
                String lHeader = "";
                String lSequence = "";
                int lCounter = 0;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith(">")) {
                        lHeader = line;
                        //write the sequence if it is the correct species
                        if (lSequence.length() > 0) {
                            boolean lUse = false;
                            String lParsedTaxonomy;
                            if (lHeader.indexOf(" GN=") > 0) {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" GN="));
                            } else {
                                lParsedTaxonomy = lHeader.substring(lHeader.indexOf("OS=") + 3, lHeader.indexOf(" PE="));
                            }
                            if (lParsedTaxonomy.equalsIgnoreCase(lSpecies)) {
                                lUse = true;
                            } else {
                                lUse = false;
                            }
                            if (lUse) {
                                lCounter = lCounter + 1;
                                lDbOutputWriter.write(lHeader + "\n" + lSequence + "\n");
                                if (lCounter % 100 == 0) {
                                    System.out.print(".");
                                }
                                if (lCounter % 2000 == 0) {
                                    System.out.print("\n");
                                }
                            }
                        }
                        lSequence = "";
                    } else {
                        lSequence = lSequence + line;
                    }
                }
                lDbOutputWriter.flush();
                lDbOutputWriter.close();
                fasta = new File(iLocation + lSpecies + ".fasta");

            } catch (FileNotFoundException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            } catch (IOException e) {
                throw new Exception( "There was a problem reading and writing the correct fasta file!");
            }
        }

        //check if the file exists
        //now we have the correct fasta file
        FastaSequenceSet lSequenceSet;
        lSequenceSet = new FastaSequenceSet(fasta.getAbsolutePath(), fasta.getName());
        if (lSequenceSet.test(fasta)) {
            lInfoFeeder.getInstance().setActiveReferenceSet(lSequenceSet);
        } else {
            throw new Exception( "There was a problem reading and writing the correct fasta file!");
        }
        lInfoFeeder.setIterationSize(lSamplingIterations);

        SamplingTypeEnum lSampling = null;
        if (lSamplingType.equalsIgnoreCase("regional")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.REGIONAL);
            lSampling = SamplingTypeEnum.REGIONAL;
            lInfoFeeder.setExperimentAnchorValue(lSamplingPosition);
        } else if (lSamplingType.equalsIgnoreCase("random")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.RANDOM);
            lSampling = SamplingTypeEnum.RANDOM;
        } else if (lSamplingType.equalsIgnoreCase("terminal")) {
            lInfoFeeder.setSamplingType(SamplingTypeEnum.TERMINAL);
            lSampling = SamplingTypeEnum.TERMINAL;
            lInfoFeeder.setAnchorStartPosition(lAnchorPosition);
            if (lSamplingDirection.equalsIgnoreCase("NtoC")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.NtermToCterm);
            } else if (lSamplingDirection.equalsIgnoreCase("CtoN")) {
                lInfoFeeder.setReferenceDirection(SamplingDirectionEnum.CtermToNterm);
            }
        }

        RawSequenceSet lPositiveSet = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);
        lPositiveSet.clearContent();
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lPositiveSet.add(lExperimentalSet[i]);
        }
        RawSequenceSet lPositiveSet2 = lInfoFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);
        if (lUseTwoSets) {
            lPositiveSet2.clearContent();
            for (int i = 0; i < lExperimentalSet2.length; i++) {
                lPositiveSet2.add(lExperimentalSet2[i]);
            }
            lInfoFeeder.setPositionSampleSize((lPositiveSet.getNumberOfSequences() + lPositiveSet2.getNumberOfSequences()) / 2);
            lInfoFeeder.setTwoExperiment(true);
        } else {
            lInfoFeeder.setPositionSampleSize(lPositiveSet.getNumberOfSequences());
        }


        // The final lModel to be created.
        String lReferenceID = lInfoFeeder.getActiveReferenceSet().getID();
        AminoAcidStatistics[] lPositionStatistics = createPositionStatistics();
        AminoAcidStatistics[] lPositionTwoStatistics = new AminoAcidStatistics[0];
        if (lUseTwoSets) {
            lPositionTwoStatistics = createPositionTwoStatistics();
        }
        AminoAcidStatistics[] lReferenceStatistics = null;
        lReferenceStatistics = createReferenceStatistics(lReferenceStatistics, lSampling);

        //create the datamodel
        // Create a OneSampleMatrixDataModel
        MatrixDataModel lModel;
        if (lUseTwoSets) {
            lModel = new TwoSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lPositionTwoStatistics, lReferenceID);
        } else {
            lModel = new OneSampleMatrixDataModel(lReferenceStatistics, lPositionStatistics, lReferenceID);
        }

        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        ConservationComponent logo = new ConservationComponent(lModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticIceLogo(String[] lExperimentalSet, String lSpecies, String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the Y axis dimension
        if (lYaxis < 1) {
            throw new Exception( "The Y axis dimension cannot be smaller than one!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }
        //check the scoring type
        if (!lScoringType.equalsIgnoreCase("foldchange") && !lScoringType.equalsIgnoreCase("percentage")) {
            throw new Exception( "The scoring type must be \"foldchange\" or \"percentage\" !");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        AminoAcidStatistics lNegative = null;
        if (lComposition != null) {
            lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lComposition, lExperimentalSet.length);
        } else {

        }

        AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lExperimentalSet[0].length()];
        for (int i = 0; i < lExperimentalSet[0].length(); i++) {
            lNegativeStatistics[i] = lNegative;
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);
        lInfoFeeder.setYaxisValue(lYaxis);
        ScoringTypeEnum lScoreType;
        if (lScoringType.equalsIgnoreCase("foldchange")) {
            lScoreType = ScoringTypeEnum.FOLDCHANGE;
        } else {
            lScoreType = ScoringTypeEnum.PERCENTAGE;
        }
        lInfoFeeder.setScoringType(lScoreType);

        IceLogoComponent logo = new IceLogoComponent(dataModel, false);
        SVGDocument lSVG = logo.getSVG();
        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticHeatMap(String[] lExperimentalSet, String lSpecies, int lStartPosition, double lPvalue) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        AminoAcidStatistics lNegative = null;
        if (lComposition != null) {
            lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lComposition, lExperimentalSet.length);
        } else {

        }

        AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lExperimentalSet[0].length()];
        for (int i = 0; i < lExperimentalSet[0].length(); i++) {
            lNegativeStatistics[i] = lNegative;
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setPvalue(lPvalue);

        HeatMapComponent logo = new HeatMapComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticSequenceLogo(String[] lExperimentalSet, String lSpecies, int lStartPosition, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        AminoAcidStatistics lNegative = null;
        if (lComposition != null) {
            lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lComposition, lExperimentalSet.length);
        } else {

        }

        AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lExperimentalSet[0].length()];
        for (int i = 0; i < lExperimentalSet[0].length(); i++) {
            lNegativeStatistics[i] = lNegative;
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setWeblogoNegativeCorrection(true);
        lInfoFeeder.setFillWeblogo(false);


        SequenceLogoComponent logo = new SequenceLogoComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getFilledLogo(String[] lExperimentalSet, int lStartPosition, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        String[] lReferenceSet = new String[]{lExperimentalSet[0], lExperimentalSet[0]};
        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setWeblogoNegativeCorrection(true);
        lInfoFeeder.setFillWeblogo(true);

        SequenceLogoComponent logo = new SequenceLogoComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticAaParameterGraph(String[] lExperimentalSet, String lSpecies, String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }


        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        AminoAcidStatistics lNegative = null;
        if (lComposition != null) {
            lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lComposition, lExperimentalSet.length);
        } else {

        }

        AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lExperimentalSet[0].length()];
        for (int i = 0; i < lExperimentalSet[0].length(); i++) {
            lNegativeStatistics[i] = lNegative;
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        //check the aaparam title
        boolean lParamSet = false;
        if (lAaMatrixTitle != null) {
            Vector<AAIndexMatrix> lAaParamMatrices = lInfoFeeder.getAaParameterMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                if (lAaParamMatrices.get(i).getTitle().equalsIgnoreCase(lAaMatrixTitle)) {
                    lInfoFeeder.setSelectedAaParameterMatrix((AAIndexParameterMatrix) lAaParamMatrices.get(i));
                    i = lAaParamMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The amino acid parameter matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The amino acid parameter matrix \"" + lAaMatrixTitle + "\" could not be recognized!");
        }
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        AAIndexComponent logo = new AAIndexComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticConservationLine(String[] lExperimentalSet, String lSpecies, String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the availability of a species
        if (lSpecies == null) {
            throw new Exception( "The species name was null!");
        }

        //load the compositions
        if (iAllSpecies == null) {
            loadCompositions();
        }
        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        SwissProtComposition lComposition = null;
        for (int i = 0; i < iAllSpecies.size(); i++) {
            if (iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(lSpecies)) {
                lComposition = iAllSpecies.get(i);
            }
        }
        //check the species
        if (lComposition == null) {
            throw new Exception( "The species \"" + lSpecies + "\" could not be recognized!");
        }

        AminoAcidStatistics lNegative = null;
        if (lComposition != null) {
            lNegative = AminoAcidStatisticsFactory.createFixedAminoAcidMatrix(lComposition, lExperimentalSet.length);
        } else {

        }

        AminoAcidStatistics[] lNegativeStatistics = new AminoAcidStatistics[lExperimentalSet[0].length()];
        for (int i = 0; i < lExperimentalSet[0].length(); i++) {
            lNegativeStatistics[i] = lNegative;
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        //check the substitution matrix title
        boolean lParamSet = false;
        if (lSubstitutionMatrixTitle != null) {
            Vector<AAIndexMatrix> lSubstitutionMatrices = lInfoFeeder.getSubstitutionMatrixes();
            for (int i = 0; i < lSubstitutionMatrices.size(); i++) {
                if (lSubstitutionMatrices.get(i).getTitle().equalsIgnoreCase(lSubstitutionMatrixTitle)) {
                    lInfoFeeder.setSubstitutionMatrix((AAIndexSubstitutionMatrix) lSubstitutionMatrices.get(i));
                    i = lSubstitutionMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The substitution matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The substitution matrix \"" + lSubstitutionMatrixTitle + "\" could not be recognized!");
        }
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        ConservationComponent logo = new ConservationComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticReferenceSetIceLogo(String[] lExperimentalSet, String[] lReferenceSet, String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " in the experimental set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check if everything in the reference set has the same length
        if (lReferenceSet != null && lReferenceSet.length != 0) {
            int lLength = lReferenceSet[0].length();
            for (int i = 0; i < lReferenceSet.length; i++) {
                if (lLength != lReferenceSet[i].length()) {
                    throw new Exception( "The sequence " + lReferenceSet[i] + " in the reference set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The reference sequence set was null or was of length zero!");
        }
        //check if the reference set and experimental set have the same sequence length
        if (lReferenceSet[0].length() != lExperimentalSet[0].length()) {
            throw new Exception( "The sequences in the reference sequence set were not of the same length as the sequences in the experimental sequence set!");
        }
        //check the Y axis dimension
        if (lYaxis < 1) {
            throw new Exception( "The Y axis dimension cannot be smaller than one!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }
        //check the scoring type
        if (!lScoringType.equalsIgnoreCase("foldchange") && !lScoringType.equalsIgnoreCase("percentage")) {
            throw new Exception( "The scoring type must be \"foldchange\" or \"percentage\" !");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);
        lInfoFeeder.setYaxisValue(lYaxis);
        ScoringTypeEnum lScoreType;
        if (lScoringType.equalsIgnoreCase("foldchange")) {
            lScoreType = ScoringTypeEnum.FOLDCHANGE;
        } else {
            lScoreType = ScoringTypeEnum.PERCENTAGE;
        }
        lInfoFeeder.setScoringType(lScoreType);

        IceLogoComponent logo = new IceLogoComponent(dataModel, false);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticReferenceSetHeatMap(String[] lExperimentalSet, String[] lReferenceSet, int lStartPosition, double lPvalue) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " in the experimental set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check if everything in the reference set has the same length
        if (lReferenceSet != null && lReferenceSet.length != 0) {
            int lLength = lReferenceSet[0].length();
            for (int i = 0; i < lReferenceSet.length; i++) {
                if (lLength != lReferenceSet[i].length()) {
                    throw new Exception( "The sequence " + lReferenceSet[i] + " in the reference set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The reference sequence set was null or was of length zero!");
        }
        //check if the reference set and experimental set have the same sequence length
        if (lReferenceSet[0].length() != lExperimentalSet[0].length()) {
            throw new Exception( "The sequences in the reference sequence set were not of the same lenght as the sequences in the experimental sequence set!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setPvalue(lPvalue);

        HeatMapComponent logo = new HeatMapComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticReferenceSetSequenceLogo(String[] lExperimentalSet, String[] lReferenceSet, int lStartPosition, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " in the experimental set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check if everything in the reference set has the same length
        if (lReferenceSet != null && lReferenceSet.length != 0) {
            int lLength = lReferenceSet[0].length();
            for (int i = 0; i < lReferenceSet.length; i++) {
                if (lLength != lReferenceSet[i].length()) {
                    throw new Exception( "The sequence " + lReferenceSet[i] + " in the reference set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The reference sequence set was null or was of length zero!");
        }
        //check if the reference set and experimental set have the same sequence length
        if (lReferenceSet[0].length() != lExperimentalSet[0].length()) {
            throw new Exception( "The sequences in the reference sequence set were not of the same lenght as the sequences in the experimental sequence set!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);
        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setWeblogoNegativeCorrection(true);

        SequenceLogoComponent logo = new SequenceLogoComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticReferenceSetAaParameterGraph(String[] lExperimentalSet, String[] lReferenceSet, String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " in the experimental set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check if everything in the reference set has the same length
        if (lReferenceSet != null && lReferenceSet.length != 0) {
            int lLength = lReferenceSet[0].length();
            for (int i = 0; i < lReferenceSet.length; i++) {
                if (lLength != lReferenceSet[i].length()) {
                    throw new Exception( "The sequence " + lReferenceSet[i] + " in the reference set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The reference sequence set was null or was of length zero!");
        }
        //check if the reference set and experimental set have the same sequence length
        if (lReferenceSet[0].length() != lExperimentalSet[0].length()) {
            throw new Exception( "The sequences in the reference sequence set were not of the same lenght as the sequences in the experimental sequence set!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);

        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        //check the aaparam title
        boolean lParamSet = false;
        if (lAaMatrixTitle != null) {
            Vector<AAIndexMatrix> lAaParamMatrices = lInfoFeeder.getAaParameterMatrixes();
            for (int i = 0; i < lAaParamMatrices.size(); i++) {
                if (lAaParamMatrices.get(i).getTitle().equalsIgnoreCase(lAaMatrixTitle)) {
                    lInfoFeeder.setSelectedAaParameterMatrix((AAIndexParameterMatrix) lAaParamMatrices.get(i));
                    i = lAaParamMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The amino acid parameter matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The amino acid parameter matrix \"" + lAaMatrixTitle + "\" could not be recognized!");
        }
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        AAIndexComponent logo = new AAIndexComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    public String getStaticReferenceSetConservationLine(String[] lExperimentalSet, String[] lReferenceSet, String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws Exception {

        //first things first, check if the received data was correct
        //check if everything in the experimental set has the same length
        if (lExperimentalSet != null && lExperimentalSet.length != 0) {
            int lLength = lExperimentalSet[0].length();
            for (int i = 0; i < lExperimentalSet.length; i++) {
                if (lLength != lExperimentalSet[i].length()) {
                    throw new Exception( "The sequence " + lExperimentalSet[i] + " in the experimental set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The experimental sequence set was null or was of length zero!");
        }
        //check if everything in the reference set has the same length
        if (lReferenceSet != null && lReferenceSet.length != 0) {
            int lLength = lReferenceSet[0].length();
            for (int i = 0; i < lReferenceSet.length; i++) {
                if (lLength != lReferenceSet[i].length()) {
                    throw new Exception( "The sequence " + lReferenceSet[i] + " in the reference set has an incorrect length (" + lExperimentalSet[i].length() + " != " + lLength + ")!");
                }
            }
        } else {
            throw new Exception( "The reference sequence set was null or was of length zero!");
        }
        //check if the reference set and experimental set have the same sequence length
        if (lReferenceSet[0].length() != lExperimentalSet[0].length()) {
            throw new Exception( "The sequences in the reference sequence set were not of the same lenght as the sequences in the experimental sequence set!");
        }
        //check the Pvalue
        if (lPvalue <= 0.0 || lPvalue >= 1.0) {
            throw new Exception( "The P value must be between 1.0 and 0.0!");
        }
        //check the height
        if (lHeight < 199) {
            throw new Exception( "The minimum height is 200!");
        }
        //check the width
        if (lWidth < 199) {
            throw new Exception( "The minimum width is 200!");
        }

        //get the infoFeeder
        MainInformationFeeder lInfoFeeder = MainInformationFeeder.getInstance();
        lInfoFeeder.setUpdate(false);

        RawSequenceSet lRawNegativeSequenceSet = new RawSequenceSet("Negative sequences");
        for (int i = 0; i < lReferenceSet.length; i++) {
            lRawNegativeSequenceSet.add(lReferenceSet[i]);
        }
        // always use the smallest set to do the statistics
        AminoAcidStatistics[] lNegativeStatistics;
        if (lExperimentalSet.length < lReferenceSet.length) {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        } else {
            lNegativeStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawNegativeSequenceSet, 1, 0, lExperimentalSet[0].length(), lReferenceSet.length);
        }

        //Create model and matrixes for the logo
        RawSequenceSet lRawPositiveSequenceSet = new RawSequenceSet("Positive sequences");
        for (int i = 0; i < lExperimentalSet.length; i++) {
            lRawPositiveSequenceSet.add(lExperimentalSet[i]);
        }
        AminoAcidStatistics[] lPositiveStatistics = AminoAcidStatisticsFactory.createFixedStatisticsVerticalPositionAminoAcidMatrix(lRawPositiveSequenceSet, 1, 0, lExperimentalSet[0].length(), lExperimentalSet.length);
        //create the datamodel
        String lReferenceID = "Static reference set";
        OneSampleMatrixDataModel dataModel = new OneSampleMatrixDataModel(lNegativeStatistics, lPositiveStatistics, lReferenceID);
        //check the substitution matrix title
        boolean lParamSet = false;
        if (lSubstitutionMatrixTitle != null) {
            Vector<AAIndexMatrix> lSubstitutionMatrices = lInfoFeeder.getSubstitutionMatrixes();
            for (int i = 0; i < lSubstitutionMatrices.size(); i++) {
                if (lSubstitutionMatrices.get(i).getTitle().equalsIgnoreCase(lSubstitutionMatrixTitle)) {
                    lInfoFeeder.setSubstitutionMatrix((AAIndexSubstitutionMatrix) lSubstitutionMatrices.get(i));
                    i = lSubstitutionMatrices.size();
                    lParamSet = true;
                }
            }
        } else {
            throw new Exception( "The substitution matrix title set was null!");
        }
        if (!lParamSet) {
            throw new Exception( "The substitution matrix \"" + lSubstitutionMatrixTitle + "\" could not be recognized!");
        }
        lInfoFeeder.setStartPosition(lStartPosition);
        lInfoFeeder.setGraphableWidth(lWidth);
        lInfoFeeder.setGraphableHeight(lHeight);
        lInfoFeeder.setPvalue(lPvalue);

        AAIndexComponent logo = new AAIndexComponent(dataModel);
        SVGDocument lSVG = logo.getSVG();

        String lResult = "";
        try {


            TranscoderInput input = new TranscoderInput(lSVG);
            // Create the transcoder output.
            Transcoder lSVGTranscoder = new SVGTranscoder();
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
            lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
            StringWriter lStringWriter = new StringWriter();
            TranscoderOutput lSVGoutput = new TranscoderOutput(lStringWriter);
            lSVGTranscoder.transcode(input, lSVGoutput);
            lResult = lStringWriter.toString();
        } catch (TranscoderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    private void loadCompositions() {
        try {

            InputStream is = this.getClass().getClassLoader().getResourceAsStream("speciesList.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(reader);
            String strLine;
            Vector<SwissProtComposition> lAllSpecies = new Vector<SwissProtComposition>();
            Vector<SwissProtComposition> lDoneAllSpecies = new Vector<SwissProtComposition>();
            while ((strLine = in.readLine()) != null) {
                lAllSpecies.add(new SwissProtComposition(strLine.substring(strLine.indexOf("= ") + 2), strLine.substring(0, strLine.indexOf(" = "))));
            }

            //read the composition file
            InputStream is2 = this.getClass().getClassLoader().getResourceAsStream("compositions.txt");
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
            iAllSpecies = lDoneAllSpecies;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private AminoAcidStatistics[] createPositionTwoStatistics() {
        MainInformationFeeder iFeeder = MainInformationFeeder.getInstance();
        SamplingDirectionEnum lDirection;
        int lVerticalOffset;
        int lIterationSize;
        int lLength;
        AminoAcidStatistics[] lPositionTwoStatistics = null;
        if (iFeeder.isTwoExperiment()) {

            ISequenceSet lPositionSetTwo = iFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT_TWO);

            lDirection = SamplingDirectionEnum.NtermToCterm;
            lVerticalOffset = 0;
            lIterationSize = 1;
            lLength = iFeeder.getNumberOfExperimentalPositions();

            lPositionTwoStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                    lPositionSetTwo,
                    lIterationSize,
                    lVerticalOffset,
                    lLength,
                    lDirection);
        }
        return lPositionTwoStatistics;
    }

    private AminoAcidStatistics[] createPositionStatistics() {
        MainInformationFeeder iFeeder = MainInformationFeeder.getInstance();
        ISequenceSet lPositionSet = iFeeder.getExperimentSequenceSet(ExperimentTypeEnum.EXPERIMENT);

        SamplingDirectionEnum lDirection = SamplingDirectionEnum.NtermToCterm;
        int lVerticalOffset = 0;
        int lIterationSize = 1;
        int lLength = iFeeder.getNumberOfExperimentalPositions();

        AminoAcidStatistics[] lPositionStatistics = null;
        lPositionStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                lPositionSet,
                lIterationSize,
                lVerticalOffset,
                lLength,
                lDirection);
        return lPositionStatistics;
    }

    private AminoAcidStatistics[] createReferenceStatistics(AminoAcidStatistics[] aReferenceStatistics, SamplingTypeEnum iSamplingTypeEnum) {
        MainInformationFeeder iFeeder = MainInformationFeeder.getInstance();
        ISequenceSet lSequenceSet = iFeeder.getActiveReferenceSet();
        switch (iSamplingTypeEnum) {

            case HORIZONTAL:
                int lHorizontalSamplingLength = iFeeder.getHorizontalSamplingLength();
                int lHorizontalSamplingOffset = iFeeder.getAnchorStartPosition();
                aReferenceStatistics = new MatrixAminoAcidStatistics[]{AminoAcidStatisticsFactory.createHorizontalPositionAminoAcidMatrix(
                        lSequenceSet,
                        lHorizontalSamplingLength,
                        lHorizontalSamplingOffset)};
                break;

            case RANDOM:
                int lSamplingSize = iFeeder.getPositionSampleSize();
                SamplingStrategy lStrategy = SamplingStrategy.ALL;
                int lNumberOfIterations = iFeeder.getIterationSize();
                aReferenceStatistics = new MatrixAminoAcidStatistics[]{AminoAcidStatisticsFactory.createRandomSampleAminoAcidMatrix(
                        lSequenceSet,
                        lSamplingSize,
                        lNumberOfIterations,
                        lStrategy.getInstance())};
                break;

            case REGIONAL:
                // In order to perform the Regional sampling, first we need to
                // fetch the anchored amino acids.
                int lExperimentAnchorValue = iFeeder.getExperimentAnchorValue();
                int lNumberOfExperimentalPositions = iFeeder.getNumberOfExperimentalPositions();
                int lPrefix = lExperimentAnchorValue; // If anchor is set to 5 (0-based), then there are 5 preceding amino acids.
                int lSuffix = lNumberOfExperimentalPositions - 1 - lExperimentAnchorValue; // If there are 12 positions, there are 6(=12-5-1) following amino acids.

                AminoAcidEnum[] lAminoAcids = iFeeder.getExperimentalAminoAcids(lExperimentAnchorValue);

                // Make sure the sequenceSet is a FastaSequenceSet.
                assert lSequenceSet instanceof FastaSequenceSet;
                // Create a RegionalFastaSequenceSet for the given experimental anchor and the required preceding and following aminoacids.
                RegionalFastaSequenceSet lRegionalFastaSequenceSet =
                        ((FastaSequenceSet) lSequenceSet).deriveRegionalSequenceSet(lAminoAcids, lPrefix, lSuffix);

                int lRegionalOffset = 0;
                int lRegionalIterationSize = iFeeder.getIterationSize();
                int lRegionalLength = iFeeder.getNumberOfExperimentalPositions();

                aReferenceStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                        lRegionalFastaSequenceSet,
                        lRegionalIterationSize,
                        lRegionalOffset,
                        lRegionalLength,
                        SamplingDirectionEnum.NtermToCterm);
                break;

            case TERMINAL:
                int lVerticalOffset = iFeeder.getAnchorStartPosition();
                int lIterationSize = iFeeder.getIterationSize();
                int lSubsetSize = iFeeder.getPositionSampleSize();
                int lLength = iFeeder.getNumberOfExperimentalPositions();
                SamplingDirectionEnum lDirection = iFeeder.getReferenceDirection();

                aReferenceStatistics = AminoAcidStatisticsFactory.createVerticalPositionAminoAcidMatrix(
                        lSequenceSet,
                        lIterationSize,
                        lVerticalOffset,
                        lLength,
                        lSubsetSize,
                        lDirection);
                break;
        }
        return aReferenceStatistics;
    }

}
