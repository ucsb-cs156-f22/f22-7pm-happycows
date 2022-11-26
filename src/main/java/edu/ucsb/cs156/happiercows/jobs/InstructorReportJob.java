package edu.ucsb.cs156.happiercows.jobs;
import edu.ucsb.cs156.happiercows.services.jobs.JobContext;
import edu.ucsb.cs156.happiercows.services.jobs.JobContextConsumer;
import lombok.Builder;
@Builder
public class InstructorReportJob implements JobContextConsumer {
    @Override
    public void accept(JobContext ctx) throws Exception {
        ctx.log("Starting to instructor report");
    }
}
