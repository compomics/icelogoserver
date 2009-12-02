/**
 * IceLogo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.computationalproteomics.icelogoserver.soap.client.wsdl2java;

public interface IceLogo extends java.rmi.Remote {
    public java.lang.String[] getAvailableAaParameters() throws java.rmi.RemoteException;
    public java.lang.String[] getAvailableSubstitutionMatrices() throws java.rmi.RemoteException;
    public java.lang.String testService(java.lang.String lTestString) throws java.rmi.RemoteException;
    public java.lang.String getSamplingIceLogo(java.lang.String[] lExperimentalSet, java.lang.String[] lExperimentalSet2, java.lang.String lSpecies, java.lang.String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth, java.lang.String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, java.lang.String lSamplingDirection) throws java.rmi.RemoteException;
    public java.lang.String getSamplingHeatMap(java.lang.String[] lExperimentalSet, java.lang.String[] lExperimentalSet2, java.lang.String lSpecies, int lStartPosition, double lPvalue, int lHeight, int lWidth, java.lang.String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, java.lang.String lSamplingDirection) throws java.rmi.RemoteException;
    public java.lang.String getSamplingAaParameterGraph(java.lang.String[] lExperimentalSet, java.lang.String[] lExperimentalSet2, java.lang.String lSpecies, java.lang.String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth, java.lang.String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, java.lang.String lSamplingDirection) throws java.rmi.RemoteException;
    public java.lang.String getSamplingConservationLine(java.lang.String[] lExperimentalSet, java.lang.String[] lExperimentalSet2, java.lang.String lSpecies, java.lang.String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth, java.lang.String lSamplingType, int lSamplingPosition, int lSamplingIterations, int lAnchorPosition, java.lang.String lSamplingDirection) throws java.rmi.RemoteException;
    public java.lang.String getStaticIceLogo(java.lang.String[] lExperimentalSet, java.lang.String lSpecies, java.lang.String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticHeatMap(java.lang.String[] lExperimentalSet, java.lang.String lSpecies, int lStartPosition, double lPvalue) throws java.rmi.RemoteException;
    public java.lang.String getStaticSequenceLogo(java.lang.String[] lExperimentalSet, java.lang.String lSpecies, int lStartPosition, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getFilledLogo(java.lang.String[] lExperimentalSet, int lStartPosition, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticAaParameterGraph(java.lang.String[] lExperimentalSet, java.lang.String lSpecies, java.lang.String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticConservationLine(java.lang.String[] lExperimentalSet, java.lang.String lSpecies, java.lang.String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticReferenceSetIceLogo(java.lang.String[] lExperimentalSet, java.lang.String[] lReferenceSet, java.lang.String lScoringType, int lYaxis, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticReferenceSetHeatMap(java.lang.String[] lExperimentalSet, java.lang.String[] lReferenceSet, int lStartPosition, double lPvalue) throws java.rmi.RemoteException;
    public java.lang.String getStaticReferenceSetSequenceLogo(java.lang.String[] lExperimentalSet, java.lang.String[] lReferenceSet, int lStartPosition, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticReferenceSetAaParameterGraph(java.lang.String[] lExperimentalSet, java.lang.String[] lReferenceSet, java.lang.String lAaMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
    public java.lang.String getStaticReferenceSetConservationLine(java.lang.String[] lExperimentalSet, java.lang.String[] lReferenceSet, java.lang.String lSubstitutionMatrixTitle, int lStartPosition, double lPvalue, int lHeight, int lWidth) throws java.rmi.RemoteException;
}
