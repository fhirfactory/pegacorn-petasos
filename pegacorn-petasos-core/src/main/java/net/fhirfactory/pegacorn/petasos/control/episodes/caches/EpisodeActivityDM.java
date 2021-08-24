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
package net.fhirfactory.pegacorn.petasos.control.episodes.caches;

import net.fhirfactory.pegacorn.deployment.topology.manager.TopologyIM;
import net.fhirfactory.pegacorn.internals.SerializableObject;
import net.fhirfactory.pegacorn.petasos.control.tasks.caches.TaskDM;
import net.fhirfactory.pegacorn.petasos.model.pathway.ActivityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeIdentifier;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeStatusEnum;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosEpisode;
import org.hl7.fhir.r4.model.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.fhirfactory.pegacorn.petasos.model.configuration.PetasosPropertyConstants;

/**
 * This is the re-factored Resilience framework ActivityMatrix for
 * ResilienceParcels. It is a representational Matrix of all the Resilience
 * Parcel activity within the ServiceModule - and has hooks for supporting
 * updates from Clustered and Multi-Site equivalents.
 *
 * @author Mark A. Hunter
 * @since 2020-06-01
 */
@ApplicationScoped
public class EpisodeActivityDM {

    private static final Logger LOG = LoggerFactory.getLogger(EpisodeActivityDM.class);

    private ConcurrentHashMap<String, PetasosEpisode> taskStatusElementCache;
    private SerializableObject taskStatusElementCacheLock;
    private ConcurrentHashMap<PetasosEpisodeIdentifier, List<PetasosTaskToken>> episodeToTaskMap;
    private SerializableObject episodeToTaskMapLock;

    @Inject
    TaskDM parcelCacheDM;

    @Inject
    TopologyIM topologyIM;

    public EpisodeActivityDM() {
        taskStatusElementCache = new ConcurrentHashMap<String, PetasosEpisode>();
        episodeToTaskMap = new ConcurrentHashMap<PetasosEpisodeIdentifier, List<PetasosTaskToken>>();
        taskStatusElementCacheLock = new SerializableObject();
        episodeToTaskMapLock = new SerializableObject();
    }
    
