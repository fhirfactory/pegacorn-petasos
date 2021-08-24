package net.fhirfactory.pegacorn.petasos.core.resources.component.factories;

import net.fhirfactory.pegacorn.petasos.core.resources.capability.datatypes.PetasosCapabilityID;
import net.fhirfactory.pegacorn.petasos.core.resources.component.PetasosComponent;

import java.util.List;

public class PetasosComponentFactory {
    public static PetasosComponent newWorkUnitProcessor(String instanceID,
                                                        PetasosComponent workshop,
                                                        List<PetasosCapabilityID> capabilitySet
                                                        ){
        PetasosComponent petasosComponent = new PetasosComponent();

        return(petasosComponent);
    }
}
