import React from "react";
import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import JobsTable from "main/components/Jobs/JobsTable";
import { useBackend } from "main/utils/useBackend";
import Accordion from 'react-bootstrap/Accordion';
import TestJobForm from "main/components/Jobs/TestJobForm";
import JobComingSoon from "main/components/Jobs/JobComingSoon";
import InstructorReportJobForm from "main/components/Jobs/InstructorReportJobForm";
import UpdateCowHealthJobForm from "main/components/Jobs/UpdateCowHealthJobForm";
//import MilkTheCowsJobForm from "main/components/Jobs/MilkTheCowsJobForm";
import { useBackendMutation } from "main/utils/useBackend";

const AdminJobsPage = () => {

    const refreshJobsIntervalMilliseconds = 5000;

    // test job 

    const objectToAxiosParamsTestJob = (data) => ({
        url: `/api/jobs/launch/testjob?fail=${data.fail}&sleepMs=${data.sleepMs}`,
        method: "POST"
    });

    // Stryker disable all
    const testJobMutation = useBackendMutation(
        objectToAxiosParamsTestJob,
        {  },
        ["/api/jobs/all"]
    );
    // Stryker enable all

    const submitTestJob = async (data) => {
        console.log("submitTestJob, data=", data);
        testJobMutation.mutate(data);
    }

    // Stryker disable all 
    const { data: jobs, error: _error, status: _status } =
        useBackend(
            ["/api/jobs/all"],
            {
                method: "GET",
                url: "/api/jobs/all",
            },
            [],
            { refetchInterval: refreshJobsIntervalMilliseconds }
        );
    // Stryker enable  all 

    return (
        <BasicLayout>
            <h2 className="p-3">Launch Jobs</h2>
            <Accordion>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>Test Job</Accordion.Header>
                    <Accordion.Body>
                        <TestJobForm submitAction={submitTestJob} />
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey="1">
                   <Accordion.Header>Milk the Cows</Accordion.Header>
                   <Accordion.Body>
                       <JobComingSoon/>
                   </Accordion.Body>
               </Accordion.Item>
               <Accordion.Item eventKey="2">
                   <Accordion.Header>Instructor Report</Accordion.Header>
                   <Accordion.Body>
                       <InstructorReportJobForm submitAction={submitTestJob} />
                   </Accordion.Body>
               </Accordion.Item>
               <Accordion.Item eventKey="3">
                   <Accordion.Header>Update Cow Health</Accordion.Header>
                   <Accordion.Body>
                       <UpdateCowHealthJobForm submitAction={submitTestJob} />
                   </Accordion.Body>
               </Accordion.Item>
            </Accordion>

            <h2 className="p-3">Job Status</h2>

            <JobsTable jobs={jobs} />
        </BasicLayout>
    );
};

export default AdminJobsPage;
