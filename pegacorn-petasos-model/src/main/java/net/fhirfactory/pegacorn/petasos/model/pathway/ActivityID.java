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
import net.fhirfactory.pegacorn.petasos.core.resources.capability.PetasosCapability;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPIdentifier;

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
    private WUPIdentifier previousWUPIdentifier;
    private SerializableObject previousWUPIdentifierLock;
    private PetasosCapability upstreamDeliveredCapability;
    private SerializableObject previousWUPFunctionTokenLock;
    private WUPIdentifier presentWUPIdentifier;
    private SerializableObject presentWUPIdentifierLock;
    private PetasosCapability currentDeliveredCapability;
    private SerializableObject presentWUPFunctionTokenLock;
    private Date creationDate;
    private SerializableObject creationDateLock;
    private boolean resilientActivity;
    private SerializableObject resilientActivityLock;

    public ActivityID(PetasosTaskToken previousParcelInstanceID, PetasosTaskToken presentParcelInstanceID, WUPIdentifier previousWUPInstanceID, WUPIdentifier presentWUPInstanceID, Date creationDate) {
        // Clear the deck
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.previousWUPIdentifier = null;
        this.upstreamDeliveredCapability = null;
        this.presentWUPIdentifier = null;
        this.currentDeliveredCapability = null;
        this.creationDate = null;

        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.presentWUPFunctionTokenLock = new SerializableObject();
        this.presentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.previousWUPFunctionTokenLock = new SerializableObject();
        this.previousWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        this.upstreamTaskID = previousParcelInstanceID;
        this.currentTaskID = presentParcelInstanceID;
        this.previousWUPIdentifier = previousWUPInstanceID;
        this.presentWUPIdentifier = presentWUPInstanceID;
        this.creationDate = creationDate;

    }

    public ActivityID(PetasosTaskToken previousParcelInstanceID, PetasosTaskToken presentParcelInstanceID, WUPIdentifier previousWUPInstanceID, WUPIdentifier presentWUPInstanceID) {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.previousWUPIdentifier = null;
        this.upstreamDeliveredCapability = null;
        this.presentWUPIdentifier = null;
        this.currentDeliveredCapability = null;
        this.creationDate = null;
        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.presentWUPFunctionTokenLock = new SerializableObject();
        this.presentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.previousWUPFunctionTokenLock = new SerializableObject();
        this.previousWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        this.upstreamTaskID = previousParcelInstanceID;
        this.currentTaskID = presentParcelInstanceID;
        this.previousWUPIdentifier = previousWUPInstanceID;
        this.presentWUPIdentifier = presentWUPInstanceID;
        this.creationDate = Date.from(Instant.now());
        this.resilientActivity = false;
    }

    public ActivityID() {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.previousWUPIdentifier = null;
        this.upstreamDeliveredCapability = null;
        this.presentWUPIdentifier = null;
        this.currentDeliveredCapability = null;
        this.creationDate = Date.from(Instant.now());
        this.creationDateLock = new SerializableObject();
        this.resilientActivity = false;
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.presentWUPFunctionTokenLock = new SerializableObject();
        this.presentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.previousWUPFunctionTokenLock = new SerializableObject();
        this.previousWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
    }

    public ActivityID(ActivityID originalRecord) {
        this.upstreamTaskID = null;
        this.upstreamEpisodeID = null;
        this.currentTaskID = null;
        this.currentEpisodeID = null;
        this.previousWUPIdentifier = null;
        this.upstreamDeliveredCapability = null;
        this.presentWUPIdentifier = null;
        this.currentDeliveredCapability = null;
        this.creationDate = null;
        this.creationDateLock = new SerializableObject();
        this.presentParcelIdenifierLock = new SerializableObject();
        this.presentEpisodeIdentifierLock = new SerializableObject();
        this.presentWUPFunctionTokenLock = new SerializableObject();
        this.presentWUPIdentifierLock = new SerializableObject();
        this.previousParcelIdentifierLock = new SerializableObject();
        this.previousEpisodeIdentifierLock = new SerializableObject();
        this.previousWUPFunctionTokenLock = new SerializableObject();
        this.previousWUPIdentifierLock = new SerializableObject();
        this.resilientActivityLock = new SerializableObject();
        // Set Values
        if (originalRecord.hasCreationDate()) {
            this.creationDate = originalRecord.getCreationDate();
        }
        if (originalRecord.hasPresentParcelIdentifier()) {
            this.currentTaskID = new PetasosTaskToken(originalRecord.getCurrentTaskID());
        }
        if (originalRecord.hasPresentEpisodeIdentifier()) {
            this.currentEpisodeID = new PetasosTaskToken(originalRecord.getCurrentEpisodeID());
        }
        if (originalRecord.hasPresentWUPIdentifier()) {
            this.presentWUPIdentifier = new WUPIdentifier(originalRecord.getPresentWUPIdentifier());
        }
        if (originalRecord.hasPresentWUPFunctionToken()) {
            this.currentDeliveredCapability = new PetasosCapability(originalRecord.getCurrentDeliveredCapability());
        }
        if (originalRecord.hasPreviousParcelIdentifier()) {
            this.upstreamTaskID = new PetasosTaskToken(originalRecord.getUpstreamTaskID());
        }
        if (originalRecord.hasPreviousEpisodeIdentifier()) {
            this.upstreamEpisodeID = new PetasosTaskToken(originalRecord.getUpstreamEpisodeID());
        }
        if (originalRecord.hasPreviousWUPIdentifier()) {
            this.previousWUPIdentifier = new WUPIdentifier(originalRecord.getPresentWUPIdentifier());
        }
        if (originalRecord.hasPreviousWUPFunctionToken()) {
            this.upstreamDeliveredCapability = new PetasosCapability(originalRecord.getCurrentDeliveredCapability());
        }
        this.resilientActivity = originalRecord.isResilientActivity();
    }

    public boolean hasPreviousParcelIdentifier() {
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

    public boolean hasPresentParcelIdentifier() {
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

    public boolean hasPreviousWUPIdentifier() {
        if (this.previousWUPIdentifier == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public WUPIdentifier getPreviousWUPIdentifier() {
        return previousWUPIdentifier;
    }

    public void setPreviousWUPIdentifier(WUPIdentifier previousWUPID) {
        synchronized (previousWUPIdentifierLock) {
            this.previousWUPIdentifier = previousWUPID;
        }
    }

    public boolean hasPresentWUPIdentifier() {
        if (this.presentWUPIdentifier == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public WUPIdentifier getPresentWUPIdentifier() {
        return presentWUPIdentifier;
    }

    public void setPresentWUPIdentifier(WUPIdentifier presentWUPID) {
        synchronized (presentWUPIdentifierLock) {
            this.presentWUPIdentifier = presentWUPID;
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

    public boolean hasPreviousWUPFunctionToken() {
        if (this.upstreamDeliveredCapability == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosCapability getUpstreamDeliveredCapability() {
        return upstreamDeliveredCapability;
    }

    public void setUpstreamDeliveredCapability(PetasosCapability upstreamDeliveredCapability) {
        synchronized (previousWUPFunctionTokenLock) {
            this.upstreamDeliveredCapability = upstreamDeliveredCapability;
        }
    }

    public boolean hasPresentWUPFunctionToken() {
        if (this.currentDeliveredCapability == null) {
            return (false);
        } else {
            return (true);
        }
    }

    public PetasosCapability getCurrentDeliveredCapability() {
        return currentDeliveredCapability;
    }

    public void setCurrentDeliveredCapability(PetasosCapability currentDeliveredCapability) {
        synchronized (presentWUPFunctionTokenLock) {
            this.currentDeliveredCapability = currentDeliveredCapability;
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
        String previousResilienceParcelInstanceIDString;
        if (hasPreviousParcelIdentifier()) {
            previousResilienceParcelInstanceIDString = "(previousParcelIdenifier:" + this.upstreamTaskID.toString() + ")";
        } else {
            previousResilienceParcelInstanceIDString = "(previousParcelIdenifier:null)";
        }
        String presentResilienceParcelInstanceIDString;
        if (hasPresentParcelIdentifier()) {
            presentResilienceParcelInstanceIDString = "(presentParcelIdentifier:" + this.currentTaskID.toString() + ")";
        } else {
            presentResilienceParcelInstanceIDString = "(presentParcelIdentifier:null)";
        }
        String presentWUAEpisodeString;
        if (hasPresentEpisodeIdentifier()) {
            presentWUAEpisodeString = "(presentEpisodeIdentifier:" + this.currentEpisodeID.toString() + ")";
        } else {
            presentWUAEpisodeString = "(presentEpisodeIdentifier:null)";
        }
        String previousWUAEpisodeString;
        if (hasPreviousEpisodeIdentifier()) {
            previousWUAEpisodeString = "(previousEpisodeIdentifier:" + this.upstreamEpisodeID.toString() + ")";
        } else {
            previousWUAEpisodeString = "(previousEpisodeIdentifier:null)";
        }
        String previousWUPFunctionTokenString;
        if (hasPreviousWUPFunctionToken()) {
            previousWUPFunctionTokenString = "(previousWUPFunctionToken:" + this.upstreamDeliveredCapability.toString() + ")";
        } else {
            previousWUPFunctionTokenString = "(previousWUPFunctionToken:null)";
        }
        String presentWUPFunctionTokenString;
        if (hasPresentWUPFunctionToken()) {
            presentWUPFunctionTokenString = "(presentWUPFunctionToken:" + this.currentDeliveredCapability.toString() + ")";
        } else {
            presentWUPFunctionTokenString = "(presentWUPFunctionToken:null)";
        }
        String previousWUPInstanceIDString;
        if (hasPreviousWUPIdentifier()) {
            previousWUPInstanceIDString = "(previousWUPIdentifier:" + this.previousWUPIdentifier.toString() + ")";
        } else {
            previousWUPInstanceIDString = "(previousWUPIdentifier:null)";
        }
        String presentWUPInstanceIDString;
        if (hasPresentWUPIdentifier()) {
            presentWUPInstanceIDString = "(presentWUPIdentifier:" + this.presentWUPIdentifier.toString() + ")";
        } else {
            presentWUPInstanceIDString = "(presentWUPIdentifier:null)";
        }
        String creationDateString;
        if (hasCreationDate()) {
            creationDateString = "(creationDate:" + this.creationDate.toString() + ")";
        } else {
            creationDateString = "(creationDate:null)";
        }
        String theString = "ActivityID{"
                + previousResilienceParcelInstanceIDString + ","
                + presentResilienceParcelInstanceIDString + ","
                + previousWUAEpisodeString + ","
                + presentWUAEpisodeString + ","
                + previousWUPFunctionTokenString + ","
                + presentWUPFunctionTokenString + ","
                + previousWUPInstanceIDString + ","
                + presentWUPInstanceIDString + ","
                + creationDateString + "}";
        return (theString);
    }
    
}
