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
package net.fhirfactory.pegacorn.petasos.core.attributes.identifier.factories;

import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDNToken;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierCodeTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierTypeFactory;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;

import java.sql.Date;
import java.time.Instant;

public class PetasosIdentifierFactory {

    public static Identifier newPetasosIdentifier(String identifierValue, PetasosIdentifierCodeTypeEnum identifierCodeType){
        CodeableConcept codeableConcept = PetasosIdentifierTypeFactory.buildIdentifierCC(identifierCodeType);
        Period period = new Period();
        period.setStart(Date.from(Instant.now()));
        Identifier identifier = new Identifier();
        identifier.setPeriod(period);
        identifier.setType(codeableConcept);
        identifier.setValue(identifierValue);
        identifier.setSystem(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem());
        return(identifier);
    }

    public static Identifier newPetasosTaskIdentifier(String identifierValue){
        Identifier identifier = newPetasosIdentifier(identifierValue, PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
        return(identifier);
    }

    public static Identifier newPetasosTaskEpisodeIdentifier(String identifierValue){
        Identifier identifier = newPetasosIdentifier(identifierValue, PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_EPISODE);
        return(identifier);
    }

    public static FDN extractFDNFromPetasosIdentifier(Identifier identifier){
        if(identifier.getSystem().contentEquals(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem())){
            if(identifier.hasValue()){
                FDNToken fdnToken = new FDNToken(identifier.getValue());
                FDN fdn = new FDN(fdnToken);
                return(fdn);
            }
        }
        return(null);
    }

    public static Identifier newPetasosIdentifier(FDN identifierFDN, PetasosIdentifierCodeTypeEnum identifierCodeType){
        if(identifierFDN != null){
            FDNToken fdnToken = identifierFDN.getToken();
            Identifier identifier = newPetasosIdentifier(fdnToken.getContent(), identifierCodeType);
            return(identifier);
        }
        return(null);
    }

    public static Identifier newPetasosTaskIdentifier(FDN identifierFDN){
        Identifier identifier = newPetasosIdentifier(identifierFDN, PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
        return(identifier);
    }

    public static Identifier newPetasosTaskEpisodeIdentifier(FDN identifierFDN){
        Identifier identifier = newPetasosIdentifier(identifierFDN, PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK_EPISODE);
        return(identifier);
    }
}
