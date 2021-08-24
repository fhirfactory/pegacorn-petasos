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
package net.fhirfactory.pegacorn.petasos.core.resources.component.valuesets;

public enum PetasosComponentTypeEnum {
    OAM_WORKSHOP("petasos.component.workshop.operations_administration_and_maintenance"),
    OAM_WORK_UNIT_PROCESSOR("petasos.component.wup.operations_administration_and_maintenance"),
    WUP("petasos.component.wup"),
    WUP_CORE("petasos.component.wup.core"),
    WUP_INTERCHANGE_PAYLOAD_TRANSFORMER("petasos.component.wup.interchange.transformer"),
    WUP_INTERCHANGE_ROUTER("petasos.component.wup.interchange.router"),
    WUP_CONTAINER_INGRES_PROCESSOR("petasos.component.wup.pathway.ingres_processor"),
    WUP_CONTAINER_INGRES_GATEKEEPER("petasos.component.wup.pathway.ingres_gatekeeper"),
    WUP_CONTAINER_INGRES_CONDUIT("petasos.component.wup.pathway.ingres_conduit"),
    WUP_CONTAINER_EGRESS_CONDUIT("petasos.component.wup.pathway.egress_conduit"),
    WUP_CONTAINER_EGRESS_PROCESSOR("petasos.component.wup.pathway.egress_processor"),
    WUP_CONTAINER_EGRESS_GATEKEEPER("petasos.component.wup.pathway.egress_gatekeeper"),
    WORKSHOP("petasos.component.workshop"),
    PROCESSING_PLANT("petasos.component.processing_plant"),
    PLATFORM("petasos.component.platform_service"),
    CLUSTER_SERVICE("petasos.component.cluster_service"),
    SITE("petasos.component.site"),
    EXTERNALISED_SERVICE("petasos.component.externalised_service"),
    SUBSYSTEM("petasos.component.subsystem"),
    SOLUTION("petasos.component.solution"),
    ENDPOINT("petasos.component.endpoint");
    
    private String nodeElementType;

    private PetasosComponentTypeEnum(String mapElementType){
        this.nodeElementType = mapElementType;
    }

    public String getNodeElementType(){
        return(this.nodeElementType);
    }
}
