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
import org.springframework.context.annotation.Import;
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
@Import(CowDeathController.class)
@AutoConfigureDataJpa
public class CowDeathControllerTests extends ControllerTestCase {

  @MockBean
  CowDeathRepository cowdeathRepository;

  @Autowired
  private ObjectMapper objectMapper;

  
  @WithMockUser(roles = { "USER" })
  @Test
  public void logged_in_users_cannot_post() throws Exception {
    mockMvc.perform(post("/api/cowdeath?avgHealth=11&commonsId=1&cowsKilled=9&createdAt=2022-11-30T05:35:00.769Z&userId=5"))
            .andExpect(status().is(403)); // normal users can't access at all
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void post_cowdeath_admin_post() throws Exception {
    ZonedDateTime someTime = ZonedDateTime.parse("2022-11-30T05:35:10Z");
    
    
    CowDeath cowdeathsample = CowDeath.builder()
    .avgHealth(11)
    .commonsId(1)
    .cowsKilled(9)
    .createdAt(someTime)
    .userId(5)
    .build();

    when(cowdeathRepository.save(cowdeathsample)).thenReturn(cowdeathsample);

    MvcResult response = mockMvc.perform(post("/api/cowdeath?commonsId=1&userId=5&createdAt=2022-11-30T05:35:10Z&cowsKilled=9&avgHealth=11")
      .with(csrf()))
            .andExpect(status().isOk()).andReturn();

    verify(cowdeathRepository, times(1)).save(cowdeathsample);

    String expectedJson = mapper.writeValueAsString(cowdeathsample);
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

@WithMockUser(roles = { "ADMIN" })
@Test
public void get_all_cowdeaths_using_commons_id() throws Exception {

  List<CowDeath> expectedCowDeaths = new ArrayList<CowDeath>();
    

  ZonedDateTime someTime = ZonedDateTime.parse("2022-11-30T05:35:10Z[UTC]");
  ZonedDateTime someOtherTime = ZonedDateTime.parse("2022-11-30T05:39:10Z[UTC]");
  CowDeath cowdeathsample = CowDeath.builder()
    .id(0)
    .avgHealth(11)
    .commonsId(1)
    .cowsKilled(9)
    .createdAt(someTime)
    .userId(5)
    .build();
  CowDeath cowdeathsample2 = CowDeath.builder()
    .id(1)
    .avgHealth(12)
    .commonsId(1)
    .cowsKilled(5)
    .createdAt(someOtherTime)
    .userId(14)
    .build();


  expectedCowDeaths.add(cowdeathsample);
  expectedCowDeaths.add(cowdeathsample2);
  when(cowdeathRepository.getCowsKilledByCommonsId(1L)).thenReturn(expectedCowDeaths);
  when(cowdeathRepository.save(cowdeathsample)).thenReturn(cowdeathsample);
  when(cowdeathRepository.save(cowdeathsample2)).thenReturn(cowdeathsample2);

  MvcResult response = mockMvc.perform(get("/api/cowdeath/bycommons?commonsId=1").with(csrf()))
            .andExpect(status().isOk()).andReturn();

  verify(cowdeathRepository, times(1)).getCowsKilledByCommonsId(1L);

  String responseString = response.getResponse().getContentAsString();
  List<CowDeath> actualCowDeaths = objectMapper.readValue(responseString, new TypeReference<List<CowDeath>>() {});
  assertEquals(expectedCowDeaths, actualCowDeaths);
}

@WithMockUser(roles = { "USER" })
@Test
public void get_cowdeaths_using_commons_id_and_user_id() throws Exception {
  ZonedDateTime someTime = ZonedDateTime.parse("2022-11-30T21:50:35.246Z[UTC]");
  CowDeath cowDeathSample = CowDeath.builder()
    .id(0)
    .avgHealth(11)
    .commonsId(1)
    .cowsKilled(9)
    .createdAt(someTime)
    .userId(100)
    .build();


  when(cowdeathRepository.getCowsKilledByCommonsIdAndUserId(1L, 100L)).thenReturn(Optional.of(cowDeathSample));

  

  MvcResult response = mockMvc.perform(get("/api/cowdeath/byusercommons?commonsId=1&userId=100").with(csrf()))
            .andExpect(status().isOk()).andReturn();
  verify(cowdeathRepository, times(1)).getCowsKilledByCommonsIdAndUserId(1L,100L);

  String responseString = response.getResponse().getContentAsString();
  CowDeath actualCowDeath = objectMapper.readValue(responseString, new TypeReference<CowDeath>() {});
  assertEquals(cowDeathSample, actualCowDeath);
}


@WithMockUser(roles = { "USER" })
@Test
public void get_cowdeath__nonexistent_using_commons_id_and_user_id() throws Exception {
  MvcResult response = mockMvc.perform(get("/api/cowdeath/byusercommons?commonsId=100000&userId=1").contentType("application/json")).andExpect(status().isNotFound()).andReturn();

  verify(cowdeathRepository, times(1)).getCowsKilledByCommonsIdAndUserId(100000L,1L);

  Map<String, Object> json = responseToJson(response);
  assertEquals("EntityNotFoundException", json.get("type"));
  assertEquals("CowDeath with commonsId 100000 and userId 1 not found", json.get("message"));
}

@WithMockUser(roles = { "ADMIN" })
@Test
public void get_cowdeath_nonexistent_using_commons_id() throws Exception {

  MvcResult response = mockMvc.perform(get("/api/cowdeath/bycommons?commonsId=20000").contentType("application/json")).andExpect(status().isOk()).andReturn();

  verify(cowdeathRepository, times(1)).getCowsKilledByCommonsId(20000L);

  String responseString = response.getResponse().getContentAsString();
  assertEquals("[]", responseString);
}
  
}
