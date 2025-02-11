package boards.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

@Entity
public class Label extends OpenkodaEntity {
    public Label(Long organizationId) {
        super(organizationId);
    }

    public Label() {
        super(null);
    }

    private String name;

    @Formula("( NULL )")
    protected String requiredReadPrivilege;
    @Formula("( NULL )")
    protected String requiredWritePrivilege;

    @Override
    public String getRequiredReadPrivilege() {
        return requiredReadPrivilege;
    }

    @Override
    public String getRequiredWritePrivilege() {
        return requiredWritePrivilege;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
