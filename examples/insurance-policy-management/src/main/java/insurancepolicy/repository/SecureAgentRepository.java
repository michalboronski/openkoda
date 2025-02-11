package insurancepolicy.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import insurancepolicy.model.Agent;
import org.springframework.stereotype.Repository;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "agent",
        descriptionFormula = "(''||id)",
        entityClass = Agent.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecureAgentRepository extends SecureRepository<Agent> {
}
