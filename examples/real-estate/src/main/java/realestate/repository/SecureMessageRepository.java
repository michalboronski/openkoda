package realestate.repository;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;
import realestate.model.Message;

import static com.openkoda.model.common.ModelConstants.DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA;

@Repository
@SearchableRepositoryMetadata(
        entityKey = "message",
        descriptionFormula = "(''||id)",
        entityClass = Message.class,
        searchIndexFormula = DEFAULT_ORGANIZATION_RELATED_REFERENCE_FIELD_FORMULA
)
public interface SecureMessageRepository extends SecureRepository<Message> {

}
