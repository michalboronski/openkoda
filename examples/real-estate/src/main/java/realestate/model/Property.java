package realestate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openkoda.model.common.OpenkodaEntity;
import com.openkoda.model.file.EntityWithFiles;
import com.openkoda.model.file.File;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Property  extends OpenkodaEntity implements EntityWithFiles {
    public Property(Long organizationId) {
        super(organizationId);
    }

    public Property() {
        super(null);
    }
    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;

    private String address; // The property's address.
    private String description; // A description of the property.
    private String heatingType; // The type of heating in the property (e.g., "central", "electric").
    private Integer numberOfBedrooms; // The number of bedrooms in the property.
    private Integer numberOfBathrooms; // The number of bathrooms in the property.
    private Boolean hasAirConditioning; // Whether the property has air conditioning.
    private Integer livingArea; // The size of the floor space in square meters
    private Integer plotArea; // The size of the plot in square meters
    private BigDecimal price; // The property's price.
    private BigDecimal listingFee; // The fee for listing the property for sale.
    private LocalDate listedDate; // The date when the property was listed for sale.
    private LocalDate soldDate; // The date when the property was sold.

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client; // The agent assigned to this client.

    @Column(name = "client_id")
    private Long clientId;
    @ManyToOne
    @JoinColumn(name = "agent_id", insertable = false, updatable = false)
    private Agent agent; // The agent assigned to this client.

    @Column(name = "agent_id")
    private Long agentId;


    private PropertyStatus status; // The current status of the property in the sales process.
    private RealEstateType realEstateType; // The type of the real estate.
    private String notes; // Additional notes or comments about the property.

    private String location = "POINT (51.10823409341639 17.035232327705767)";

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

    @Override
    public List<File> getFiles() {
        return files;
    }

    @Override
    public List<Long> getFilesId() {
        return filesId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeatingType() {
        return heatingType;
    }

    public void setHeatingType(String heatingType) {
        this.heatingType = heatingType;
    }

    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(Integer numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(Integer numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public Boolean getHasAirConditioning() {
        return hasAirConditioning;
    }

    public void setHasAirConditioning(Boolean hasAirConditioning) {
        this.hasAirConditioning = hasAirConditioning;
    }

    public Integer getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(Integer livingArea) {
        this.livingArea = livingArea;
    }

    public Integer getPlotArea() {
        return plotArea;
    }

    public void setPlotArea(Integer plotArea) {
        this.plotArea = plotArea;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getListingFee() {
        return listingFee;
    }

    public void setListingFee(BigDecimal listingFee) {
        this.listingFee = listingFee;
    }

    public LocalDate getListedDate() {
        return listedDate;
    }

    public void setListedDate(LocalDate listedDate) {
        this.listedDate = listedDate;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public RealEstateType getRealEstateType() {
        return realEstateType;
    }

    public void setRealEstateType(RealEstateType realEstateType) {
        this.realEstateType = realEstateType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setFilesId(List<Long> filesId) {
        this.filesId = filesId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    // Constructor, getters, and setters are omitted for brevity.
}
