package com.auraboot.smart.framework.engine.persister.custom;

import java.util.Collection;
import java.util.List;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.exception.EngineException;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.persister.custom.session.PersisterSession;



/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
@ExtensionBinding(group = ExtensionConstant.CUSTOM, bindKey = ActivityInstanceStorage.class)
public class CustomActivityInstanceStorage implements ActivityInstanceStorage {


    @Override
    public void insert(ActivityInstance instance,
                       ProcessEngineConfiguration processEngineConfiguration) {
    }

    @Override
    public ActivityInstance update(ActivityInstance instance,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        ProcessInstance owner = PersisterSessionTreeUtil.findProcessInstance(
            session, instance.getProcessInstanceId());
        if (owner == null) {
            owner = PersisterSessionTreeUtil.findActivityInstanceOwner(session, instance.getInstanceId());
        }
        if (owner == null || owner.getActivityInstances() == null) {
            // Nothing to reconcile against: the caller mutated the instance in
            // place and the session tree will adopt it on the next insert.
            return instance;
        }
        ActivityInstance stored = PersisterSessionTreeUtil.findActivityInstanceIn(
            owner, instance.getInstanceId());
        if (stored == null) {
            owner.getActivityInstances().add(instance);
        } else if (stored != instance) {
            // Different object with the same identity: point the tree at the
            // caller's authoritative view.
            List<ActivityInstance> activityInstances = owner.getActivityInstances();
            for (int i = 0; i < activityInstances.size(); i++) {
                if (instance.getInstanceId().equals(
                    activityInstances.get(i) == null ? null : activityInstances.get(i).getInstanceId())) {
                    activityInstances.set(i, instance);
                    break;
                }
            }
        }
        return instance;
    }

    @Override
    public ActivityInstance find(String activityInstanceId,String tenantId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;
        ActivityInstance matchedActivityInstance = null;

        for (ProcessInstance processInstance : processInstances) {
            List<ActivityInstance> activityInstances = processInstance.getActivityInstances();

            for (ActivityInstance activityInstance : activityInstances) {
                if (activityInstance.getInstanceId().equals(activityInstanceId)) {
                    matched= true;
                    matchedActivityInstance = activityInstance;
                    break;
                }
            }
            if(matched){
                break;
            }
        }


        return matchedActivityInstance;
    }

    @Override
    public ActivityInstance findWithShading(String processInstanceId, String activityInstanceId,String tenantId,
            ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance owner = PersisterSessionTreeUtil.findProcessInstance(
            PersisterSession.currentSession(), processInstanceId);
        return owner == null ? null
            : PersisterSessionTreeUtil.findActivityInstanceIn(owner, activityInstanceId);
    }


    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
            if (activityInstances == null) {
                continue;
            }
            activityInstances.removeIf(activityInstance ->
                activityInstance != null && instanceId.equals(activityInstance.getInstanceId()));
        }
    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId,String tenantId,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance= PersisterSession.currentSession().getProcessInstance(processInstanceId);
        return null == processInstance ? null : processInstance.getActivityInstances();
    }
}
