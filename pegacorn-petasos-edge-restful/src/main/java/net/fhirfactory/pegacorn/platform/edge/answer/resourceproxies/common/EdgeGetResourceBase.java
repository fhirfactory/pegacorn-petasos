/*
 * Copyright (c) 2020 Mark A. Hunter
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
package net.fhirfactory.pegacorn.platform.edge.answer.resourceproxies.common;

import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import net.fhirfactory.pegacorn.components.transaction.model.TransactionStatusEnum;
import net.fhirfactory.pegacorn.components.transaction.model.TransactionMethodOutcome;
import org.hl7.fhir.r4.model.*;

public abstract class EdgeGetResourceBase extends EdgeProxyBase {
    private static String parameterSepeator = "|";


    /**
     * This is the default findByIdentifier() search function. It is called by the subclass (@Search annotated) public
     * Resource specific findByIdentifier() classes after those classes have resolved the Identifier.
     *
     * @param identifier the (partially populated) Identifier to search for
     * @return A FHIR::Bundle containing one or more resources matching the Identifier or an empty FHIR::Bundle.
     */
    protected Resource findResourceViaIdentifier(Identifier identifier) {
        getLogger().debug(".findByIdentifier(): Entry, identifier --> {}", identifier);

        TransactionMethodOutcome outcome = getActualResourceAccessor().searchUsingIdentifier(identifier);

        if (outcome.getStatusEnum().equals(TransactionStatusEnum.REVIEW_FINISH) || outcome.getStatusEnum().equals(TransactionStatusEnum.SEARCH_FINISHED)) {
            getLogger().trace("findByIdentifier(): search is finished, extracting result Resource");
            Resource searchOutcome = (Resource) outcome.getResource();
            getLogger().debug("findByIdentifier(): Exit, resulting Resource is {}", searchOutcome);
            return (searchOutcome);
        } else {
            Resource emptyOutcomeBundle = searchProcessHasFailed(".findByIdentifier()");
            getLogger().debug("findByIdentifier(): Exit, search failed (not just not finding any matching resources, but the search process itself faield)!");
            return (emptyOutcomeBundle);
        }
    }


    @Override
    protected TransactionMethodOutcome getResource(IdType id) {
        return null;
    }

    /**
     * This method converts a received TokenParam (representing a FHIR::Identifier) and converts it to
     * an actual FHIR::Identifier object.
     *
     * Contrary to the understood structure of the TokenParam attribute within the HAPI FHIR documentation,
     * Pegacorn maps the System & Code to be associated to the FHIR::Identifier.type.system &  FHIR::Identifier.type.code
     * rather than the System attribute of the TokenParam mapping to the FHIR::Identifier.system.
     *
     * @param identifierParam The TokenParam attribute representing an FHIR::Identifier
     * @return A (minimally populated) FHIR::Identifier element.
     */
    protected Identifier tokenParam2Identifier(TokenParam identifierParam){
        getLogger().debug(".tokenParam2Identifier(): Entry, identifierParam (TokenParam) --> {}", identifierParam);
        if(identifierParam == null){
            getLogger().info(".tokenParam2Identifier(): Parameter identifierParam (TokenParam) is null");
            return(null);
        }
        if(getLogger().isDebugEnabled()){
            getLogger().debug(".tokenParam2Identifier(): identifierParam.system --> {}", identifierParam.getSystem());
            getLogger().debug(".tokenParam2Identifier(): identifierParam.value --> {}", identifierParam.getValue());
            if(identifierParam.getModifier() != null){
                getLogger().debug(".tokenParam2Identifier(): identifierParam.modifier --> {}", identifierParam.getModifier().getValue());
            }
        }
        boolean hasAppropriateModifier = false;
        if(identifierParam.getModifier() == null) {
            getLogger().trace(".tokenParam2Identifier(): There is no modifier present");
            hasAppropriateModifier = false;
        } else {
            if(identifierParam.getModifier().getValue().equals(TokenParamModifier.OF_TYPE.getValue())){
                hasAppropriateModifier = true;
            }
        }
        // HAPI FHIR Server is not passing Modifiers
        // This code manually checks to see if a modifier 
        // has been used. 
        if(!hasAppropriateModifier) {
            if(identifierParam.getValue().contains("|")) {
                getLogger().trace(".tokenParam2Identifier(): identifierParam.value contains pipe separator, treating as \"of_type\"");
                hasAppropriateModifier = true;
            }
        }
        String identifierValue = null;
        Identifier generatedIdentifier = new Identifier();
        generatedIdentifier.setSystem(identifierParam.getSystem());
        if(hasAppropriateModifier){
            String identifierTypeCodeValue = null;
            String paramValue = identifierParam.getValue();
            String[] values = paramValue.split("\\|");
            identifierTypeCodeValue = values[0];
            identifierValue = values[1];
            CodeableConcept identifierType = new CodeableConcept();
            Coding identifierTypeCode = new Coding();
            identifierTypeCode.setCode(identifierTypeCodeValue);
            identifierTypeCode.setSystem(identifierParam.getSystem());
            identifierType.addCoding(identifierTypeCode);
            identifierType.setText(identifierParam.getSystem() + ":" + identifierTypeCodeValue);
            generatedIdentifier.setType(identifierType);
        } 
        else {
            identifierValue = identifierParam.getValue();
        }
        generatedIdentifier.setValue(identifierValue);
        getLogger().debug(".tokenParam2Identifier(): Exit, created Identifier --> {}", generatedIdentifier);
        return(generatedIdentifier);
    }


}
