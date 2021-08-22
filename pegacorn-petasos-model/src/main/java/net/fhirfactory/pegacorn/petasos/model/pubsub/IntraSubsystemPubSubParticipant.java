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
package net.fhirfactory.pegacorn.petasos.model.pubsub;

import net.fhirfactory.pegacorn.petasos.core.resources.node.datatypes.PetasosNodeToken;

import java.io.Serializable;
import java.util.Objects;

public class IntraSubsystemPubSubParticipant implements Serializable {
    private IntraSubsystemPubSubParticipantIdentifier identifier;

    //
    // Constructors
    //
    public IntraSubsystemPubSubParticipant(){
        this.identifier = null;
    }

    public IntraSubsystemPubSubParticipant(IntraSubsystemPubSubParticipantIdentifier identifier){
        this.identifier = identifier;
    }

    public IntraSubsystemPubSubParticipant(PetasosNodeToken wupToken){
        this.identifier = new IntraSubsystemPubSubParticipantIdentifier(wupToken);
    }

    public IntraSubsystemPubSubParticipant(IntraSubsystemPubSubParticipant ori){
        this.identifier = ori.getIdentifier();
    }

    //
    // Getters and Setters
    //

    public IntraSubsystemPubSubParticipantIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(IntraSubsystemPubSubParticipantIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntraSubsystemPubSubParticipant)) return false;
        IntraSubsystemPubSubParticipant that = (IntraSubsystemPubSubParticipant) o;
        return Objects.equals(getIdentifier(), that.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier());
    }

    @Override
    public String toString() {
        return "LocalPubSubParticipant{" +
                "identifier=" + identifier +
                '}';
    }
}
