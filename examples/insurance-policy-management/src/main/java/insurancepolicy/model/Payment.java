package insurancepolicy.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment extends OpenkodaEntity {

    private BigDecimal amount;
    private LocalDate paymentDueDate;
    private LocalDate paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "policy_id", insertable = false, updatable = false)
    private Policy policy;

    @Column(name = "policy_id")
    private Long policyId;

    public Payment() {
        super(null);
    }

    public Payment(Long organizationId) {
        super(organizationId);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(LocalDate paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
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

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;
}
