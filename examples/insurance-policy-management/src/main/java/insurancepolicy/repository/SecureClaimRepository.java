package insurancepolicy.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import insurancepolicy.model.Claim;
import org.springframework.stereotype.Repository;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "claim",
        descriptionFormula = "(''||id)",
        entityClass = Claim.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecureClaimRepository extends SecureRepository<Claim> {
}
