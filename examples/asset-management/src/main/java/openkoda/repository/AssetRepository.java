package openkoda.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import openkoda.model.Asset;
import openkoda.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static openkoda.AssetManagementApp.ASSET;

@Repository
@SearchableRepositoryMetadata(
        entityKey = ASSET,
        descriptionFormula =  "(name||' '||type_id)",
        entityClass = Asset.class,
        searchIndexFormula = "lower(id || '' || name || ' ' || status)"
)
public interface AssetRepository extends SecureRepository<Asset> {

    @Query("SELECT COUNT(*) FROM Asset a WHERE a.status = :status AND a.organizationId = :organizationId")
    BigDecimal getCountByStatus(@Param("status") Status status, @Param("organizationId") Integer organizationId);

    @Query("SELECT SUM(a.price) FROM Asset a WHERE a.organizationId = :organizationId")
    BigDecimal getPriceSum(@Param("organizationId") Integer organizationId);

    @Query("SELECT SUM(a.price * a.depreciationRate) FROM Asset a WHERE a.organizationId = :organizationId")
    BigDecimal getTotalDepreciation(@Param("organizationId") Integer organizationId);

    @Query("SELECT AVG(a.price * a.depreciationRate) FROM Asset a WHERE a.organizationId = :organizationId")
    BigDecimal getAvgDepreciation(@Param("organizationId") Integer organizationId);

    @Query("SELECT userId,count(*) assets FROM Asset a WHERE a.organizationId = :organizationId GROUP BY userId")
    List getAssetsPerEmployee(@Param("organizationId") Integer organizationId);

    @Query(value = "SELECT date_part('year',purchase_date), date_part('month',purchase_date), SUM(price * depreciation_rate), AVG(price * depreciation_rate) FROM Asset WHERE purchase_date > date_trunc('month', now()) - interval '11 month' AND organization_id = :organizationId GROUP BY date_part('year',purchase_date), date_part('month',purchase_date) ORDER BY date_part('year',purchase_date),  date_part('month',purchase_date)",
            nativeQuery = true)
    List getDepreciationByMonth(@Param("organizationId") Integer organizationId);


}