    /**
     * This function registers (adds) the ParcelIdentifier and an associated ParcelStatusElement to the
     * ParcelStatusElementCache (ConcurrentHashMap<ResilienceParcelIdentifier, ParcelStatusElement>). It
     * first checks to see if there is an instance already there. This functionality needs to be
     * enhanced to support cluster-based behaviours.
     *
     * It then registers (adds) the ParcelIdentifier to the WUAEpisode2ParcelMap
     * (ConcurrentHashMap<EpisodeIdentifier,HashSet<ResilienceParcelIdentifier>>) to track that
     * the specific Parcel is part of a processing Episode.
     *
     * @param activityID The WUP/Parcel ActivityID
     * @param initialProcessingStatus The initial (provided) Processing Status of the ResilienceParcel
     * @return A ParcelStatusElement which is used by the WUP Components to determine execution & status privileges.
     */
    public PetasosEpisode addEpisodeActivity(ActivityID activityID, PetasosEpisodeStatusEnum initialProcessingStatus) {
   		LOG.debug(".addEpisodeActivity(): Entry");
        if (activityID == null) {
            throw (new IllegalArgumentException(".registerParcelExecution(): activityID is null"));
        }
        // First we are going to update the ParcelCache
        LOG.trace(".addEpisodeActivity(): Adding/Updating the ParcelStatusElementCache with a new ParcelStatusElement");
        PetasosEpisode newStatusElement = null;
        // First see if there is a status element for the currentTaskID and/or the currentEpisodeID
        LOG.trace(".addEpisodeActivity(): Checking to see if a status element already exists for this currentTaskID/WUP Combination" );
        PetasosEpisode existingStatusElement = findByCurrentTask(activityID.getCurrentTaskID());
        if(existingStatusElement != null)
            if(existingStatusElement.getCurrentWUPIdentifier().equals(activityID.getCurrentWUPIdentifier())){
                LOG.trace(".addEpisodeActivity(): An existing status element is in the cache for this TaskID/WUP");
        }
        if(taskStatusElementCache.containsKey(activityID.getCurrentTaskID()))
        {
            LOG.trace(".addEpisode(): ParcelIdentifier already registered in the ParcelStatusElementCache, let's make sure it's the same though!");
            PetasosEpisode existingStatusElement = taskStatusElementCache.get(activityID.getCurrentTaskID());
            boolean sameInstanceID = existingStatusElement.getEpisodeID().equals(activityID.getCurrentTaskID());
            boolean sameEpisodeID = existingStatusElement.getCurrentEpisodeID().equals(activityID.getCurrentEpisodeID());
            boolean sameWUPInstanceID = existingStatusElement.getCurrentWUPIdentifier().equals(activityID.getCurrentWUPIdentifier());
            boolean sameWUPTypeID = existingStatusElement.getCurrentWUPType().equals(activityID.getCurrentWUPType());
            boolean sameUpstreamEpisodeID = existingStatusElement.getUpstreamEpisodeID().equals(activityID.getUpstreamEpisodeID());
            if( sameInstanceID && sameEpisodeID && sameWUPInstanceID && sameWUPTypeID && sameUpstreamEpisodeID ){
                LOG.trace(".addEpisode(): New ActivityID and existing (registered) ID the same, so update the status (maybe) and then exit");
                existingStatusElement.setActivityStatus(initialProcessingStatus);
                LOG.trace(".addEpisode(): Set the to-be-returned ParcelStatusElement to the existingStatusElement");
                newStatusElement = existingStatusElement;
            } else {
                LOG.trace(".addEpisode(): New ActivityID and existing (registered) ID are different, so delete the existing one from the ParcelStatusElementCache!");
                taskStatusElementCache.remove(activityID.getCurrentTaskID());
                LOG.trace(".addEpisode(): Now create a new ParcelStatusElement, set its initial status and add it to the ParcelStatusElementCache!");
                newStatusElement = new PetasosEpisode(activityID);
                newStatusElement.setActivityStatus(initialProcessingStatus);
                LOG.trace(".addEpisode(): New ParcelStatusElement created, newStatusElement --> {}", newStatusElement);
                taskStatusElementCache.put(activityID.getCurrentTaskID(), newStatusElement);
            }
        } else {
            LOG.trace(".addEpisode(): Create a new ParcelStatusElement, set its initial status and add it to the ParcelStatusElementCache!");
            newStatusElement = new PetasosEpisode(activityID);
            newStatusElement.setActivityStatus(initialProcessingStatus);
            LOG.trace(".addEpisode(): New ParcelStatusElement created, newStatusElement --> {}", newStatusElement);
            taskStatusElementCache.put(activityID.getCurrentTaskID(), newStatusElement);
        }
        // Now let's update the WUAEpisode2ParcelMap for the Episode/ResilienceParcel combination
        if(LOG.isTraceEnabled()) {
            LOG.trace(".addWUA(): Adding the ReslienceParcelIdentifier to the WUAEpisode2ParcelMap");
            LOG.trace(".addWUA(): EpisodeIdentifier --> {}", activityID.getCurrentEpisodeID() );
            LOG.trace(".addWUA(): ResilienceParcelIdentifier --> {}", activityID.getCurrentTaskID());
        }
        if(!episodeToTaskMap.containsKey(activityID.getCurrentEpisodeID()) ){
        	LOG.trace(".addWUA(): No WUAEpisode2ParcelMap Entry for this Episode, creating!");
        	HashSet<PetasosTaskToken> wuaEpisodeParcelSet = new HashSet<PetasosTaskToken>();
        	LOG.trace(".addWUA(): Add the ResilienceParcelIdentifier to the HashSet<ResilienceParcelIdentifier> set of Parcels associated to this Episode");
            wuaEpisodeParcelSet.add(activityID.getCurrentTaskID());
            LOG.trace(".addWUA(): Add the EpisodeIdentifier/HashSet<ResilienceParcelIdentifier> combination to the WUAEpisode2ParcelMap");
        	episodeToTaskMap.put(activityID.getCurrentEpisodeID(),wuaEpisodeParcelSet );
        } else {
        	LOG.trace(".addWUA(): WUAEpisode2ParcelMap Entry exists for this Episode, so retrieve the HashSet<ResilienceParcelIdentifier> set of Parcels associated to this Episode!");
            Set<PetasosTaskToken> wuaEpisodeParcelSet = episodeToTaskMap.get(activityID.getCurrentEpisodeID());
            LOG.trace(".addWUA(): Now check to see if the ResilienceParcelIdentifier is already in the HashSet<ResilienceParcelIdentifier> set of Parcels associated to this Episode?");
            Iterator<PetasosTaskToken> setIterator = wuaEpisodeParcelSet.iterator();
            boolean foundParcelIdentifierInSet = false;
            while(setIterator.hasNext()){
                PetasosTaskToken currentParcelIdentifier = setIterator.next();
                if(currentParcelIdentifier.equals(activityID.getCurrentTaskID())){
                    foundParcelIdentifierInSet = true;
                    break;
                }
            }
            if(!foundParcelIdentifierInSet) {
                LOG.trace(".addWUA(): The ResilienceParcelIdentifier is not already in the HashSet<ResilienceParcelIdentifier> set, so add it!");
                wuaEpisodeParcelSet.add(activityID.getCurrentTaskID());
            } else {
                LOG.trace(".addWUA(): The ResilienceParcelIdentifier is already in the HashSet<ResilienceParcelIdentifier> set, so do nothing!");
            }
        }
        LOG.debug(".addWUA(): Exit, newStatusElement --> {}", newStatusElement);
        return(newStatusElement);
    }

