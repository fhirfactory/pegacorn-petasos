/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.petasos.core.payloads.uow;

import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDNToken;
import net.fhirfactory.pegacorn.common.model.generalid.RDN;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierCodeTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierTypeFactory;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class UoWIdentifier extends Identifier implements Serializable {
	
    public UoWIdentifier(FDNToken originalToken) {
        super();
        FDN identifierFDN = new FDN(originalToken);
        RDN identifierRDN = identifierFDN.getUnqualifiedRDN();
        String identifierValue = identifierRDN.getValue();
        this.setValue(identifierValue);
        CodeableConcept identifierType = PetasosIdentifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
        this.setType(identifierType);
        this.setUse(IdentifierUse.OFFICIAL);
        Period identifierPeriod = new Period();
        identifierPeriod.setStart(Date.from(Instant.now()));
    }

    public UoWIdentifier(Identifier ori){
        super();
        if(ori.hasType()) {
            this.setType(ori.getType());
        }
        if(ori.hasValue()) {
            this.setValue(ori.getValue());
        }
        if(ori.hasUse()) {
            this.setUse(ori.getUse());
        }
        if(ori.hasPeriod()) {
            this.setPeriod(ori.getPeriod());
        }
        if(ori.hasSystem()){
            this.setSystem(ori.getSystem());
        }
        if(ori.hasAssigner()) {
            this.setAssigner(ori.getAssigner());
        }
    }
    
    public UoWIdentifier(){
        super();
    }

}
