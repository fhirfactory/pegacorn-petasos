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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Mark A. Hunter
 * @since 2020-08-07
 */
public class PetasosNodeFunctionFDN extends PetasosNodeFDN {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosNodeFunctionFDN.class);

    public PetasosNodeFunctionFDN(PetasosNodeFunctionFDN oriFDN){
        super(oriFDN);
    }

    public PetasosNodeFunctionFDN(){
        super();
    }

    public PetasosNodeFunctionFDN(PetasosComponentTypeToken token){
        LOG.debug(".TopologyNodeFunctionFDN(): Entry, token->{}", token);
        ArrayList<PetasosNodeRDN> nodeSet= new ArrayList<>();
        try{
            JsonMapper mapper = new JsonMapper();
            PetasosNodeRDNSet nodeRDNSet = mapper.readValue(token.getToken(), PetasosNodeRDNSet.class);
            int rdnCount = nodeRDNSet.getPayload().size();
            LOG.trace(".TopologyNodeFunctionFDN: RDN Count --> {}", rdnCount);
            for(int counter = 0; counter < rdnCount; counter ++){
                PetasosNodeRDN currentRDN = nodeRDNSet.getPayload().get(counter);
                nodeSet.add(currentRDN);
            }
            LOG.trace(".TopologyNodeFunctionFDN: Built nodeSet");
            setHierarchicalNameSet(nodeSet);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    public PetasosComponentTypeToken getFunctionToken(){
        LOG.debug(".getFunctionToken(): Entry");
        PetasosNodeRDNSet nodeRDNSet = new PetasosNodeRDNSet(this.getHierarchicalNameSet());
        String tokenString = null;
        try{
            JsonMapper mapper = new JsonMapper();
            tokenString = mapper.writeValueAsString(nodeRDNSet);
            LOG.trace(".getFunctionToken(): Generated tokenString, value->{}", tokenString);
        } catch(JsonProcessingException jsonException){
            jsonException.printStackTrace();
            tokenString = "";
        }
        PetasosComponentTypeToken newToken = new PetasosComponentTypeToken(tokenString);
        return(newToken);
    }
}
