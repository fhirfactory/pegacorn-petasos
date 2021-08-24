/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.petasos.model.pathway;

import net.fhirfactory.pegacorn.internals.SerializableObject;
import net.fhirfactory.pegacorn.petasos.core.resources.capability.datatypes.PetasosCapabilityID;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosComponentTypeToken;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.Identifier;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Mark A. Hunter
 */
public class ActivityID implements Serializable {

    private PetasosTaskToken upstreamTaskID;
    private SerializableObject previousParcelIdentifierLock;
    private PetasosTaskToken upstreamEpisodeID;
    private SerializableObject previousEpisodeIdentifierLock;
    private PetasosTaskToken currentTaskID;
    private SerializableObject presentParcelIdenifierLock;
    private PetasosTaskToken currentEpisodeID;
    private SerializableObject presentEpisodeIdentifierLock;
    private Identifier upstreamWUPIdentifier;
    private SerializableObject upstreamWUPIdentifierLock;
    private PetasosComponentTypeToken upstreamWUPType;
    private SerializableObject upstreamConsumedCapabilityLock;
    private Identifier currentWUPIdentifier;
    private SerializableObject currentWUPIdentifierLock;
    private PetasosComponentTypeToken currentWUPType;
    private SerializableObject currentConsumedCapabilityLock;
    private Date creationDate;
    private SerializableObject creationDateLock;
    private boolean resilientActivity;
    private SerializableObject resilientActivityLock;

    public ActivityID( PetasosTaskToken upstreamTaskID,
                       PetasosTaskToken currentTaskID,
                       Identifier upstreamWUPIdentifier,
                       Identifier currentWUPIdentifier,
                       Date creationDate) {
        // Clear the deck
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.upstreamWUPIdentifier = null;
        this.upstreamWUPType = null;
        this.currentWUPIdentifier = null;
        this.currentWUPType = null;
        this.creationDate = null;

        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.currentConsumedCapabilityLock = new SerializableObject();
        this.currentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.upstreamConsumedCapabilityLock = new SerializableObject();
        this.upstreamWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        this.upstreamTaskID = upstreamTaskID;
        this.currentTaskID = currentTaskID;
        this.upstreamWUPIdentifier = upstreamWUPIdentifier;
        this.currentWUPIdentifier = currentWUPIdentifier;
        this.creationDate = creationDate;

    }

    public ActivityID(PetasosTaskToken upstreamTaskID,
                      PetasosTaskToken currentTaskID,
                      Identifier upstreamWUPIdentifier,
                      Identifier currentWUPIdentifier) {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.upstreamWUPIdentifier = null;
        this.upstreamWUPType = null;
        this.currentWUPIdentifier = null;
        this.currentWUPType = null;
        this.creationDate = null;
        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.currentConsumedCapabilityLock = new SerializableObject();
        this.currentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.upstreamConsumedCapabilityLock = new SerializableObject();
        this.upstreamWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        this.upstreamTaskID = upstreamTaskID;
        this.currentTaskID = currentTaskID;
        this.upstreamWUPIdentifier = upstreamWUPIdentifier;
        this.currentWUPIdentifier = currentWUPIdentifier;
        this.creationDate = Date.from(Instant.now());
        this.resilientActivity = false;
    }

    public ActivityID() {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.upstreamWUPIdentifier = null;
        this.upstreamWUPType = null;
        this.currentWUPIdentifier = null;
        this.currentWUPType = null;
        this.creationDate = Date.from(Instant.now());
        this.creationDateLock = new SerializableObject();
        this.resilientActivity = false;
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.currentConsumedCapabilityLock = new SerializableObject();
        this.currentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.upstreamConsumedCapabilityLock = new SerializableObject();
        this.upstreamWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
    }

    public ActivityID(ActivityID originalRecord) {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.upstreamWUPIdentifier = null;
        this.upstreamWUPType = null;
        this.currentWUPIdentifier = null;
        this.currentWUPType = null;
        this.creationDate = null;
        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.currentConsumedCapabilityLock = new SerializableObject();
        this.currentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.upstreamConsumedCapabilityLock = new SerializableObject();
        this.upstreamWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        if (originalRecord.hasCreationDate()) {
            this.creationDate = SerializationUtils.clone(originalRecord.getCreationDate());
        }
        if (originalRecord.hasCurrentTaskID()) {
            this.currentTaskID = SerializationUtils.clone(originalRecord.getCurrentTaskID());
        }
        if (originalRecord.hasPresentEpisodeIdentifier()) {
            this.currentEpisodeID = SerializationUtils.clone(originalRecord.getCurrentEpisodeID());
        }
        if (originalRecord.hasCurrentWUPIdentifier()) {
            this.currentWUPIdentifier = SerializationUtils.clone(originalRecord.getCurrentWUPIdentifier());
        }
        if (originalRecord.hasCurrentWUPType()) {
            this.currentWUPType = SerializationUtils.clone(originalRecord.getCurrentWUPType());
        }
        if (originalRecord.hasUpstreamTaskID()) {
            this.upstreamTaskID = SerializationUtils.clone(originalRecord.getUpstreamTaskID());
        }
        if (originalRecord.hasPreviousEpisodeIdentifier()) {
            this.upstreamEpisodeID = SerializationUtils.clone(originalRecord.getUpstreamEpisodeID());
        }
        if (originalRecord.hasUpstreamWUPIdentifier()) {
            this.upstreamWUPIdentifier = SerializationUtils.clone(originalRecord.getCurrentWUPIdentifier());
        }
        if (originalRecord.hasUpstreamWUPType()) {
            this.upstreamWUPType = SerializationUtils.clone(originalRecord.getCurrentWUPType());
        }
        this.resilientActivity = originalRecord.isResilientActivity();
    }

