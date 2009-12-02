/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 11-Oct-2009
 * Time: 09:47:45
 * To change this template use File | Settings | File Templates.
 */
package com.computationalproteomics.icelogoserver.webapplication;

import be.proteomics.logo.core.dbComposition.SwissProtComposition;

import java.util.Vector;

/**
 * The IceLogoWepAppSingelton. This singelton will hold all the SwissProtCompositions for all the species.
 */
public class IceLogoWepAppSingelton {
    /**
     * The instance
     */
    private static IceLogoWepAppSingelton ourInstance = new IceLogoWepAppSingelton();

    /**
     * Vector with the possible compositions
     */
    private Vector<SwissProtComposition> iAllSpecies;

    public static IceLogoWepAppSingelton getInstance() {
        return ourInstance;
    }

    private IceLogoWepAppSingelton() {
    }

    /**
     * Set the compositions
     * @param lSpecies Vector with the SwissProtCompositions
     */
    public void setCompositions(Vector<SwissProtComposition> lSpecies){
        iAllSpecies = lSpecies;
    }

    /**
     * Check if the species compositions are set
     * @return Boolean is true if the species are set
     */
    public boolean speciesSet(){
        if(iAllSpecies == null){
            return false;
        }
        return true;
    }

    /**
     * Get a specific SwissProtComposition for a specific species
     * @param aSpecies String with the scientific name of the species for the asked composition
     * @return SwissProtComposition
     */
    public SwissProtComposition getComposition(String aSpecies){
        for(int i = 0; i<iAllSpecies.size(); i ++){
            if(iAllSpecies.get(i).getSpecieName().equalsIgnoreCase(aSpecies)){
                return iAllSpecies.get(i);
            }
        }
        return null;
    }


}
