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
package net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets;

public enum PetasosTaskExtensionTypeEnum {
    PETASOS_TASK_FINALISATION_STATUS_EXTENSION("petasos_task.finalisation_status"),
    PETASOS_TASK_FINALISATION_UPDATE_DATE_EXTENSION("petasos_task.finalisation_update_date");

    private String petasosTaskReferenceExtensionType;

    private PetasosTaskExtensionTypeEnum(String extensionType){
        this.petasosTaskReferenceExtensionType = extensionType;
    }

    public String getPetasosExtensionType() {
        return petasosTaskReferenceExtensionType;
    }

    public static PetasosTaskExtensionTypeEnum fromPetasosTaskExtensionTypeString(String extensionType){
        for(PetasosTaskExtensionTypeEnum currentType: PetasosTaskExtensionTypeEnum.values()){
            if(currentType.getPetasosExtensionType().contentEquals(extensionType)){
                return(currentType);
            }
        }
        return(null);
    }
}
