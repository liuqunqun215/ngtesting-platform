package com.ngtesting.platform.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tst_run")
public class TestRun extends BaseEntity {
    private static final long serialVersionUID = 8005561488367274306L;

    private String name;
	private Integer estimate;

    @Enumerated(EnumType.STRING)
    private RunStatus status = RunStatus.not_start;

	@Column(name = "descr", length = 1000)
    private String descr;

    private Integer ordr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_project_id", insertable = false, updatable = false)
    private TestProject caseProject;
    @Column(name = "case_project_id")
    private Long caseProjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private TestProject project;

    @Column(name = "project_id")
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private TestUser user;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", insertable = false, updatable = false)
    private TestPlan plan;

    @Column(name = "plan_id")
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "env_id", insertable = false, updatable = false)
    private TestEnv env;
    @Column(name = "env_id")
    private Long envId;

    @OneToMany(mappedBy="run", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    private List<TestCaseInRun> testcases = new LinkedList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "tst_r_run_assignee", joinColumns = {
            @JoinColumn(name = "run_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "assignee_id",
                    nullable = false, updatable = false) })
    private Set<TestUser> assignees = new HashSet(0);

    public static enum RunStatus {
        not_start("not_start"),
        in_progress("in_progress"),
        end("end");

        RunStatus(String val) {
            this.val = val;
        }

        private String val;
        public String toString() {
            return val;
        }
    }

    public Long getCaseProjectId() {
        return caseProjectId;
    }

    public void setCaseProjectId(Long caseProjectId) {
        this.caseProjectId = caseProjectId;
    }

    public TestProject getCaseProject() {
        return caseProject;
    }

    public void setCaseProject(TestProject caseProject) {
        this.caseProject = caseProject;
    }

    public TestUser getUser() {
        return user;
    }

    public void setUser(TestUser user) {
        this.user = user;
    }

    public TestEnv getEnv() {
        return env;
    }

    public void setEnv(TestEnv env) {
        this.env = env;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    public Set<TestUser> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<TestUser> assignees) {
        this.assignees = assignees;
    }

//    public TestUser getUser() {
//        return user;
//    }
//
//    public void setUser(TestUser user) {
//        this.user = user;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<TestCaseInRun> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<TestCaseInRun> testcases) {
        this.testcases = testcases;
    }

    public Integer getOrdr() {
        return ordr;
    }

    public void setOrdr(Integer ordr) {
        this.ordr = ordr;
    }

    public TestPlan getPlan() {
        return plan;
    }

    public void setPlan(TestPlan plan) {
        this.plan = plan;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEstimate() {
        return estimate;
    }

    public void setEstimate(Integer estimate) {
        this.estimate = estimate;
    }

    public RunStatus getStatus() {
        return status;
    }

    public void setStatus(RunStatus status) {
        this.status = status;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public TestProject getProject() {
        return project;
    }

    public void setProject(TestProject project) {
        this.project = project;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


}
