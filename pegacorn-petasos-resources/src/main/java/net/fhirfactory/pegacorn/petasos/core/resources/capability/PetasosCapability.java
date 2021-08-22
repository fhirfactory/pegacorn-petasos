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
package net.fhirfactory.pegacorn.petasos.core.resources.capability;

import java.io.Serializable;
import java.util.Objects;

public class PetasosCapability implements Serializable {
    private String capabilityLayer;
    private String capabilitySubLayer;
    private String level1CapabilityName;
    private String level2CapabilityName;
    private String level3CapabilityName;
    private String serviceName;
    private String functionName;

    public PetasosCapability(){
        this.capabilityLayer = null;
        this.capabilitySubLayer = null;
        this.level1CapabilityName = null;
        this.level2CapabilityName = null;
        this.level3CapabilityName = null;
        this.serviceName = null;
        this.functionName = null;
    }

    public PetasosCapability(PetasosCapability ori){
        this.setCapabilityLayer(ori.getCapabilityLayer());
        this.setCapabilitySubLayer(ori.getCapabilitySubLayer());
        this.setLevel1CapabilityName(ori.getLevel1CapabilityName());
        this.setLevel2CapabilityName(ori.getLevel2CapabilityName());
        this.setLevel3CapabilityName(ori.getLevel3CapabilityName());
        this.setServiceName(ori.getServiceName());
        this.setFunctionName(ori.getFunctionName());
    }

    public String getCapabilityLayer() {
        return capabilityLayer;
    }

    public void setCapabilityLayer(String capabilityLayer) {
        this.capabilityLayer = capabilityLayer;
    }

    public String getCapabilitySubLayer() {
        return capabilitySubLayer;
    }

    public void setCapabilitySubLayer(String capabilitySubLayer) {
        this.capabilitySubLayer = capabilitySubLayer;
    }

    public String getLevel1CapabilityName() {
        return level1CapabilityName;
    }

    public void setLevel1CapabilityName(String level1CapabilityName) {
        this.level1CapabilityName = level1CapabilityName;
    }

    public String getLevel2CapabilityName() {
        return level2CapabilityName;
    }

    public void setLevel2CapabilityName(String level2CapabilityName) {
        this.level2CapabilityName = level2CapabilityName;
    }

    public String getLevel3CapabilityName() {
        return level3CapabilityName;
    }

    public void setLevel3CapabilityName(String level3CapabilityName) {
        this.level3CapabilityName = level3CapabilityName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "PetasosCapability{" +
                "capabilityLayer='" + capabilityLayer + '\'' +
                ", capabilitySubLayer='" + capabilitySubLayer + '\'' +
                ", level1CapabilityName='" + level1CapabilityName + '\'' +
                ", level2CapabilityName='" + level2CapabilityName + '\'' +
                ", level3CapabilityName='" + level3CapabilityName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", functionName='" + functionName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosCapability)) return false;
        PetasosCapability that = (PetasosCapability) o;
        return Objects.equals(getCapabilityLayer(), that.getCapabilityLayer()) && Objects.equals(getCapabilitySubLayer(), that.getCapabilitySubLayer()) && Objects.equals(getLevel1CapabilityName(), that.getLevel1CapabilityName()) && Objects.equals(getLevel2CapabilityName(), that.getLevel2CapabilityName()) && Objects.equals(getLevel3CapabilityName(), that.getLevel3CapabilityName()) && Objects.equals(getServiceName(), that.getServiceName()) && Objects.equals(getFunctionName(), that.getFunctionName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCapabilityLayer(), getCapabilitySubLayer(), getLevel1CapabilityName(), getLevel2CapabilityName(), getLevel3CapabilityName(), getServiceName(), getFunctionName());
    }
}
