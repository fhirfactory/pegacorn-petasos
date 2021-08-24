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
package net.fhirfactory.pegacorn.petasos.core.resources.component;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResourceDef(name = "PetasosComponent", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class PetasosComponent extends Device {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(PetasosComponent.class);

    @Override
    public PetasosComponent copy() {
        PetasosComponent retVal = new PetasosComponent();
        super.copyValues(retVal);
        return (retVal);
    }

    public PetasosComponent() {
        super();
        FhirContext ctx = FhirContext.forR4();
        ctx.registerCustomType(PetasosComponent.class);
    }

    public String getUserFriendlyName(){
        if(!hasDeviceName()){
            return(null);
        }
        for(DeviceDeviceNameComponent currentDeviceName: getDeviceName()){
            if(currentDeviceName.getType().equals(DeviceNameType.USERFRIENDLYNAME)){
                return(currentDeviceName.getName());
            }
        }
        return(null);
    }

    public void setUserFriendlyName(String userFriendlyName){
        if(StringUtils.isEmpty(userFriendlyName)){
            return;
        }
        DeviceDeviceNameComponent deviceName = new DeviceDeviceNameComponent();
        deviceName.setType(DeviceNameType.USERFRIENDLYNAME);
        deviceName.setName(userFriendlyName);
        addDeviceName(deviceName);
    }
}
