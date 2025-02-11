package realestate.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Formula;

@Entity
public class Client  extends OpenkodaEntity {

    public Client(Long organizationId) {
        super(organizationId);
    }

    public Client() {
        super(null);
    }
    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

    private String name; // The client's name.
    private String phoneNumber; // The client's phone number.
    private String email; // The client's email address.


    @ManyToOne
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    private Agent agent; // The agent assigned to this client.

    @Column(name = "agent_id")
    private Long agentId;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
