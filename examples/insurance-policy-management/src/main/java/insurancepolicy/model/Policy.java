package insurancepolicy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Policy extends OpenkodaEntity {


    private String policyName;
    @Enumerated(EnumType.STRING)
    private PolicyType policyType;
    private String policyDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate coverageStartDate;
    private LocalDate coverageEndDate;
    private BigDecimal premium;
    @Enumerated(EnumType.STRING)
    private PremiumPaymentType premiumPaymentType;
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    private Agent agent;

    @Column(name = "agent_id")
    private Long agentId;

    @OneToOne
    @JoinColumn(name = "coverage_id")
    private Coverage coverage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="policy_beneficiary_label",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns =  @JoinColumn(name = "beneficiary_id"),
            joinColumns = @JoinColumn(name = "policy_beneficiary_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    @OrderColumn(name="sequence")
    protected List<Beneficiary> beneficiaries;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Long.class)
    @CollectionTable(name = "policy_beneficiary_label", joinColumns = @JoinColumn(name = "policy_beneficiary_id"))
    @Column(name="beneficiary_id")
    @OrderColumn(name="sequence")
    protected List<Long> beneficiariesId = new ArrayList<>();


    public Policy() {
        super(null);
    }

    public Policy(Long organizationId) {
        super(organizationId);
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public void setPolicyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getCoverageStartDate() {
        return coverageStartDate;
    }

    public void setCoverageStartDate(LocalDate coverageStartDate) {
        this.coverageStartDate = coverageStartDate;
    }

    public LocalDate getCoverageEndDate() {
        return coverageEndDate;
    }

    public void setCoverageEndDate(LocalDate coverageEndDate) {
        this.coverageEndDate = coverageEndDate;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public PremiumPaymentType getPremiumPaymentType() {
        return premiumPaymentType;
    }

    public void setPremiumPaymentType(PremiumPaymentType premiumPaymentType) {
        this.premiumPaymentType = premiumPaymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

}
