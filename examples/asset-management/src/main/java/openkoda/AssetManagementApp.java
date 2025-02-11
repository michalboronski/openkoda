package openkoda;

import com.openkoda.App;
import com.openkoda.core.customisation.CustomisationService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.openkoda", "openkoda"})
@ComponentScan({"com.openkoda", "openkoda"})
@EnableJpaRepositories({"com.openkoda", "openkoda.repository"})
@EntityScan({"com.openkoda", "openkoda.model"})
public class AssetManagementApp extends App {

    public static final String VENDOR = "vendor";
    public static final String ASSET = "asset";
    public static final String MAINTENANCE = "maintenance";
    public static final String STATUS = "status";
    public static final String TYPE = "type";


    public static void main(String[] args) {
        startApp(AssetManagementApp.class, args);
    }

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
