package insurancepolicy.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;

@Entity
public class Agent extends OpenkodaEntity {

    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private BigDecimal commissionRate;

    public Agent() {
        super(null);
    }

    public Agent(Long organizationId) {
        super(organizationId);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;
}
