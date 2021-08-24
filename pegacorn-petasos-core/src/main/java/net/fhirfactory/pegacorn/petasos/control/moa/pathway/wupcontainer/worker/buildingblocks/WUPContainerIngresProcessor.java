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

package net.fhirfactory.pegacorn.petasos.control.moa.pathway.wupcontainer.worker.buildingblocks;

import net.fhirfactory.pegacorn.petasos.core.resources.component.PetasosComponent;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosComponentTypeToken;
import net.fhirfactory.pegacorn.deployment.topology.manager.TopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.model.mode.ConcurrencyModeEnum;
import net.fhirfactory.pegacorn.deployment.topology.model.mode.ResilienceModeEnum;
import net.fhirfactory.pegacorn.petasos.wup.moa.broker.PetasosMOAServicesBroker;
import net.fhirfactory.pegacorn.petasos.control.moa.pathway.naming.RouteElementNames;
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import net.fhirfactory.pegacorn.petasos.model.configuration.PetasosPropertyConstants;
import net.fhirfactory.pegacorn.petasos.model.pathway.ActivityID;
import net.fhirfactory.pegacorn.petasos.model.pathway.WorkUnitTransportPacket;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosEpisode;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPActivityStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

/**
 * @author Mark A. Hunter
 * @since 2020-06-01
 */
