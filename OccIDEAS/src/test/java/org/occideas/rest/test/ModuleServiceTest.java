package org.occideas.rest.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.occideas.Module;
import org.occideas.rest.response.DataResponse;
import org.occideas.rest.response.StatusOnly;

public class ModuleServiceTest {
	@Test
	public void testModule() {
		long module_id = 0;

		try {
			module_id = testCreateModule();
			testGetModule(module_id);
			testUpdateModule(module_id);
			testDeleteModule(module_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long testCreateModule() throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Module module = new Module();
		module.setName("Module");
		module.setDescription("Module");
		module.setType("Module");

		String input = "{\"name\" : \"" + module.getName() + "\",\"description\" : \"" + module.getDescription()
				+ "\",\"type\" : \"" + module.getType() + "\"}";

		ClientResponse resp = service.path("rest").path("module/create").type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		System.out.println("CREATE Response : " + text);

		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertNotEquals(null, obj.getData());

		return this.getIdNode(obj.getData().toString());
	}

	private void testGetModule(long module_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("module/get").queryParam("id", String.valueOf(module_id))
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		String text = resp.getEntity(String.class);
		System.out.println("GET Response : " + text);

		assertEquals(200, resp.getStatus());

		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertEquals(module_id, this.getIdNode(obj.getData().toString()));

	}

	private void testUpdateModule(long module_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Module module = new Module();
		module.setName("Module 2");
		module.setDescription("Module 2");
		module.setType("Module 2");

		String input = "{\"idNode\" : \"" + module_id + "\",\"name\" : \"" + module.getName()
				+ "\",\"description\" : \"" + module.getDescription() + "\",\"type\" : \"" + module.getType()
				+ "\"}";

		ClientResponse resp = service.path("rest").path("module/update").type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, input);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		System.out.println("UPDATE Response : " + text);
		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertNotEquals(null, obj.getData());
		assertEquals(module_id, this.getIdNode(obj.getData().toString()));
	}

	private void testDeleteModule(long module_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("module/delete").queryParam("id", String.valueOf(module_id))
				.accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		String text = resp.getEntity(String.class);
		System.out.println("DELETE Response : " + text);

		assertEquals(200, resp.getStatus());

		Gson gson = new Gson();
		StatusOnly obj = gson.fromJson(text, StatusOnly.class);

		assertEquals("1", obj.getStatus());
	}

	private long getIdNode(String data) {
		int start = data.indexOf("idNode=");
		int end = data.indexOf(", name");
		return (long) Double.parseDouble(data.substring(start + 7, end));
	}
}
