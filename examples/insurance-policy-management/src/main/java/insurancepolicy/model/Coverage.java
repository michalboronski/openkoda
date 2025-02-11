package insurancepolicy.model;

import com.openkoda.model.common.OpenkodaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;

@Entity
public class Coverage extends OpenkodaEntity {

    @Enumerated(EnumType.STRING)
    private CoverageType coverageType;
    private String coverageDescription;
    private BigDecimal coverageAmount;

    public Coverage() {
        super(null);
    }

    public Coverage(Long organizationId) {
        super(organizationId);
    }

    public CoverageType getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(CoverageType coverageType) {
        this.coverageType = coverageType;
    }

    public String getCoverageDescription() {
        return coverageDescription;
    }

    public void setCoverageDescription(String coverageDescription) {
        this.coverageDescription = coverageDescription;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    @Formula("(NULL)")
    protected String requiredReadPrivilege;
    @Formula("(NULL)")
    protected String requiredWritePrivilege;
}
