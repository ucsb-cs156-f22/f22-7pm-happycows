package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.ControllerTestCase;
import edu.ucsb.cs156.happiercows.ControllerTestCase;
import edu.ucsb.cs156.happiercows.repositories.CowDeathRepository;
import edu.ucsb.cs156.happiercows.entities.CowDeath;
import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(controllers = CowDeathController.class)
@AutoConfigureDataJpa
public class CowDeathControllerTests extends ControllerTestCase {

  @MockBean
  CowDeathRepository cowdeathRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @WithMockUser(roles = { "USER" })
  @Test
  public void logged_in_users_cannot_post() throws Exception {
    mockMvc.perform(post("/api/cowdeath/post?avgHealth=11&commonsId=1&cowsKilled=9&createdAt=2022-11-30T05:35:00.769Z&userId=5"))
            .andExpect(status().is(403)); // normal users can't access at all
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void post_CowDeath_admin_post() throws Exception {
    ZonedDateTime someTime = ZonedDateTime.parse("2022-11-30T05:35:00.769Z");
    ZonedDateTime someOtherTime = ZonedDateTime.parse("2022-11-30T05:39:00.769Z");
    
    CowDeath turnedIntoSteak = CowDeath.builder()
    .avgHealth(11)
    .commonsId(1)
    .cowsKilled(9)
    .createdAt(someTime)
    .userId(5)
    .build();

    when(cowdeathRepository.save(turnedIntoSteak)).thenReturn(turnedIntoSteak);

    MvcResult response = mockMvc.perform(post("/api/cowdeath/post?commonsId=11&userId=10&createdAt=2022-11-30T05:35:00.769Z&cowsKilled=9&avgHealth=1").with(csrf())).andDo(print()).andExpect(status().isOk()).andReturn();

    verify(cowdeathRepository, times(1)).save(turnedIntoSteak);

    String expectedJson = mapper.writeValueAsString(turnedIntoSteak);
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }
/*
@WithMockUser(roles = { "USER" })
@Test
public void get_cowdeath_all_commons_using_commons_id() throws Exception {
  List<Profit> expectedProfits = new ArrayList<Profit>();
  UserCommons expectedUserCommons = UserCommons.builder().id(1).commonsId(2).userId(1).build();
  Profit p1 = Profit.builder().id(36).profit(100).timestamp(12).userCommons(expectedUserCommons).build();
  expectedProfits.add(p1);
  when(profitRepository.findAllByUserCommonsId(1L)).thenReturn(expectedProfits);
  when(userCommonsRepository.findByCommonsIdAndUserId(2L,1L)).thenReturn(Optional.of(expectedUserCommons));

  MvcResult response = mockMvc.perform(get("/api/profits/all/commonsid?commonsId=2")).andDo(print()).andExpect(status().isOk()).andReturn();

  verify(profitRepository, times(1)).findAllByUserCommonsId(1L);

  String responseString = response.getResponse().getContentAsString();
  List<Profit> actualProfits = objectMapper.readValue(responseString, new TypeReference<List<Profit>>() {});
  assertEquals(actualProfits, expectedProfits);
}

@WithMockUser(roles = { "USER" })
@Test
public void get_profits_all_commons_other_user_using_commons_id() throws Exception {
  List<Profit> expectedProfits = new ArrayList<Profit>();
  UserCommons expectedUserCommons = UserCommons.builder().id(1).commonsId(2).userId(2).build();
  Profit p1 = Profit.builder().id(36).profit(100).timestamp(12).userCommons(expectedUserCommons).build();
  when(profitRepository.findAllByUserCommonsId(1L)).thenReturn(expectedProfits);
  when(userCommonsRepository.findByCommonsIdAndUserId(2L,1L)).thenReturn(Optional.of(expectedUserCommons));

  MvcResult response = mockMvc.perform(get("/api/profits/all/commonsid?commonsId=2").contentType("application/json")).andExpect(status().isNotFound()).andReturn();

  verify(userCommonsRepository, times(1)).findByCommonsIdAndUserId(2L,1L);

  Map<String, Object> json = responseToJson(response);
  assertEquals("EntityNotFoundException", json.get("type"));
  assertEquals("UserCommons with id 1 not found", json.get("message"));
}

@WithMockUser(roles = { "USER" })
@Test
public void get_profits_all_commons_nonexistent_using_commons_id() throws Exception {
  MvcResult response = mockMvc.perform(get("/api/profits/all/commonsid?commonsId=2").contentType("application/json")).andExpect(status().isNotFound()).andReturn();

  verify(userCommonsRepository, times(1)).findByCommonsIdAndUserId(2L,1L);

  Map<String, Object> json = responseToJson(response);
  assertEquals("EntityNotFoundException", json.get("type"));
  assertEquals("UserCommons with commonsId 2 and userId 1 not found", json.get("message"));
}
    */
}
