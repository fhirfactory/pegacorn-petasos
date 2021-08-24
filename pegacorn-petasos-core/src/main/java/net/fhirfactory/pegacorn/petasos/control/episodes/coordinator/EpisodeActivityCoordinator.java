/*
 * Copyright (c) 2021 Mark A. Hunter
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
package net.fhirfactory.pegacorn.petasos.control.episodes.coordinator;

import net.fhirfactory.pegacorn.petasos.control.episodes.coordinator.states.*;
import net.fhirfactory.pegacorn.petasos.core.activitymgt.coordinator.states.*;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosEpisode;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EpisodeActivityCoordinator {
    private static final Logger LOG = LoggerFactory.getLogger(EpisodeActivityCoordinator.class);

    @Inject
    private EpisodeStateProcessorActive activeStateProcessor;

    @Inject
    private EpisodeStateProcessorFailed failedStateProcessor;

    @Inject
    private EpisodeStateProcessorFinalised finalisedStateProcessor;

    @Inject
    private EpisodeStateProcessorFinished finishedStateProcessor;

    @Inject
    private EpisodeStateProcessorReady readyStateProcessor;

    @Inject
    private EpisodeStateProcessorCancelled cancelledStateProcessor;

    public PetasosEpisode processEpisodeStateChange(PetasosEpisode statusElement, PetasosEpisodeStatusEnum newStatus){
        LOG.debug(".processEpisodeStateChange(): Entry, statusElement->{}, newStatus->{}", statusElement, newStatus);
        PetasosEpisode outcomeStatusElement = null;
        boolean iterateAgain = false;
        do {
            switch (newStatus) {
                case PARCEL_STATUS_INITIATED:
                case PARCEL_STATUS_ACTIVE: {
                    outcomeStatusElement = activeStateProcessor.processActiveStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_FAILED:{
                    outcomeStatusElement = failedStateProcessor.processFailedStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_FINISHED:{
                    outcomeStatusElement = finishedStateProcessor.processFinishedStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_CANCELLED:{
                    outcomeStatusElement = cancelledStateProcessor.processCancelledStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_FINALISED:{
                    outcomeStatusElement = finalisedStateProcessor.processFinalisedStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_REGISTERED:{
                    outcomeStatusElement = readyStateProcessor.processReadyStatus(statusElement);
                    break;
                }
                case PARCEL_STATUS_ACTIVE_ELSEWHERE:
                case PARCEL_STATUS_FINISHED_ELSEWHERE:
                case PARCEL_STATUS_FINALISED_ELSEWHERE:
                default:{
                    LOG.error(".processEpisodeStateChange(): Bad Status / Deprecated Status");
                    outcomeStatusElement = statusElement;
                    outcomeStatusElement.setActivityStatus(PetasosEpisodeStatusEnum.PARCEL_STATUS_FAILED);
                    break;
                }
            }
            if(outcomeStatusElement != null){
                if(outcomeStatusElement.getActivityStatus().equals(newStatus)){
                    iterateAgain = false;
                } else {
                    iterateAgain = true;
                    newStatus = outcomeStatusElement.getActivityStatus();
                }
            }
        } while(iterateAgain);
        LOG.debug(".processEpisodeStateChange(): Exit, outcomeStatusElement->{}", outcomeStatusElement);
        return(outcomeStatusElement);
    }
}
