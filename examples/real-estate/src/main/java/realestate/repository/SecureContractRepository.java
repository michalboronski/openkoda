package realestate.repository;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;
import realestate.model.Contract;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "contract",
        descriptionFormula = "('[' || id || '] ' || (select ccc.name from client ccc where ccc.id = client_id))",
        entityClass = Contract.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecureContractRepository extends SecureRepository<Contract> {

}
