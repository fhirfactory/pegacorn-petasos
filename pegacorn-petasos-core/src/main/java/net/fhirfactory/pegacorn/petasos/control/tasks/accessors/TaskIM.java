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

package net.fhirfactory.pegacorn.petasos.control.tasks.accessors;

import net.fhirfactory.pegacorn.petasos.audit.brokers.MOAServicesAuditBroker;
import net.fhirfactory.pegacorn.petasos.control.tasks.caches.TaskDM;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.pathway.ActivityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

/**
 * @author Mark A. Hunter
 */
@ApplicationScoped
public class TaskIM {
    private static final Logger LOG = LoggerFactory.getLogger(TaskIM.class);
//    private FDN nodeInstanceFDN;

    @Inject
    TaskDM taskCacheDM;

    @Inject
    MOAServicesAuditBroker auditServicesBroker;
    

    public PetasosTask registerTask(ActivityID activityID, PetasosTask task, boolean synchronousWriteToAudit) {
        LOG.debug(".registerTask(): Entry");
        if ((task == null) || (activityID == null)) {
            throw (new IllegalArgumentException("task, wupTypeID or wupInstanceID are null in method invocation"));
        }
        if(LOG.isDebugEnabled()) {
    		LOG.debug(".registerTask(): activityID (ActivityID).previousParcelIdentifier -->{}", activityID.getUpstreamTaskID());
    		LOG.debug(".registerTask(): activityID (ActivityID).previousEpisodeIdentifier --> {}", activityID.getUpstreamEpisodeID());
    		LOG.debug(".registerTask(): activityID (ActivityID).previousWUPFunctionTokan --> {}", activityID.getUpstreamWUPType());
    		LOG.debug(".registerTask(): activityID (ActivityID).previousWUPIdentifier --> {}", activityID.getUpstreamWUPIdentifier());
    		LOG.debug(".registerTask(): activityID (ActivityID).presentParcelIdentifier -->{}", activityID.getCurrentTaskID());
    		LOG.debug(".registerTask(): activityID (ActivityID).presentEpisodeIdentifier --> {}", activityID.getCurrentEpisodeID());
    		LOG.debug(".registerTask(): activityID (ActivityID).presentWUPFunctionTokan --> {}", activityID.getCurrentWUPType());
    		LOG.debug(".registerTask(): activityID (ActivityID).presentWUPIdentifier --> {}", activityID.getCurrentWUPIdentifier());
    		LOG.debug(".registerTask(): activityID (ContunuityID).createDate --> {}", activityID.getCreationDate());
    		LOG.debug(".registerTask(): unitOfWork task --> {}", task);
        }
        LOG.trace(".registerTask(): Checking and/or Creating a WUAEpisde ID");
//        if(!activityID.hasPresentEpisodeIdentifier()) {
//        	FDN newWUAFDN = new FDN(activityID.getCurrentWUPType().toVersionBasedFDNToken());
//        	FDN uowTypeFDN = new FDN(unitOfWork.getCurrentImplementingCapability());
//        	newWUAFDN.appendFDN(uowTypeFDN);
//        	PetasosEpisodeIdentifier wuaEpisodeToken = new PetasosEpisodeIdentifier(newWUAFDN.getToken());
//        	activityID.setCurrentEpisodeID(wuaEpisodeToken);
//        }
        // 1st, lets register the Task
        LOG.trace(".registerTask(): check for existing ResilienceParcel instance for this WUP/UoW combination");
        PetasosTask registeredTask =  taskCacheDM.getTask(activityID.getCurrentTaskID());
        if(registeredTask != null){
            LOG.trace(".registerParcel(): Well, there seems to be a Parcel already for this WUPInstanceID/UoWInstanceID. Odd, but let's use it!");
        } else {
            LOG.trace(".registerParcel(): Attempted to retrieve existing ResilienceParcel, and there wasn't one, so let's create it!");
            parcelInstance = new ResilienceParcel(activityID, unitOfWork);
            taskCacheDM.addTask(parcelInstance);
            LOG.trace(".registerParcel(): Set the PresentParcelInstanceID in the ActivityID (ActivityID), ParcelInstanceID --> {}", parcelInstance.getIdentifier());
            activityID.setCurrentTaskID(parcelInstance.getIdentifier());
            Date registrationDate = Date.from(Instant.now());
            LOG.trace(".registerParcel(): Set the Registration Date --> {}", registrationDate);
            parcelInstance.setRegistrationDate(registrationDate);
            LOG.trace(".registerParcel(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
            parcelInstance.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
            LOG.trace(".registerParcel(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_REGISTERED);
            parcelInstance.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_REGISTERED);
            LOG.trace(".registerParcel(): Doing an Audit Write");
            auditServicesBroker.logActivity(parcelInstance);

        }
        LOG.debug(".registerParcel(): Exit");
        if(LOG.isDebugEnabled()) {
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).episodeID --> {}", parcelInstance.getEpisodeIdentifier());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).upsteamEpisodeID --> {}", parcelInstance.getUpstreamEpisodeIdentifier());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).parcelInstanceID --> {}", parcelInstance.getIdentifier());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).associatedWUPInstanceID --> {}", parcelInstance.getAssociatedWUPIdentifier());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).processingStatus --> {}", parcelInstance.getProcessingStatus());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).typeID --> {}", parcelInstance.getTypeID());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).registrationDate --> {}", parcelInstance.getRegistrationDate());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).startDate --> {}", parcelInstance.getStartDate());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).finishedDate --> {}", parcelInstance.getFinishedDate());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).finalisationDate --> {}", parcelInstance.getFinalisationDate());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).finalisationStatus --> {}", parcelInstance.getFinalisationStatus());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).cancellationDate --> {}", parcelInstance.getCancellationDate());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).instanceID --> {}", parcelInstance.getActualUoW().getInstanceID());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).typeID --> {}", parcelInstance.getActualUoW().getCurrentImplementingCapability());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).payloadTopicID --> {}", parcelInstance.getActualUoW().getPayloadTopicID());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).ingresContent --> {}", parcelInstance.getActualUoW().getIngresContent());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).egressContent --> {}", parcelInstance.getActualUoW().getEgressContent());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).payloadTopicID --> {}", parcelInstance.getActualUoW().getPayloadTopicID());
        	LOG.debug(".registerParcel(): parcelInstance (ResilienceParcel).actualUoW (UoW).processingOutcome --> {}", parcelInstance.getActualUoW().getProcessingOutcome());
        }
        return(parcelInstance);
    }

    @Transactional
    public ResilienceParcel notifyParcelProcessingStart(PetasosTaskToken parcelID) {
        LOG.debug(".notifyParcelProcessingStart(): Entry, parcelID --> {}", parcelID);
        if (parcelID == null) {
            throw (new IllegalArgumentException("parcelID is null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingStart(): retrieve existing Parcel");
        ResilienceParcel currentParcel = taskCacheDM.getTask(parcelID);
        Date startDate = Date.from(Instant.now());
        LOG.trace(".notifyParcelProcessingStart(): Set the Start Date --> {}", startDate);
        currentParcel.setStartDate(startDate);
        LOG.trace(".notifyParcelProcessingStart(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        LOG.trace(".notifyParcelProcessingStart(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_ACTIVE);
        currentParcel.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_ACTIVE);
        // TODO Check to see if we should do an Audit Entry when we start processing (as well as when it is registered)
        // LOG.trace(".notifyParcelProcessingStart(): Doing an Audit Write, note that it is asynchronous by design");
        // auditWriter.writeAuditEntry(currentParcel,false);
        LOG.debug(".notifyParcelProcessingStart(): Exit, returning finished Parcel --> {}", currentParcel);
        return(currentParcel);
    }

    @Transactional
    public ResilienceParcel notifyParcelProcessingFinish(PetasosTaskToken parcelID, UoW unitOfWork) {
        LOG.debug(".notifyParcelProcessingFinish(): Entry, parcelID (ResilienceParcelIdentifier) --> {}, unitOfWork (UoW) --> {}", parcelID, unitOfWork);
        if ((unitOfWork == null) || (parcelID == null)) {
            throw (new IllegalArgumentException("unitOfWork or parcelID are null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingFinish(): retrieve existing Parcel");
        ResilienceParcel currentParcel = taskCacheDM.getTask(parcelID);
        LOG.trace(".notifyParcelProcessingFinish(): Parcel Retrieved, contents --> {}", currentParcel);
        LOG.trace(".notifyParcelProcessingFinish(): update the UoW --> but only if the UoW content comes from the Agent, not the actual WUP itself");
        if(!(unitOfWork == currentParcel.getActualUoW())) {
            LOG.trace(".notifyParcelProcessingFinish(): update the UoW (Egress Content)");
            currentParcel.getActualUoW().setEgressContent(unitOfWork.getEgressContent());
            LOG.trace(".notifyParcelProcessingFinish(): update the UoW Processing Outcome --> {}", unitOfWork.getProcessingOutcome());
            currentParcel.getActualUoW().setProcessingOutcome(unitOfWork.getProcessingOutcome());
        }
        Date finishDate = Date.from(Instant.now());
        LOG.trace(".notifyParcelProcessingFinish(): Set the Finish Date --> {}", finishDate);
        currentParcel.setFinishedDate(finishDate);
        LOG.trace(".notifyParcelProcessingFinish(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        LOG.trace(".notifyParcelProcessingFinish(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_FINISHED);
        currentParcel.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FINISHED);
        // TODO Check to see if we should do an Audit Entry when we finish processing
        // LOG.trace(".notifyParcelProcessingFinish(): Doing an Audit Write, note that it is asynchronous by design");
        auditServicesBroker.logActivity(currentParcel);
       	LOG.debug(".notifyParcelProcessingFinish(): Exit, parcelInstance (ResilienceParcel) --> {}", currentParcel);
        return(currentParcel);
    }

    @Transactional
    public ResilienceParcel notifyParcelProcessingFailure(PetasosTaskToken parcelID, UoW unitOfWork) {
        LOG.debug(".notifyParcelProcessingFailure(): Entry, parcelID --> {}, unitOfWork --> {}", parcelID, unitOfWork);
        if ((unitOfWork == null) || (parcelID == null)) {
            throw (new IllegalArgumentException(".notifyParcelProcessingFailure(): unitOfWork or parcelID are null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingFailure(): retrieve existing Parcel");
        ResilienceParcel currentParcel = taskCacheDM.getTask(parcelID);
        LOG.trace(".notifyParcelProcessingFailure(): update the UoW (Egress Content)");
        currentParcel.getActualUoW().setEgressContent(unitOfWork.getEgressContent());
        LOG.trace(".notifyParcelProcessingFailure(): update the UoW Processing Outcome --> {}", unitOfWork.getProcessingOutcome());
        currentParcel.getActualUoW().setProcessingOutcome(unitOfWork.getProcessingOutcome());
        Date finishDate = Date.from(Instant.now());
        LOG.trace(".notifyParcelProcessingFailure(): Set the Finish Date --> {}", finishDate);
        currentParcel.setFinishedDate(finishDate);
        LOG.trace(".notifyParcelProcessingFailure(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_NOT_FINALISED);
        LOG.trace(".notifyParcelProcessingFailure(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_FAILED);
        currentParcel.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FAILED);
        LOG.trace(".notifyParcelProcessingFailure(): Doing an Audit Write, note that it is asynchronous by desgin");
        auditServicesBroker.logActivity(currentParcel);
        LOG.debug(".notifyParcelProcessingFailure(): Exit, returning failed Parcel --> {}", currentParcel);
        return(currentParcel);
    }

    @Transactional
    public ResilienceParcel notifyParcelProcessingFinalisation(PetasosTaskToken parcelID) {
        LOG.debug(".notifyParcelProcessingFinalisation(): Entry, parcelID --> {}, unitOfWork --> {}", parcelID);
        if (parcelID == null) {
            throw (new IllegalArgumentException(".notifyParcelProcessingFinalisation(): parcelID is null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingFinalisation(): retrieve existing Parcel");
        ResilienceParcel currentParcel = taskCacheDM.getTask(parcelID);
        LOG.trace(".notifyParcelProcessingFinalisation(): checking to see if finish date has been set and, if not, setting it");
        if(!currentParcel.hasFinishedDate()) {
            Date finishDate = Date.from(Instant.now());
            LOG.trace(".notifyParcelProcessingFinalisation(): Set the Finish Date --> {}", finishDate);
            currentParcel.setFinishedDate(finishDate);
        }
        Date finalisationDate = Date.from(Instant.now());
        LOG.trace(".notifyParcelProcessingFinalisation(): Set the Finalisation Date --> {}", finalisationDate);
        currentParcel.setFinalisationDate(finalisationDate);
        LOG.trace(".notifyParcelProcessingFinalisation(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_FINALISED);
        currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_FINALISED);
        LOG.trace(".notifyParcelProcessingFinalisation(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_FINALISED);
        currentParcel.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FINALISED);
        LOG.trace(".notifyParcelProcessingFinalisation(): Doing an Audit Write, note that it is asynchronous by design");
        auditServicesBroker.logActivity(currentParcel);
        LOG.debug(".notifyParcelProcessingFinalisation(): Exit, returning finished Parcel --> {}", currentParcel);
        return(currentParcel);
    }

    @Transactional
    public ResilienceParcel notifyParcelProcessingCancellation(PetasosTaskToken parcelID) {
        LOG.debug(".notifyParcelProcessingCancellation(): Entry, parcelID --> {}", parcelID);
        if (parcelID == null) {
            throw (new IllegalArgumentException(".notifyParcelProcessingFinalisation(): parcelID is null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingCancellation(): retrieve existing Parcel");
        ResilienceParcel currentParcel = taskCacheDM.getTask(parcelID);
        LOG.trace(".notifyParcelProcessingCancellation(): checking to see if finish date has been set and, if not, setting it");
        if(!currentParcel.hasFinishedDate()) {
            Date finishDate = Date.from(Instant.now());
            LOG.trace(".notifyParcelProcessingCancellation(): Set the Finish Date --> {}", finishDate);
            currentParcel.setFinishedDate(finishDate);
        }
        Date finalisationDate = Date.from(Instant.now());
        LOG.trace(".notifyParcelProcessingCancellation(): Set the Finalisation Date --> {}", finalisationDate);
        currentParcel.setFinalisationDate(finalisationDate);
        LOG.trace(".notifyParcelProcessingCancellation(): Set the Parcel Finalisation Status --> {} ", ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_FINALISED);
        currentParcel.setFinalisationStatus(ResilienceParcelFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_FINALISED);
        LOG.trace(".notifyParcelProcessingCancellation(): Set the Parcel Processing Status --> {}", PetasosEpisodeStatusEnum.PARCEL_STATUS_FINALISED);
        currentParcel.setProcessingStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FINALISED);
        LOG.trace(".notifyParcelProcessingCancellation(): Doing an Audit Write, note that it is asynchronous by design");
        auditServicesBroker.logActivity(currentParcel);
        LOG.debug(".notifyParcelProcessingCancellation(): Exit, returning finished Parcel --> {}", currentParcel);
        return(currentParcel);
    }

    @Transactional
    public void notifyParcelProcessingPurge(PetasosTaskToken parcelID) {
        LOG.debug(".notifyParcelProcessingPurge(): Entry, parcelID --> {}, unitOfWork --> {}", parcelID);
        if (parcelID == null) {
            throw (new IllegalArgumentException(".notifyParcelProcessingPurge(): parcelID is null in method invocation"));
        }
        LOG.trace(".notifyParcelProcessingPurge(): retrieve existing Parcel");
        // TODO: Ascertain if we need to do an audit-entry for this.
        //        LOG.trace(".notifyParcelProcessingPurge(): Doing an Audit Write, note that it is asynchronous by design");
        //        auditWriter.writeAuditEntry(currentParcel,false);
        //LOG.debug(".notifyParcelProcessingPurge(): Exit, returning finished Parcel --> {}", currentParcel);
    }
}
