/**
 * IceLogoServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.computationalproteomics.icelogoserver.soap.client.wsdl2java;

import com.computationalproteomics.icelogoserver.soap.client.wsdl2java.IceLogo;
import com.computationalproteomics.icelogoserver.soap.client.wsdl2java.IceLogoService;

public class IceLogoServiceLocator extends org.apache.axis.client.Service implements IceLogoService {

    public IceLogoServiceLocator() {
    }


    public IceLogoServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IceLogoServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for icelogo
    private java.lang.String icelogo_address = "http://icelogo.ugent.be/icelogoserver/services/icelogo";

    public java.lang.String geticelogoAddress() {
        return icelogo_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String icelogoWSDDServiceName = "icelogo";

    public java.lang.String geticelogoWSDDServiceName() {
        return icelogoWSDDServiceName;
    }

    public void seticelogoWSDDServiceName(java.lang.String name) {
        icelogoWSDDServiceName = name;
    }

    public IceLogo geticelogo() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(icelogo_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return geticelogo(endpoint);
    }

    public IceLogo geticelogo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            IcelogoSoapBindingStub _stub = new IcelogoSoapBindingStub(portAddress, this);
            _stub.setPortName(geticelogoWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void seticelogoEndpointAddress(java.lang.String address) {
        icelogo_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IceLogo.class.isAssignableFrom(serviceEndpointInterface)) {
                IcelogoSoapBindingStub _stub = new IcelogoSoapBindingStub(new java.net.URL(icelogo_address), this);
                _stub.setPortName(geticelogoWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("icelogo".equals(inputPortName)) {
            return geticelogo();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:IceLogoFetcher", "IceLogoService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:IceLogoFetcher", "icelogo"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("icelogo".equals(portName)) {
            seticelogoEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
