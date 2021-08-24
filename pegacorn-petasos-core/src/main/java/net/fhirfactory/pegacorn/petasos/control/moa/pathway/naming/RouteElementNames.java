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

package net.fhirfactory.pegacorn.petasos.control.moa.pathway.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Mark A. Hunter
 * @since 2020-06-01
 */
public class RouteElementNames {
    private static final Logger LOG = LoggerFactory.getLogger(RouteElementNames.class);

    private boolean mustBeDirect;
    private String wupFullName;
    private String wupVersion;
    private static final String INTRA_FUNCTION_DIRECT_TYPE = "direct:";
    private static final String DIRECT_INTER_FUNCTION_DIRECT_TYPE = "direct:";
    private static final String SEDA_INTER_FUNCTION_DIRECT_TYPE = "seda:";

    public RouteElementNames(String wupFullName, boolean mustBeDirect){
        LOG.debug(".RouteElementNames(): Entry, functionToken->{}, mustBeDirect->{}", wupFullName, mustBeDirect);
        this.wupFullName = wupFullName;
        this.mustBeDirect = mustBeDirect;
    }

    public RouteElementNames(String wupFullName){
        LOG.debug(".RouteElementNames(): Entry, wupFullName->{}", wupFullName);
        this.wupFullName = wupFullName;
        this.mustBeDirect = false;
    }

    public String getWupFullName(){
        return(this.wupFullName);
    }
    
    public String getRouteCoreWUP(){
        return(this.wupFullName +".WUP.Core");
    }

    public String getEndPointWUPContainerIngresProcessorIngres() {
        LOG.debug(".getEndPointWUPContainerIngresProcessorIngres(): Entry");
        String endpointName;
        if(this.mustBeDirect){
            endpointName = DIRECT_INTER_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.IngresProcessor.Ingres";
        } else {
            endpointName = SEDA_INTER_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.IngresProcessor.Ingres";
        }
        return(endpointName);
    }

