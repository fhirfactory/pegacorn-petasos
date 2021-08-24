package net.fhirfactory.pegacorn.petasos.wup.sta;

import net.fhirfactory.pegacorn.petasos.wup.sta.common.GenericSTAWUPTemplate;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPArchetypeEnum;

public abstract class GenericSTAServerWUPTemplate extends GenericSTAWUPTemplate {

    public GenericSTAServerWUPTemplate() {
        super();
    }

    @Override
    protected WUPArchetypeEnum specifyWUPArchetype(){
        return(WUPArchetypeEnum.WUP_NATURE_API_ANSWER);
    }
}
