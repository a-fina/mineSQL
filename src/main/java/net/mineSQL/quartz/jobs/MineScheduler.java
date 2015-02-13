/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mineSQL.quartz.jobs;

import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author alessio.finamore
 */
public class MineScheduler {
    
    public void scheduleAllJobs(Scheduler scheduler) throws SchedulerException{
            // Define job instance
            JobDetail sJob = newJob(StivJob.class)
                    .withIdentity("job1", "group1")
                    .usingJobData("eat", "apple")
                    .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(simpleSchedule()
                    .withIntervalInSeconds(40)
                    .repeatForever())
                    .build();
            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(sJob, trigger);
    
    }
}
