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
package net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.pegacorn.petasos.core.payloads.manifest.DataParcelManifest;
import net.fhirfactory.pegacorn.petasos.core.payloads.manifest.DataParcelQualityStatement;
import net.fhirfactory.pegacorn.petasos.core.payloads.uow.UoWPayload;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskPayloadCodeSystem;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetasosTaskPayload extends Task.ParameterComponent {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosTaskPayload.class);

    public PetasosTaskPayload() {
        super();
    }

    public PetasosTaskPayload(PetasosTaskPayload originalUoWPayload) {
        this.setValue(SerializationUtils.clone(originalUoWPayload.getValue()));
        this.setType(SerializationUtils.clone(originalUoWPayload.getType()));
    }

    public PetasosTaskPayload(DataParcelManifest payloadType, String payloadContent){
        this.setPayload(SerializationUtils.clone(payloadContent));
        this.setPayloadManifest(SerializationUtils.clone(payloadType));
    }

    public PetasosTaskPayload(UoWPayload ori){
        this.setPayloadManifest(SerializationUtils.clone(ori.getPayloadManifest()));
        this.setPayload(SerializationUtils.clone(ori.getPayload()));
    }

    public PetasosTaskPayload(Task.ParameterComponent ori){
        this.setType(SerializationUtils.clone(ori.getType()));
        this.setValue(SerializationUtils.clone(ori.getValue()));
    }

    public PetasosTaskPayload(Task.TaskOutputComponent ori){
        this.setType(SerializationUtils.clone(ori.getType()));
        this.setValue(SerializationUtils.clone(ori.getValue()));
    }

    //
    // Content Mapping Between Simple (UoW Style) and Canonical (FHIR::Task.ParameterComponent)
    //

    private CodeableConcept convertToCodeableConcept(DataParcelManifest parcelManifest){
        if (parcelManifest == null) {
            return(null);
        }
        CodeableConcept codeableConcept = new CodeableConcept();
        Coding coding = new Coding();
        coding.setCode(parcelManifest.toJSON());
        coding.setSystem(PetasosTaskPayloadCodeSystem.getPetasosTaskPayloadCodeSystem());
        coding.setDisplay(parcelManifest.toString());
        coding.setVersion(parcelManifest.getContentDescriptor().getVersion());
        codeableConcept.getCoding().add(coding);
        codeableConcept.setText("DataParcelManifest as JSON");
        return(codeableConcept);
    }

    private DataParcelManifest convertToDataParcelManifest(CodeableConcept valueType){
        String codingCode = valueType.getCodingFirstRep().getCode();
        DataParcelManifest dataParcelManifest = new DataParcelManifest(codingCode);
        return(dataParcelManifest);
    }

    public boolean hasPayload(){
        boolean hasPayload = this.hasValue();
        return(hasPayload);
    }

    public String getPayload() {
        LOG.debug(".getPayload(): Entry");
        StringType valueAsStringType = (StringType)this.getValue();
        String valueAsString = valueAsStringType.getValue();
        LOG.debug(".getPayload(): Exit, returning Payload (String) --> {}", valueAsString);
        return (valueAsString);
    }

    public void setPayload(String payload) {
        LOG.debug(".setPayload(): Entry, payload (String) --> {}", payload);
        StringType valueAsStringType = new StringType(SerializationUtils.clone(payload));
        this.setValue(valueAsStringType);
    }

    public boolean hasPayloadManifest(){
        boolean hasManifiest = this.hasType();
        return(hasManifiest);
    }

    public DataParcelManifest getPayloadManifest() {
        LOG.debug(".getPayloadTopicID(): Entry");
        DataParcelManifest manifest = convertToDataParcelManifest(this.getType());
        LOG.debug(".getPayloadTopicID(): Exit, returning Payload (String) --> {}", manifest);
        return manifest;
    }

    public void setPayloadManifest(DataParcelManifest payloadManifest) {
        LOG.debug(".setPayloadTopicID(): Entry, payloadTopicID (TopicToken) --> {}", payloadManifest);
        CodeableConcept codeableConcept = convertToCodeableConcept(SerializationUtils.clone(payloadManifest));
        this.setType(codeableConcept);
    }

    public boolean hasDataParcelQualityStatement(){
        if(hasPayloadManifest()){
            boolean hasQualityStatement = getPayloadManifest().hasDataParcelQualityStatement();
            return(hasQualityStatement);
        } else {
            return (false);
        }
    }

    @JsonIgnore
    public DataParcelQualityStatement getPayloadQuality() {
        LOG.debug(".getPayloadQuality(): Entry");
        if(hasDataParcelQualityStatement()){
            LOG.debug(".getPayloadQuality(): Exit, returning payloadQuality->{}", this.getPayloadManifest().getPayloadQuality());
            return(this.getPayloadManifest().getPayloadQuality());
        } else {
            LOG.debug(".getPayloadQuality(): Exit, no payloadQuality statement available");
            return (null);
        }
    }

}
