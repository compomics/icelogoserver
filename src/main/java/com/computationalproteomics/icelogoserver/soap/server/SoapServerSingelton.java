package com.computationalproteomics.icelogoserver.soap.server;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 25-Nov-2009
 * Time: 16:36:46
 * To change this template use File | Settings | File Templates.
 */
public class SoapServerSingelton {
    private static SoapServerSingelton ourInstance = new SoapServerSingelton();
    private boolean iWritingDatabase = false;


    public static SoapServerSingelton getInstance() {
       return ourInstance;
   }

   private SoapServerSingelton() {
   }

    public boolean isWritingDatabase(){
        return iWritingDatabase;
    }

    public void setWritingDatabase(boolean lWriting){
        iWritingDatabase = lWriting;
    }

}
