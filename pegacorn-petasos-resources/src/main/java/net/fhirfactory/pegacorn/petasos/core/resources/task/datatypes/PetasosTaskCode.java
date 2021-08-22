package net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes;

import net.fhirfactory.pegacorn.petasos.core.resources.capability.PetasosCapability;
import net.fhirfactory.pegacorn.petasos.core.resources.capability.valuesets.PetasosCapabilityDefinitionScopeEnum;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskCodeCodingSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

public class PetasosTaskCode extends PetasosCapability {

    public PetasosTaskCode(CodeableConcept taskCode){
        if(taskCode == null){
            this.setCapabilityLayer(null);
            this.setCapabilitySubLayer(null);;
            this.setLevel1CapabilityName(null);
            this.setLevel2CapabilityName(null);
            this.setLevel3CapabilityName(null);
            this.setServiceName(null);
            this.setFunctionName(null);
        } else {
            for(Coding currentCoding: taskCode.getCoding()){
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
        taskCodeableConcept.setText(toText.toString());

        return(taskCodeableConcept);
    }

}
