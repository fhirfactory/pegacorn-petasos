package net.fhirfactory.pegacorn.petasos.audit.logger;

import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.pegacorn.internals.fhir.r4.internal.topics.FHIRElementTopicFactory;

import net.fhirfactory.pegacorn.petasos.audit.logger.base.AuditEntryLoggerServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class StandardAuditEntryLoggerService  extends AuditEntryLoggerServiceBase {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionAuditEntryLoggerService.class);

    private static final String FHIR_VERSION = "4.0.1";

    @Override
    protected Logger specifyLogger(){return(LOG);}

    @Inject
    private net.fhirfactory.pegacorn.util.FHIRContextUtility FHIRContextUtility;

    private IParser parserR4;

    @Inject
    private FHIRElementTopicFactory topicIDBuilder;

}
