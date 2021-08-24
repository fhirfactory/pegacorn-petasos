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
package net.fhirfactory.pegacorn.processingplant;

import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosNodeFDN;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosNodeFunctionFDN;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosNodeRDN;
import net.fhirfactory.pegacorn.petasos.core.resources.component.valuesets.PetasosComponentTypeEnum;
import net.fhirfactory.pegacorn.petasos.model.capabilities.CapabilityFulfillmentInterface;
import net.fhirfactory.pegacorn.petasos.model.capabilities.base.CapabilityUtilisationRequest;
import net.fhirfactory.pegacorn.petasos.model.capabilities.base.CapabilityUtilisationResponse;
import net.fhirfactory.pegacorn.components.interfaces.topology.PegacornTopologyFactoryInterface;
import net.fhirfactory.pegacorn.components.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.pegacorn.deployment.properties.configurationfilebased.common.archetypes.ClusterServiceDeliverySubsystemPropertyFile;
import net.fhirfactory.pegacorn.deployment.topology.factories.archetypes.interfaces.SolutionNodeFactoryInterface;
import net.fhirfactory.pegacorn.deployment.topology.manager.TopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.model.common.TopologyNode;
import net.fhirfactory.pegacorn.deployment.topology.model.common.valuesets.NetworkSecurityZoneEnum;
import net.fhirfactory.pegacorn.deployment.topology.model.nodes.ProcessingPlantTopologyNode;
import net.fhirfactory.pegacorn.deployment.topology.model.nodes.WorkshopTopologyNode;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ProcessingPlant extends RouteBuilder implements ProcessingPlantInterface {

    private ProcessingPlantTopologyNode processingPlantNode;
    private String instanceQualifier;
    private boolean isInitialised;

    ConcurrentHashMap<String, CapabilityFulfillmentInterface> capabilityDeliveryServices;

    @Inject
    private TopologyIM topologyIM;

    abstract protected ClusterServiceDeliverySubsystemPropertyFile specifyPropertyFile();
    abstract protected Logger specifyLogger();
    abstract protected PegacornTopologyFactoryInterface specifyTopologyFactory();
    abstract protected SolutionNodeFactoryInterface specifySolutionNodeFactory();
    abstract protected void executePostConstructActivities();

    protected Logger getLogger(){return(specifyLogger());}
    protected ClusterServiceDeliverySubsystemPropertyFile getPropertyFile(){return(specifyPropertyFile());}

    @Override
    public PegacornTopologyFactoryInterface getTopologyFactory(){
        return(specifyTopologyFactory());
    }

    public ProcessingPlant() {
        super();
        this.isInitialised = false;
        this.instanceQualifier = UUID.randomUUID().toString();
    }

    @PostConstruct
    public void initialise() {
        if (!isInitialised) {
            getLogger().debug("StandardProcessingPlatform::initialise(): Invoked!");
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising TopologyIM --> Start");
            getTopologyIM().initialise();
            getTopologyFactory().initialise();
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising TopologyIM --> Finish");
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising Building Subsystem Topology --> Start");
            specifySolutionNodeFactory().initialise();
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising Building Subsystem Topology --> Finish");
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising ProcessingPlant Resolution --> Start");
            resolveProcessingPlant();
            getLogger().debug("StandardProcessingPlatform::initialise(): Initialising ProcessingPlant Resolution --> Finish");

            capabilityDeliveryServices = new ConcurrentHashMap<>();

            executePostConstructActivities();
            isInitialised = true;
        }
    }

    @Override
    public void initialisePlant() {
        initialise();
    }


    public TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    public PetasosNodeFunctionFDN getNodeToken() {
        return (this.processingPlantNode.getNodeFunctionFDN());
    }

    private void resolveProcessingPlant() {
        getLogger().debug(".resolveProcessingPlant(): Entry");
        String processingPlantName = getPropertyFile().getSubsystemInstant().getProcessingPlantName();
        String processingPlantVersion = getPropertyFile().getSubsystemInstant().getProcessingPlantVersion();
        getLogger().debug(".resolveProcessingPlant(): Getting ProcessingPlant->{}, version->{}", processingPlantName, processingPlantVersion);
        getLogger().trace(".resolveProcessingPlant(): Resolving list of available ProcessingPlants");
        List<TopologyNode> topologyNodes = topologyIM.nodeSearch(PetasosComponentTypeEnum.PROCESSING_PLANT, processingPlantName, processingPlantVersion);
        if(getLogger().isTraceEnabled()){
            if(topologyNodes == null){
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return a null list");
            }
            if(topologyNodes.isEmpty()){
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return an empty list");
            }
            if(topologyNodes.size() > 1){
                getLogger().trace(".resolveProcessingPlant(): nodeSearch return a list containing more than 1 entry!");
            }
        }
        getLogger().trace(".resolveProcessingPlant(): Matching to my Name/Version");
        if(topologyNodes.isEmpty() || topologyNodes.size() > 1){
            throw new RuntimeException("Unable to resolve ProcessingPlant");
        }
        this.processingPlantNode = (ProcessingPlantTopologyNode) topologyNodes.get(0);
        getLogger().debug(".resolveProcessingPlant(): Exit, Resolved ProcessingPlant, processingPlant->{}", processingPlantNode);
    }

    @Override
    public void configure() throws Exception {
        String processingPlantName = getFriendlyName();

        from("timer://"+processingPlantName+"?delay=1000&repeatCount=1")
            .routeId("ProcessingPlant::"+processingPlantName)
            .log(LoggingLevel.DEBUG, "Starting....");
    }

    private String getFriendlyName(){
        getLogger().debug(".getFriendlyName(): Entry");
        String nodeName = getProcessingPlantNode().getNodeRDN().getNodeName();
        String nodeVersion = getProcessingPlantNode().getNodeRDN().getNodeVersion();
        String friendlyName = nodeName + "(" + nodeVersion + ")";
        return(nodeName);
    }

    @Override
    public ProcessingPlantTopologyNode getProcessingPlantNode() {
        return (this.processingPlantNode);
    }

    public PetasosNodeFDN getProcessingPlantNodeFDN() {
        return (this.processingPlantNode.getNodeFDN());
    }

    @Override
    public WorkshopTopologyNode getWorkshop(String workshopName, String version) {
        getLogger().debug(".getWorkshop(): Entry, workshopName --> {}, version --> {}", workshopName, version);
        boolean found = false;
        WorkshopTopologyNode foundWorkshop = null;
        for (PetasosNodeFDN containedWorkshopFDN : this.processingPlantNode.getWorkshops()) {
            WorkshopTopologyNode containedWorkshop = (WorkshopTopologyNode)topologyIM.getNode(containedWorkshopFDN);
            PetasosNodeRDN testRDN = new PetasosNodeRDN(PetasosComponentTypeEnum.WORKSHOP, workshopName, version);
            if (testRDN.equals(containedWorkshop.getNodeRDN())) {
                found = true;
                foundWorkshop = containedWorkshop;
                break;
            }
        }
        if (found) {
            getLogger().debug(".getWorkshop(): Exit, workshop found!");
            return (foundWorkshop);
        }
        getLogger().debug(".getWorkshop(): Exit, workshop not found!");
        return (null);
    }

    public WorkshopTopologyNode getWorkshop(String workshopName){
        getLogger().debug(".getWorkshop(): Entry, workshopName --> {}", workshopName);
        String version = this.processingPlantNode.getNodeRDN().getNodeVersion();
        WorkshopTopologyNode workshop = getWorkshop(workshopName, version);
        getLogger().debug(".getWorkshop(): Exit");
        return(workshop);
    }

    @Override
    public String getSimpleFunctionName() {
        PetasosNodeRDN functionRDN = getProcessingPlantNode().getNodeFunctionFDN().extractRDNForNodeType(PetasosComponentTypeEnum.PROCESSING_PLANT);
        String functionName = functionRDN.getNodeName();
        return (functionName);
    }

    @Override
    public String getSimpleInstanceName() {
        String instanceName = getSimpleFunctionName() + "(" + instanceQualifier + ")";
        return (instanceName);
    }

    @Override
    public NetworkSecurityZoneEnum getNetworkZone(){
        NetworkSecurityZoneEnum securityZone = getProcessingPlantNode().getSecurityZone();
        return(securityZone);
    }

    @Override
    public String getIPCServiceName() {
        return (getProcessingPlantNode().getSubsystemName());
    }

    @Override
    public String getDeploymentSite() {
        PetasosNodeRDN siteRDN = getProcessingPlantNode().getNodeFDN().extractRDNForNodeType(PetasosComponentTypeEnum.SITE);
        String siteName = siteRDN.getNodeName();
        return (siteName);
    }


    @Override
    public void registerCapabilityFulfillmentService(String capabilityName, CapabilityFulfillmentInterface fulfillmentInterface) {
        if(fulfillmentInterface == null){
            return;
        }
        this.capabilityDeliveryServices.put(capabilityName, fulfillmentInterface);
    }

    @Override
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request) {
        if(request == null){
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setDateCompleted(Instant.now());
            response.setSuccessful(false);
            return(response);
        }
        String capabilityName = request.getRequiredCapabilityName();
        CapabilityFulfillmentInterface interfaceToUse = this.capabilityDeliveryServices.get(capabilityName);
        if(interfaceToUse == null){
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setDateCompleted(Instant.now());
            response.setSuccessful(false);
            response.setInScope(false);
            return(response);
        }
        CapabilityUtilisationResponse capabilityUtilisationResponse = interfaceToUse.executeTask(request);
        return(capabilityUtilisationResponse);
    }
}