    /**
     * Update the Status of the WUA Element to reflect the requested change. The system is operating in
     * Standalone/Standalone Resilience/Concurrency mode - so there is no need to check for SystemWide/Cluster
     * Focus or support any form of concurrent processing.
     *
     * @param activityID The unique Identifier that distinctly represents this work/resilience activity function
     * @param status     The new status to be applied to the WUA Element
     */
    public void updateWUA(ActivityID activityID, PetasosEpisodeStatusEnum status) {
        LOG.debug(".updateWUA(): Entry, activityID --> {}, status --> {}", activityID, status);
        if (activityID == null) {
            throw (new IllegalArgumentException(".updateParcelActivity(): ActivityID (activityID) Processing Status (status) is null"));
        }
        LOG.trace(".updateWUA(): Get the current ParcelStatusElement");
        PetasosTaskToken parcelInstanceID = activityID.getCurrentTaskID();
        PetasosEpisode currentStatusElement;
        if(taskStatusElementCache.containsKey(parcelInstanceID)) {
            LOG.trace(".updateWUA(): ParcelStatusElement exists -> get it!");
            currentStatusElement = taskStatusElementCache.get(parcelInstanceID);
            LOG.trace(".updateWUA(): Updating status of the ParcelStatusElement!");
            currentStatusElement.setActivityStatus(status);
        } else {
            LOG.trace(".updateWUA(): ParcelStatusElement does not exist -> create it!");
            currentStatusElement = this.addEpisode(activityID, status);
        }
        LOG.debug(".updateWUA(): Exit, updated currentStatusElement --> {}", currentStatusElement);
    }

    public PetasosEpisode getParcelStatusElement(PetasosTaskToken parcelInstanceID) {
        LOG.debug(".getCurrentParcelStatusElement(): Entry, parcelInstanceID --> {}", parcelInstanceID);
        if(taskStatusElementCache.containsKey(parcelInstanceID)) {
	        PetasosEpisode requestedElement = taskStatusElementCache.get(parcelInstanceID);
	        LOG.debug(".getCurrentParcelStatusElement(): Exit, returning requestedElement --> {}", requestedElement);
	        return (requestedElement);
        }
        else {
            LOG.debug(".getCurrentParcelStatusElement(): no matching element.");
            return (null);        	
        }
    }

