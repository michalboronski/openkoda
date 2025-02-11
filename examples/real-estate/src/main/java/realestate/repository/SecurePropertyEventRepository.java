package realestate.repository;

import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;
import realestate.model.PropertyEvent;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "propertyEvent",
        descriptionFormula = "(''||id)",
        entityClass = PropertyEvent.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecurePropertyEventRepository extends SecureRepository<PropertyEvent> {

}
