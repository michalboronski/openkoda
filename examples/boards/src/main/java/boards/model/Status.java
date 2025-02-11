package boards.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


@Entity
public class Status extends OpenkodaEntity implements Comparable<Status> {

    public Status(Long organizationId) {
        super(organizationId);
    }

    public Status() {
        super(null);
    }

    @ManyToOne
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;
    @Column(name = "project_id")
    private Long projectId;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private Collection<BoardTask> boardTasks;

    private String name;

    @Column(name = "ordinal", nullable = false, columnDefinition = "integer default 10")
    private Integer ordinal;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Collection<BoardTask> getBoardTasks() {
        return boardTasks;
    }

    public void setBoardTasks(Collection<BoardTask> boardTasks) {
        this.boardTasks = boardTasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public int compareTo(@NotNull Status o) {
        if (o.ordinal > this.ordinal) {
            return -1;
        } else if (o.ordinal < this.ordinal) {
            return 1;
        }
        return 0;
    }
}
