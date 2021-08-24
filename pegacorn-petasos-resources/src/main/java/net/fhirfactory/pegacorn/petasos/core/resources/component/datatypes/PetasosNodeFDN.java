/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.RDN;
import net.fhirfactory.pegacorn.petasos.core.resources.component.valuesets.PetasosComponentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Mark A. Hunter
 * @since 2020-08-07
 */
public class PetasosNodeFDN implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosNodeFDN.class);

    private ArrayList<PetasosNodeRDN> hierarchicalNameSet;

    public PetasosNodeFDN(){
        hierarchicalNameSet = new ArrayList<>();
    }

    public PetasosNodeFDN(PetasosNodeFDN originalToken) {
        LOG.debug(".TopologyNodeFDN(TopologyNodeFDN): Entry, originalToken->{}", originalToken);
        this.hierarchicalNameSet = new ArrayList<>();
        int size = originalToken.getHierarchicalNameSet().size();
        for(int counter = 0; counter < size; counter ++){
            this.hierarchicalNameSet.add(counter, originalToken.getHierarchicalNameSet().get(counter));
        }
        LOG.debug(".TopologyNodeFDN(TopologyNodeFDN): Exit");
    }

    public ArrayList<PetasosNodeRDN> getHierarchicalNameSet() {
        return hierarchicalNameSet;
    }

    public void setHierarchicalNameSet(ArrayList<PetasosNodeRDN> hierarchicalNameSet) {
        this.hierarchicalNameSet = hierarchicalNameSet;
    }

    public void appendTopologyNodeRDN(PetasosNodeRDN newRDN){
        LOG.debug(".appendTopologyNodeRDN: Entry, newRDN->{}", newRDN);
        int count = this.hierarchicalNameSet.size();
        this.hierarchicalNameSet.add(count, newRDN);
    }

    public void appendTopologyNodeFDN(PetasosNodeFDN additionalFDN){
        int additionalElementSize = additionalFDN.getHierarchicalNameSet().size();
        int currentElementSize = this.hierarchicalNameSet.size();
        for(int counter = 0; counter < additionalElementSize; counter ++){
            int position = currentElementSize + counter;
            this.hierarchicalNameSet.add(position, additionalFDN.getHierarchicalNameSet().get(counter));
        }
    }

    public PetasosNodeRDN getLeafRDN(){
        int size = getHierarchicalNameSet().size();
        if(size <= 0){
            return(null);
        }
        PetasosNodeRDN leafRDN = getHierarchicalNameSet().get(size-1);
        return(leafRDN);
    }

    @Override
    public String toString() {
        String simpleString = "{TopologyNodeFDN:";
        for(PetasosNodeRDN nodeRDN: hierarchicalNameSet){
            simpleString += "("+nodeRDN.getNodeType()+"="+nodeRDN.getNodeName()+"-"+nodeRDN.getNodeVersion()+")";
        }
        simpleString += "}";
        return (simpleString);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PetasosNodeFDN)) {
            return false;
        }
        PetasosNodeFDN that = (PetasosNodeFDN) o;
        int thisSize = getHierarchicalNameSet().size();
        int thatSize = that.getHierarchicalNameSet().size();
        if(thisSize != thatSize){
            return(false);
        }
        for(int counter = 0; counter < thisSize; counter ++){
            PetasosNodeRDN thisCurrentRDN = getHierarchicalNameSet().get(counter);
            PetasosNodeRDN thatCurrentRDN = that.getHierarchicalNameSet().get(counter);
            boolean sameName = thisCurrentRDN.getNodeName().contentEquals(thatCurrentRDN.getNodeName());
            boolean sameVersion = thisCurrentRDN.getNodeVersion().contentEquals(thatCurrentRDN.getNodeVersion());
            boolean sameType = thisCurrentRDN.getNodeType().equals(thatCurrentRDN.getNodeType());
            if(!sameName || !sameVersion || !sameType){
                return(false);
            }
        }
        return (true);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHierarchicalNameSet());
    }

    @JsonIgnore
    public String toTag(){
        String simpleTag = new String();
        int count = hierarchicalNameSet.size();
        int counter = 0;
        for(PetasosNodeRDN nodeRDN: hierarchicalNameSet){
            simpleTag += nodeRDN.getNodeName()+"("+nodeRDN.getNodeVersion()+")";
            if(counter < (count -1)){
                simpleTag += ".";
            }
        }
        return(simpleTag);
    }

    @JsonIgnore
    public PetasosComponentIdentifierToken getToken(){
        PetasosNodeRDNSet nodeRDNSet = new PetasosNodeRDNSet(this.hierarchicalNameSet);
        String tokenString = null;
        try{
            JsonMapper mapper = new JsonMapper();
            tokenString = mapper.writeValueAsString(nodeRDNSet);
        } catch(JsonProcessingException jsonException){
            jsonException.printStackTrace();
            tokenString = "";
        }
        PetasosComponentIdentifierToken newToken = new PetasosComponentIdentifierToken(tokenString);
        return(newToken);
    }

    public PetasosNodeFDN(PetasosComponentIdentifierToken token){
        LOG.debug(".TopologyNodeFDN(): Entry, token->{}", token);
        this.hierarchicalNameSet = new ArrayList<>();
        try{
            JsonMapper mapper = new JsonMapper();
            PetasosNodeRDNSet nodeRDNSet = mapper.readValue(token.getTokenValue(), PetasosNodeRDNSet.class);
            int rdnCount = nodeRDNSet.getPayload().size();
            for(int counter = 0; counter < rdnCount; counter ++){
                this.hierarchicalNameSet.add(counter, nodeRDNSet.getPayload().get(counter));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public PetasosNodeFDN(String tokenString){
        LOG.debug(".TopologyNodeFDN(): Entry, tokenString->{}", tokenString);
        this.hierarchicalNameSet = new ArrayList<>();
        try{
            JsonMapper mapper = new JsonMapper();
            PetasosNodeRDNSet nodeRDNSet = mapper.readValue(tokenString, PetasosNodeRDNSet.class);
            int rdnCount = nodeRDNSet.getPayload().size();
            LOG.trace(".TopologyNodeFDN(): Converted tokenString (String) to nodeRDNSet(TopologyNodeRDNSet), rdnCount->{}", rdnCount);
            for(int counter = 0; counter < rdnCount; counter ++){
                PetasosNodeRDN petasosNodeRDN = nodeRDNSet.getPayload().get(counter);
                LOG.trace(".TopologyNodeFDN(): Adding entry[{}], to this.hierarchicalNameSet, value->{}", counter, petasosNodeRDN);
                this.hierarchicalNameSet.add(counter, petasosNodeRDN);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public PetasosNodeRDN extractRDNForNodeType(PetasosComponentTypeEnum nodeType){
        LOG.debug(".TopologyNodeFDN(): Entry, nodeType->{}", nodeType);
        for(PetasosNodeRDN nodeRDN: hierarchicalNameSet){
            if(nodeRDN.getNodeType().equals(nodeType)){
                return(nodeRDN);
            }
        }
        return(null);
    }

    @JsonIgnore
    public FDN toTypeBasedFDN(){
        FDN newFDN = new FDN();
        for(PetasosNodeRDN nodeRDN: hierarchicalNameSet){
            newFDN.appendRDN(new RDN(nodeRDN.getNodeType().getNodeElementType(),nodeRDN.getNodeName()));
        }
        return(newFDN);
    }

    @JsonIgnore
    public FDN toTypeBasedFDNWithVersion(){
        FDN newFDN = new FDN();
        PetasosNodeRDN lastRDN = null;
        for(PetasosNodeRDN currentNodeRDN: hierarchicalNameSet){
            newFDN.appendRDN(new RDN(currentNodeRDN.getNodeName(), currentNodeRDN.getNodeName()));
            lastRDN = currentNodeRDN;
        }
        if(lastRDN != null){
            newFDN.appendRDN(new RDN("Version", lastRDN.getNodeVersion()));
        }
        return(newFDN);
    }
}