    private PetasosEpisodeIdentifier findEpisodeIdentifierForParcelInstance(PetasosTaskToken parcelInstanceID) {
        Enumeration<PetasosEpisodeIdentifier> wuaEpisodeEnumeration = episodeToTaskMap.keys();
        while (wuaEpisodeEnumeration.hasMoreElements()) {
            PetasosEpisodeIdentifier presentWUAEpisode = wuaEpisodeEnumeration.nextElement();
            Set<PetasosTaskToken> tokenSet = episodeToTaskMap.get(presentWUAEpisode);
            if(tokenSet == null){
                return(null);
            }
            Iterator<PetasosTaskToken> tokenSetIterator = tokenSet.iterator();
            while(tokenSetIterator.hasNext()) {
                PetasosTaskToken currentIdentifier = tokenSetIterator.next();
                if (currentIdentifier.equals(parcelInstanceID)) {
                    return (presentWUAEpisode);
                }
            }
        }
        return (null);
    }

    /**
     * This method extracts the list of Parcel ID's associated with a WUA
     * Episode and iterates through each - using them to get their associated
     * ParcelStatusElement - which is then checked to see if it has the System
     * Wide Focus for the WUA Episode.
     *
     * @param wuaEpisodeID The Work Unit Activity Episode ID we are attempting
     *                     to determine which Parcel has System Wide Focus for.
     * @return Returns the Parcel Instance Identifier for the Parcel that has
     * System Wide Focus, or null.
     */
    public PetasosTaskToken getSiteWideFocusElement(PetasosEpisodeIdentifier wuaEpisodeID) {
        LOG.debug(".getSiteWideFocusElement(): Entry, wuaEpisodeID --> {}", wuaEpisodeID);
        LOG.trace(".getSiteWideFocusElement(): Retrieve the ResilienceParcels for the Episode");
        if(!episodeToTaskMap.containsKey(wuaEpisodeID)) {
            LOG.debug(".getSiteWideFocusElement(): Exit, No parcel was found with System Wide Focus, returning -null-");
            return (null);
        }
        Set<PetasosTaskToken> wuaEpisodeParcelIDSet = episodeToTaskMap.get(wuaEpisodeID);
        LOG.trace(".getSiteWideFocusElement(): Extracted the set of ResilienceParcel IDs for the Episode (wuaEpisode), wuaEpisodeParcelIDSet (FDNTokenSet) --> {}", wuaEpisodeParcelIDSet);
        LOG.trace(".getSiteWideFocusElement(): Iterator through the Parcel IDs, extract each actual ParcelStatusElement and check to see if it has SystemWide focus");
        Iterator<PetasosTaskToken> wuaEpisodeParcelIDIterator = wuaEpisodeParcelIDSet.iterator();
        if(LOG.isTraceEnabled()) {
        	Enumeration<PetasosTaskToken> statusElementKeys = taskStatusElementCache.keys();
        	while(statusElementKeys.hasMoreElements()) {
        		LOG.trace(".getSiteWideFocusElement(): ParcelStatusElementCache, Key --> {}", statusElementKeys.nextElement());
        	}
        }
        while (wuaEpisodeParcelIDIterator.hasNext()) {
            PetasosTaskToken currentParcelID = wuaEpisodeParcelIDIterator.next();
            LOG.trace(".getSiteWideFocusElement(): Checking ParcelStatusElement for ResilienceParcel ID --> {}", currentParcelID);
            PetasosEpisode currentStatusElement = taskStatusElementCache.get(currentParcelID);
            LOG.trace(".getSiteWideFocusElement(): Extracted ParcelStatusElement --> {}", currentStatusElement);
            if (currentStatusElement.getHasSystemWideFocus()) {
                LOG.debug(".getSiteWideFocusElement(): Exit, Parcel has been found that has System Wide Focus, returning parcelInstanceID --> {}", currentParcelID);
                return (currentParcelID);
            }
        }
        LOG.debug(".getSiteWideFocusElement(): Exit, No parcel was found with System Wide Focus, returning -null-");
        return (null);
    }

