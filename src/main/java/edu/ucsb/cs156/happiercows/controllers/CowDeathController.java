package edu.ucsb.cs156.happiercows.controllers;

import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import edu.ucsb.cs156.happiercows.models.CurrentUser;
import edu.ucsb.cs156.happiercows.entities.CowDeath;

import edu.ucsb.cs156.happiercows.repositories.CowDeathRepositories;
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

@Api(description = "")
@RequestMapping("")
@RestController
@Slf4j
public class CowDeathController extends ApiController {


    @Autowired
    CowDeathRepository cowdeathRepository;

    @ApiOperation(value = "Creates a new CowDeath entity as Admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("api/cowdeath")
    public CowDeath postCowDeath(
            @ApiParam("commonsId") @RequestParam long commonsId,
            @ApiParam("userId") @RequestParam long userId,
            @ApiParam("createdAt") @RequestParam("createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt),
            @ApiParam("cowsKilled") @RequestParam Integer cowsKilled,
            @ApiParam("avgHealth") @RequestParam long avgHealth
            throws JsonProcessingException {
        log.info("commonsId={}, userId={}, createdAt={}, cowsKilled={}, avgHealth={}", commonsId, userId, createdAt,cowsKilled, avgHealth);

        return cowdeathRepository.save(
                CowDeath.builder()
                        .commonsId(commonsId)
                        .userId(userId)
                        .createdAt(createdAt)
                        .cowsKileed(cowsKilled)
                        .avgHealth(avgHealth)
                        .build());
    }

    @ApiOperation(value = "Get the number of cow deaths of a common for admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("api/cowdeath/bycommons")
    public CowDeath getCowDeathByCommonsId_admin(
            @ApiParam("commonsId") @RequestParam Long commonsId) {
        CowDeath cowdeath = cowdeathRepository.findByCommonsId(id).orElseThrow(() -> new EntityNotFoundException(CowDeath.class, id));
        return cowdeath;
    }

    @ApiOperation(value = "Get the number of cow deaths of a user common for admin")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/cowdeath/byusercommons")
    public CowDeath getCowDeathByCommonsIdAndUserId(
            @ApiParam("commonsId") @RequestParam Long commonsId, @ApiParam("userId") @RequestParam Long userId) {
        CowDeath cowdeath = cowdeathRepository.findByCommonsId(id).orElseThrow(() -> new EntityNotFoundException(CowDeath.class, id));
        return cowdeath;
    }
}