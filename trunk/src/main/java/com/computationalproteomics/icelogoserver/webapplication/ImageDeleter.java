package com.computationalproteomics.icelogoserver.webapplication;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 26-Nov-2009
 * Time: 08:15:45
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class will delete old images on the server
 */
public class ImageDeleter extends Thread {

    /**
     * The folder location of the images
     */
    private String iSaveLocation;

    /**
     * Construtor
     * @param aSaveLocation The folder location
     */
    public ImageDeleter(String aSaveLocation){
        super();
        this.iSaveLocation = aSaveLocation;
    }


    /**
     * This thread will be run in the background and will delete all old images on the server
     */
    public void run(){
        //get the folder
        File lSaveFolder = new File(iSaveLocation);
        if(lSaveFolder.exists()){
            //get the images
            File[] lImages = lSaveFolder.listFiles();
            for(int i = 0; i<lImages.length; i ++){
                String lFileName = lImages[i].getName();
                if(lFileName.indexOf(".") > 0){
                    try{
                        //check the timestamp of the images
                        double lTimeStamp = Double.valueOf(lFileName.substring(0, lFileName.indexOf(".")));
                        double lCurrent = System.currentTimeMillis();
                        if(lTimeStamp < lCurrent - 3600000.0){
                            if(lImages[i].exists()){
                                //delete the old image
                                lImages[i].delete();
                            }
                        }
                    } catch(Exception e){
                        //no problemo this file will be deleted the next time
                    }
                }
            }
        }
    }

}
