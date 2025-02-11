package realestate.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;

@Entity
public class Message  extends OpenkodaEntity {

    public Message(Long organizationId) {
        super(organizationId);
    }

    public Message() {
        super(null);
    }
    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

    private String text; // Message text between agent and client.
    private LocalDate date; // Date of the message.
    private MessageSide side; // The side that sent the message (CLIENT or AGENT).

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

    @ManyToOne
    @JoinColumn(name = "property_id", insertable = false, updatable = false)
    private Property property; // The agent assigned to this client.

    @Column(name = "property_id")
    private Long propertyId;

    @ManyToOne
    @JoinColumn(name = "contract_id", insertable = false, updatable = false)
    private Contract contract; // The agent assigned to this client.

    @Column(name = "contract_id")
    private Long contractId;


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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public MessageSide getSide() {
        return side;
    }

    public void setSide(MessageSide side) {
        this.side = side;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    // Constructor, getters, and setters are omitted for brevity.
}
