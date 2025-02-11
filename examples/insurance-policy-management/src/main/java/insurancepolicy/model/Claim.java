package insurancepolicy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Claim extends OpenkodaEntity {

    private LocalDate claimDate;
    private LocalDate claimReportedDate;
    private LocalDate claimPaidDate;
    private LocalDate claimEndDate;
    private BigDecimal claimAmount;
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
    private String description;

    public Claim(){
        super(null);
    }
    public Claim(Long organizationId) {
        super(organizationId);
    }

    @ManyToOne
    @JoinColumn(name = "policy_id", insertable = false, updatable = false)
    private Policy policy;

    @Column(name = "policy_id")
    private Long policyId;

    @ManyToMany
    @JoinTable(
            name="claim_beneficiary_label",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns =  @JoinColumn(name = "beneficiary_id"),
            joinColumns = @JoinColumn(name = "claim_beneficiary_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    @OrderColumn(name="sequence")
    private List<Beneficiary> beneficiary;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Long.class)
    @CollectionTable(name = "claim_beneficiary_label", joinColumns = @JoinColumn(name = "policy_beneficiary_id"))
    @Column(name="beneficiary_id")
    @OrderColumn(name="sequence")
    private List<Long> beneficiaryId;

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public LocalDate getClaimReportedDate() {
        return claimReportedDate;
    }

    public void setClaimReportedDate(LocalDate claimReportedDate) {
        this.claimReportedDate = claimReportedDate;
    }

    public LocalDate getClaimPaidDate() {
        return claimPaidDate;
    }

    public void setClaimPaidDate(LocalDate claimPaidDate) {
        this.claimPaidDate = claimPaidDate;
    }

    public LocalDate getClaimEndDate() {
        return claimEndDate;
    }

    public void setClaimEndDate(LocalDate claimEndDate) {
        this.claimEndDate = claimEndDate;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public List<Beneficiary> getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(List<Beneficiary> beneficiary) {
        this.beneficiary = beneficiary;
    }

    public List<Long> getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(List<Long> beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;
}
