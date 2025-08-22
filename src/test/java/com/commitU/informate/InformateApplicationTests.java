package com.commitU.informate;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.calendar.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class InformateApplicationTests {

	@Autowired MockMvc mvc;
	@Autowired EventRepository repo;
	@Autowired ObjectMapper om;

	@Test
	void controller_getEventsByRange_returnsOverlapping() throws Exception {
		// 저장 후 반드시 flush
		repo.saveAndFlush(new Event(
				"수업",
				LocalDateTime.parse("2025-08-10T09:00:00"),
				LocalDateTime.parse("2025-08-10T10:00:00"),
				"user1"
		));

		mvc.perform(get("/api/events/range")
						.param("userId", "user1")
						.param("start", "2025-08-01T00:00:00")
						.param("end",   "2025-08-31T23:59:59"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("수업"));
	}

	@Test
	void controller_searchByTitle_ignoreCase() throws Exception {
		repo.saveAndFlush(new Event(
				"회의",
				LocalDateTime.parse("2025-08-11T09:00:00"),
				LocalDateTime.parse("2025-08-11T10:00:00"),
				"user1"
		));

		mvc.perform(get("/api/events/search")
						.param("userId", "user1")
						.param("title",  "회"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("회의"));
	}

	@Test
	void controller_getByCategory() throws Exception {
		var e = new Event(
				"과제",
				LocalDateTime.parse("2025-08-12T09:00:00"),
				LocalDateTime.parse("2025-08-12T10:00:00"),
				"user2"
		);
		e.setCategory("학사");
		repo.saveAndFlush(e);

		mvc.perform(get("/api/events/category")
						.param("userId", "user1")
						.param("category", "학사"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("과제"));
	}
}