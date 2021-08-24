package net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes;

import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDNToken;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class PetasosComponentTypeToken implements Serializable {
    public String token;

    public PetasosComponentTypeToken(){
        token = new String();
    }

    public PetasosComponentTypeToken(PetasosComponentTypeToken ori){
        this.token = SerializationUtils.clone(ori.getToken());
    }

    public PetasosComponentTypeToken(String token){
        this.token = SerializationUtils.clone(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public FDNToken toTypeBasedFDNToken(){
        PetasosNodeFunctionFDN topologyFDN = new PetasosNodeFunctionFDN(this);
        FDN genericFDN = topologyFDN.toTypeBasedFDN();
        FDNToken genericFDNToken = genericFDN.getToken();
        return(genericFDNToken);
    }

    public FDNToken toVersionBasedFDNToken(){
        PetasosNodeFunctionFDN topologyFDN = new PetasosNodeFunctionFDN(this);
        FDN genericFDN = topologyFDN.toTypeBasedFDNWithVersion();
        FDNToken genericFDNToken = genericFDN.getToken();
        return(genericFDNToken);
    }

    @Override
    public String toString() {
        return "TopologyNodeFunctionFDNToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
