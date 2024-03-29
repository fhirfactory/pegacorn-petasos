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
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import net.fhirfactory.pegacorn.petasos.model.resilience.activitymatrix.moa.PetasosTaskStatusElement;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import net.fhirfactory.pegacorn.petasos.model.wup.WUPJobCard;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkUnitTransportPacket implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(WorkUnitTransportPacket.class);

    private ActivityID packetID;
    private SerializableObject packetIDLock;
    private Date senderSendDate;
    private SerializableObject senderSendDateLock;
    private boolean isARetry;
    private SerializableObject isARetryLock;
    private WUPJobCard currentJobCard;
    private SerializableObject currentJobCardLock;
    private PetasosTaskStatusElement currentParcelStatus;
    private SerializableObject currentParcelStatusLock;
    private PetasosTask task;
    private SerializableObject payloadLock;
    private String generatedString;
    private SerializableObject generatedStringLock;
    
    public WorkUnitTransportPacket(ActivityID newPacketID, Date senderSendDate, PetasosTask payload) {
        this.senderSendDate = senderSendDate;
        this.task = payload;
        this.packetID = newPacketID;
        this.isARetry = false;
        this.currentJobCard = null;
        this.currentParcelStatus = null;
        this.generatedString = null;
        this.senderSendDateLock = new SerializableObject();
        this.payloadLock = new SerializableObject();
        this.isARetryLock = new SerializableObject();
        this.currentJobCardLock = new SerializableObject();
        this.currentParcelStatusLock = new SerializableObject();
        this.generatedStringLock = new SerializableObject();
        this.packetIDLock = new SerializableObject();
        generateString();
    }

    public WorkUnitTransportPacket(WorkUnitTransportPacket originalPacket) {
        this.senderSendDate = null;
        this.task = null;
        this.packetID = null;
        this.isARetry = false;
        this.currentJobCard = null;
        this.currentParcelStatus = null;
        this.generatedString = null;
        this.senderSendDateLock = new SerializableObject();
        this.payloadLock = new SerializableObject();
        this.isARetryLock = new SerializableObject();
        this.currentJobCardLock = new SerializableObject();
        this.currentParcelStatusLock = new SerializableObject();
        this.generatedStringLock = new SerializableObject();
        this.packetIDLock = new SerializableObject();
        // Assign values if available
        if(originalPacket.hasPacketID()) {
            this.packetID = originalPacket.getPacketID();
        }
        if(originalPacket.hasSenderSendDate()) {
            this.senderSendDate = originalPacket.getSenderSendDate();
        }
        this.isARetry = originalPacket.getIsARetry();
        if(originalPacket.hasCurrentJobCard()) {
            this.currentJobCard = originalPacket.getCurrentJobCard();
        }
        if(originalPacket.hasCurrentParcelStatus()) {
            this.currentParcelStatus = originalPacket.getCurrentParcelStatus();
        }
        if(originalPacket.hasPayload()) {
            this.task = originalPacket.getTask();
        }
        generateString();
    }

    public boolean hasIsARetry(){
        return(true);
    }

    public boolean getIsARetry() {
        return (this.isARetry);
    }

    public void setRetryCount(boolean retry) {
        synchronized (isARetryLock) {
            this.isARetry = retry;
            generateString();
        }
    }
    
    // CurrentJobCard helper/bean methods

    public boolean hasCurrentJobCard(){
        if(this.currentJobCard==null){
            return(false);
        } else {
            return(true);
        }
    }

    public WUPJobCard getCurrentJobCard() {
        return currentJobCard;
    }

    public void setCurrentJobCard(WUPJobCard currentJobCard) {
        synchronized (currentJobCardLock) {
            this.currentJobCard = currentJobCard;
            generateString();
        }
    }

    // CurrentParcelStatus helper/bean methods

    public boolean hasCurrentParcelStatus(){
        if(this.currentParcelStatus==null){
            return(false);
        } else {
            return(true);
        }
    }

    public PetasosTaskStatusElement getCurrentParcelStatus() {
        return currentParcelStatus;
    }

    public void setCurrentParcelStatus(PetasosTaskStatusElement currentParcelStatus) {
        synchronized (currentParcelStatusLock) {
            this.currentParcelStatus = currentParcelStatus;
            generateString();
        }
    }

    // SenderSendDate helper/bean methods

    public boolean hasSenderSendDate(){
        if(this.senderSendDate==null){
            return(false);
        } else {
            return(true);
        }
    }
    public Date getSenderSendDate() {
        return senderSendDate;
    }

    public void setSenderSendDate(Date senderSendDate) {
        synchronized (senderSendDateLock) {
            this.senderSendDate = senderSendDate;
            generateString();
        }
    }

    // PacketID helper/bean methods

    public boolean hasPacketID(){
        if(this.packetID == null){
            return(false);
        } else {
            return(true);
        }
    }

    public ActivityID getPacketID(){
        return(this.packetID);
    }

    public void setPacketID(ActivityID newPacketID){
        synchronized(packetIDLock){
            this.packetID = newPacketID;
            this.generateString();
        }
    }

    // Payload helper/bean methods

    public boolean hasPayload(){
        if(this.task ==null){
            return(false);
        } else {
            return(true);
        }
    }
    
    public PetasosTask getTask() {
        return task;
    }

    public void setTask(PetasosTask task) {
        synchronized (payloadLock) {
            this.task = task;
            generateString();
        }
    }

    // toString method(s)

    private void generateString(){
        String packetIDString;
        String currentJobCardString;
        String currentParcelStatusString;
        String senderSendDateString;
        String payloadString;
        String isARetryString;
        if(hasPayload()){
            payloadString = "(payload:" + this.task + ")";
        } else {
            payloadString = "(payload:null)";
        }
        if(hasPacketID()){
            packetIDString = "(packetID:" + this.packetID + ")";
        } else {
            packetIDString = "(packetID:null)";
        }
        if(hasCurrentParcelStatus()){
            currentParcelStatusString = "(currentParcelStatus:" + this.currentParcelStatus + ")";
        } else {
            currentParcelStatusString = "(currentParcelStatus:null)";
        }
        if(hasSenderSendDate()){
            senderSendDateString = "(senderSendDate:" + this.senderSendDate + ")";
        } else {
            senderSendDateString = "(senderSendDate:null)";
        }
        if(hasCurrentParcelStatus()){
            currentJobCardString = "(currentJobCard:" + this.currentJobCard + ")";
        } else {
            currentJobCardString = "(currentJobCard:null)";
        }
        isARetryString = "(isARetry:" + this.isARetry + ")";
        this.generatedString = "WorkUnitTransportPacket={"
                + packetIDString + ","
                + senderSendDateString + ","
                + currentJobCardString + ","
                + currentParcelStatusString + ","
                + payloadString + ","
                + isARetryString + "}";
    }

    @Override
    public String toString(){
        return(this.generatedString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkUnitTransportPacket that = (WorkUnitTransportPacket) o;
        return isARetry == that.isARetry &&
                Objects.equals(getPacketID(), that.getPacketID()) &&
                Objects.equals(getSenderSendDate(), that.getSenderSendDate()) &&
                Objects.equals(getCurrentJobCard(), that.getCurrentJobCard()) &&
                Objects.equals(getCurrentParcelStatus(), that.getCurrentParcelStatus()) &&
                Objects.equals(getTask(), that.getTask());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPacketID(), getSenderSendDate(), isARetry, getCurrentJobCard(), getCurrentParcelStatus(), getTask());
    }

    public WorkUnitTransportPacket deepClone(){
        WorkUnitTransportPacket newPacket = (WorkUnitTransportPacket) SerializationUtils.clone(this);
        newPacket.generateString();
        return(newPacket);
    }

    private WorkUnitTransportPacket(){
        this.senderSendDate = null;
        this.task = null;
        this.packetID = null;
        this.isARetry = false;
        this.currentJobCard = null;
        this.currentParcelStatus = null;
        this.generatedString = null;
        this.senderSendDateLock = new SerializableObject();
        this.payloadLock = new SerializableObject();
        this.isARetryLock = new SerializableObject();
        this.currentJobCardLock = new SerializableObject();
        this.currentParcelStatusLock = new SerializableObject();
        this.generatedStringLock = new SerializableObject();
        this.packetIDLock = new SerializableObject();
    }
}