    /**
     * This method extracts the list of Parcel ID's associated with a WUA
     * Episode and iterates through each - using them to get their associated
     * ParcelStatusElement - which is then checked to see if it has the Cluster
     * Focus for the WUA Episode.
     *
     * @param wuaEpisodeID The Work Unit Activity Episode ID we are attempting
     *                     to determine which Parcel has Cluster Focus for.
     * @return Returns the Parcel Instance Identifier for the Parcel that has
     * Cluster Focus, or null.
     */
    public PetasosTaskToken getClusterFocusElement(PetasosEpisodeIdentifier wuaEpisodeID) {
        LOG.debug(".getClusterFocusElement(): Entry, wuaEpisodeID --> {}", wuaEpisodeID);
        Set<PetasosTaskToken> wuaEpisodeParcelIDSet = episodeToTaskMap.get(wuaEpisodeID);
        Iterator<PetasosTaskToken> wuaEpisodeParcelIDIterator = wuaEpisodeParcelIDSet.iterator();
        while (wuaEpisodeParcelIDIterator.hasNext()) {
            PetasosTaskToken currentParcelID = wuaEpisodeParcelIDIterator.next();
            PetasosEpisode currentStatusElement = taskStatusElementCache.get(currentParcelID);
            if (currentStatusElement.getHasClusterFocus()) {
                LOG.debug(".getClusterFocusElement(): Exit, Parcel has been found that has Cluster Focus, returning parcelInstanceID --> {}", currentParcelID);
                return (currentParcelID);
            }
        }
        LOG.debug(".getClusterFocusElement(): Exit, No parcel was found with Cluster Focus, returning -null-");
        return (null);
    }

    /**
     * Clear any aged content from the Cache systems
     */
    public List<PetasosTaskToken> getAgedContentFromUpActivityMatrix() {
        LOG.debug(".getAgedContentFromUpActivityMatrix(): Entry");
        ArrayList<PetasosTaskToken> agedContent = new ArrayList<PetasosTaskToken>();
        Enumeration<PetasosEpisodeIdentifier> parcelEpisodeIDIterator = episodeToTaskMap.keys();
        LOG.trace(".getAgedContentFromUpActivityMatrix(): Iterating through each EpisodeID");
        Date currentDate = Date.from(Instant.now());
        Long cutOffAge = currentDate.getTime() - (PetasosPropertyConstants.CACHE_ENTRY_RETENTION_PERIOD_SECONDS);
        Long timeOutAge = currentDate.getTime() - (PetasosPropertyConstants.WUP_ACTIVITY_DURATION_SECONDS);
        while (parcelEpisodeIDIterator.hasMoreElements()) {
            PetasosEpisodeIdentifier parcelEpisodeID = parcelEpisodeIDIterator.nextElement();
            Set<PetasosTaskToken> statusSet = episodeToTaskMap.get(parcelEpisodeID);
            LOG.trace(".clearAgedContentFromUpActivityMatrix(): Iterating through ALL ParcelStatusElements to see if one is FINISHED, ParcelEpisodeID --> {}, ", parcelEpisodeID);
            Iterator<PetasosTaskToken> initialSearchStatusIterator = statusSet.iterator();
            boolean foundFinished = false;
            PetasosTaskToken finishedParcelID = null;
            while (initialSearchStatusIterator.hasNext()) {
                PetasosTaskToken currentParcelInstanceID = initialSearchStatusIterator.next();
                PetasosEpisode currentElement = taskStatusElementCache.get(currentParcelInstanceID);
                switch (currentElement.getActivityStatus()) {
                    case PARCEL_STATUS_FINALISED:
                    case PARCEL_STATUS_FINALISED_ELSEWHERE:
                        if (currentElement.getCreationDate().getTime() > cutOffAge) {
                            agedContent.add(currentParcelInstanceID);
                        }
                        break;
                    case PARCEL_STATUS_REGISTERED:
                    case PARCEL_STATUS_INITIATED:
                    case PARCEL_STATUS_ACTIVE:
                    case PARCEL_STATUS_FINISHED:
                    case PARCEL_STATUS_FINISHED_ELSEWHERE:
                        if (currentElement.getCreationDate().getTime() < timeOutAge) {
                            break;
                        }
                    case PARCEL_STATUS_FAILED:
                    default:
                        agedContent.add(currentParcelInstanceID);
                        break;
                }
            }
        }
        return(agedContent);
    }

