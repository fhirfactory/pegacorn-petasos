package net.fhirfactory.pegacorn.petasos.core.resources.capability;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ResourceDef(name = "PetasosCapability", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosCapability extends Basic {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosCapability.class);

    private static final long serialVersionUID = 1L;

    private List<Reference> enablingCapabilities;
    private CodeableConcept capabilityType;

    private String enterpriseArchitectureLayerName;
    private String enterpriseArchitectureSubLayerName;

    private String version;

    private String description;

    @Override
    public PetasosCapability copy() {
        PetasosCapability retVal = new PetasosCapability();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosCapability() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosCapability.class);
        enablingCapabilities = new ArrayList<>();
    }

    public boolean hasEnablingCapabilities(){
        if(this.getEnablingCapabilities() != null){
            if(!this.getEnablingCapabilities().isEmpty()){
                return(true);
            }
        }
        return(false);
    }

    public List<Reference> getEnablingCapabilities() {
        return enablingCapabilities;
    }

    public void setEnablingCapabilities(List<Reference> enablingCapabilities) {
        this.enablingCapabilities = enablingCapabilities;
    }

    public boolean hasCapabilityType(){
        boolean hasValue = getCapabilityType() != null;
        return(hasValue);
    }
    public CodeableConcept getCapabilityType() {
        return capabilityType;
    }

    public void setCapabilityType(CodeableConcept capabilityType) {
        this.capabilityType = capabilityType;
    }

    public boolean hasEnterpriseArchitectureLayerName(){
        boolean hasValue = getEnterpriseArchitectureLayerName() != null;
        return(hasValue);
    }

    public String getEnterpriseArchitectureLayerName() {
        return enterpriseArchitectureLayerName;
    }

    public void setEnterpriseArchitectureLayerName(String enterpriseArchitectureLayerName) {
        this.enterpriseArchitectureLayerName = enterpriseArchitectureLayerName;
    }

    public boolean hasEnterpriseArchitectureSubLayerName(){
        boolean hasValue = getEnterpriseArchitectureSubLayerName() != null;
        return(hasValue);
    }

    public String getEnterpriseArchitectureSubLayerName() {
        return enterpriseArchitectureSubLayerName;
    }

    public void setEnterpriseArchitectureSubLayerName(String enterpriseArchitectureSubLayerName) {
        this.enterpriseArchitectureSubLayerName = enterpriseArchitectureSubLayerName;
    }

    public boolean hasVersion(){
        boolean hasValue = getVersion() != null;
        return(hasValue);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean hasDescription(){
        boolean hasValue = getDescription() != null;
        return(hasValue);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PetasosCapability{" +
                "enablingCapabilities=" + enablingCapabilities +
                ", capabilityType=" + capabilityType +
                ", identifier=" + identifier +
                ", code=" + code +
                ", subject=" + subject +
                ", subjectTarget=" + subjectTarget +
                ", created=" + created +
                ", author=" + author +
                ", authorTarget=" + authorTarget +
                '}';
    }
}