@Dependent
public class WUPContainerIngresProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerIngresProcessor.class);
    private RouteElementNames elementNames;

    @Inject
    PetasosMOAServicesBroker petasosMOAServicesBroker;

    @Inject
    TopologyIM topologyProxy;
    
    /**
     * This class/method is used as the injection point into the WUP Processing Framework for the specific WUP Type/Instance in question.
     * It registers the following:
     *      - A ResilienceParcel for the UoW (registered with the SystemModule Parcel Cache: via the PetasosServiceBroker)
     *      - A WUPJobCard for the associated Work Unit Activity (registered into the SystemModule Activity Matrix: via the PetasosServiceBroker)
     *      - A ParcelStatusElement for the ResilienceParcel (again, register into the SystemModule Activity Matrix: via the PetasosServiceBroker)
     *
     * The function handles both new UoW or UoW instances that are being re-tried.
     *
     * It performs checks on the Status (WUPJobCard.currentStatus & ParcelStatusElement.hasClusterFocus) to determine if this WUP-Thread should
     * actually perform the Processing of the UoW via the WUP.
     *
     * It also checks on / assigns values to the Status (ParcelStatusElement.parcelStatus) if there are issues with the parcel. If there are, it may also
     * assign a "failed" status to both the WUPJobCard and ParcelStatusElement, and trigger a discard of this Parcel (for a retry) via setting the
     * WUPJobCard.isToBeDiscarded attribute to true.
     *
     * Finally, if all is going OK, but this WUP-Thread does not have the Cluster Focus (or SystemWide Focus), it waits in a sleep/loop until a condition
     * changes.
     *
     * @param transportPacket The WorkUnitTransportPacket that is to be forwarded to the Intersection (if all is OK)
     * @param camelExchange The Apache Camel Exchange object, used to store a Semaphors and Attributes
     * @return Should return a WorkUnitTransportPacket that is forwarding onto the WUP Ingres Gatekeeper.
     */
    public WorkUnitTransportPacket ingresContentProcessor(WorkUnitTransportPacket transportPacket, Exchange camelExchange) {
        LOG.debug(".ingresContentProcessor(): Enter, transportPacket (WorkUnitTransportPacket)->{}", transportPacket );
        // Get my Petasos Context
        LOG.trace(".ingresContentProcessor(): Retrieving the WUPTopologyNode from the camelExchange (Exchange) passed in");
        PetasosComponent node = camelExchange.getProperty(PetasosPropertyConstants.WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME, PetasosComponent.class);
        PetasosComponentTypeToken wupComponentType = new PetasosComponentTypeToken(node.getType().getCodingFirstRep().getCode());
        LOG.trace(".ingresContentProcessor(): wupComponentType (NodeElementFunctionToken) for this activity --> {}", wupComponentType);
        // Now, continue with business logic
        elementNames = new RouteElementNames(node.getUserFriendlyName());
        LOG.trace(".ingresContentProcessor(): Now, check if this the 1st time the associated UoW has been (attempted to be) processed");
        WorkUnitTransportPacket newTransportPacket;
        if (transportPacket.getIsARetry()) {
            LOG.trace(".ingresContentProcessor(): This is a recovery or retry iteration of processing this UoW, so send to .alternativeIngresContentProcessor()");
            newTransportPacket = alternativeIngresContentProcessor(transportPacket, camelExchange, wupComponentType, node.getIdentifierFirstRep());
        } else {
            LOG.trace(".ingresContentProcessor(): This is the 1st time this UoW is being processed, so send to .standardIngresContentProcessor()");
            newTransportPacket = standardIngresContentProcessor(transportPacket, camelExchange, wupComponentType, node.getIdentifierFirstRep());
        }
        long waitTime = PetasosPropertyConstants.WUP_SLEEP_INTERVAL_MILLISECONDS;
        boolean waitState = true;
        WUPJobCard jobCard = newTransportPacket.getCurrentJobCard();
        PetasosEpisode statusElement = newTransportPacket.getCurrentParcelStatus();
        while (waitState) {
            switch (jobCard.getCurrentStatus()) {
                case WUP_ACTIVITY_STATUS_WAITING:
                    LOG.trace(".ingresContentProcessor(): jobCard.getCurrentStatus --> {}",WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING );
                    jobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
                    petasosMOAServicesBroker.synchroniseJobCard(jobCard);
                    if (jobCard.getGrantedStatus() == WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING) {
                        jobCard.setCurrentStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING);
                        petasosMOAServicesBroker.notifyStartOfWorkUnitActivity(jobCard);
                        LOG.trace(".ingresContentProcessor(): We've been granted execution privileges!");
                        waitState = false;
                        break;
                    }
                    break;
                case WUP_ACTIVITY_STATUS_EXECUTING:
                case WUP_ACTIVITY_STATUS_FINISHED:
                case WUP_ACTIVITY_STATUS_FAILED:
                case WUP_ACTIVITY_STATUS_CANCELED:
                default:
                    LOG.trace(".ingresContentProcessor(): jobCard.getCurrentStatus --> Default");
                    jobCard.setIsToBeDiscarded(true);
                    waitState = false;
                    jobCard.setCurrentStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
                    jobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
            }
            if (waitState) {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    LOG.trace(".ingresContentProcessor(): Something interrupted my nap! reason --> {}", e.getMessage());
                }
            }
        }
        if (jobCard.getIsToBeDiscarded()) {
            PetasosEpisode currentParcelStatus = newTransportPacket.getCurrentParcelStatus();
            currentParcelStatus.setRequiresRetry(true);
            currentParcelStatus.setActivityStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FAILED);
        }
        LOG.debug(".ingresContentProcessor(): Exit, newTransportPacket --> {}", newTransportPacket);
        return (newTransportPacket);
    }

    public WorkUnitTransportPacket standardIngresContentProcessor(WorkUnitTransportPacket transportPacket, Exchange camelExchange, PetasosComponentTypeToken wupType, Identifier nodeIdentifier) {
        LOG.debug(".standardIngresContentProcessor(): Enter, transportPacket (WorkUnitTransportPacket) --> {}, nodeIdentifier (NodeElementIdentifier) --> {}", transportPacket,nodeIdentifier );
        PetasosTask theUoW = transportPacket.getTask();
        LOG.trace(".standardIngresContentProcessor(): Creating a new ActivityID/ActivityID");
        Identifier wupID = SerializationUtils.clone(nodeIdentifier);
        PetasosComponentTypeToken wupCapability = SerializationUtils.clone(wupType);
        ActivityID oldActivityID = transportPacket.getPacketID();
        ActivityID newActivityID = new ActivityID();
        PetasosTaskToken upstreamTaskID = SerializationUtils.clone(oldActivityID.getCurrentTaskID());
        PetasosTaskToken upstreamTaskEpisodeID =  SerializationUtils.clone(oldActivityID.getCurrentEpisodeID());
        Identifier upstreamWUPInstanceID =  SerializationUtils.clone(oldActivityID.getCurrentWUPIdentifier());
        PetasosComponentTypeToken upstreamConsumedCapability =  SerializationUtils.clone(oldActivityID.getCurrentWUPType());
        newActivityID.setUpstreamTaskID(upstreamTaskID);
        newActivityID.setUpstreamEpisodeID(upstreamTaskEpisodeID);
        newActivityID.setUpstreamWUPIdentifier(upstreamWUPInstanceID);
        newActivityID.setUpstreamWUPType(upstreamConsumedCapability);
        newActivityID.setCurrentWUPType(wupCapability);
        newActivityID.setCurrentWUPIdentifier(wupID);
        LOG.trace(".standardIngresContentProcessor(): Creating new JobCard");
        WUPJobCard activityJobCard = new WUPJobCard(newActivityID, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_WAITING, WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_EXECUTING, ConcurrencyModeEnum.CONCURRENCY_MODE_STANDALONE, ResilienceModeEnum.RESILIENCE_MODE_STANDALONE, Date.from(Instant.now()));
        LOG.trace(".standardIngresContentProcessor(): Registering the Work Unit Activity using the ActivityID --> {} and UoW --> {}", newActivityID, theUoW);
        PetasosEpisode statusElement = petasosMOAServicesBroker.registerStandardWorkUnitActivity(activityJobCard, theUoW);
        LOG.trace(".standardIngresContentProcessor(): Let's check the status of everything");
        switch (statusElement.getActivityStatus()) {
            case PARCEL_STATUS_REGISTERED:
            case PARCEL_STATUS_ACTIVE_ELSEWHERE:
                LOG.trace(".standardIngresContentProcessor(): The Parcel is either Registered or Active_Elsewhere - both are acceptable at this point");
                break;
            case PARCEL_STATUS_FAILED:
            case PARCEL_STATUS_ACTIVE:
            case PARCEL_STATUS_FINALISED_ELSEWHERE:
            case PARCEL_STATUS_FINALISED:
            case PARCEL_STATUS_FINISHED_ELSEWHERE:
            case PARCEL_STATUS_FINISHED:
            case PARCEL_STATUS_INITIATED:
            default:
                LOG.trace(".standardIngresContentProcessor(): The Parcel is doing something odd, none of the above states should be in-play, so cancel");
                statusElement.setActivityStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FAILED);
                statusElement.setRequiresRetry(true);
                activityJobCard.setRequestedStatus(WUPActivityStatusEnum.WUP_ACTIVITY_STATUS_CANCELED);
                activityJobCard.setIsToBeDiscarded(true);
        }
        WorkUnitTransportPacket newTransportPacket = new WorkUnitTransportPacket(newActivityID, Date.from(Instant.now()),transportPacket.getTask());
        newTransportPacket.setCurrentJobCard(activityJobCard);
        newTransportPacket.setCurrentParcelStatus(statusElement);
        LOG.debug(".ingresContentProcessor(): Exit, newTransportPacket --> {}", newTransportPacket);
        return (newTransportPacket);
    }

    public WorkUnitTransportPacket alternativeIngresContentProcessor(WorkUnitTransportPacket ingresPacket, Exchange camelExchange, PetasosComponentTypeToken wupFunctionToken, Identifier wupInstanceID) {
        LOG.debug(".alternativeIngresContentProcessor(): Enter, ingresPacket --> {}, wupFunctionToken --> {}, wupInstanceID --> {}", ingresPacket, wupFunctionToken, wupInstanceID);
        // TODO Implement alternate flow for ingressContentProcessor functionality (retry functionality).
        return (ingresPacket);
    }
}
