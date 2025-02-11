package boards;

import boards.model.Project;
import boards.repository.ProjectRepository;
import com.openkoda.App;
import com.openkoda.core.customisation.CustomisationService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.openkoda", "boards"})
@ComponentScan({"com.openkoda", "boards"})
@EnableJpaRepositories({"com.openkoda", "boards.repository"})
@EntityScan({"com.openkoda", "boards.model"})
public class BoardsApp extends App {

    public static final String PROJECT = "project";
    public static final String _PROJECT = "/" + PROJECT;
    public static final String LABEL = "label";
    public static final String COMMENT = "comment";
    public static final String BOARDTASK = "boardTask";
    public static final String STATUS = "status";

    public static void main(String[] args) {
        startApp(BoardsApp.class, args);
    }

    @Inject
    ProjectRepository projectRepository;

    @Inject
    CustomisationService customisationService;

    @PostConstruct
    void init() {
        customisationService.registerOnApplicationStartListener(
                c -> {
                }
        );
    }
}
