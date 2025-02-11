package insurancepolicy.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;

@Entity
public class Customer extends OpenkodaEntity {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String contactNumber;
    private String email;
    private String address;

    public Customer() {
        super(null);
    }

    public Customer(Long organizationId) {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

}
