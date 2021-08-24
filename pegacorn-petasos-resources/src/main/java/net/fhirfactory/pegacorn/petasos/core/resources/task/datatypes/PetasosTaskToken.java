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

package net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes;

import java.time.Instant;
import java.util.Date;

import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierCodeTypeEnum;
import net.fhirfactory.pegacorn.petasos.core.attributes.identifier.valuesets.PetasosIdentifierTypeFactory;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosNodeFunctionFDN;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosComponentTypeToken;
import net.fhirfactory.pegacorn.petasos.core.resources.component.datatypes.PetasosNodeRDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDN;
import net.fhirfactory.pegacorn.common.model.generalid.FDNToken;
import net.fhirfactory.pegacorn.common.model.generalid.RDN;
import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Period;

public class PetasosTaskToken extends Identifier  {

	private static String INSTANCE_QUALIFIER="InstanceQaulifier";

	//
	// Constructors
	//

    public PetasosTaskToken(FDNToken originalToken) {
    	super();
		if(originalToken == null) {
			this.setSystem(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem());
		} else {
			CodeableConcept cc = PetasosIdentifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
			this.setType(cc);
			this.setValue(originalToken.getContent());
			this.setUse(IdentifierUse.OFFICIAL);
			Period period = new Period();
			period.setStart(Date.from(Instant.now()));
			this.setPeriod(period);
		}
    }

    public PetasosTaskToken(){
    	super();
	}

	public PetasosTaskToken(String tokenContent) {
    	super();
		if(tokenContent == null) {
			this.setSystem(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem());
		} else {
			CodeableConcept cc = PetasosIdentifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
			this.setType(cc);
			this.setValue(tokenContent);
			this.setUse(IdentifierUse.OFFICIAL);
			Period period = new Period();
			period.setStart(Date.from(Instant.now()));
			this.setPeriod(period);
		}
	}

	public PetasosTaskToken(PetasosComponentTypeToken nodeFunctionToken){
    	super();
		PetasosNodeFunctionFDN nodeFunctionFDN = new PetasosNodeFunctionFDN(nodeFunctionToken);
		FDN newFDN = new FDN();
		for(PetasosNodeRDN currentRDN: nodeFunctionFDN.getHierarchicalNameSet()) {
			newFDN.appendRDN(new RDN(currentRDN.getNodeName(), currentRDN.getNodeVersion()));
		}
		setContent(newFDN.getToken().getContent());
	}

	public PetasosTaskToken(Identifier identifier){
    	super();
    	if(identifier != null){
    		if(identifier.hasAssigner()){
    			this.setAssigner(identifier.getAssigner());
			}
    		if(identifier.hasSystem()){
    			this.setSystem(identifier.getSystem());
			}
    		if(identifier.hasPeriod()){
    			this.setPeriod(identifier.getPeriod());
			}
    		if(identifier.hasValue()){
    			this.setValue(identifier.getValue());
			}
    		if(identifier.hasType()){
    			this.setType(identifier.getType());
			}
		}
	}

	//
	// Identifier <-> FDN
	//

	private FDN toFDN(Identifier identifier){
    	if(identifier == null){
    		return(null);
		}
    	if(identifier.getSystem().contentEquals(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem())){
    		FDNToken fdnToken = new FDNToken(identifier.getValue());
    		FDN fdn = new FDN(fdnToken);
    		return(fdn);
		} else {
    		return(null);
		}
	}

	private void PetasosTaskToken(FDN fdn){
    	if(fdn == null){
    		this.setSystem(PetasosIdentifierTypeFactory.getPetasosTaskIdentifierCodeSystem());
		} else {
			FDNToken fdnToken = fdn.getToken();
			CodeableConcept cc = PetasosIdentifierTypeFactory.buildIdentifierCC(PetasosIdentifierCodeTypeEnum.IDENTIFIER_CODE_PETASOS_TASK);
			this.setType(cc);
			this.setValue(fdnToken.getContent());
			this.setUse(IdentifierUse.OFFICIAL);
			Period period = new Period();
			period.setStart(Date.from(Instant.now()));
			this.setPeriod(period);
		}
	}

	public String getContent() {
		return (this.getValue());
	}

	public void setContent(String tokenContent) {
		this.setValue(SerializationUtils.clone(tokenContent));
	}

	public String toTag(){
		String tag = new String();
		FDNToken tempToken = new FDNToken();
		tempToken.setContent(this.getContent());
		FDN tempFDN = new FDN(tempToken);
		int setSize = tempFDN.getRDNSet().size();
		int counter = 0;
		for (RDN currentRDN: tempFDN.getRDNSet()){
			String rdnValueEntry = currentRDN.getValue();
			if (rdnValueEntry.contains(".")) {
				String outputString = rdnValueEntry.replace(".", "_");
				tag = tag + outputString;
			} else {
				tag = tag + rdnValueEntry;
			}
			if (counter < (setSize - 1)) {
				tag = tag + ".";
			}
			counter++;
		}
		return(tag);
	}
}