    public List<PetasosEpisode> getEpisodeElementSet(PetasosEpisodeIdentifier episodeID){
        LOG.debug(".getEpisodeElementSet(): Entry, episodeID --> {}", episodeID);
        ArrayList<PetasosEpisode> episodeSet = new ArrayList<PetasosEpisode>();
        Set<PetasosTaskToken> episodeParcelIDs = episodeToTaskMap.get(episodeID);
        if(episodeParcelIDs == null){
            return(episodeSet);
        }
        if(episodeParcelIDs.isEmpty()){
            return(episodeSet);
        }
        Iterator<PetasosTaskToken> parcelStatusIDIterator = episodeParcelIDs.iterator();
        while(parcelStatusIDIterator.hasNext()){
            PetasosEpisode currentElement = taskStatusElementCache.get(parcelStatusIDIterator.next());
            if(currentElement != null){
                episodeSet.add(currentElement);
            }
        }
        return(episodeSet);
    }

    /**
     * This function assigns the SystemWideFocus for the specified episode (EpisodeIdentifier) to the specified
     * parcelIdentifier (ResilienceParcelIdentifier). It revokes it from any other ResilienceParcel if it
     * was assigned to them.
     *
     * @param episode The EpisodeIdentifier for the Episode we want to assign (operational) focus for
     * @param parcelIdentifier The ResilienceParcelIdentifier for the Parcel we want to take the lead on doing the actual work -
     *                         with the context of the whole deployed system.
     */
    public void setSystemWideFocusElement(PetasosEpisodeIdentifier episode, PetasosTaskToken parcelIdentifier) {
        if(LOG.isDebugEnabled()){
            LOG.debug(".setSiteWideFocusElement(): Entry");
            LOG.debug(".setSiteWideFocusElement(): episode (EpisodeIdentifier) --> {}", episode);
            LOG.debug(".setSiteWideFocusElement(): parcelIdentifier (ResilienceParcelIdentifier) --> {}", parcelIdentifier);
        }
        // First, let's see if someone already has it!
        PetasosTaskToken currentSiteWideFocusParcel = this.getSiteWideFocusElement(episode);
        boolean alreadyHasFocus = false;
        if(currentSiteWideFocusParcel != null){
            if(currentSiteWideFocusParcel.equals(parcelIdentifier)){
                LOG.trace(".setSiteWideFocusElement(): Nothing to do, the ResilienceParcel already has the focus");
                alreadyHasFocus = true;
            } else {
                LOG.trace(".setSiteWideFocusElement(): Another ResilienceParcel has the focus, so we need to revoke it");
                PetasosEpisode associateStatusElement = this.getParcelStatusElement(currentSiteWideFocusParcel);
                associateStatusElement.setHasSystemWideFocus(false);
                alreadyHasFocus = false;
            }
        }
        if(!alreadyHasFocus){
            LOG.trace(".setSiteWideFocusElement(): Assigning the SiteWide Focus to the provided ResilienceParcelIdentifier");
            PetasosEpisode statusElement = this.getParcelStatusElement(parcelIdentifier);
            statusElement.setHasSystemWideFocus(true);
        }
        LOG.debug(".setSiteWideFocusElement(): Exit");
    }

