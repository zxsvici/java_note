package com.zxs.note.boot.scheduler.controller;

import com.zxs.note.boot.scheduler.config.CronTaskRegistrar;
import com.zxs.note.boot.scheduler.config.SchedulingRunnable;
import com.zxs.note.boot.scheduler.model.Cron;
import com.zxs.note.boot.scheduler.service.ICronService;
import com.zxs.note.boot.scheduler.util.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author 81509
 */
@RestController
public class CronController {

    @Autowired
    private ICronService cronService;

    @Autowired
    private CronTaskRegistrar taskRegistrar;


    @GetMapping("/saveCron")
    public HttpResult save() {
        Cron cron = new Cron();
        //每天八点一十五分
        cron.setCronStr("*/5 * * * * ? ");
        cron.setStatus(1);
        cron.setSendTime("08:15");
        boolean success = cronService.saveOrUpdate(cron);
        if (!success) {
            return HttpResult.error("定时任务保存失败");
        }
        if (cron.getStatus() == 1) {
            SchedulingRunnable task = new SchedulingRunnable(cron);
            taskRegistrar.removeCronTask(cron.getId());
            taskRegistrar.addCronTask(cron.getId(), () -> System.out.println("sb is " + cron.getId()), cron.getCronStr());
        }
        if (cron.getStatus() == 0) {
            taskRegistrar.removeCronTask(cron.getId());
        }
        return HttpResult.ok("定时任务保存成功");
    }

    @GetMapping("/deleteCron")
    public HttpResult delete(Integer cronId) {
        if (cronId != null) {
            Cron cron = cronService.getById(cronId);
            boolean remove = cronService.removeById(cronId);
            if (remove) {
                taskRegistrar.removeCronTask(cronId);
            }
            return HttpResult.ok("定时任务删除成功");
        }
        return HttpResult.error("传入数据为空");
    }

    public List<Cron> findCrons() {
        return cronService.list();
    }


}
