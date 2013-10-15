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

import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.plugin.AbstractPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class ThalassaPlugin extends AbstractPlugin {

    private final String aqueductUrl;
    private final String thalassaUrl;
    private final String frontendBind;
    private final int    registrationRefreshInterval;
    private final String appHost;
    private final int    appPort;
    private final String appName;
    private final String version;

    private static final Logger LOG = LoggerFactory.getLogger(ThalassaPlugin.class);


    /**
     *
     * @param thalassaUrl
     * @param aqueductUrl
     * @param frontendBind
     * @param appHost
     * @param appPort
     * @param appName
     * @param version
     * @param registrationRefreshInterval
     */
    public ThalassaPlugin(String thalassaUrl, String aqueductUrl, String frontendBind, String appHost, int appPort, String appName, String version, int registrationRefreshInterval) {
        this.thalassaUrl = thalassaUrl;
        this.aqueductUrl = aqueductUrl;
        this.frontendBind = frontendBind;
        this.registrationRefreshInterval = registrationRefreshInterval;
        this.appHost = appHost;
        this.appPort = appPort;
        this.appName = appName;
        this.version = version;
    }


    /**
     * Registers Front and Backend settings with Aqueduct and begins Thalassa registration loop
     *
     * @param server RestExpress server, unused in this method
     * @return this instance of the Thalassa plugin
     */
    @Override
    public ThalassaPlugin register(RestExpress server) {
        try {
            // Create HaProxy Backend
            ThalassaAqueductService.createBackendRegistration(aqueductUrl, appName, version);

            // Create HaProxy Frontend
            ThalassaAqueductService.createFrontendRegistration(aqueductUrl, appName, frontendBind, appName);

            // Run registrations
            RegistrationScheduler scheduler = new RegistrationScheduler(thalassaUrl, appHost, appPort, appName, version, registrationRefreshInterval);
            scheduler.run();

        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return this;
    }
}
