package com.status.services;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.status.bot.TelegramBot;
import com.status.entities.SwitchEntity;

@Aspect
@Component
public class TelegramService {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private StatusService statusService;

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)") 
    public void afterScheduledExecution(JoinPoint joinPoint) {
        System.out.println("AFTER: " + statusService.getOfflines().size());
        List<SwitchEntity> offlines = new ArrayList<>(statusService.getOfflines());
        if (statusService.getPreviousOfflines() != null && !statusService.getPreviousOfflines().isEmpty()) {
            for (SwitchEntity switchEntity : offlines) {
                if (!statusService.getPreviousOfflines().contains(switchEntity)) {
                    telegramBot.sendMessage("The switch " + switchEntity.getIp() + " has just went offline!");
                }
            }
        }
        statusService.setPreviousOfflines(new ArrayList<>(offlines));
    }
}
