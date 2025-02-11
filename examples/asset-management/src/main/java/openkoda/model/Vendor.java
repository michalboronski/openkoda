package openkoda.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

@Entity
public class Vendor extends OpenkodaEntity {

    public Vendor(Long organizationId) {
        super(organizationId);
    }

    public Vendor() {
        super(null);
    }

    public Vendor(String name, String contactInformation, String products) {
        super(null);
        this.name = name;
        this.contactInformation = contactInformation;
        this.products = products;
    }

    @Column
    private String name;

    @Column
    private String contactInformation;

    @Column
    private String products;

    @Formula("( NULL )")
    protected String requiredReadPrivilege;
    @Formula("( NULL )")
    protected String requiredWritePrivilege;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