    public String getEndPointWUPContainerIngresProcessorEgress() {
        LOG.debug(".getEndPointWUPContainerIngresProcessorEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.IngresProcessor.Egress";
        return(endpointName);
    }

    public String getEndPointWUPContainerIngresGatekeeperIngres() {
        LOG.debug(".getEndPointWUPContainerIngresGatekeeperIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.IngresGatekeeper.Ingres";
        return(endpointName);
    }

    public String getEndPointWUPIngresConduitIngres() {
        LOG.debug(".getEndPointWUPIngresConduitIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUP.IngresConduit.Ingres";
        return(endpointName);
    }

    public String getEndPointWUPIngres() {
        LOG.debug(".getEndPointWUPIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUP.Ingres";
        return(endpointName);
    }

    public String getEndPointWUPEgress() {
        LOG.debug(".getEndPointWUPEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUP.Egress";
        return(endpointName);
    }

    public String getEndPointWUPEgressConduitEgress() {
        LOG.debug(".getEndPointWUPEgressConduitEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUP.EgressConduit.Egress";
        return(endpointName);
    }

    public String getEndPointWUPContainerEgressGatekeeperIngres() {
        LOG.debug(".getEndPointWUPContainerEgressGatekeeperIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.EgressGatekeeper.Ingres";
        return(endpointName);
    }

    public String getEndPointWUPContainerEgressProcessorIngres() {
        LOG.debug(".getEndPointWUPContainerEgressProcessorIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.EgressProcessor.Ingres";
        return(endpointName);
    }

    public String getEndPointWUPContainerEgressProcessorEgress() {
        LOG.debug(".getEndPointWUPContainerEgressProcessorEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".WUPContainer.EgressProcessor.Egress";
        return(endpointName);
    }

    public String getEndPointInterchangePayloadTransformerIngres() {
        LOG.debug(".getEndPointInterchangePayloadTransformerIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".Interchange.PayloadTransformer.Ingres";
        return(endpointName);
    }

    public String getEndPointInterchangePayloadTransformerEgress() {
        LOG.debug(".getEndPointInterchangePayloadTransformerEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".Interchange.PayloadTransformer.Egress";
        return(endpointName);
    }

    public String getEndPointInterchangeRouterIngres() {
        LOG.debug(".getEndPointInterchangeRouterIngres(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".Interchange.Router.Ingres";
        return(endpointName);
    }

    public String getEndPointInterchangeRouterEgress() {
        LOG.debug(".getEndPointInterchangeRouterEgress(): Entry");
        String endpointName = INTRA_FUNCTION_DIRECT_TYPE + wupFullName + ".Interchange.Router.Egress";
        return(endpointName);
    }

    public String getRouteIngresProcessorEgress2IngresGatekeeperIngres() {
        LOG.debug(".getRouteIngresProcessorEgress2IngresGatekeeperIngres(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUPC.IP.E-To-" + wupFullName +".WUPC.IG.I" ;
        return(endpointName);
    }

    public String getRouteIngresConduitIngres2WUPIngres() {
        LOG.debug(".getRouteIngresConduitIngres2WUPIngres(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUP.IC.I-To-" + wupFullName +".WUP.I" ;
        return(endpointName);
    }

    public String getRouteWUPEgress2WUPEgressConduitEgress() {
        LOG.debug(".getRouteWUPEgress2WUPEgressConduitEgress(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUP.E-To-" + wupFullName +".WUP.EC.E" ;
        return(endpointName);
    }

    public String getRouteWUPEgressConduitEgress2WUPEgressProcessorIngres() {
        LOG.debug(".getRouteWUPEgressConduitEgress2WUPEgressProcessorIngres(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUP.EC.E-To-" + wupFullName +".WUPC.EP.I" ;
        return(endpointName);
    }

    public String getRouteWUPEgressProcessorEgress2WUPEgressGatekeeperIngres() {
        LOG.debug(".getRouteWUPEgressProcessorEgress2WUPEgressGatekeeperIngres(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUP.EP.E-To-" + wupFullName +".WUPC.EG.I" ;
        return(endpointName);
    }

    public String getRouteInterchangePayloadTransformerEgress2InterchangePayloadRouterIngres() {
        LOG.debug(".getRouteInterchangePayloadTransformerEgress2InterchangePayloadRouterIngres(): Entry");
        String endpointName = "FROM-" + wupFullName + ".IC.PT.E-To-" + wupFullName +".IC.R.I" ;
        return(endpointName);
    }

    public String getRouteWUPContainerIngressProcessor() {
        LOG.debug(".getRouteWUPContainerIngressProcessor(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUPC.IP.I-To-" + wupFullName +".WUPC.IP.E" ;
        return(endpointName);
    }

    public String getRouteWUPContainerIngresGateway() {
        LOG.debug(".getRouteWUPContainerIngresGateway(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUPC.IG.I-To-" + wupFullName +".WUPC.IG.E" ;
        return(endpointName);
    }

    public String getRouteWUPContainerEgressGateway() {
        LOG.debug(".getRouteWUPContainerEgressGateway(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUPC.EG.I-To-" + wupFullName +".WUPC.EG.E" ;
        return(endpointName);
    }

    public String getRouteWUPContainerEgressProcessor() {
        LOG.debug(".getRouteWUPContainerEgressProcessor(): Entry");
        String endpointName = "FROM-" + wupFullName + ".WUPC.EP.I-To-" + wupFullName +".WUPC.EP.E" ;
        return(endpointName);
    }

    public String getRouteInterchangePayloadTransformer(){
        LOG.debug(".getRouteInterchangePayloadTransformer(): Entry");
        String endpointName = "FROM-" + wupFullName + ".IC.PT.I-To-" + wupFullName +".IC.PT.E" ;
        return(endpointName);
    }

    public String getRouteInterchangeRouter(){
        LOG.debug(".getRouteInterchangeRouter(): Entry");
        String endpointName = "FROM-" + wupFullName + ".IC.R.I-To-" + wupFullName +".IC.R.E" ;
        return(endpointName);
    }
}
