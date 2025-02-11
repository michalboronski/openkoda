package boards.repository;

import boards.model.Status;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static boards.BoardsApp.STATUS;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Status.class,
        entityKey = STATUS,
        descriptionFormula = "(name)"
)
public interface StatusRepository extends SecureRepository<Status> {


    @Query(nativeQuery = true, value =
            "select * from status s where s.project_id = :projectId"
    )
    List<Status> findAllByProjectId(@Param("projectId") String projectId);
}
