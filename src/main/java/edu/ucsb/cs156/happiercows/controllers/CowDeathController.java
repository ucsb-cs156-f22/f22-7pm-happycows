package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import edu.ucsb.cs156.happiercows.models.CurrentUser;
import edu.ucsb.cs156.happiercows.entities.CowDeath;


import edu.ucsb.cs156.happiercows.controllers.ApiController;
import edu.ucsb.cs156.happiercows.repositories.CowDeathRepository;
import edu.ucsb.cs156.happiercows.repositories.CommonsRepository;
import edu.ucsb.cs156.happiercows.repositories.UserCommonsRepository;
import edu.ucsb.cs156.happiercows.repositories.UserRepository;

import edu.ucsb.cs156.happiercows.entities.Commons;
import edu.ucsb.cs156.happiercows.entities.CommonsPlus;
import edu.ucsb.cs156.happiercows.entities.User;
import edu.ucsb.cs156.happiercows.entities.UserCommons;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Optional;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@Api(description = "Cow Deaths")
@RequestMapping("")
@RestController
@Slf4j
public class CowDeathController extends ApiController {
  @Autowired
  CowDeathRepository cowdeathRepository;

  @Autowired
  CommonsRepository commonsRepository;

  @Autowired
  UserRepository userRepository;
  @Autowired
  ObjectMapper mapper;

  @ApiOperation(value = "Creates a new CowDeath entity as Admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/api/cowdeath")
  public CowDeath postCowDeath(
      @ApiParam("commonsId") @RequestParam long commonsId,
      @ApiParam("userId") @RequestParam long userId,
      @ApiParam("cowsKilled") @RequestParam Integer cowsKilled,
      @ApiParam("avgHealth") @RequestParam long avgHealth)
    throws JsonProcessingException {
    commonsRepository.findById(commonsId)
        .orElseThrow(() -> new EntityNotFoundException(Commons.class, commonsId));
    userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException(User.class, userId));

    log.info("commonsId={}, userId={}, cowsKilled={}, avgHealth={}", commonsId, userId, cowsKilled, avgHealth);
    return cowdeathRepository.save(CowDeath.builder()
        .commonsId(commonsId)
        .userId(userId)
        .cowsKilled(cowsKilled)
        .avgHealth(avgHealth)
        .build());
  }

  @ApiOperation(value = "Get the number of cow deaths of a common for admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/api/cowdeath/bycommons")
  public Iterable<CowDeath> getCowDeathByCommonsId_admin(
      @ApiParam("commonsId") @RequestParam Long commonsId)
    throws JsonProcessingException {
    Iterable<CowDeath> cowdeath = cowdeathRepository.getCowsKilledByCommonsId(commonsId);
    return cowdeath;
  }

  @ApiOperation(value = "Get the number of cow deaths of a user common for user")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/api/cowdeath/byusercommons")
  public CowDeath getCowDeathByCommonsIdAndUserId (
      @ApiParam("commonsId") @RequestParam Long commonsId, 
      @ApiParam("userId") @RequestParam Long userId) 
    throws JsonProcessingException {
    CowDeath cowdeath = cowdeathRepository.getCowsKilledByCommonsIdAndUserId(commonsId, userId)
        .orElseThrow(() -> new EntityNotFoundException(CowDeath.class, "commonsId", commonsId, "userId", userId));
    return cowdeath;
  }
}