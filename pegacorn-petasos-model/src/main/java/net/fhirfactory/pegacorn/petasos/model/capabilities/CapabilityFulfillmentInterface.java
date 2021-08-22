package net.fhirfactory.pegacorn.petasos.model.capabilities;

import net.fhirfactory.pegacorn.petasos.model.capabilities.base.CapabilityUtilisationRequest;
import net.fhirfactory.pegacorn.petasos.model.capabilities.base.CapabilityUtilisationResponse;

public interface CapabilityFulfillmentInterface {
    public CapabilityUtilisationResponse executeTask(CapabilityUtilisationRequest request);
}
