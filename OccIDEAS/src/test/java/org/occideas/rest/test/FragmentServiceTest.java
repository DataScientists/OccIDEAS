package org.occideas.rest.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.occideas.Fragment;
import org.occideas.rest.response.DataResponse;
import org.occideas.rest.response.StatusOnly;

public class FragmentServiceTest {
	@Test
	public void testFragment() {
		long fragment_id = 0;

		try {
			fragment_id = testCreateFragment();
			testGetFragment(fragment_id);
			testUpdateFragment(fragment_id);
			testDeleteFragment(fragment_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long testCreateFragment() throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Fragment fragment = new Fragment();
		fragment.setName("Fragment");
		fragment.setDescription("Fragment");
		fragment.setType("Fragment");

		String input = "{\"name\" : \"" + fragment.getName() + "\",\"description\" : \"" + fragment.getDescription()
				+ "\",\"type\" : \"" + fragment.getType() + "\"}";

		ClientResponse resp = service.path("rest").path("fragment/create").type(MediaType.APPLICATION_JSON)
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

	private void testGetFragment(long fragment_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("fragment/get").queryParam("id", String.valueOf(fragment_id))
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		String text = resp.getEntity(String.class);
		System.out.println("GET Response : " + text);

		assertEquals(200, resp.getStatus());

		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertEquals(fragment_id, this.getIdNode(obj.getData().toString()));

	}

	private void testUpdateFragment(long fragment_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Fragment fragment = new Fragment();
		fragment.setName("Fragment 2");
		fragment.setDescription("Fragment 2");
		fragment.setType("Fragment 2");

		String input = "{\"idNode\" : \"" + fragment_id + "\",\"name\" : \"" + fragment.getName()
				+ "\",\"description\" : \"" + fragment.getDescription() + "\",\"type\" : \"" + fragment.getType()
				+ "\"}";

		ClientResponse resp = service.path("rest").path("fragment/update").type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, input);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		System.out.println("UPDATE Response : " + text);
		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertNotEquals(null, obj.getData());
		assertEquals(fragment_id, this.getIdNode(obj.getData().toString()));
	}

	private void testDeleteFragment(long fragment_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("fragment/delete").queryParam("id", String.valueOf(fragment_id))
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
