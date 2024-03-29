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

package net.fhirfactory.pegacorn.petasos.core.moa.pathway.wupcontainer.worker.buildingblocks;

import net.fhirfactory.pegacorn.petasos.core.resources.node.datatypes.PetasosNodeFunctionFDNToken;
import net.fhirfactory.pegacorn.deployment.topology.manager.TopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.model.nodes.WorkUnitProcessorTopologyNode;
import net.fhirfactory.pegacorn.petasos.core.moa.brokers.PetasosMOAServicesBroker;
import net.fhirfactory.pegacorn.petasos.core.moa.pathway.naming.RouteElementNames;
import net.fhirfactory.pegacorn.petasos.model.configuration.PetasosPropertyConstants;
import net.fhirfactory.pegacorn.petasos.model.pathway.WorkUnitTransportPacket;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosTaskStatusElement;
import net.fhirfactory.pegacorn.petasos.model.resilience.parcel.ResilienceParcelProcessingStatusEnum;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoW;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * @author Mark A. Hunter
 * @since 2020-07-01
 */
@Dependent
public class WUPContainerEgressProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerEgressProcessor.class);
    RouteElementNames elementNames = null;

    @Inject
    PetasosMOAServicesBroker petasosMOAServicesBroker;

    @Inject
    TopologyIM topologyProxy;


    public WorkUnitTransportPacket egressContentProcessor(WorkUnitTransportPacket ingresPacket, Exchange camelExchange) {
      	LOG.debug(".egressContentProcessor(): Entry, ingresPacket (WorkUnitTransportPacket) --> {}, wupNodeFDNTokenValue (String) --> {}", ingresPacket);
        // Get my Petasos Context
        LOG.trace(".egressContentProcessor(): Retrieving the WUPTopologyNode from the camelExchange (Exchange) passed in");
        WorkUnitProcessorTopologyNode node = camelExchange.getProperty(PetasosPropertyConstants.WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME, WorkUnitProcessorTopologyNode.class);
        LOG.trace(".egressContentProcessor(): Retrieved the WUPTopologyNode, value->{}", node);
        PetasosNodeFunctionFDNToken wupFunctionToken = node.getNodeFunctionFDN().getFunctionToken();
        LOG.trace(".egressContentProcessor(): wupFunctionToken (NodeElementFunctionToken) for this activity --> {}", wupFunctionToken);
        // Now, continue with business logic
        WorkUnitTransportPacket egressPacket = null;
        switch (node.getResilienceMode()) {
            case RESILIENCE_MODE_MULTISITE:
            case RESILIENCE_MODE_KUBERNETES_MULTISITE:
                LOG.trace(".egressContentProcessor(): Deployment Mode --> PETASOS_MODE_MULTISITE");
            case RESILIENCE_MODE_CLUSTERED:
            case RESILIENCE_MODE_KUBERNETES_CLUSTERED:
                LOG.trace(".egressContentProcessor(): Deployment Mode --> PETASOS_MODE_CLUSTERED");
            case RESILIENCE_MODE_STANDALONE:
            case RESILIENCE_MODE_KUBERNETES_STANDALONE:
                LOG.trace(".egressContentProcessor(): Deployment Mode --> PETASOS_MODE_STANDALONE");
                egressPacket = standaloneDeploymentModeECP(ingresPacket, camelExchange,node);
        }
		LOG.debug(".egressContentProcessor(): Exit, egressPacket (WorkUnitTransportPacket) --> {}", egressPacket);
        return (egressPacket);
    }

    private WorkUnitTransportPacket standaloneDeploymentModeECP(WorkUnitTransportPacket ingresPacket, Exchange camelExchange, WorkUnitProcessorTopologyNode wupNode) {
       	LOG.debug(".standaloneDeploymentModeECP(): Entry, ingresPacket (WorkUnitTransportPacket) --> {}, wupNode (NodeElement) --> {}", ingresPacket, wupNode);
        elementNames = new RouteElementNames(wupNode.getNodeFDN().getToken());
        LOG.trace(".standaloneDeploymentModeECP(): Now, extract WUPJobCard from ingresPacket (WorkUnitTransportPacket)");
        WUPJobCard jobCard = ingresPacket.getCurrentJobCard();
        LOG.trace(".standaloneDeploymentModeECP(): Now, extract ParcelStatusElement from ingresPacket (WorkUnitTransportPacket)");
        PetasosTaskStatusElement statusElement = ingresPacket.getCurrentParcelStatus();
        LOG.trace(".standaloneDeploymentModeECP(): Now, extract UoW from ingresPacket (WorkUnitTransportPacket)");
        UoW uow = ingresPacket.getTask();
		LOG.debug(".standaloneDeploymentModeECP(): uow (UoW) --> {}", uow);
		LOG.trace(".standaloneDeploymentModeECP(): Now, continue processing based on the ParcelStatusElement.getParcelStatus() (ResilienceParcelProcessingStatusEnum)");
        ResilienceParcelProcessingStatusEnum parcelProcessingStatusEnum = statusElement.getParcelStatus();
        switch (parcelProcessingStatusEnum) {
            case PARCEL_STATUS_FINISHED:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED);
                petasosMOAServicesBroker.notifyFinishOfWorkUnitActivity(jobCard, uow);
                break;
            case PARCEL_STATUS_ACTIVE_ELSEWHERE:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE_ELSEWHERE);
            case PARCEL_STATUS_FINISHED_ELSEWHERE:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINISHED_ELSEWHERE);
            case PARCEL_STATUS_FINALISED_ELSEWHERE:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED_ELSEWHERE);
            case PARCEL_STATUS_REGISTERED:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_REGISTERED);
            case PARCEL_STATUS_INITIATED:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_INITIATED);
            case PARCEL_STATUS_ACTIVE:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_ACTIVE);
            case PARCEL_STATUS_FINALISED:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FINALISED);
            case PARCEL_STATUS_FAILED:
            	LOG.trace(".standaloneDeploymentModeECP(): ParcelStatus (ResilienceParcelProcessingStatusEnum) --> {}", ResilienceParcelProcessingStatusEnum.PARCEL_STATUS_FAILED);
            default:
                petasosMOAServicesBroker.notifyFailureOfWorkUnitActivity(jobCard, uow);
        }
        return (ingresPacket);
    }
}
