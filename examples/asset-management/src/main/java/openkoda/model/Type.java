package openkoda.model;

import com.openkoda.model.PrivilegeNames;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.Formula;

@Entity
public class Type extends OpenkodaEntity {

    @Column
    private String type;


    @Formula("( NULL )")
    protected String requiredReadPrivilege;
    @Formula("( NULL )")
    protected String requiredWritePrivilege;

    public Type(Long organizationId) {
        super(organizationId);
    }

    public Type() {
        super(null);
    }

    public Type(String type) {
        super(null);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Type{");
        sb.append("type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
