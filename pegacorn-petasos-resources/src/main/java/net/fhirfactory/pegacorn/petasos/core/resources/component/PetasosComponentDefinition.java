package net.fhirfactory.pegacorn.petasos.core.resources.component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.DeviceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResourceDef(name = "PetasosNode", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosComponentDefinition extends DeviceDefinition {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PetasosComponentDefinition.class);

    @Override
    public PetasosComponentDefinition copy() {
        PetasosComponentDefinition retVal = new PetasosComponentDefinition();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosComponentDefinition() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosComponent.class);
    }
}
