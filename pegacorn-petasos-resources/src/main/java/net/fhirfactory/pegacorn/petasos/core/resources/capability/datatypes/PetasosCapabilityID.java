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
package net.fhirfactory.pegacorn.petasos.core.resources.capability.datatypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.RDN;
import net.fhirfactory.pegacorn.petasos.core.resources.capability.valuesets.PetasosCapabilityDefinitionScopeEnum;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskCodeCodingSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.io.Serializable;
import java.util.Objects;

public class PetasosCapabilityID implements Serializable {

    private static String VERSION_QUALIFIER_NAME = "version";

    private String capabilityLayer;
    private String capabilitySubLayer;
    private String level1CapabilityName;
    private String level2CapabilityName;
    private String level3CapabilityName;
    private String serviceName;
    private String functionName;
    private String version;

    public PetasosCapabilityID(){
        this.capabilityLayer = null;
        this.capabilitySubLayer = null;
        this.level1CapabilityName = null;
        this.level2CapabilityName = null;
        this.level3CapabilityName = null;
        this.serviceName = null;
        this.functionName = null;
        this.version = null;
    }

    public PetasosCapabilityID(PetasosCapabilityID ori){
        this.setCapabilityLayer(ori.getCapabilityLayer());
        this.setCapabilitySubLayer(ori.getCapabilitySubLayer());
        this.setLevel1CapabilityName(ori.getLevel1CapabilityName());
        this.setLevel2CapabilityName(ori.getLevel2CapabilityName());
        this.setLevel3CapabilityName(ori.getLevel3CapabilityName());
        this.setServiceName(ori.getServiceName());
        this.setFunctionName(ori.getFunctionName());
        this.setVersion(ori.getVersion());
    }

