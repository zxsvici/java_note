package com.zxs.note.boot.scheduler.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxs.note.boot.scheduler.mapper.CronMapper;
import com.zxs.note.boot.scheduler.model.Cron;
import com.zxs.note.boot.scheduler.service.ICronService;
import org.springframework.stereotype.Service;


/**
 * @author 81509
 */
@Service
public class ICronServiceImpl extends ServiceImpl<CronMapper, Cron> implements ICronService {
}
