package openkoda.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import openkoda.model.Maintenance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static openkoda.AssetManagementApp.MAINTENANCE;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Maintenance.class,
        entityKey = MAINTENANCE
)
public interface MaintenanceRepository extends SecureRepository<Maintenance> {

    @Query("SELECT SUM(costOfService) FROM Maintenance WHERE serviceStartDate < CURRENT_DATE AND serviceEndDate > CURRENT_DATE AND organizationId = :organizationId")
    BigDecimal getMaintenanceCosts(@Param("organizationId") Integer organizationId);

    @Query(value = "SELECT date_part('year',service_start_date), date_part('month',service_start_date), SUM(cost_of_service) FROM maintenance WHERE organization_id = :organizationId AND service_start_date > date_trunc('month', now()) - interval '11 month' GROUP BY date_part('year',service_start_date), date_part('month',service_start_date) ORDER BY date_part('year',service_start_date),  date_part('month',service_start_date)",
            nativeQuery = true)
      List getMaintenanceCostsByMonth(@Param("organizationId") Integer organizationId);
}
