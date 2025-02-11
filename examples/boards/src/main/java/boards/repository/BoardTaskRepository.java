package boards.repository;

import boards.model.BoardTask;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;

import static boards.BoardsApp.BOARDTASK;

@Repository
@SearchableRepositoryMetadata(
        entityClass = BoardTask.class,
        entityKey = BOARDTASK,
        descriptionFormula = "(name)"
)
public interface BoardTaskRepository extends SecureRepository<BoardTask> {
}