    public PetasosCapabilityID(FDN fdn){
        if(fdn != null){
            for(RDN currentRDN: fdn.getRDNSet()){
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LAYER.getCapabilityDefinitionScope())){
                    setCapabilityLayer(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SUBLAYER.getCapabilityDefinitionScope())){
                    setCapabilitySubLayer(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL1.getCapabilityDefinitionScope())){
                    setLevel1CapabilityName(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL2.getCapabilityDefinitionScope())){
                    setLevel2CapabilityName(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL3.getCapabilityDefinitionScope())){
                    setLevel3CapabilityName(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SERVICE.getCapabilityDefinitionScope())){
                    setServiceName(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_FUNCTION.getCapabilityDefinitionScope())){
                    setFunctionName(currentRDN.getValue());
                }
                if(currentRDN.getQualifier().contentEquals(VERSION_QUALIFIER_NAME)){
                    setVersion(currentRDN.getValue());
                }
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCapabilityLayer() {
        return capabilityLayer;
    }

    public void setCapabilityLayer(String capabilityLayer) {
        this.capabilityLayer = capabilityLayer;
    }

    public String getCapabilitySubLayer() {
        return capabilitySubLayer;
    }

    public void setCapabilitySubLayer(String capabilitySubLayer) {
        this.capabilitySubLayer = capabilitySubLayer;
    }

    public String getLevel1CapabilityName() {
        return level1CapabilityName;
    }

    public void setLevel1CapabilityName(String level1CapabilityName) {
        this.level1CapabilityName = level1CapabilityName;
    }

    public String getLevel2CapabilityName() {
        return level2CapabilityName;
    }

    public void setLevel2CapabilityName(String level2CapabilityName) {
        this.level2CapabilityName = level2CapabilityName;
    }

    public String getLevel3CapabilityName() {
        return level3CapabilityName;
    }

    public void setLevel3CapabilityName(String level3CapabilityName) {
        this.level3CapabilityName = level3CapabilityName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "PetasosCapability{" +
                "capabilityLayer='" + capabilityLayer + '\'' +
                ", capabilitySubLayer='" + capabilitySubLayer + '\'' +
                ", level1CapabilityName='" + level1CapabilityName + '\'' +
                ", level2CapabilityName='" + level2CapabilityName + '\'' +
                ", level3CapabilityName='" + level3CapabilityName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", functionName='" + functionName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosCapabilityID)) return false;
        PetasosCapabilityID that = (PetasosCapabilityID) o;
        return Objects.equals(getCapabilityLayer(), that.getCapabilityLayer()) && Objects.equals(getCapabilitySubLayer(), that.getCapabilitySubLayer()) && Objects.equals(getLevel1CapabilityName(), that.getLevel1CapabilityName()) && Objects.equals(getLevel2CapabilityName(), that.getLevel2CapabilityName()) && Objects.equals(getLevel3CapabilityName(), that.getLevel3CapabilityName()) && Objects.equals(getServiceName(), that.getServiceName()) && Objects.equals(getFunctionName(), that.getFunctionName()) && Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCapabilityLayer(), getCapabilitySubLayer(), getLevel1CapabilityName(), getLevel2CapabilityName(), getLevel3CapabilityName(), getServiceName(), getFunctionName(), getVersion());
    }

    @JsonIgnore
    public FDN toFDN(){
        FDN fdn = new FDN();

        RDN layerRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LAYER.getCapabilityDefinitionScope(), getCapabilityLayer());
        fdn.appendRDN(layerRDN);

        RDN subLayerRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SUBLAYER.getCapabilityDefinitionScope(), getCapabilitySubLayer());
        fdn.appendRDN(subLayerRDN);

        RDN level1NameRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL1.getCapabilityDefinitionScope(), getLevel1CapabilityName());
        fdn.appendRDN(level1NameRDN);

        RDN level2NameRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL2.getCapabilityDefinitionScope(), getLevel2CapabilityName());
        fdn.appendRDN(level2NameRDN);

        RDN level3NameRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL3.getCapabilityDefinitionScope(), getLevel3CapabilityName());
        fdn.appendRDN(level3NameRDN);

        RDN serviceNameRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SERVICE.getCapabilityDefinitionScope(), getServiceName());
        fdn.appendRDN(serviceNameRDN);

        RDN functionNameRDN = new RDN(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_FUNCTION.getCapabilityDefinitionScope(), getFunctionName());
        fdn.appendRDN(functionNameRDN);

        RDN versionRDN = new RDN(VERSION_QUALIFIER_NAME, getVersion());
        fdn.appendRDN(versionRDN);

        return(fdn);
    }

    public PetasosCapabilityID(CodeableConcept codeableConcept){
        if(codeableConcept == null){
            this.setCapabilityLayer(null);
            this.setCapabilitySubLayer(null);;
            this.setLevel1CapabilityName(null);
            this.setLevel2CapabilityName(null);
            this.setLevel3CapabilityName(null);
            this.setServiceName(null);
            this.setFunctionName(null);
        } else {
            for(Coding currentCoding: codeableConcept.getCoding()){
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LAYER))){
                    setCapabilityLayer(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SUBLAYER))){
                    setCapabilitySubLayer(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL1))){
                    setLevel1CapabilityName(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL2))){
                    setLevel2CapabilityName(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL3))){
                    setLevel3CapabilityName(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SERVICE))){
                    setServiceName(currentCoding.getCode());
                }
                if(currentCoding.getSystem().contentEquals(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_FUNCTION))){
                    setFunctionName(currentCoding.getCode());
                }
            }
        }
    }

    public CodeableConcept toCodeableConcept(){
        CodeableConcept taskCodeableConcept = new CodeableConcept();

        Coding layerCoding = new Coding();
        layerCoding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LAYER));
        layerCoding.setCode(getCapabilityLayer());
        taskCodeableConcept.getCoding().add(layerCoding);

        Coding subLayerCoding = new Coding();
        subLayerCoding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SUBLAYER));
        subLayerCoding.setCode(getCapabilitySubLayer());
        taskCodeableConcept.getCoding().add(subLayerCoding);

        Coding level1Coding = new Coding();
        level1Coding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL1));
        level1Coding.setCode(getLevel1CapabilityName());
        taskCodeableConcept.getCoding().add(level1Coding);

        Coding level2Coding = new Coding();
        level2Coding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL2));
        level2Coding.setCode(getLevel2CapabilityName());
        taskCodeableConcept.getCoding().add(level2Coding);

        Coding level3Coding = new Coding();
        level3Coding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_LEVEL3));
        level3Coding.setCode(getLevel3CapabilityName());
        taskCodeableConcept.getCoding().add(level3Coding);

        Coding serviceCoding = new Coding();
        serviceCoding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_SERVICE));
        serviceCoding.setCode(getServiceName());
        taskCodeableConcept.getCoding().add(serviceCoding);

        Coding functionCoding = new Coding();
        functionCoding.setSystem(PetasosTaskCodeCodingSystem.getPetasosTaskCodeCodingSystem(PetasosCapabilityDefinitionScopeEnum.PETASOS_CAPABILITY_FUNCTION));
        functionCoding.setCode(getFunctionName());
        taskCodeableConcept.getCoding().add(functionCoding);

        functionCoding.setVersion(getVersion());

        StringBuilder toText = new StringBuilder();
        toText.append(this.getCapabilityLayer());
        toText.append(".");
        toText.append(this.getCapabilitySubLayer());
        toText.append(".");
        toText.append(this.getLevel1CapabilityName());
        toText.append(".");
        toText.append(this.getLevel2CapabilityName());
        toText.append(".");
        toText.append(this.getLevel3CapabilityName());
        toText.append(".");
        toText.append(this.getServiceName());
        toText.append(".");
        toText.append(this.getFunctionName());
        toText.append("(");
        toText.append(this.getVersion());
        toText.append(")");
        taskCodeableConcept.setText(toText.toString());

        return(taskCodeableConcept);
    }
}
