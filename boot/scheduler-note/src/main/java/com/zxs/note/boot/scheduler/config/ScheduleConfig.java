package com.zxs.note.boot.scheduler.config;

import com.zxs.note.boot.scheduler.controller.CronController;
import com.zxs.note.boot.scheduler.model.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 初始化定时任务
 */
@Component
public class ScheduleConfig implements CommandLineRunner {


    @Autowired
    private CronTaskRegistrar taskRegistrar;

    @Autowired
    private CronController cronController;

    @Override
    public void run(String... args) throws Exception {
        List<Cron> crons = cronController.findCrons();
        for (Cron cron : crons) {
            //状态为1时为正常定时任务
            if (cron.getStatus() == 1) {
                SchedulingRunnable task = new SchedulingRunnable(cron);
                taskRegistrar.addCronTask(cron.getId(), task, cron.getCronStr());
            }
        }
    }

}