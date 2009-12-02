package com.computationalproteomics.icelogoserver.soap.client.manual;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 10-Nov-2009
 * Time: 16:55:26
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class will run all the examples
 */
public class RunAllExamples {

    public static void main(String[] args){
        FilledLogoExample lFilledLogoExample = new FilledLogoExample();
        SamplingSingleExperimentalSetConservationLineExample lSamplingSingleExperimentalSetConservationLineExample = new SamplingSingleExperimentalSetConservationLineExample();
        SamplingSingleExperimentalSetHeatMapExample lSamplingSingleExperimentalSetHeatMapExample = new SamplingSingleExperimentalSetHeatMapExample();
        StaticAaParameterGraphExample lStaticAaParameterGraphExample = new StaticAaParameterGraphExample();
        StaticIceLogoExample lStaticIceLogoExample = new StaticIceLogoExample();
        StaticSequenceLogoExample lStaticSequenceLogoExample = new StaticSequenceLogoExample();
    }
}