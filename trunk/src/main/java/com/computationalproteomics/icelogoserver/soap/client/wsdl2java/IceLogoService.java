/**
 * IceLogoService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.computationalproteomics.icelogoserver.soap.client.wsdl2java;

import com.computationalproteomics.icelogoserver.soap.client.wsdl2java.IceLogo;

public interface IceLogoService extends javax.xml.rpc.Service {
    public java.lang.String geticelogoAddress();

    public com.computationalproteomics.icelogoserver.soap.client.wsdl2java.IceLogo geticelogo() throws javax.xml.rpc.ServiceException;

    public IceLogo geticelogo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
