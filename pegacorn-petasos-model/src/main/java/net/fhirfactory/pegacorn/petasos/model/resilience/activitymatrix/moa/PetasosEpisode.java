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

package net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa;

import net.fhirfactory.pegacorn.internals.SerializableObject;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.pathway.ActivityID;
import net.fhirfactory.pegacorn.petasos.model.resilience.episode.PetasosEpisodeStatusEnum;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PetasosEpisode implements Serializable {
    private String episodeID;
    private PetasosEpisodeStatusEnum activityStatus;
    private Integer retryCount;
    private Instant creationDate;
    private Instant lastUpdateDate;
    private Instant finishDate;
    private Instant finalisationDate;
    private List<PetasosTaskToken> alternateTasks;
    private SerializableObject alternateTasksLock;
    private PetasosTaskToken activeTask;
    private boolean requiresRetry;
    private String upstreamEpisodeID;

    public PetasosEpisode(ActivityID newID) {
        this.episodeID = UUID.randomUUID().toString();
        this.creationDate = Date.from(Instant.now());
        this.alternateTasks = new ArrayList<>();
        this.activeTask = null;
        this.activityStatus = null;
        this.requiresRetry = false;
    }

    public String getEpisodeID() {
        return(this.episodeID);
    }

    public void setEpisodeID(String elementID){
        this.episodeID = elementID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
    }

    public void addAlternativeTask(PetasosTaskToken taskToken){
        if(taskToken != null){
            synchronized(alternateTasksLock) {
                if (!getAlternateTasks().contains(taskToken)) {
                    getAlternateTasks().add(taskToken);
                }
            }
        }
    }

    public void removeAlternativeTask(PetasosTaskToken taskToken){
        if(taskToken != null){
            synchronized(alternateTasksLock) {
                if (getAlternateTasks().contains(taskToken)) {
                    getAlternateTasks().remove(taskToken);
                }
            }
        }
    }

    public List<PetasosTaskToken> getAlternateTasks() {
        return alternateTasks;
    }

    public void setAlternateTasks(List<PetasosTaskToken> alternateTasks) {
        this.alternateTasks = alternateTasks;
    }

    public PetasosTaskToken getActiveTask() {
        return activeTask;
    }

    public void setActiveTask(PetasosTaskToken activeTask) {
        this.activeTask = activeTask;
    }

    public PetasosEpisodeStatusEnum getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(PetasosEpisodeStatusEnum activityStatus) {
            this.activityStatus = activityStatus;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
            this.retryCount = retryCount;
    }

    public boolean isRequiresRetry() {
        return requiresRetry;
    }

    public void setRequiresRetry(boolean requiresRetry) {
            this.requiresRetry = requiresRetry;
    }

    @Override
    public String toString() {
        return "PetasosEpisode{" +
                "upstreamTaskID=" + getUpstreamTaskID() +
                ", currentTaskID=" + getCurrentTaskID() +
                ", upstreamWUPIdentifier=" + getUpstreamWUPIdentifier() +
                ", currentWUPIdentifier=" + getCurrentWUPIdentifier() +
                ", creationDate=" + getCreationDate() +
                ", upstreamEpisodeID=" + getUpstreamEpisodeID() +
                ", currentEpisodeID=" + getCurrentEpisodeID() +
                ", upstreamWUPType=" + getUpstreamWUPType() +
                ", currentWUPType=" + getCurrentWUPType() +
                ", statusElementID='" + episodeID + '\'' +
                ", activityStatus=" + activityStatus +
                ", retryCount=" + retryCount +
                ", entryDate=" + creationDate +
                ", alternateTasks=" + alternateTasks +
                ", activeTask=" + activeTask +
                ", requiresRetry=" + requiresRetry +
                '}';
    }
}
