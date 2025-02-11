package openkoda.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Maintenance extends OpenkodaEntity {

    public Maintenance(Long organizationId) {
        super(organizationId);
    }

    public Maintenance() {
        super(null);
    }


    @ManyToOne
    @JoinColumn(name = "asset_id", updatable = false, insertable = false)
    private Asset asset;

    @Column(name = "asset_id")
    private Long assetId;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime serviceStartDate;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime serviceEndDate;

    @Column
    private BigDecimal costOfService;

    @ManyToOne
    @JoinColumn(name = "vendor_id", updatable = false, insertable = false)
    private Vendor vendor;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(length = 1000)
    private String description;

    @Formula("( NULL )")
    protected String requiredReadPrivilege;
    @Formula("( NULL )")
    protected String requiredWritePrivilege;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }


    public LocalDateTime getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDateTime serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDateTime getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDateTime serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public BigDecimal getCostOfService() {
        return costOfService;
    }

    public void setCostOfService(BigDecimal costOfService) {
        this.costOfService = costOfService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
