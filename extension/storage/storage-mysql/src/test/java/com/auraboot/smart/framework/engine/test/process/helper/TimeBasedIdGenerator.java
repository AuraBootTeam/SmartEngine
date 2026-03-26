package com.auraboot.smart.framework.engine.test.process.helper;

import java.util.concurrent.atomic.AtomicLong;

import com.auraboot.smart.framework.engine.configuration.IdGenerator;
import com.auraboot.smart.framework.engine.model.instance.Instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  00:02.
 */
public class TimeBasedIdGenerator implements IdGenerator {

    private  static AtomicLong temp = new AtomicLong(0);

    @Override
    public void generate(Instance instance) {
        String s = System.currentTimeMillis() + temp.getAndIncrement() + "";
        instance.setInstanceId(s);
//        return s;
    }
}
