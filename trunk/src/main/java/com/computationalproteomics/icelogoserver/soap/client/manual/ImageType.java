package com.computationalproteomics.icelogoserver.soap.client.manual;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 13-Nov-2009
 * Time: 16:03:22
 * To change this template use File | Settings | File Templates.
 */

/**
 * This enumeration describes the different image types that can be converted by the SvgImageConverterAndSaver class.
 */
public enum ImageType {
    JPEG (".jpg"), TIFF(".tiff"), PNG (".png"), PDF(".pdf"), SVG (".svg");

    /**
     * A String with the file extention for a specific image type
     */
    public String iExtension;

    /**
     * Constructor
     * @param lExtension The image file extension
     */
    ImageType(String lExtension){
        this.iExtension = lExtension;
    }

    /**
     * Getter for the image file extension
     * @return String with the file extension
     */
    public String getExtension(){
        return this.iExtension;
    }
}
