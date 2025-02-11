package boards.repository;

import boards.model.Comment;
import com.openkoda.model.common.SearchableRepositoryMetadata;
import com.openkoda.repository.SecureRepository;
import org.springframework.stereotype.Repository;

import static boards.BoardsApp.COMMENT;

@Repository
@SearchableRepositoryMetadata(
        entityClass = Comment.class,
        entityKey = COMMENT,
        descriptionFormula = "(content)"
)
public interface CommentRepository extends SecureRepository<Comment> {
}
