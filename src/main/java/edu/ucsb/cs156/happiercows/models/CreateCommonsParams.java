package edu.ucsb.cs156.happiercows.models;

import java.time.LocalDateTime;
import java.util.Collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.DateTimeFormat;



@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateCommonsParams {
  private String name;
  @NumberFormat
  private double cowPrice;
  @NumberFormat
  private double milkPrice;
  @NumberFormat
  private double startingBalance;
  @Builder.Default
  @NumberFormat
  private Integer carryingCapacity = 100;
  @NumberFormat private double degradationRate;
  @DateTimeFormat
  private LocalDateTime startingDate;
  @DateTimeFormat 
  private LocalDateTime endingDate;
  @Builder.Default
  private Boolean showLeaderboard = false; 
}
