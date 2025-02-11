package insurancepolicy.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import insurancepolicy.model.Vehicle;
import org.springframework.stereotype.Repository;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "vehicle",
        descriptionFormula = "(''||id)",
        entityClass = Vehicle.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecureVehicleRepository extends SecureRepository<Vehicle> {
}
