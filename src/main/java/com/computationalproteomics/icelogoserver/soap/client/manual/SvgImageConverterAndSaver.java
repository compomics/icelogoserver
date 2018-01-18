package com.computationalproteomics.icelogoserver.soap.client.manual;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
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
import org.w3c.dom.svg.SVGDocument;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 13-Nov-2009
 * Time: 16:03:08
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class can convert a Svg String and save it like an image.
 */
public class SvgImageConverterAndSaver {

    /**
     * String with the svg content
     */
    public String iSVG;
    /**
     * The image type
     */
    public ImageType iImageType;
    /**
     * The save folder location
     */
    public String iSaveLocation;
    /**
     * The filename
     */
    public String iFileName;

    /**
     * Constructor
     * @param lSVG String with the svg content
     * @param lImageType The image type
     * @param lSaveLocation The save folder location
     * @param lFileName The filename
     */
    public SvgImageConverterAndSaver(String lSVG, ImageType lImageType, String lSaveLocation, String lFileName) {
        this.iSVG = lSVG;
        this.iImageType = lImageType;
        this.iSaveLocation = lSaveLocation;
        if(iSaveLocation.endsWith("\\") || iSaveLocation.endsWith("/")){
        } else {
            iSaveLocation = iSaveLocation + "/";
        }
        this.iFileName = lFileName;
    }

    /**
     * This method will save the image
     * @return Boolean that indicates if everything went well
     */
    public boolean save(){
        try {
            //create an svg document
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            InputStream is = null;
            is = new ByteArrayInputStream(iSVG.getBytes("UTF-8"));
            SVGDocument lSVGDocument = (SVGDocument) f.createDocument(null, is);

            //create the input
            TranscoderInput input = new TranscoderInput(lSVGDocument);
            //create an output and an outputstream
            OutputStream lOutstream = new FileOutputStream(iSaveLocation + iFileName + iImageType.getExtension());
            TranscoderOutput lOutput = new TranscoderOutput(lOutstream);
            //do the transcoding
            if (iImageType == ImageType.JPEG) {
                //we need a jpeg figure
                Transcoder lJPEGTranscoder = new JPEGTranscoder();
                lJPEGTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1.0));
                lJPEGTranscoder.transcode(input, lOutput);
            } else if (iImageType == ImageType.PNG) {
                //we need a png figure
                Transcoder lPNGTranscoder = new PNGTranscoder();
                lPNGTranscoder.addTranscodingHint(PNGTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));
                lPNGTranscoder.transcode(input, lOutput);
            } else if (iImageType == ImageType.TIFF) {
                //we need a tiff figure
                Transcoder lTIFFTranscoder = new TIFFTranscoder();
                lTIFFTranscoder.addTranscodingHint(TIFFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));
                lTIFFTranscoder.addTranscodingHint(TIFFTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);
                lTIFFTranscoder.transcode(input, lOutput);
            } else if (iImageType == ImageType.PDF) {
                //we need a pdf figure
                Transcoder lPDFTranscoder = new PDFTranscoder();
                lPDFTranscoder.addTranscodingHint(PDFTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, new Float(0.084666f));
                lPDFTranscoder.transcode(input, lOutput);
            } else if (iImageType == ImageType.SVG) {
                //we need a svg figure
                Transcoder lSVGTranscoder = new SVGTranscoder();
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, SVGTranscoder.VALUE_DOCTYPE_CHANGE);
                lSVGTranscoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, SVGTranscoder.VALUE_FORMAT_OFF);
                FileWriter lOutstreamSvg = new FileWriter(iSaveLocation + iFileName + iImageType.getExtension());
                TranscoderOutput lOutputSvg = new TranscoderOutput(lOutstreamSvg);
                lSVGTranscoder.transcode(input, lOutputSvg);
                lOutstreamSvg.flush();
                lOutstreamSvg.close();
            }
            //close the stream
            lOutstream.flush();
            lOutstream.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (TranscoderException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
