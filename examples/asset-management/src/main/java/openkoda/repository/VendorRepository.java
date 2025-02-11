package openkoda.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import openkoda.model.Vendor;
import org.springframework.stereotype.Repository;

import static openkoda.AssetManagementApp.VENDOR;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Vendor.class,
        entityKey = VENDOR,
        descriptionFormula = "(name)",
        searchIndexFormula = "lower(name || '' || contact_information || ' ' || products)"
)
public interface VendorRepository extends SecureRepository<Vendor> {

}
