package edu.ucsb.cs156.happiercows.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.ucsb.cs156.happiercows.entities.jobs.Job;
import edu.ucsb.cs156.happiercows.services.jobs.JobContext;

public class UpdateCowHealthJobTests {
    @Test
    void test_log_output() throws Exception {

        // Arrange

        Job jobStarted = Job.builder().build();
        JobContext ctx = new JobContext(null, jobStarted);

        // Act
        UpdateCowHealthJob updateCowHealthJob = UpdateCowHealthJob.builder()
                .build();
        updateCowHealthJob.accept(ctx);
        // Assert

        String expected = "Starting to update cow health";

        assertEquals(expected, jobStarted.getLog());

    }
}