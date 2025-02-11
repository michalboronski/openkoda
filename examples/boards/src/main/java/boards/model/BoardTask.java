package boards.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.common.OpenkodaEntity;
import com.openkoda.model.file.EntityWithFiles;
import com.openkoda.model.file.File;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BoardTask extends OpenkodaEntity implements EntityWithFiles {

    public BoardTask(Long organizationId) {
        super(organizationId);
    }

    public BoardTask() {
        super(null);
    }

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private Status status;
    @Column(name = "status_id")
    private Long statusId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="board_task_label",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns =  @JoinColumn(name = "label_id"),
            joinColumns = @JoinColumn(name = "board_task_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    @OrderColumn(name="sequence")
    protected List<Label> labels;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Long.class)
    @CollectionTable(name = "board_task_label", joinColumns = @JoinColumn(name = "board_task_id"))
    @Column(name="label_id")
    @OrderColumn(name="sequence")
    protected List<Long> labelsId = new ArrayList<>();

//    TODO users
//    protected List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(
            name = "board_task_file",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns = @JoinColumn(name = "file_id"),
            joinColumns = @JoinColumn(name = "board_task_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    @OrderColumn(name = "sequence")
    protected List<File> files;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Long.class)
    @CollectionTable(name = " board_task_file", joinColumns = @JoinColumn(name = "board_task_id"), foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Column(name = "file_id")
    @OrderColumn(name = "sequence")
    protected List<Long> filesId = new ArrayList<>();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dueDate;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Long> getLabelsId() {
        return labelsId;
    }

    public void setLabelsId(List<Long> labelsId) {
        this.labelsId = labelsId;
    }

    @Override
    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public List<Long> getFilesId() {
        return filesId;
    }

    public void setFilesId(List<Long> filesId) {
        this.filesId = filesId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
