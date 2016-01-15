package org.occideas.rest.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.occideas.PossibleAnswer;
import org.occideas.rest.response.DataResponse;
import org.occideas.rest.response.StatusOnly;

public class PossibleAnswerServiceTest {
	@Test
	public void testPossibleAnswer() {
		long possibleAnswer_id = 0;

		try {
			possibleAnswer_id = testCreatePossibleAnswer();
			testGetPossibleAnswer(possibleAnswer_id);
			testUpdatePossibleAnswer(possibleAnswer_id);
			testDeletePossibleAnswer(possibleAnswer_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long testCreatePossibleAnswer() throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		PossibleAnswer possibleAnswer = new PossibleAnswer();
		possibleAnswer.setName("PossibleAnswer");
		possibleAnswer.setDescription("PossibleAnswer");
		possibleAnswer.setType("PossibleAnswer");

		String input = "{\"name\" : \"" + possibleAnswer.getName() + "\",\"description\" : \"" + possibleAnswer.getDescription()
				+ "\",\"type\" : \"" + possibleAnswer.getType() + "\"}";

		ClientResponse resp = service.path("rest").path("possibleAnswer/create").type(MediaType.APPLICATION_JSON)
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

	private void testGetPossibleAnswer(long possibleAnswer_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("possibleAnswer/get").queryParam("id", String.valueOf(possibleAnswer_id))
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		String text = resp.getEntity(String.class);
		System.out.println("GET Response : " + text);

		assertEquals(200, resp.getStatus());

		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertEquals(possibleAnswer_id, this.getIdNode(obj.getData().toString()));

	}

	private void testUpdatePossibleAnswer(long possibleAnswer_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		PossibleAnswer possibleAnswer = new PossibleAnswer();
		possibleAnswer.setName("PossibleAnswer 2");
		possibleAnswer.setDescription("PossibleAnswer 2");
		possibleAnswer.setType("PossibleAnswer 2");

		String input = "{\"idNode\" : \"" + possibleAnswer_id + "\",\"name\" : \"" + possibleAnswer.getName()
				+ "\",\"description\" : \"" + possibleAnswer.getDescription() + "\",\"type\" : \"" + possibleAnswer.getType()
				+ "\"}";

		ClientResponse resp = service.path("rest").path("possibleAnswer/update").type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, input);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		System.out.println("UPDATE Response : " + text);
		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertNotEquals(null, obj.getData());
		assertEquals(possibleAnswer_id, this.getIdNode(obj.getData().toString()));
	}

	private void testDeletePossibleAnswer(long possibleAnswer_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("possibleAnswer/delete").queryParam("id", String.valueOf(possibleAnswer_id))
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
