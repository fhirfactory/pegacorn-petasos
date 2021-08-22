/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.pegacorn.petasos.audit.forwarder.asynchronous.queue;

import org.hl7.fhir.r4.model.AuditEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.Stack;

@ApplicationScoped
public class InMemoryAuditEntryQueue {
    protected Stack<AuditEvent> auditQueue;
    private Object auditQueueLock;

    public InMemoryAuditEntryQueue(){
        this.auditQueue = new Stack<>();
        this.auditQueueLock = new Object();
    }

    public Stack<AuditEvent> getAuditQueue() {
        return auditQueue;
    }

    public void setAuditQueue(Stack<AuditEvent> auditQueue) {
        this.auditQueue = auditQueue;
    }

    public void addAuditEntry(AuditEvent newEntry){
        if( newEntry != null) {
            synchronized (auditQueueLock) {
                getAuditQueue().add(newEntry);
            }
        }
    }

    public AuditEvent nextAuditEntry(){
        if(getAuditQueue().isEmpty()){
            return(null);
        } else {
            AuditEvent nextEntry = null;
            synchronized (auditQueueLock) {
                nextEntry = (AuditEvent) getAuditQueue().pop();
            }
            return(nextEntry);
        }
    }

    public int getSize(){
        int queueSize = 0;
        synchronized (auditQueueLock) {
            queueSize = getAuditQueue().size();
        }
        return(queueSize);
    }
}