    /**
     * This function assigns the ClusterFocus for the specified episode (EpisodeIdentifier) to the specified
     * parcelIdentifier (ResilienceParcelIdentifier). It revokes it from any other ResilienceParcel if it
     * was assigned to them.
     *
     * @param episode The EpisodeIdentifier for the Episode we want to assign (operational) focus for
     * @param parcelIdentifier The ResilienceParcelIdentifier for the Parcel we want to take the lead on doing the actual work -
     *                         the context of the Cluster.
     */
    public void setClusterWideFocusElement(PetasosEpisodeIdentifier episode, PetasosTaskToken parcelIdentifier){
        if(LOG.isDebugEnabled()){
            LOG.debug(".setClusterWideFocusElement(): Entry");
            LOG.debug(".setClusterWideFocusElement(): episode (EpisodeIdentifier) --> {}", episode);
            LOG.debug(".setClusterWideFocusElement(): parcelIdentifier (ResilienceParcelIdentifier) --> {}", parcelIdentifier);
        }
        // First, let's see if someone already has it!
        PetasosTaskToken currentClusterWideFocusParcel = this.getClusterFocusElement(episode);
        boolean alreadyHasFocus = false;
        if(currentClusterWideFocusParcel != null){
            if(currentClusterWideFocusParcel.equals(parcelIdentifier)){
                LOG.trace(".setClusterWideFocusElement(): Nothing to do, the ResilienceParcel already has the focus");
                alreadyHasFocus = true;
            } else {
                LOG.trace(".setClusterWideFocusElement(): Another ResilienceParcel has the focus, so we need to revoke it");
                PetasosEpisode associateStatusElement = this.getParcelStatusElement(currentClusterWideFocusParcel);
                associateStatusElement.setHasClusterFocus(false);
                alreadyHasFocus = false;
            }
        }
        if(!alreadyHasFocus){
            LOG.trace(".setClusterWideFocusElement(): Assigning the SiteWide Focus to the provided ResilienceParcelIdentifier");
            PetasosEpisode statusElement = this.getParcelStatusElement(parcelIdentifier);
            statusElement.setHasClusterFocus(true);
        }
        LOG.debug(".setClusterWideFocusElement(): Exit");
    }

    //
    // Helpers
    //

    public PetasosEpisode findByCurrentTask(PetasosTaskToken token){
        PetasosEpisode foundElement = null;
        synchronized (this.taskStatusElementCacheLock) {
            Collection<PetasosEpisode> values = this.taskStatusElementCache.values();
            for (PetasosEpisode currentStatusElement : values) {
                if (currentStatusElement.getCurrentTaskID().equals(token)) {
                    foundElement = currentStatusElement;
                    break;
                }
            }
        }
        return(foundElement);
    }

    public PetasosEpisode findByCurrentWUP(Identifier wupIdentifier){
        PetasosEpisode foundElement = null;
        synchronized (this.taskStatusElementCacheLock) {
            Collection<PetasosEpisode> values = this.taskStatusElementCache.values();
            for (PetasosEpisode currentStatusElement : values) {
                if (currentStatusElement.getCurrentWUPIdentifier().equals(wupIdentifier)) {
                    foundElement = currentStatusElement;
                    break;
                }
            }
        }
        return(foundElement);
    }

    public List<PetasosEpisode> findByCurrentEpisode(PetasosTaskToken episodeToken){
        List<PetasosEpisode> foundElements = new ArrayList<>();
        synchronized (this.taskStatusElementCacheLock) {
            Collection<PetasosEpisode> values = this.taskStatusElementCache.values();
            for (PetasosEpisode currentStatusElement : values) {
                if (currentStatusElement.getCurrentEpisodeID().equals(episodeToken)) {
                    foundElements.add(currentStatusElement);
                }
            }
        }
        return(foundElements);
    }
}
