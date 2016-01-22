package org.occideas.rest.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.occideas.entity.Question;
import org.occideas.rest.response.DataResponse;
import org.occideas.rest.response.StatusOnly;

@Ignore
public class QuestionServiceTest {
	@Test
	public void testQuestion() {
		long question_id = 0;

		try {
			question_id = testCreateQuestion();
			testGetQuestion(question_id);
			testUpdateQuestion(question_id);
			testDeleteQuestion(question_id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long testCreateQuestion() throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Question question = new Question();
		question.setName("Question");
		question.setDescription("Question");
		question.setType("Question");

		String input = "{\"name\" : \"" + question.getName() + "\",\"description\" : \"" + question.getDescription()
				+ "\",\"type\" : \"" + question.getType() + "\"}";

		ClientResponse resp = service.path("rest").path("question/create").type(MediaType.APPLICATION_JSON)
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

	private void testGetQuestion(long question_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("question/get").queryParam("id", String.valueOf(question_id))
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		String text = resp.getEntity(String.class);
		System.out.println("GET Response : " + text);

		assertEquals(200, resp.getStatus());

		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertEquals(question_id, this.getIdNode(obj.getData().toString()));

	}

	private void testUpdateQuestion(long question_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		Question question = new Question();
		question.setName("Question 2");
		question.setDescription("Question 2");
		question.setType("Question 2");

		String input = "{\"idNode\" : \"" + question_id + "\",\"name\" : \"" + question.getName()
				+ "\",\"description\" : \"" + question.getDescription() + "\",\"type\" : \"" + question.getType()
				+ "\"}";

		ClientResponse resp = service.path("rest").path("question/update").type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, input);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		System.out.println("UPDATE Response : " + text);
		Gson gson = new Gson();
		DataResponse obj = gson.fromJson(text, DataResponse.class);

		assertEquals("1", obj.getStatus());
		assertNotEquals(null, obj.getData());
		assertEquals(question_id, this.getIdNode(obj.getData().toString()));
	}

	private void testDeleteQuestion(long question_id) throws IOException {
		Client client = Client.create();
		WebResource service = client.resource("http://localhost:8080/occideas");

		ClientResponse resp = service.path("rest").path("question/delete").queryParam("id", String.valueOf(question_id))
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
