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
package net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskReferenceExtensionMeanings;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskReferenceExtensionTypeEnum;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ResourceDef(name = "PetasosTaskReference", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosTaskReference extends Reference {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PetasosTask.class);

    private PetasosTaskReferenceExtensionMeanings extensionMeanings;

    @Override
    public PetasosTaskReference copy() {
        PetasosTaskReference retVal = new PetasosTaskReference();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosTaskReference() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosTaskReference.class);
        extensionMeanings = new PetasosTaskReferenceExtensionMeanings();
    }

    public boolean isUpstreamPetasosEpisode(){
        LOG.debug(".isUpstreamPetasosEpisode(): Entry (checking to see if the Reference has UpstreamPetasosEpisode)");
        if(this.hasExtension(extensionMeanings.getPetasosEpisodeTaskReferenceExtension())){
            Extension episodeExtension = getExtensionByUrl(extensionMeanings.getPetasosEpisodeTaskReferenceExtension());
            Coding episodeCoding = (Coding)episodeExtension.getValue();
            if(episodeCoding.getCode().contentEquals(PetasosTaskReferenceExtensionTypeEnum.UPSTREAM_EPISODE_PETASOS_TASK_REFERENCE.getPetasosTaskReferenceExtensionType())){
                LOG.debug(".isUpstreamPetasosEpisode(): Exit, returning -true-");
                return(true);
            }
        }
        LOG.debug(".isUpstreamPetasosEpisode(): Exit, returning -false-");
        return(false);
    }

    public boolean isDownstreamPetasosEpisode(){
        LOG.debug(".isDownstreamPetasosEpisode(): Entry (checking to see if the Reference has DownstreamPetasosEpisode)");
        if(this.hasExtension(extensionMeanings.getPetasosEpisodeTaskReferenceExtension())){
            Extension episodeExtension = getExtensionByUrl(extensionMeanings.getPetasosEpisodeTaskReferenceExtension());
            Coding episodeCoding = (Coding)episodeExtension.getValue();
            if(episodeCoding.getCode().contentEquals(PetasosTaskReferenceExtensionTypeEnum.DOWNSTREAM_EPISODE_PETASOS_TASK_REFERENCE.getPetasosTaskReferenceExtensionType())){
                LOG.debug(".isDownstreamPetasosEpisode(): Exit, returning -true-");
                return(true);
            }
        }
        LOG.debug(".isDownstreamPetasosEpisode(): Exit, returning -false-");
        return(false);
    }

    public void setPetasosTaskReferenceExtensionType(PetasosTaskReferenceExtensionTypeEnum extensionType){
        LOG.debug(".setPetasosEpisodeReferenceExtension(): Entry, extensionType->{}", extensionType);
        if(this.hasExtension(extensionMeanings.getPetasosEpisodeTaskReferenceExtension())) {
            this.removeExtension(extensionMeanings.getPetasosEpisodeTaskReferenceExtension());
        }
        Coding episodeCoding = new Coding();
        episodeCoding.setCode(extensionType.getPetasosTaskReferenceExtensionType());
        Extension episodeExtension = new Extension();
        episodeExtension.setUrl(extensionMeanings.getPetasosEpisodeTaskReferenceExtension());
        episodeExtension.setValue(episodeCoding);
        this.addExtension(episodeExtension);
        LOG.debug(".setPetasosEpisodeReferenceExtension(): Exit");
    }

    public PetasosTaskReferenceExtensionTypeEnum getPetasosTaskReferenceExtensionType(){
        LOG.debug(".getPetasosTaskReferenceExtensionType(): Entry");
        if(this.hasExtension(extensionMeanings.getPetasosEpisodeTaskReferenceExtension())){
            Extension episodeExtension = getExtensionByUrl(extensionMeanings.getPetasosEpisodeTaskReferenceExtension());
            Coding episodeCoding = (Coding)episodeExtension.getValue();
            if(episodeCoding.getCode().contentEquals(PetasosTaskReferenceExtensionTypeEnum.DOWNSTREAM_EPISODE_PETASOS_TASK_REFERENCE.getPetasosTaskReferenceExtensionType())){
                PetasosTaskReferenceExtensionTypeEnum extensionTypeEnum = PetasosTaskReferenceExtensionTypeEnum.fromPetasosTaskReferenceExtensionTypeString(episodeCoding.getCode());
                LOG.debug(".getPetasosTaskReferenceExtensionType(): Exit, extensionType->{}", extensionTypeEnum);
                return(extensionTypeEnum);
            }
        }
        LOG.debug(".getPetasosTaskReferenceExtensionType(): Exit, returning null");
        return(null);
    }
}
