package boards.repository;

import boards.model.Label;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;

import static boards.BoardsApp.LABEL;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Label.class,
        entityKey = LABEL,
        descriptionFormula = "(name)"
)
public interface LabelRepository extends SecureRepository<Label> {
}
