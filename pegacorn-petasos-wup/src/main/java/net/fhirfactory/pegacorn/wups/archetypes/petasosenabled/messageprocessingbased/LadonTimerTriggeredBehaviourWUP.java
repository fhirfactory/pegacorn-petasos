/*
 * Copyright (c) 2020 MAHun
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

package net.fhirfactory.pegacorn.wups.archetypes.petasosenabled.messageprocessingbased;

import net.fhirfactory.pegacorn.components.dataparcel.DataParcelManifest;
import net.fhirfactory.pegacorn.petasos.wup.moa.MessageBasedWUPEndpoint;
import net.fhirfactory.pegacorn.petasos.wup.moa.GenericMessageBasedWUPTemplate;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPArchetypeEnum;

import java.util.ArrayList;
import java.util.List;

public abstract class LadonTimerTriggeredBehaviourWUP extends GenericMessageBasedWUPTemplate {
    
    public LadonTimerTriggeredBehaviourWUP() {
        super();
//        getLogger().debug(".MessagingIngresGatewayWUP(): Entry, Default constructor");
    }

    protected abstract String specifyIngresTopologyEndpointName();
    protected abstract String specifyIngresEndpointVersion();

    @Override
    protected WUPArchetypeEnum specifyWUPArchetype(){
        return(WUPArchetypeEnum.WUP_NATURE_LADON_TIMER_TRIGGERED_BEHAVIOUR);
    }

    @Override
    protected MessageBasedWUPEndpoint specifyIngresEndpoint(){
        getLogger().debug(".specifyIngresTopologyEndpoint(): Entry");
        MessageBasedWUPEndpoint ingresEndpoint = new MessageBasedWUPEndpoint();
        ingresEndpoint.setFrameworkEnabled(false);
        // Get Port
        String ingresEndPoint;
        ingresEndPoint = specifyEndpointComponentDefinition();
        ingresEndPoint = ingresEndPoint + ":";
        ingresEndPoint = ingresEndPoint + this.specifyEndpointProtocol();
        ingresEndPoint = ingresEndPoint + this.specifyEndpointProtocolLeadIn();
//        ingresEndPoint = ingresEndPoint + this.getWUPTopologyNode().getContainingComponent().getContainingComponent();
//        ingresEndPoint = ingresEndPoint + ":" + this.getIngresTopologyEndpointElement().getExposedPort();
        ingresEndPoint = ingresEndPoint + specifyEndpointProtocolLeadout();
        ingresEndpoint.setEndpointSpecification(ingresEndPoint);
        getLogger().debug(".specifyIngresEndpoint(): Exit, ingresEndPoint --> {}", ingresEndPoint);
        return(ingresEndpoint);
    }

    @Override
    protected MessageBasedWUPEndpoint specifyEgressEndpoint(){
        getLogger().debug(".specifyEgressTopologyEndpoint(): Entry");
        MessageBasedWUPEndpoint egressEndpoint = new MessageBasedWUPEndpoint();
        egressEndpoint.setFrameworkEnabled(true);
        egressEndpoint.setEndpointSpecification(this.getNameSet().getEndPointWUPEgress());
        getLogger().debug(".specifyEgressTopologyEndpoint(): Exit");
        return(egressEndpoint);
    }

    @Override
    protected boolean getUsesWUPFrameworkGeneratedIngresEndpoint() {
        return(false);
    }


    /**
     * The Ingres Message Gateway doesn't subscribe to ANY topics as it receives it's 
     * input from an external system.
     * 
     * @return An empty Set<TopicToken>
     */
    @Override
    protected List<DataParcelManifest> specifySubscriptionTopics() {
        List<DataParcelManifest> subTopics = new ArrayList<>();
        return(subTopics);
    }
    
    abstract protected String specifyEndpointComponentDefinition();
    abstract protected String specifyEndpointProtocol();
    abstract protected String specifyEndpointProtocolLeadIn();
    abstract protected String specifyEndpointProtocolLeadout();
}
