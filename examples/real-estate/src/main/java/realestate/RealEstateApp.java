package realestate;

import com.openkoda.App;
import com.openkoda.controller.HtmlCRUDControllerConfigurationMap;
import com.openkoda.core.customisation.CustomisationService;
import com.openkoda.core.form.FrontendMappingDefinition;
import com.openkoda.core.form.ReflectionBasedEntityForm;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import realestate.model.Agent;
import realestate.model.Client;
import realestate.model.ContractType;
import realestate.model.Property;
import realestate.repository.SecureAgentRepository;
import realestate.repository.SecureClientRepository;
import realestate.repository.SecureContractRepository;
import realestate.repository.SecurePropertyRepository;

import static com.openkoda.core.form.FrontendMappingDefinition.createFrontendMappingDefinition;
import static com.openkoda.model.Privilege.canManageBackend;
import static com.openkoda.model.Privilege.canReadBackend;

@SpringBootApplication(scanBasePackages = {"com.openkoda", "realestate"})
@ComponentScan({"com.openkoda", "realestate"})
@EnableJpaRepositories({"com.openkoda", "realestate.repository"})
@EntityScan({"com.openkoda", "realestate.model"})
public class RealEstateApp extends App {

    public static final String VENDOR = "vendor";
    public static final String ASSET = "asset";
    public static final String MAINTENANCE = "maintenance";
    public static final String STATUS = "status";
    public static final String TYPE = "type";


    public static void main(String[] args) {
        startApp(RealEstateApp.class, args);
    }

    @Inject
    CustomisationService customisationService;
    @Inject
    SecureClientRepository clientRepository;
    @Inject
    SecureAgentRepository agentRepository;
    @Inject
    SecurePropertyRepository propertyRepository;
    @Inject
    SecureContractRepository contractRepository;

    @Inject
    HtmlCRUDControllerConfigurationMap htmlCrudControllerConfigurationMap;


    FrontendMappingDefinition agentForm = createFrontendMappingDefinition("agent", canReadBackend, canManageBackend,
            a -> a.text("name")
                    .text("phoneNumber")
    );

    FrontendMappingDefinition clientForm = createFrontendMappingDefinition("client", canReadBackend, canManageBackend,
            a -> a.text("name")
                    .text("phoneNumber")
                    .text("email")
                    .datalist("agents", d -> d.dictionary(Agent.class))
                    .dropdown("agentId", "agents"));
    FrontendMappingDefinition propertyForm = createFrontendMappingDefinition("property", canReadBackend, canManageBackend,
            a -> a
                    .textarea("address")
                    .textarea("description")
                    .text("heatingType")
                    .number("numberOfBedrooms")
                    .number("numberOfBathrooms")
                    .checkbox("hasAirConditioning")
                    .number( "livingArea")
                    .number( "plotArea")
                    .number("price")
                    .number("listingFee")
                    .date("listedDate")
                    .date("soldDate")
                    .files("filesId", (f, d) -> d.getFileDtos((Property) f.getEntity()), "image/png,image/jpeg")
                    .map("location"));
    FrontendMappingDefinition contractForm = createFrontendMappingDefinition("contract", canReadBackend, canManageBackend,
            a -> a
                    .datalist("agents", d -> d.dictionary(Agent.class))
                    .dropdown("agentId", "agents")
                    .datalist("clients", d -> d.dictionary(Client.class))
                    .dropdown("clientId", "clients")
                    .datalist("contractTypes", d -> d.enumDictionary(ContractType.values()))
                    .dropdown("contractType", "contractTypes")
                    .textarea("contractNotes")
                    .number("paymentAmount")
                    .number("feePercentage")
                    .number("feeAmount")
                    .date("dateSigned")
                    .date("startDate")
                    .date("endDate"));

    @PostConstruct
    void init() {
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                                agentForm, agentRepository, ReflectionBasedEntityForm.class)
                        .setGenericTableFields("name", "phoneNumber"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                                clientForm, clientRepository, ReflectionBasedEntityForm.class)
                        .setGenericTableFields("name", "phoneNumber", "email"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                                propertyForm, propertyRepository, ReflectionBasedEntityForm.class)
                        .setGenericTableFields("address", "description"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                                contractForm, contractRepository, ReflectionBasedEntityForm.class)
                        .setGenericTableFields("contractType", "dateSigned", "agentId", "clientId"));
    }
}
