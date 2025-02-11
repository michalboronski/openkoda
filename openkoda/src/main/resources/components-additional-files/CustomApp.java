package custom;

import com.openkoda.App;
import com.openkoda.OpenkodaApp;
import com.openkoda.core.customisation.CustomisationService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages = {"com.openkoda", "custom"})
@ComponentScan({"com.openkoda", "custom"})
public class CustomApp extends App {

    public static void main(String[] args) {
        try {
            OpenkodaApp.startOpenkodaApp(CustomApp.class, args);
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            System.out.println(Character.toString(0x1F480) + " Application failed to start.");
            System.out.println(Character.toString(0x1F480) + " Error " + e.getMessage());
            throw new RuntimeException(e);
        }

        CustomisationService customisationService = context.getBean(CustomisationService.class);
        customisationService.registerOnApplicationStartListener(
            c -> {
                //add customizations here
            }
        );
    }

}
