package boards.api;

import boards.model.Project;
import com.openkoda.controller.api.CRUDApiController;
import com.openkoda.controller.common.URLConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static boards.BoardsApp.PROJECT;
import static boards.BoardsApp._PROJECT;
import static com.openkoda.controller.common.URLConstants.*;

@RestController
@RequestMapping({_API_V2_ORGANIZATION_ORGANIZATIONID + _PROJECT, _API_V2 + _PROJECT})
public class BoardsControllerApi extends CRUDApiController<Project> {

    public BoardsControllerApi() {
        super(PROJECT);
    }
}
