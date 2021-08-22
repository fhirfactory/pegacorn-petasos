package net.fhirfactory.pegacorn.petasos.core.resources.node.datatypes;

import java.util.ArrayList;
import java.util.HashMap;

public class PetasosNodeRDNSet {
    private HashMap<Integer, PetasosNodeRDN> payload;

    public PetasosNodeRDNSet(ArrayList<PetasosNodeRDN> rdnArrayList){
        this.payload = new HashMap<>();
        if(rdnArrayList == null){
            return;
        }
        int counter = 0;
        for(PetasosNodeRDN currentRDN: rdnArrayList){
            payload.put(counter, currentRDN);
            counter += 1;
        }
    }

    public PetasosNodeRDNSet(){
        this.payload = new HashMap<>();
    }

    public HashMap<Integer, PetasosNodeRDN> getPayload() {
        return payload;
    }

    public void setPayload(HashMap<Integer, PetasosNodeRDN> payload) {
        this.payload = payload;
    }
}
