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
package net.fhirfactory.pegacorn.petasos.control.tasks.caches;

import net.fhirfactory.pegacorn.petasos.core.resources.task.datatypes.PetasosTaskToken;
import net.fhirfactory.pegacorn.petasos.model.wup.WUPIdentifier;
import net.fhirfactory.pegacorn.petasos.core.resources.task.PetasosTask;
import net.fhirfactory.pegacorn.petasos.core.resources.task.valuesets.PetasosTaskFinalisationStatusEnum;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class acts as the Data Manager for the Parcel Cache within the local
 * ProcessingPlant. That is, it is the single management point for the
 * Parcel element set itself. It does not implement business logic associated
 * with the surrounding activity associated with each Parcel beyond provision
 * of helper methods associated with search-set and status-set collection
 * methods.
 *
 * @author Mark A. Hunter
 * @since 2020-06-01
 */
@ApplicationScoped
public class TaskDM {
    private static final Logger LOG = LoggerFactory.getLogger(TaskDM.class);

    private ConcurrentHashMap<PetasosTaskToken, PetasosTask> petasosTaskCache;

    public TaskDM() {
        petasosTaskCache = new ConcurrentHashMap<PetasosTaskToken, PetasosTask>();
    }

    public void addTask(PetasosTask parcel) {
        LOG.debug(".addParcel(): Entry, parcel --> {}", parcel);
        if (parcel == null) {
            return;
        }
        if (!parcel.hasPetasosTaskToken()) {
            return;
        }
        PetasosTaskToken parcelInstanceID = parcel.getPetasosTaskToken();
        if(petasosTaskCache.containsKey(parcelInstanceID)){
            petasosTaskCache.remove(parcelInstanceID);
        }
        petasosTaskCache.put(parcelInstanceID, parcel);
    }

    public PetasosTask getTask(PetasosTaskToken taskToken) {
        LOG.debug(".getTask(): Entry, taskToken --> {}", taskToken);
        if (petasosTaskCache.containsKey(taskToken)) {
            return (petasosTaskCache.get(taskToken));
        }
        return (null);
    }

    public void removeTask(PetasosTask petasosTask) {
        LOG.debug(".removeTask(): Entry, petasosTask --> {}", petasosTask);
        if (petasosTask == null) {
            return;
        }
        if (!petasosTask.hasPetasosTaskToken()) {
            return;
        }
        if(petasosTaskCache.containsKey(petasosTask.getPetasosTaskToken())) {
            petasosTaskCache.remove(petasosTask.getPetasosTaskToken());
        }
    }

    public void removeTask(PetasosTaskToken taskToken) {
        LOG.debug(".removeTask(): Entry, taskToken --> {}", taskToken);
        if (taskToken == null) {
            return;
        }
        if(petasosTaskCache.containsKey(taskToken)) {
            petasosTaskCache.remove(taskToken);
        }
    }

    public List<PetasosTask> getPetasosTaskList() {
        LOG.debug(".getPetasosTaskList(): Entry");
        List<PetasosTask> parcelList = new ArrayList<>();
        Collection<PetasosTask> taskCollection = petasosTaskCache.values();
        parcelList.addAll(taskCollection);
        return (parcelList);
    }

    public List<PetasosTask> getPetasosTaskByState(Task.TaskStatus status) {
        LOG.debug(".getParcelSet(): Entry, status --> {}", status);
        List<PetasosTask> parcelList = new ArrayList<>();
        Collection<PetasosTask> taskCollection = petasosTaskCache.values();
        for(PetasosTask currentTask: taskCollection){
            if (currentTask.hasStatus()) {
                if (currentTask.getStatus().equals(status)) {
                    parcelList.add(currentTask);
                }
            }
        }
        return (parcelList);
    }

    public List<PetasosTask> getActiveTaskList() {
        List<PetasosTask> parcelList = getPetasosTaskByState(Task.TaskStatus.INPROGRESS);
        return (parcelList);
    }

    public List<PetasosTask> getFinishedTaskList() {
        List<PetasosTask> parcelList = getPetasosTaskByState(Task.TaskStatus.COMPLETED);
        return (parcelList);
    }

    public List<PetasosTask> getFinalisedTaskList() {
        List<PetasosTask> parcelList = new ArrayList<>();
        Collection<PetasosTask> taskCollection = petasosTaskCache.values();
        for(PetasosTask currentTask: taskCollection){
            if (currentTask.hasFinalisationStatus()) {
                if (currentTask.getFinalisationStatus().equals(PetasosTaskFinalisationStatusEnum.PARCEL_FINALISATION_STATUS_FINALISED)) {
                    parcelList.add(currentTask);
                }
            }
        }
        return (parcelList);
    }

    public List<PetasosTask> getReadyTaskList() {
        LOG.debug(".getInProgressParcelSet(): Entry");
        List<PetasosTask> parcelList = getPetasosTaskByState(Task.TaskStatus.READY);
        return (parcelList);
    }

    public List<PetasosTask> getTaskListForEpisode(PetasosTaskToken episodeID) {
        LOG.debug(".getTaskListForEpisode(): Entry, episodeID --> {}" + episodeID);
        List<PetasosTask> taskList = new LinkedList<>();
        Collection<PetasosTask> allTasksList = this.petasosTaskCache.values();
        for(PetasosTask currentTask: allTasksList){
            if(currentTask.hasGroupIdentifier()){
                if(currentTask.getGroupIdentifier().getValue().contentEquals(episodeID.getContent())){
                    taskList.add(currentTask);
                }
            }
        }
        return (taskList);
    }

    public List<PetasosTask> getCurrentTasksForWUP(WUPIdentifier wupInstanceID) {
        LOG.debug(".getCurrentParcel(): Entry, wupInstanceID --> {}" + wupInstanceID);
        List<PetasosTask> taskList = new ArrayList<>();
        Collection<PetasosTask> fullTaskList = this.petasosTaskCache.values();
        for(PetasosTask currentTask: fullTaskList) {
            if (currentTask.hasOwner()) {
                if (currentTask.getOwner().getIdentifier().getValue().contentEquals(wupInstanceID.getTokenValue())) {
                    taskList.add(currentTask);
                }
            }
        }
        return (taskList);
    }

}
