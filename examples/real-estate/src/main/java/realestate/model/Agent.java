package realestate.model;

import com.openkoda.model.PrivilegeNames;
import com.openkoda.model.common.ModelConstants;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
public class Agent extends OpenkodaEntity {

    private String name; // The agent's name.
    private String phoneNumber; // The agent's phone number.

    public Agent(Long organizationId) {
        super(organizationId);
    }

    public Agent() {
        super(null);
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

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


}
