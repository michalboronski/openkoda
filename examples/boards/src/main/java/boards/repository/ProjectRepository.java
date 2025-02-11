package boards.repository;

import boards.model.Project;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;

import static boards.BoardsApp.PROJECT;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Project.class,
        entityKey = PROJECT,
        descriptionFormula = "(name)"
)
public interface ProjectRepository extends SecureRepository<Project> {
}
