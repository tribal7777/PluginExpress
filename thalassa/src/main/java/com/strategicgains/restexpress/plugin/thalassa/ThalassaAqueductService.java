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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 *  Service class of static methods to create the Thalassa Aqueduct Frontend
 *  and Backend entries
 */
public final class ThalassaAqueductService {

    private ThalassaAqueductService() {}

    private static final Logger LOG = LoggerFactory.getLogger(ThalassaAqueductService.class);


    /**
     * Http PUT's the Backend registration to Thalassa Aqueduct
     *
     * @param aqueductServer HTTP Url of the Aqueduct server and port (Ex: http://1.2.3.4:10000)
     * @param backendName the name of the backend to create
     * @param version the version of the backend to create
     * @throws IOException
     */
    public static void createBackendRegistration(String aqueductServer, String backendName, String version) throws IOException {
        String backendCreation = "{\"type\":\"dynamic\", \"name\":\""+backendName+"\", \"version\":\""+version+"\"}";
        HttpClient httpClient = new DefaultHttpClient();

        StringEntity requestEntity = new StringEntity(backendCreation, ContentType.APPLICATION_JSON);
        HttpPut putRequest = new HttpPut(aqueductServer + "/backends/" + backendName);
        putRequest.setEntity(requestEntity);
        final HttpResponse response = httpClient.execute(putRequest);
        LOG.debug("Backend Registration Created: " + response.getStatusLine());
    }


    /**
     * Http PUT's to Frontend registration to Thalassa Aqueduct
     *
     * @param aqueductServer Http URL of the Aqueduct server and port (Ex: http://1.2.3.4:10000)
     * @param frontendName   the name of the fronted to create
     * @param bindAddresses  the address and port to bind to (Ex: *.80)
     * @param backendName    the name of the backend to route requests to
     * @throws IOException
     */
    public static void createFrontendRegistration(String aqueductServer, String frontendName, String bindAddresses, String backendName) throws IOException {
        String backendCreation = "{\"bind\":\""+bindAddresses+"\",\"backend\":\""+backendName+"\",\"mode\":\"http\"}";
        HttpClient httpClient = new DefaultHttpClient();

        StringEntity requestEntity = new StringEntity(backendCreation, ContentType.APPLICATION_JSON);
        HttpPut putRequest = new HttpPut(aqueductServer + "/frontends/" + frontendName);
        putRequest.setEntity(requestEntity);
        final HttpResponse response = httpClient.execute(putRequest);
        LOG.debug("Frontend Registration Created: " + response.getStatusLine());
    }
}
