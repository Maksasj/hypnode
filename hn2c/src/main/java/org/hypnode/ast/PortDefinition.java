package org.hypnode.ast;

import java.util.List;

import org.utils.StringUtils;

import gen.sym;

public class PortDefinition {
    private String symbolName;
    private List<IPortAttribute> attributes;
    private String portName;
    private ITypeImplementation typeImplementation;

    public PortDefinition(List<IPortAttribute> attributes, String portName, ITypeImplementation typeImplementation, Object value) {
        this.attributes = attributes;
        this.portName = portName;
        this.typeImplementation = typeImplementation;

        generateSymbolName();
    }

    public List<IPortAttribute> getAttributes() {
        return attributes;
    }

    public String getPortName() {
        return portName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public ITypeImplementation getTypeImplementation() {
        return typeImplementation;
    }
    
    public void setTypeImplementation(ITypeImplementation typeImplementation) {
        this.typeImplementation = typeImplementation;
    }

    private void generateSymbolName() {
        symbolName = "psym_" + StringUtils.generateRandomString(16);
    };
}