    public boolean hasUpstreamTaskID() {
        if (this.upstreamTaskID == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosTaskToken getUpstreamTaskID() {
        return upstreamTaskID;
    }

    public void setUpstreamTaskID(PetasosTaskToken previousParcelID) {
        synchronized (previousParcelIdentifierLock) {
            this.upstreamTaskID = previousParcelID;
        }
    }

    public boolean hasCurrentTaskID() {
        if (this.currentTaskID == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosTaskToken getCurrentTaskID() {
        return currentTaskID;
    }

    public void setCurrentTaskID(PetasosTaskToken presentParcelID) {
        synchronized (presentParcelIdenifierLock) {
            this.currentTaskID = presentParcelID;
        }
    }

    public boolean hasUpstreamWUPIdentifier() {
        if (this.upstreamWUPIdentifier == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public Identifier getUpstreamWUPIdentifier() {
        return upstreamWUPIdentifier;
    }

    public void setUpstreamWUPIdentifier(Identifier previousWUPID) {
        synchronized (upstreamWUPIdentifierLock) {
            this.upstreamWUPIdentifier = previousWUPID;
        }
    }

    public boolean hasCurrentWUPIdentifier() {
        if (this.currentWUPIdentifier == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public Identifier getCurrentWUPIdentifier() {
        return currentWUPIdentifier;
    }

    public void setCurrentWUPIdentifier(Identifier presentWUPID) {
        synchronized (currentWUPIdentifierLock) {
            this.currentWUPIdentifier = presentWUPID;
        }
    }

    public boolean hasCreationDate() {
        if (this.creationDate == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        synchronized (creationDateLock) {
            this.creationDate = creationDate;
        }
    }

    public boolean hasPreviousEpisodeIdentifier() {
        if (this.upstreamEpisodeID == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosTaskToken getUpstreamEpisodeID() {
        return upstreamEpisodeID;
    }

    public void setUpstreamEpisodeID(PetasosTaskToken previousWUAEpisodeID) {
        synchronized (previousEpisodeIdentifierLock) {
            this.upstreamEpisodeID = previousWUAEpisodeID;
        }
    }

    public boolean hasPresentEpisodeIdentifier() {
        if (this.currentEpisodeID == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosTaskToken getCurrentEpisodeID() {
        return currentEpisodeID;
    }

    public void setCurrentEpisodeID(PetasosTaskToken presentWUAEpisodeID) {
        synchronized (presentEpisodeIdentifierLock) {
            this.currentEpisodeID = presentWUAEpisodeID;
        }
    }

    public boolean hasUpstreamWUPType() {
        if (this.upstreamWUPType == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosComponentTypeToken getUpstreamWUPType() {
        return upstreamWUPType;
    }

    public void setUpstreamWUPType(PetasosComponentTypeToken upstreamWUPType) {
        synchronized (upstreamConsumedCapabilityLock) {
            this.upstreamWUPType = upstreamWUPType;
        }
    }

    public boolean hasCurrentWUPType() {
        if (this.currentWUPType == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosComponentTypeToken getCurrentWUPType() {
        return currentWUPType;
    }

    public void setCurrentWUPType(PetasosComponentTypeToken currentWUPType) {
        synchronized (currentConsumedCapabilityLock) {
            this.currentWUPType = currentWUPType;
        }
    }

    public boolean isResilientActivity() {
        return resilientActivity;
    }

    public void setResilientActivity(boolean resilientActivity) {
        this.resilientActivity = resilientActivity;
    }

    @Override
    public String toString() {
        return "ActivityID{" +
                "upstreamTaskID=" + upstreamTaskID +
                ", upstreamEpisodeID=" + upstreamEpisodeID +
                ", currentTaskID=" + currentTaskID +
                ", currentEpisodeID=" + currentEpisodeID +
                ", upstreamWUPIdentifier=" + upstreamWUPIdentifier +
                ", upstreamWUPType=" + upstreamWUPType +
                ", currentWUPIdentifier=" + currentWUPIdentifier +
                ", currentWUPType=" + currentWUPType +
                ", creationDate=" + creationDate +
                ", resilientActivity=" + resilientActivity +
                '}';
    }
}
