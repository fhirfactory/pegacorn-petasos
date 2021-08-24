package net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes;

import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDNToken;

import java.io.Serializable;
import java.util.Objects;

public class PetasosComponentIdentifierToken implements Serializable {
    private String tokenValue;

    public PetasosComponentIdentifierToken(){
        tokenValue = new String();
    }

    public PetasosComponentIdentifierToken(PetasosComponentIdentifierToken ori){
        this.tokenValue = ori.getTokenValue();
    }

    public PetasosComponentIdentifierToken(String token){
        this.tokenValue = token;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public FDNToken toTypeBasedFDNToken(){
        PetasosNodeFDN topologyFDN = new PetasosNodeFDN(this);
        FDN genericFDN = topologyFDN.toTypeBasedFDN();
        FDNToken genericFDNToken = genericFDN.getToken();
        return(genericFDNToken);
    }

    public FDNToken toVersionBasedFDNToken(){
        PetasosNodeFDN topologyFDN = new PetasosNodeFDN(this);
        FDN genericFDN = topologyFDN.toTypeBasedFDNWithVersion();
        FDNToken genericFDNToken = genericFDN.getToken();
        return(genericFDNToken);
    }

    @Override
    public String toString() {
        return "TopologyNodeFDNToken{" +
                "tokenValue='" + tokenValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosComponentIdentifierToken)) return false;
        PetasosComponentIdentifierToken that = (PetasosComponentIdentifierToken) o;
        return Objects.equals(getTokenValue(), that.getTokenValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTokenValue());
    }
}
