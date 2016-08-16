package ru.sbrf.efs.rmkmcib.bht.app.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessRoutingStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.ProcessStep;
import ru.sbrf.efs.rmkmcib.bht.app.process.crm.actions.db.CRMBoundedClassesProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 10.08.2016.
 *
 * Класс ждет окончание запуска spring контекста, чтобы доконфигурировать бины, имплементирующие ProcessRoutingStep
 * @see ProcessRoutingStep
 */
@Component
public class ProcessRoutingStepApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext ctx = contextRefreshedEvent.getApplicationContext();
        Map<String, ProcessRoutingStep> beans = ctx.getBeansOfType(ProcessRoutingStep.class);
        if(beans != null){
            for(Map.Entry<String,ProcessRoutingStep> bean : beans.entrySet()){
                selectProcesses(ctx,bean.getValue());
            }
        }
    }

    public void selectProcesses(ApplicationContext ctx, ProcessRoutingStep bean){
        Map<String,ProcessStep> target = new HashMap<>();
        Map<String, Object> processes = ctx.getBeansWithAnnotation(bean.getProcessSelectorAnnotation());
        for(Map.Entry<String,Object> process : processes.entrySet()){
            //TODO очень криво, надо переделать, тут хардкод не нужен
            if(process.getValue().getClass().getAnnotation(bean.getProcessSelectorAnnotation()).annotationType().equals(CRMBoundedClassesProcessor.class)){
                CRMBoundedClassesProcessor temp = (CRMBoundedClassesProcessor) process.getValue().getClass().getAnnotation(bean.getProcessSelectorAnnotation());
                target.put(temp.requestClass().getName().concat(temp.responseClass().getName()), (ProcessStep) process.getValue());
            }
        }
        bean.setAvailableProcesses(target);
    }
}
