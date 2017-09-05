/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.t2m.devcoach.restful;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 * Jersey REST client generated for REST resource:Accounts Service
 * [accounts/ClientLogin]<br>
 * USAGE:
 * <pre>
 *        JerseyClientRESTful client = new JerseyClientRESTful();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Administrador
 */
public class JerseyClientRESTful {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://www.google.com";

    public JerseyClientRESTful() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("accounts/ClientLogin");
    }

    /**
     * @param responseType Class representing the response
     * @param accountType form parameter
     * @param email form parameter
     * @param passwd form parameter
     * @param service form parameter
     * @param source form parameter
     * @return response object (instance of responseType class)
     */
    public <T> T accounts_clientLogin(Class<T> responseType, String accountType, String email, String passwd, String service, String source) throws ClientErrorException {
        String[] formParamNames = new String[]{"accountType", "Email", "Passwd", "service", "source"};
        String[] formParamValues = new String[]{accountType, email, passwd, service, source};
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED).post(javax.ws.rs.client.Entity.form(getQueryOrFormParams(formParamNames, formParamValues)), responseType);
    }

    private Form getQueryOrFormParams(String[] paramNames, String[] paramValues) {
        Form form = new javax.ws.rs.core.Form();
        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                form = form.param(paramNames[i], paramValues[i]);
            }
        }
        return form;
    }

    public void close() {
        client.close();
    }

}
