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

package net.fhirfactory.pegacorn.petasos.core.moa.resilience.processingplant.manager.tasks;

import net.fhirfactory.pegacorn.petasos.core.resources.node.datatypes.PetasosNodeFDN;
import net.fhirfactory.pegacorn.deployment.topology.manager.TopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.model.nodes.WorkUnitProcessorTopologyNode;
import net.fhirfactory.pegacorn.petasos.core.moa.resilience.processingplant.cache.ProcessingPlantWUAEpisodeActivityMatrixDM;
import net.fhirfactory.pegacorn.petasos.model.pathway.ActivityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeIdentifier;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosTaskStatusElement;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class SynchroniseMOAWorkUnitActivityJobCardTask {
	private static final Logger LOG = LoggerFactory.getLogger(SynchroniseMOAWorkUnitActivityJobCardTask.class);

	@Inject
	ProcessingPlantWUAEpisodeActivityMatrixDM activityMatrixDM;

	@Inject
	TopologyIM topologyIM;

	public void synchroniseJobCard(WUPJobCard submittedJobCard) {
		LOG.debug(".synchroniseJobCard(): Entry"); 
		if (submittedJobCard == null) {
			throw (new IllegalArgumentException(".doTask(): submittedJobCard is null"));
		}
        if(LOG.isDebugEnabled()) {
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).previousParcelIdentifier -->{}", submittedJobCard.getActivityID().getUpstreamTaskID());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).previousEpisodeIdentifier --> {}", submittedJobCard.getActivityID().getUpstreamEpisodeID());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).previousWUPFunctionTokan --> {}", submittedJobCard.getActivityID().getUpstreamDeliveredCapability());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).perviousWUPIdentifier --> {}", submittedJobCard.getActivityID().getPreviousWUPIdentifier());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).presentParcelIdentifier -->{}", submittedJobCard.getActivityID().getCurrentTaskID());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).presentEpisodeIdentifier --> {}", submittedJobCard.getActivityID().getCurrentEpisodeID());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).presentWUPFunctionTokan --> {}", submittedJobCard.getActivityID().getCurrentDeliveredCapability());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ActivityID).presentWUPIdentifier --> {}", submittedJobCard.getActivityID().getPresentWUPIdentifier());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).cardID (ContunuityID).createDate --> {}", submittedJobCard.getActivityID().getCreationDate());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).clusterMode (ConcurrencyModeEnum) -->{}", submittedJobCard.getClusterMode());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).currentStatus (WUPActivityStatusEnum) --> {}", submittedJobCard.getCurrentStatus());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).grantedStatus (WUPActivityStatusEnum) --> {}", submittedJobCard.getGrantedStatus());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).toBeDiscarded (boolean) --> {}", submittedJobCard.getIsToBeDiscarded());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).requestedStatus (WUPActivityStatusEnum) --> {}", submittedJobCard.getRequestedStatus());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).systemMode (ResilienceModeEnum) --> {}", submittedJobCard.getSystemMode());
        	LOG.debug(".synchroniseJobCard(): submittedJobCard (WUPJobCard).updateDate (Date) --> {}", submittedJobCard.getUpdateDate());
        }
		ActivityID activityID = submittedJobCard.getActivityID();
        PetasosNodeFDN nodeFDN = new PetasosNodeFDN(activityID.getPresentWUPIdentifier());
		WorkUnitProcessorTopologyNode wup = (WorkUnitProcessorTopologyNode)topologyIM.getNode(nodeFDN);
		switch (wup.getResilienceMode()) {
			case RESILIENCE_MODE_MULTISITE: {
				LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_MULTISITE");
				switch (wup.getConcurrencyMode()) {
					case CONCURRENCY_MODE_CONCURRENT: // Woo hoo - we are full-on highly available
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_MULTISITE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_CONCURRENT");
						break;
					case CONCURRENCY_MODE_STANDALONE: // WTF - why bother!
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_MULTISITE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_STANDALONE");
						break;
					case CONCURRENCY_MODE_ONDEMAND: // make it reliable, scalable
					default:
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_MULTISITE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_ONDEMAND (default concurrency mode)");

				}
				break;
			}
			case RESILIENCE_MODE_CLUSTERED: {
				LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_CLUSTERED");
				switch (wup.getConcurrencyMode()) {
					case CONCURRENCY_MODE_CONCURRENT: // Not possible
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_CLUSTERED, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_CONCURRENT");
					case CONCURRENCY_MODE_STANDALONE: // A waste, we can have multiple - but only want one!
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_CLUSTERED, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_STANDALONE");
					case CONCURRENCY_MODE_ONDEMAND: // OK, preferred & MVP
					default:
						LOG.trace(
								".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_CLUSTERED, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_ONDEMAND (default concurrency mode)");
				}
				break;
			}
			case RESILIENCE_MODE_STANDALONE:
				LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_STANDALONE");
			default: {
				switch (wup.getConcurrencyMode()) {
					case CONCURRENCY_MODE_CONCURRENT: // Not possible!
						LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_STANDALONE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_CONCURRENT (not possible)");
					case CONCURRENCY_MODE_ONDEMAND: // Not possible!
						LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_STANDALONE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_ONDEMAND (not possible)");
					case CONCURRENCY_MODE_STANDALONE: // Really only good for PoCs and Integration Testing
					default:
						LOG.trace(".synchroniseJobCard(): Deployment Mode --> PETASOS_MODE_STANDALONE, Concurrency Mode --> PETASOS_WUA_CONCURRENCY_MODE_STANDALONE (default concurrent mode)");
						standaloneModeSynchroniseJobCard(submittedJobCard);
				}
			}
		}
	}

	/**
	 *
	 * @param actionableJobCard
	 */
	public void standaloneModeSynchroniseJobCard(WUPJobCard actionableJobCard) {
		LOG.debug(".standaloneModeSynchroniseJobCard(): Entry"); 
		if (actionableJobCard == null) {
			throw (new IllegalArgumentException(".doTask(): actionableJobCard is null"));
		}
        if(LOG.isDebugEnabled()) {
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousParcelIdentifier -->{}", actionableJobCard.getActivityID().getUpstreamTaskID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousEpisodeIdentifier --> {}", actionableJobCard.getActivityID().getUpstreamEpisodeID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousWUPFunctionTokan --> {}", actionableJobCard.getActivityID().getUpstreamDeliveredCapability());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).perviousWUPIdentifier --> {}", actionableJobCard.getActivityID().getPreviousWUPIdentifier());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentParcelIdentifier -->{}", actionableJobCard.getActivityID().getCurrentTaskID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentEpisodeIdentifier --> {}", actionableJobCard.getActivityID().getCurrentEpisodeID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentWUPFunctionTokan --> {}", actionableJobCard.getActivityID().getCurrentDeliveredCapability());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentWUPIdentifier --> {}", actionableJobCard.getActivityID().getPresentWUPIdentifier());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ContunuityID).createDate --> {}", actionableJobCard.getActivityID().getCreationDate());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).clusterMode (ConcurrencyModeEnum) -->{}", actionableJobCard.getClusterMode());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).currentStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getCurrentStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).grantedStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getGrantedStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).toBeDiscarded (boolean) --> {}", actionableJobCard.getIsToBeDiscarded());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).requestedStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getRequestedStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).systemMode (ResilienceModeEnum) --> {}", actionableJobCard.getSystemMode());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).updateDate (Date) --> {}", actionableJobCard.getUpdateDate());
        }
		PetasosTaskToken parcelInstanceID = actionableJobCard.getActivityID().getCurrentTaskID();
		PetasosEpisodeIdentifier wuaEpisodeID = actionableJobCard.getActivityID().getCurrentEpisodeID();
		LOG.trace(".standaloneModeSynchroniseJobCard(): Retrieve the ParcelStatusElement from the Cache for ParcelInstanceID --> {}", parcelInstanceID);
		PetasosTaskStatusElement statusElement = activityMatrixDM.getParcelStatusElement(parcelInstanceID);
		LOG.trace(".standaloneModeSynchroniseJobCard(): Retrieved ParcelStatusElement --> {}", statusElement);
		LOG.trace(".standaloneModeSynchroniseJobCard(): Retrieve the ParcelInstanceSet for the wuaEpisodeID --> {}", wuaEpisodeID);
		List<PetasosTaskStatusElement> parcelSet = activityMatrixDM.getEpisodeElementSet(wuaEpisodeID);
		if (LOG.isTraceEnabled()) {
			LOG.trace(".standaloneModeSynchroniseJobCard(): The ParcelSet associated with the ParcelEpisodeID --> {} contains {} elements", wuaEpisodeID, parcelSet.size());
		}
		PetasosTaskToken systemWideFocusedParcelInstanceID = activityMatrixDM.getSiteWideFocusElement(wuaEpisodeID);
		LOG.trace(".standaloneModeSynchroniseJobCard(): The Parcel with systemWideFocusedParcel --> {}", systemWideFocusedParcelInstanceID);
		PetasosTaskToken clusterFocusedParcelInstanceID = activityMatrixDM.getClusterFocusElement(wuaEpisodeID);
		LOG.trace(".standaloneModeSynchroniseJobCard(): The Parcel with clusterFocusedParcel --> {}", clusterFocusedParcelInstanceID);
		if (parcelSet.isEmpty()) {
			throw (new IllegalArgumentException(".synchroniseJobCard(): There are no ResilienceParcels for the given ParcelEpisodeID --> something is very wrong!"));
		}
		LOG.trace( ".standaloneModeSynchroniseJobCard(): Now, again, for the standalone mode - there should only be a single thread per WUA Episode ID, so set it to have FOCUS");
		statusElement.setHasSystemWideFocus(true);
		statusElement.setHasClusterFocus(true);
		LOG.trace(".standaloneModeSynchroniseJobCard(): Now, lets update the JobCard based on the ActivityMatrix");
		actionableJobCard.setGrantedStatus(actionableJobCard.getRequestedStatus());
		actionableJobCard.setUpdateDate(Date.from(Instant.now()));
		if (LOG.isDebugEnabled()) {
			LOG.debug(".standaloneModeSynchroniseJobCard(): Exit");
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousParcelIdentifier -->{}", actionableJobCard.getActivityID().getUpstreamTaskID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousEpisodeIdentifier --> {}", actionableJobCard.getActivityID().getUpstreamEpisodeID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).previousWUPFunctionTokan --> {}", actionableJobCard.getActivityID().getUpstreamDeliveredCapability());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).perviousWUPIdentifier --> {}", actionableJobCard.getActivityID().getPreviousWUPIdentifier());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentParcelIdentifier -->{}", actionableJobCard.getActivityID().getCurrentTaskID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentEpisodeIdentifier --> {}", actionableJobCard.getActivityID().getCurrentEpisodeID());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentWUPFunctionTokan --> {}", actionableJobCard.getActivityID().getCurrentDeliveredCapability());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ActivityID).presentWUPIdentifier --> {}", actionableJobCard.getActivityID().getPresentWUPIdentifier());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).cardID (ContunuityID).createDate --> {}", actionableJobCard.getActivityID().getCreationDate());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).clusterMode (ConcurrencyModeEnum) -->{}", actionableJobCard.getClusterMode());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).currentStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getCurrentStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).grantedStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getGrantedStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).toBeDiscarded (boolean) --> {}", actionableJobCard.getIsToBeDiscarded());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).requestedStatus (WUPActivityStatusEnum) --> {}", actionableJobCard.getRequestedStatus());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).systemMode (ResilienceModeEnum) --> {}", actionableJobCard.getSystemMode());
        	LOG.debug(".standaloneModeSynchroniseJobCard(): actionableJobCard (WUPJobCard).updateDate (Date) --> {}", actionableJobCard.getUpdateDate());
		}
	}
}
