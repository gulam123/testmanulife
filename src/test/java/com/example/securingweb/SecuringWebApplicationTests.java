package com.example.securingweb;

import com.example.securingweb.model.Task;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecuringWebApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void loginWithValidUserThenAuthenticated() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("admin")
			.password("admin");

		mockMvc.perform(login)
			.andExpect(authenticated().withUsername("admin"));
	}

	@Test
	public void loginWithInvalidUserThenUnauthenticated() throws Exception {
		FormLoginRequestBuilder login = formLogin()
			.user("invalid")
			.password("invalidpassword");

		mockMvc.perform(login)
			.andExpect(unauthenticated());
	}

	@Test
	public void accessUnsecuredResourceThenOk() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk());
	}

	@Test
	public void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
		mockMvc.perform(get("/hello"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser
	public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
		mockMvc.perform(get("/hello"))
				.andExpect(status().isOk());
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void getTaskListTest() throws Exception {
		String uri = "/task/GetTaskList";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Task[] taskList = mapFromJson(content, Task[].class);
		assertTrue(taskList.length > 0);
	}

	@Test
	public void getTaskTest() throws Exception {
		String uri = "/task/GetTask/1";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Task taskList = mapFromJson(content, Task.class);
		assertTrue(!taskList.equals(null));
	}

	@Test
	public void newTaskTest() throws Exception {
		String uri = "/task/newTask";
		Task task = new Task();
		task.setName("Lemon");
		task.setDescription("Lemon");
		task.setStatus(true);

		String inputJson = mapToJson(task);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf()).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals("task is created", "task is created");
	}

	@Test
	public void updateTaskTest() throws Exception {
		String uri = "/task/updateTask/2";
		Task task = new Task();
		task.setId(2L);
		task.setName("Edit dari unit test");
		task.setDescription("Edit dari unit test");
		task.setStatus(false);

		String inputJson = mapToJson(task);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf()).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals("task is updated", "task is updated");
	}

	@Test
	public void deleteTaskTest() throws Exception {
		String uri = "/task/deleteTask/2";
		Task task = new Task();
		task.setId(2L);
		task.setName("Edit dari unit test");
		task.setDescription("Edit dari unit test");
		task.setStatus(false);

		String inputJson = mapToJson(task);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf()).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals("task is deleted", "task is deleted");
	}

}
