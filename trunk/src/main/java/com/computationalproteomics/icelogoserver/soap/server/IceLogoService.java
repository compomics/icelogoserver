/**
 * IceLogoService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.computationalproteomics.icelogoserver.soap.server;

public interface IceLogoService extends javax.xml.rpc.Service {
    public java.lang.String geticelogoAddress();

    public com.computationalproteomics.icelogoserver.soap.server.IceLogo geticelogo() throws javax.xml.rpc.ServiceException;

    public com.computationalproteomics.icelogoserver.soap.server.IceLogo geticelogo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
