package com.zxs.note.boot.scheduler.controller;

import com.zxs.note.boot.scheduler.quartz.AsyncJobFactory;
import org.quartz.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class JobController {

    @Resource
    private Scheduler scheduler;


    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public Boolean add() {

        String jobName = "job";
        String jobGroup = "group";
        System.out.println("aaa");

        Class<? extends Job> jobClass = AsyncJobFactory.class;
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

        //表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ? *");

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

        String jobTrigger = trigger.getKey().getName();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return true;
    }
}
