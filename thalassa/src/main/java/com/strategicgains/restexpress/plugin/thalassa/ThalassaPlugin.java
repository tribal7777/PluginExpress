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
import com.strategicgains.restexpress.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 */
public final class ThalassaPlugin extends AbstractPlugin {

    private String aqueductUrl;
    private String thalassaUrl;
    private String frontendBind;
    private int    registrationRefreshInterval;
    private String appHost;
    private int    appPort;
    private String appName;
    private String version;

    private static final Logger LOG = LoggerFactory.getLogger(ThalassaPlugin.class);


    /**
     *
     * @param thalassaUrl
     * @return
     */
    public ThalassaPlugin thalassaURL(String thalassaUrl) {
        this.thalassaUrl = thalassaUrl;
        return this;
    }


    /**
     *
     * @param aqueductUrl
     * @return
     */
    public ThalassaPlugin aqueductURL(String aqueductUrl) {
        this.aqueductUrl = aqueductUrl;
        return this;
    }


    /**
     *
     * @param version
     * @return
     */
    public ThalassaPlugin appVersion(String version) {
        this.version = version;
        return this;
    }


    /**
     *
     * @param appName
     * @return
     */
    public ThalassaPlugin appName(String appName) {
        this.appName = appName;
        return this;
    }


    /**
     *
     * @param appPort
     * @return
     */
    public ThalassaPlugin appPort(Integer appPort) {
        this.appPort = appPort;
        return this;
    }


    /**
     *
     * @param appHost
     * @return
     */
    public ThalassaPlugin appHost( String appHost) {
        this.appHost = appHost;
        return this;
    }


    /**
     *
     * @param frontendBind
     * @return
     */
    public ThalassaPlugin frontendBindAddress(String frontendBind) {
        this.frontendBind = frontendBind;
        return this;
    }


    /**
     *
     * @param registrationRefreshInterval
     * @return
     */
    public ThalassaPlugin registrationRefreshInterval(Integer registrationRefreshInterval) {
        this.registrationRefreshInterval = registrationRefreshInterval;
        return this;
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
            if(requiredPropertiesArePresent(this))  {
                // Create HaProxy Backend
                ThalassaAqueductService.createBackendRegistration(aqueductUrl, appName, version);

                // Create HaProxy Frontend
                ThalassaAqueductService.createFrontendRegistration(aqueductUrl, appName, frontendBind, appName);

                // Run registrations
                RegistrationScheduler scheduler = new RegistrationScheduler(thalassaUrl, appHost, appPort, appName, version, registrationRefreshInterval);
                scheduler.run();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    /**
     *
     * @param plugin
     * @return
     */
    private static Boolean requiredPropertiesArePresent(ThalassaPlugin plugin) {
        verifyStringProperty("Aqueduct URL", plugin.aqueductUrl);
        verifyStringProperty("Thalassa URL", plugin.thalassaUrl);
        verifyStringProperty("Frontend Bind", plugin.frontendBind);
        verifyIntegerProperty("Registration Refresh Interval", plugin.registrationRefreshInterval);
        verifyStringProperty("Application Host", plugin.appHost);
        verifyIntegerProperty("Application Port", plugin.appPort);
        verifyStringProperty("Application Name", plugin.appName);
        verifyStringProperty("Application Version", plugin.version);

        return Boolean.TRUE;
    }


    private static void verifyStringProperty(String propertyName, String propertyValue) {
        if(propertyValue == null) {
            throw new RuntimeException("The Thalassa Plugin requires " + propertyName + " to be set.");
        }
    }


    private static void verifyIntegerProperty(String propertyName, Integer propertyValue) {
        if(propertyValue <= 0) {
           throw new RuntimeException("The Thalassa Plugin requires " + propertyName + " to be set.");
        }

    }

}
