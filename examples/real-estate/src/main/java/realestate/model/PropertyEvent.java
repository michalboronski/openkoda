package realestate.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;

@Entity
public class PropertyEvent  extends OpenkodaEntity {
    public PropertyEvent(Long organizationId) {
        super(organizationId);
    }

    public PropertyEvent() {
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

    @ManyToOne
    @JoinColumn(name = "property_id", insertable = false, updatable = false)
    private Property property; // The agent assigned to this client.

    @Column(name = "property_id")
    private Long propertyId;

    private LocalDate date; // The date of the event.
    private Integer offeredPrice; // The price the client offered for the property, if applicable.
    private PropertyEventType eventType; // The type of event.
    private String notes;

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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(Integer offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public PropertyEventType getEventType() {
        return eventType;
    }

    public void setEventType(PropertyEventType eventType) {
        this.eventType = eventType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
// Constructor, getters and setters are omitted for brevity.
}
