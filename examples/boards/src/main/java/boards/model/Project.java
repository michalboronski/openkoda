package boards.model;

import com.openkoda.model.User;
import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project extends OpenkodaEntity {

    public Project(Long organizationId) {
        super(organizationId);
    }

    public Project() {
        super(null);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="project_user",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns =  @JoinColumn(name = "user_id"),
            joinColumns = @JoinColumn(name = "project_id", insertable = false, updatable = false)
    )
    protected List<User> users;

    @CreatedDate
    @Column(
            name = "created_on",
            columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false
    )
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )
    protected LocalDateTime createdOn;

    @Column
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
