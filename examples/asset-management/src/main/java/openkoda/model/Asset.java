package openkoda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.User;
import com.openkoda.model.common.OpenkodaEntity;
import com.openkoda.model.file.EntityWithFiles;
import com.openkoda.model.file.File;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Asset extends OpenkodaEntity implements EntityWithFiles {

    public Asset() {
        super(null);
    }

    public Asset(Long organizationId) {
        super(organizationId);
    }

    @Column
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id", updatable = false, insertable = false)
    private Type type;

    @Column(name = "type_id")
    private Long typeId;

    @Column
    private String location;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime purchaseDate;

    @Column
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id")
    private Long userId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(
            name = "file_reference",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseJoinColumns = @JoinColumn(name = "file_id"),
            joinColumns = @JoinColumn(name = "organization_related_entity_id", insertable = false, updatable = false)
    )
    @JsonIgnore
    @OrderColumn(name = "sequence")
    protected List<File> files;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Long.class)
    @CollectionTable(name = "file_reference", joinColumns = @JoinColumn(name = "organization_related_entity_id"), foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Column(name = "file_id")
    @OrderColumn(name = "sequence")
    protected List<Long> filesId = new ArrayList<>();

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime nextServiceDate;

    @Column
    private BigDecimal operatingCost;

    @Column
    private BigDecimal disposalCost;

    @Column
    private BigDecimal depreciationRate;


    @Formula("price * depreciation_rate")
    protected String depreciationValue;


    @Formula("price")
    protected String currentValue;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Long> getFilesId() {
        return filesId;
    }

    public void setFilesId(List<Long> filesId) {
        this.filesId = filesId;
    }

    public LocalDateTime getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(LocalDateTime nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public BigDecimal getOperatingCost() {
        return operatingCost;
    }

    public void setOperatingCost(BigDecimal operatingCost) {
        this.operatingCost = operatingCost;
    }

    public BigDecimal getDisposalCost() {
        return disposalCost;
    }

    public void setDisposalCost(BigDecimal disposalCost) {
        this.disposalCost = disposalCost;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getDepreciationValue() {
        return depreciationValue;
    }

    public void setDepreciationValue(String depreciationValue) {
        this.depreciationValue = depreciationValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
