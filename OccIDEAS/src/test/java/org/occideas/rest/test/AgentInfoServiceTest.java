package org.occideas.rest.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.occideas.AgentInfo;

public class AgentInfoServiceTest {

	@Test
    public void testGetDefaultUser() throws IOException {
        Client client = Client.create(new DefaultClientConfig());
        WebResource service = client.resource("http://localhost:8080");
        
        AgentInfo  info = new AgentInfo();
   		info.setName("Test");
   		info.setDescription("Test");        

   		ClientResponse resp = service.path("rest").path("agentinfo/create")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class,info);
        
        System.out.println("Got stuff: " + resp);
        
        String text = resp.getEntity(String.class);

        assertEquals(200, resp.getStatus());
    }
}
