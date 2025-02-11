package openkoda.repository;


import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import openkoda.model.Type;
import org.springframework.stereotype.Repository;

import static openkoda.AssetManagementApp.TYPE;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Type.class,
        entityKey = TYPE,
        descriptionFormula = "(type)",
        searchIndexFormula = "lower(type)"
)
public interface TypeRepository extends SecureRepository<Type> {
}
