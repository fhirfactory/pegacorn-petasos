/*
 * The MIT License
 *
 * Copyright 2021 Mark A. Hunter (ACT Health).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.fhirfactory.pegacorn.petasos.core.resources.task;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierCodeTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierTypeFactory;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoW;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoWIdentifier;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoWPayload;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskCode;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskReference;
import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskExtensionMeanings;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskExtensionTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskFinalisationStatusEnum;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskReferenceExtensionTypeEnum;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ResourceDef(name = "PetasosTask", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosTask extends Task {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PetasosTask.class);

    private PetasosTaskExtensionMeanings extensionMeanings;

    @Override
    public PetasosTask copy() {
        PetasosTask retVal = new PetasosTask();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosTask() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosTask.class);
        extensionMeanings = new PetasosTaskExtensionMeanings();
    }

    //
    // Getters (and Setters)
    //

    // Helper methods for the this.cancellationDate attribute
    public boolean hasCancellationDate() {
        if(this.getStatus().equals(TaskStatus.CANCELLED)) {
            if (this.hasLastModified()) {
                return (true);
            }
        }
        return(false);
    }

    @JsonIgnore
    public Date getCancellationDate() {
        if(this.getStatus().equals(TaskStatus.CANCELLED)) {
            if (this.hasLastModified()) {
                return (this.getLastModified());
            }
        }
        return (null);
    }

    @JsonIgnore
    public void setCancellationDate(Date newCancellationDate) {
        this.setStatus(TaskStatus.CANCELLED);
        this.setLastModified(newCancellationDate);
    }


    // Helper methods for the this.actualUoW attribute
    public boolean hasDownstreamEpisodeIdentifierList() {
        if(hasPartOf()){
            List<Reference> partOfSet = this.getPartOf();
            for(Reference currentReference: partOfSet){
                if(currentReference instanceof PetasosTaskReference){
                    PetasosTaskReference petasosTaskReference = (PetasosTaskReference) currentReference;
                    if(petasosTaskReference.isDownstreamPetasosEpisode()){
                        return(true);
                    }
                }
            }
        }
        return(false);
    }

    @JsonIgnore
    public List<Identifier> getDownstreamEpisodeIdentifierList() {
        List<Identifier> downstreamList = new ArrayList<>();
        if(hasPartOf()){
            List<Reference> partOfSet = this.getPartOf();
            for(Reference currentReference: partOfSet){
                if(currentReference instanceof PetasosTaskReference){
                    PetasosTaskReference petasosTaskReference = (PetasosTaskReference) currentReference;
                    if(petasosTaskReference.isDownstreamPetasosEpisode()){
                        downstreamList.add(petasosTaskReference.getIdentifier());
                    }
                }
            }
        }
        return(downstreamList);
    }

    @JsonIgnore
    public void setDownstreamEpisodeIdentifierList(List<Identifier> downstreamEpisodeList) {
        if(downstreamEpisodeList == null) {
            return;
        }
        if(downstreamEpisodeList.isEmpty()) {
            return;
        }
        for(Identifier currentIdentifier: downstreamEpisodeList){
            PetasosTaskReference currentReference = new PetasosTaskReference();
            currentReference.setPetasosTaskReferenceExtensionType(PetasosTaskReferenceExtensionTypeEnum.DOWNSTREAM_EPISODE_PETASOS_TASK_REFERENCE);
            currentReference.setIdentifier(currentIdentifier);
            currentReference.setType("PetasosTaskReference");
            this.getPartOf().add(currentReference);
        }
    }

    public boolean hasUpstreamEpisodeIdentifierList() {
        if(hasPartOf()){
            List<Reference> partOfSet = this.getPartOf();
            for(Reference currentReference: partOfSet){
                if(currentReference instanceof PetasosTaskReference){
                    PetasosTaskReference petasosTaskReference = (PetasosTaskReference) currentReference;
                    if(petasosTaskReference.isUpstreamPetasosEpisode()){
                        return(true);
                    }
                }
            }
        }
        return(false);
    }

    @JsonIgnore
    public Identifier getUpstreamEpisodeIdentifier() {
        if(hasPartOf()){
            List<Reference> partOfSet = this.getPartOf();
            for(Reference currentReference: partOfSet){
                if(currentReference instanceof PetasosTaskReference){
                    PetasosTaskReference petasosTaskReference = (PetasosTaskReference) currentReference;
                    if(petasosTaskReference.isUpstreamPetasosEpisode()){
                        return(petasosTaskReference.getIdentifier());
                    }
                }
            }
        }
        return(null);
    }

    @JsonIgnore
    public void setUpstreamEpisodeIdentifier(Identifier upstreamEpisodeIdentifier) {
        if(hasPartOf()){
            List<Reference> partOfSet = this.getPartOf();
            for(Reference currentReference: partOfSet){
                if(currentReference instanceof PetasosTaskReference){
                    PetasosTaskReference petasosTaskReference = (PetasosTaskReference) currentReference;
                    if(petasosTaskReference.isUpstreamPetasosEpisode()){
                        this.getPartOf().remove(petasosTaskReference);
                        break;
                    }
                }
            }
        }
        PetasosTaskReference currentReference = new PetasosTaskReference();
        currentReference.setPetasosTaskReferenceExtensionType(PetasosTaskReferenceExtensionTypeEnum.UPSTREAM_EPISODE_PETASOS_TASK_REFERENCE);
        currentReference.setIdentifier(upstreamEpisodeIdentifier);
        currentReference.setType("PetasosTaskReference");
        this.getPartOf().add(currentReference);
    }

    public boolean hasFinalisationStatus(){
        LOG.debug(".hasFinalisationStatus(): Entry (checking to see if the PetasosTask has a FinalisationStatus)");
        if(this.hasExtension(extensionMeanings.getPetasosTaskExtension())){
            Extension episodeExtension = getExtensionByUrl(extensionMeanings.getPetasosTaskExtension());
            Coding episodeCoding = (Coding)episodeExtension.getValue();
            if(episodeCoding.getCode().contentEquals(PetasosTaskExtensionTypeEnum.PETASOS_TASK_FINALISATION_STATUS_EXTENSION.getPetasosExtensionType())){
                LOG.debug(".hasFinalisationStatus(): Exit, returning -true-");
                return(true);
            }
        }
        LOG.debug(".hasFinalisationStatus(): Exit, returning -false-");
        return(false);
    }

    @JsonIgnore
    public PetasosTaskFinalisationStatusEnum getFinalisationStatus(){
        LOG.debug(".getFinalisationStatus(): Entry ");
        if(this.hasExtension(extensionMeanings.getPetasosTaskExtension())){
            Extension taskExtension = getExtensionByUrl(extensionMeanings.getPetasosTaskExtension());
            Coding episodeCoding = (Coding)taskExtension.getValue();
            if(episodeCoding.getCode().contentEquals(PetasosTaskExtensionTypeEnum.PETASOS_TASK_FINALISATION_STATUS_EXTENSION.getPetasosExtensionType())){
                StringType statusStringType = (StringType)taskExtension.getValue();
                PetasosTaskFinalisationStatusEnum status = PetasosTaskFinalisationStatusEnum.fromPetasosTaskFinalisationStatusString(statusStringType.getValue());
                LOG.debug(".getFinalisationStatus(): Exit, returning ->{}", status);
                return(status);
            }
        }
        LOG.debug(".getFinalisationStatus(): Exit, returning -null-");
        return(null);
    }

    @JsonIgnore
    public void setFinalisationStatus(PetasosTaskFinalisationStatusEnum finalisationStatus){
        LOG.debug(".setFinalisationStatus(): Entry, finalisationStatus->{}", finalisationStatus);
        if(this.hasExtension(extensionMeanings.getPetasosTaskExtension())) {
            Extension taskExtension = getExtensionByUrl(extensionMeanings.getPetasosTaskExtension());
            this.getExtension().remove(taskExtension);
        }
        Coding episodeCoding = new Coding();
        episodeCoding.setCode(PetasosTaskExtensionTypeEnum.PETASOS_TASK_FINALISATION_STATUS_EXTENSION.getPetasosExtensionType());
        Extension taskExtension = new Extension();
        taskExtension.setUrl(extensionMeanings.getPetasosTaskExtension());
        StringType statusStringType = new StringType(finalisationStatus.getPetasosTaskFinalisationStatus());
        taskExtension.setValue(statusStringType);
        this.getExtension().add(taskExtension);
        LOG.debug(".setFinalisationStatus(): Exit");
    }

    public boolean hasPetasosTaskToken(){
        List<Identifier> identifierList = getIdentifier();
        if(identifierList.isEmpty()){
            return(false);
        }
        for(Identifier currentIdentifier: identifierList){
            CodeableConcept currentIdentifierType = currentIdentifier.getType();
            List<Coding> codingList = currentIdentifierType.getCoding();
            for(Coding currentCoding: codingList){
                if(currentCoding.getCode().contentEquals(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_TOKEN.getPetasosIdentifierCode())){
                    return(true);
                }
            }
        }
        return(false);
    }

    @JsonIgnore
    public void setPetasosTaskToken(PetasosTaskToken tokenValue){
        List<Identifier> identifierList = getIdentifier();
        if(!identifierList.isEmpty()){
            for(Identifier currentIdentifier: identifierList){
                CodeableConcept currentIdentifierType = currentIdentifier.getType();
                List<Coding> codingList = currentIdentifierType.getCoding();
                for(Coding currentCoding: codingList){
                    if(currentCoding.getCode().contentEquals(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_TOKEN.getPetasosIdentifierCode())){
                        currentIdentifier.setValue(tokenValue.getContent());
                        return;
                    }
                }
            }
        }
        PetasosIdentifierTypeFactory identifierTypeFactory = new PetasosIdentifierTypeFactory();
        CodeableConcept codeableConcept = identifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_TOKEN);
        Identifier identifier = new Identifier();
        identifier.setType(codeableConcept);
        Period identifierPeriod = new Period();
        identifierPeriod.setStart(Date.from(Instant.now()));
        identifier.setPeriod(identifierPeriod);
        identifier.setValue(tokenValue.getContent());
        getIdentifier().add(identifier);
    }

    @JsonIgnore
    public PetasosTaskToken getPetasosTaskToken(){
        List<Identifier> identifierList = getIdentifier();
        if(!identifierList.isEmpty()){
            for(Identifier currentIdentifier: identifierList){
                CodeableConcept currentIdentifierType = currentIdentifier.getType();
                List<Coding> codingList = currentIdentifierType.getCoding();
                for(Coding currentCoding: codingList){
                    if(currentCoding.getCode().contentEquals(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_TOKEN.getPetasosIdentifierCode())){
                        PetasosTaskToken token = new PetasosTaskToken();
                        token.setContent(currentIdentifier.getValue());
                        return(token);
                    }
                }
            }
        }
        return(null);
    }

    @JsonIgnore
    public boolean hasEpisodeToken(){
        if(hasGroupIdentifier()){
            return(true);
        } else {
            return(false);
        }
    }

    @JsonIgnore
    public PetasosTaskToken getEpisodeToken(){
        if(hasPetasosTaskToken()){
            PetasosTaskToken episodeToken = new PetasosTaskToken();
            episodeToken.setContent(getGroupIdentifier().getValue());
            return(episodeToken);
        } else {
            return(null);
        }
    }

    @JsonIgnore
    public void setEpisodeToken(PetasosTaskToken token){
        PetasosIdentifierTypeFactory identifierTypeFactory = new PetasosIdentifierTypeFactory();
        CodeableConcept codeableConcept = identifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_EPISODE);
        Identifier identifier = new Identifier();
        identifier.setType(codeableConcept);
        Period identifierPeriod = new Period();
        identifierPeriod.setStart(Date.from(Instant.now()));
        identifier.setPeriod(identifierPeriod);
        identifier.setValue(token.getContent());
        this.setGroupIdentifier(identifier);
    }

    //
    // UoW Inter-Play
    //

    @JsonIgnore
    public UoW toUoW(){
        LOG.debug(".getUoW(): Entry");
        UoW uow = new UoW();

        LOG.trace(".getUoW(): [start] Build the Ingres Payload");
        UoWPayload uowPayload = new UoWPayload(this.getInputFirstRep());
        uow.setIngresContent(uowPayload);
        LOG.trace(".getUoW(): [Finish] Build the Ingres Payload");
        LOG.trace(".getUoW(): [Start] Build the Egress Payload(s)");
        for(TaskOutputComponent currentEgressElement: getOutput()){
            UoWPayload currentPayload = new UoWPayload(currentEgressElement);
            uow.getEgressContent().addPayloadElement(currentPayload);
        }
        LOG.trace(".getUoW(): [Finish] Build the Egress Payload(s)");
        LOG.trace(".getUoW(): [Start] Assemble the Instance ID");
        UoWIdentifier uoWIdentifier = new UoWIdentifier(this.getIdentifierFirstRep());
        uow.setInstanceID(uoWIdentifier.getValue());
        LOG.trace(".getUoW(): [Finish] Assemble the Instance ID");
        LOG.trace(".getUoW(): [Start] Assign the Processing Status");
        UoWProcessingOutcomeEnum outcomeEnum = UoWProcessingOutcomeEnum.fromFHIRTaskStatus(getStatus());
        uow.setProcessingOutcome(outcomeEnum);
        LOG.trace(".getUoW(): [Finish] Assign the Processing Status");
        LOG.trace(".getUoW(): [Start] Build the UoWTypeID");
        PetasosTaskCode taskCode = new PetasosTaskCode(getCode());
        uow.setUoWTypeID(taskCode);
        LOG.trace(".getUoW(): [Finish] Build the UoWTypeID");
        LOG.trace(".getUoW(): [Start] Populate the FailureDescription field");
        if(hasStatusReason()) {
            uow.setFailureDescription(getStatusReason().getText());
        }
        LOG.trace(".getUoW(): [Finish] Populate the FailureDescription field");

        LOG.debug(".getUoW(): Exit, uow->{}", uow);
        return(uow);
    }

    @JsonIgnore
    public void setUoW(UoW uow){

    }
}
