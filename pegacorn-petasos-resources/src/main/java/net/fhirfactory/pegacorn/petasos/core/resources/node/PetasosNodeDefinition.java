package net.fhirfactory.pegacorn.petasos.core.resources.node;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import org.hl7.fhir.r4.model.DeviceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResourceDef(name = "PetasosNode", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosNodeDefinition extends DeviceDefinition {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PetasosNodeDefinition.class);

    @Override
    public PetasosNodeDefinition copy() {
        PetasosNodeDefinition retVal = new PetasosNodeDefinition();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosNodeDefinition() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosNode.class);
    }
}
