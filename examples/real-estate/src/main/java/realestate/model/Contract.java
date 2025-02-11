package realestate.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Contract  extends OpenkodaEntity {

    public Contract(Long organizationId) {
        super(organizationId);
    }

    public Contract() {
        super(null);
    }
    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client; // The agent assigned to this client.

    @Column(name = "client_id")
    private Long clientId;


    @ManyToOne
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    private Agent agent; // The agent assigned to this client.

    @Column(name = "agent_id")
    private Long agentId;

    private ContractType contractType; // References the contract_type dictionary and denotes if the contract relates to buying, selling, leasing, or renting property.
    private String contractNotes; // A detailed description of the contact, stored in text format.
    private BigDecimal paymentAmount; // The total amount paid.
    private BigDecimal feePercentage; // The percentage we charge the client. For example, we might charge 5% of a house’s sale price as a fee.
    private BigDecimal feeAmount; // The total fee amount we’ll charge the client for this contract.
    private LocalDate dateSigned; // The date when the contract was signed.
    private LocalDate startDate; // The date when the contract becomes valid (e.g., for a rental or lease contract).
    private LocalDate endDate; // The date when the contract expires. It can be NULL.

    @Override
    public String getRequiredReadPrivilege() {
        return requiredReadPrivilege;
    }

    public void setRequiredReadPrivilege(String requiredReadPrivilege) {
        this.requiredReadPrivilege = requiredReadPrivilege;
    }

    @Override
    public String getRequiredWritePrivilege() {
        return requiredWritePrivilege;
    }

    public void setRequiredWritePrivilege(String requiredWritePrivilege) {
        this.requiredWritePrivilege = requiredWritePrivilege;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public String getContractNotes() {
        return contractNotes;
    }

    public void setContractNotes(String contractNotes) {
        this.contractNotes = contractNotes;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getFeePercentage() {
        return feePercentage;
    }

    public void setFeePercentage(BigDecimal feePercentage) {
        this.feePercentage = feePercentage;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public LocalDate getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(LocalDate dateSigned) {
        this.dateSigned = dateSigned;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    // Constructor, getters, and setters are omitted for brevity.
}
