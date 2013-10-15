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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * This class performes Http POST required to maintain the applications
 * Thalassa registration.  Execution of this job is configured by the
 * {@link RegistrationScheduler} class.
 *
 * @author codelotus
 */
public final class RegistrationJob implements Job {

    public static final String THALASSA_URL = "thalassaUrl";
    public static final String APP_HOST     = "appHost";
    public static final String APP_PORT     = "appPort";
    public static final String APP_NAME     = "appName";
    public static final String APP_VERION   = "appVersion";

    private HttpClient httpClient;
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationJob.class);


    /**
     * POSTs the application registration to the Thalassa server using the {@link JobDataMap}
     * provided in the {@link JobExecutionContext}
     *
     * @param context JobExecutionContext containing details need required to perform the job
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        LOG.debug("RegistrationJob: " + jobKey + " executing at " + new Date());
        JobDataMap jobData = context.getMergedJobDataMap();
        String aqueductUrl = (String) jobData.get(RegistrationJob.THALASSA_URL);
        String appHost = (String) jobData.get(RegistrationJob.APP_HOST);
        Integer appPort = (Integer) jobData.get(RegistrationJob.APP_PORT);
        String appName = (String) jobData.get(RegistrationJob.APP_NAME);
        String appVersion = (String) jobData.get(RegistrationJob.APP_VERION);

        String registrationUrl = aqueductUrl + "/registrations/" + appName + '/' + appVersion + '/' + appHost + '/' + appPort;

        HttpPost postRequest = new HttpPost(registrationUrl);
        httpClient = new DefaultHttpClient();
        try {
            final HttpResponse response = httpClient.execute(postRequest);
            LOG.debug("Sent registration to Aqueduct: " + response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
