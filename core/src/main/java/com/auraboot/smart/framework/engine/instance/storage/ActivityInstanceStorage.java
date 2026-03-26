package com.auraboot.smart.framework.engine.instance.storage;

import java.util.List;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.Instance;

public interface ActivityInstanceStorage {

    void insert(ActivityInstance activityInstance,
                ProcessEngineConfiguration processEngineConfiguration);

    ActivityInstance update(ActivityInstance activityInstance,
                            ProcessEngineConfiguration processEngineConfiguration);

    ActivityInstance find(String activityInstanceId,String tenantId,
                          ProcessEngineConfiguration processEngineConfiguration);

    ActivityInstance findWithShading(String processInstanceId, String activityInstanceId,String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    void remove(String activityInstanceId,String tenantId,
                ProcessEngineConfiguration processEngineConfiguration);

    List<ActivityInstance> findAll(String processInstanceId,String tenantId,
                                   ProcessEngineConfiguration processEngineConfiguration);

}
