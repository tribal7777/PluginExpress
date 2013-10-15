/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.restexpress.plugin.thalassa;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * This class sets up the Thalassa registration interval and process.  Quartz
 * is used to configure the interval, in seconds, the {@link RegistrationJob}
 * should be excuted
 */
public final class RegistrationScheduler {

    private final String  thalassaUrl;
    private final String  appHost;
    private final Integer appPort;
    private final String  appName;
    private final String  appVersion;
    private final int     registrationRefreshInterval;

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationScheduler.class);


    /**
     * Sets the required properties for Thalassa registrations
     *
     * @param thalassaUrl  url to post registration details to
     * @param appHost      host ip of the system running the application to register
     * @param appPort      port the application is bound to
     * @param appName      application name
     * @param appVersion   application version
     * @param registrationRefreshInterval  the interval at which registrations will be posted to Thalassa (in seconds)
     */
    public RegistrationScheduler(String thalassaUrl, String appHost, int appPort, String appName, String appVersion, int registrationRefreshInterval) {
        this.thalassaUrl = thalassaUrl;
        this.appHost = appHost;
        this.appPort = appPort;
        this.appName = appName;
        this.appVersion = appVersion;
        this.registrationRefreshInterval = registrationRefreshInterval;
    }


    /**
     * Initializes and begins the execution of the {@link RegistrationJob}.
     * A Quartz {@link CronTrigger} is used to run the job on an interval
     * defined by the registrationRefreshInterval in seconds.
     *
     * @throws Exception
     */
    public void run() throws Exception {
        LOG.info("------- Initializing RegistrationScheduler -------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        LOG.info("------- Initialization Complete ------------------");

        LOG.info("------- Scheduling Jobs --------------------------");

        // jobs can be scheduled before sched.start() has been called
        JobDataMap registrationJobData = new JobDataMap();

        registrationJobData.put(RegistrationJob.THALASSA_URL, thalassaUrl);
        registrationJobData.put(RegistrationJob.APP_HOST, appHost);
        registrationJobData.put(RegistrationJob.APP_PORT, appPort);
        registrationJobData.put(RegistrationJob.APP_NAME, appName);
        registrationJobData.put(RegistrationJob.APP_VERION, appVersion);

        JobDetail job = JobBuilder.newJob(RegistrationJob.class)
                .withIdentity("registrationJob", "thalassaRegistrationGroup")
                .setJobData(registrationJobData)
                .build();

        // job registrationTrigger will run every registrationRefreshInterval seconds
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("registrationTrigger", "thalassaRegistrationGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/"+registrationRefreshInterval+" * * * * ?"))
                .build();

        Date ft = sched.scheduleJob(job, trigger);
        LOG.info(job.getKey() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());

        LOG.info("------- Starting Scheduler -----------------------");

        sched.start();

        LOG.info("------- Started Scheduler ------------------------");
    }
}
