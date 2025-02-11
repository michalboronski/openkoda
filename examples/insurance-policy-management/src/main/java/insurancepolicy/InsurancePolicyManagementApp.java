package insurancepolicy;

import com.openkoda.DevelopmentApp;
import com.openkoda.controller.HtmlCRUDControllerConfigurationMap;
import com.openkoda.core.customisation.CustomisationService;
import com.openkoda.core.form.FrontendMappingDefinition;
import com.openkoda.core.form.ReflectionBasedEntityForm;
import com.openkoda.repository.SecureFormRepository;
import com.openkoda.repository.SecureFrontendResourceRepository;
import com.openkoda.repository.SecureServerJsRepository;
import com.openkoda.repository.organization.SecureOrganizationRepository;
import insurancepolicy.model.*;
import insurancepolicy.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.openkoda.core.form.FrontendMappingDefinition.createFrontendMappingDefinition;
import static com.openkoda.model.Privilege.canManageBackend;
import static com.openkoda.model.Privilege.canReadBackend;


@SpringBootApplication(scanBasePackages = {"com.openkoda", "insurancepolicy"})
@ComponentScan({"com.openkoda", "insurancepolicy"})
@EnableJpaRepositories({"com.openkoda", "insurancepolicy.repository"})
@EntityScan({"com.openkoda", "insurancepolicy.model"})
public class InsurancePolicyManagementApp extends DevelopmentApp {

    public static void main(String[] args) {
        startApp(InsurancePolicyManagementApp.class, args);
    }

    @Inject
    CustomisationService customisationService;

    @Inject
    HtmlCRUDControllerConfigurationMap htmlCrudControllerConfigurationMap;
    @Inject
    SecureServerJsRepository serverJsRepository;
    @Inject
    SecureFrontendResourceRepository frontendResourceRepository;
    @Inject
    SecureOrganizationRepository secureOrganizationRepository;
    @Inject
    SecureFormRepository formRepository;
    @Inject
    SecureAgentRepository agentRepository;
    @Inject
    SecureBeneficiaryRepository beneficiaryRepository;
    @Inject
    SecureClaimRepository claimRepository;
    @Inject
    SecureCoverageRepository coverageRepository;
    @Inject
    SecureCustomerRepository customerRepository;
    @Inject
    SecurePaymentRepository paymentRepository;
    @Inject
    SecurePolicyRepository policyRepository;
    @Inject
    SecurePropertyRepository propertyRepository;
    @Inject
    SecureVehicleRepository vehicleRepository;

    FrontendMappingDefinition agentForm = createFrontendMappingDefinition("agent", canReadBackend, canManageBackend,
            a -> a.text("firstName")
                    .text("lastName")
                    .text("contactNumber")
                    .text("email")
                    .number("commissionRate"));

    FrontendMappingDefinition beneficiaryForm = createFrontendMappingDefinition("beneficiary", canReadBackend, canManageBackend,
            a -> a.text("firstName")
                    .text("lastName")
                    .text("relationship")
                    .text("contactNumber")
                    .datalist("policy", d -> d.dictionary(Policy.class))
                    .dropdown("policyId", "policy"));

    FrontendMappingDefinition claimForm = createFrontendMappingDefinition("claim", canReadBackend, canManageBackend,
            a -> a.date("claimDate")
                    .date("claimReportedDate")
                    .date("claimPaidDate")
                    .date("claimEndDate")
                    .number("claimAmount")
                    .datalist("claimStatuses", d -> d.enumDictionary(ClaimStatus.values()))
                    .dropdown("status", "claimStatuses")
                    .text("description")
                    .datalist("policy", d -> d.dictionary(Policy.class))
                    .dropdown("policyId", "policy"));

    FrontendMappingDefinition coverageForm = createFrontendMappingDefinition("coverage", canReadBackend, canManageBackend,
            a -> a.datalist("coverageTypes", d -> d.enumDictionary(CoverageType.values()))
                    .dropdown("coverageType", "coverageTypes")
                    .text("coverageDescription")
                    .number("coverageAmount"));

    FrontendMappingDefinition customerForm = createFrontendMappingDefinition("customer", canReadBackend, canManageBackend,
            a -> a.text("firstName")
                    .text("lastName")
                    .date("dateOfBirth")
                    .text("gender")
                    .text("contactNumber")
                    .text("email")
                    .text("address"));

    FrontendMappingDefinition paymentForm = createFrontendMappingDefinition("payment", canReadBackend, canManageBackend,
            a -> a.number("amount")
                    .date("paymentDueDate")
                    .date("paymentDate")
                    .datalist("paymentMethods", d -> d.enumDictionary(PaymentMethod.values()))
                    .dropdown("paymentMethod", "paymentMethods")
                    .datalist("paymentStatus", d -> d.enumDictionary(PaymentStatus.values()))
                    .dropdown("paymentStatus", "paymentStatus")
                    .datalist("policy", d -> d.dictionary(Policy.class))
                    .dropdown("policyId", "policy"));

    FrontendMappingDefinition policyForm = createFrontendMappingDefinition("policy", canReadBackend, canManageBackend,
            a -> a.text("policyName")
                    .datalist("policyTypes", d -> d.enumDictionary(PolicyType.values()))
                    .dropdown("policyType", "policyTypes")
                    .text("policyDescription")
                    .date("startDate")
                    .date("endDate")
                    .date("coverageStartDate")
                    .date("coverageEndDate")
                    .number("premium")
                    .datalist("premiumPaymentTypes", d -> d.enumDictionary(PremiumPaymentType.values()))
                    .dropdown("premiumPaymentType", "premiumPaymentTypes")
                    .text("status")
                    .datalist("customers", d -> d.dictionary(Customer.class))
                    .dropdown("customerId", "customers")
                    .datalist("agents", d -> d.dictionary(Agent.class))
                    .dropdown("agentId", "agents")
                    .datalist("coverages", d -> d.dictionary(Coverage.class))
                    .dropdown("coverage", "coverage")
                    .datalist("beneficiaries", d -> d.dictionary(Beneficiary.class))
                    .dropdown("beneficiariesId", "beneficiaries"));


    FrontendMappingDefinition propertyForm = createFrontendMappingDefinition("property", canReadBackend, canManageBackend,
            a -> a.text("address")
                    .text("city")
                    .text("state")
                    .text("zipCode")
                    .number("propertyValue")
                    .datalist("propertyTypes", d -> d.enumDictionary(PropertyType.values()))
                    .dropdown("propertyType", "propertyTypes")
    );

    FrontendMappingDefinition vehicleForm = createFrontendMappingDefinition("vehicle", canReadBackend, canManageBackend,
            a -> a.text("make")
                    .text("model")
                    .text("licensePLate")
                    .text("vin")
                    .number("year")
                    .text("color")
    );

    @PostConstruct
    void init() {
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        agentForm, agentRepository, ReflectionBasedEntityForm.class).setGenericTableFields("firstName", "lastName"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        beneficiaryForm, beneficiaryRepository, ReflectionBasedEntityForm.class).setGenericTableFields("firstName", "lastName"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        claimForm, claimRepository, ReflectionBasedEntityForm.class).setGenericTableFields("description", "claimAmount"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        coverageForm, coverageRepository, ReflectionBasedEntityForm.class).setGenericTableFields("coverageDescription", "coverageAmount"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        customerForm, customerRepository, ReflectionBasedEntityForm.class).setGenericTableFields("firstName", "lastName"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        paymentForm, paymentRepository, ReflectionBasedEntityForm.class).setGenericTableFields("paymentDate", "amount"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        policyForm, policyRepository, ReflectionBasedEntityForm.class).setGenericTableFields("policyName", "startDate", "endDate"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        propertyForm, propertyRepository, ReflectionBasedEntityForm.class).setGenericTableFields("city", "address"));
        customisationService.registerOnApplicationStartListener(
                a -> htmlCrudControllerConfigurationMap.registerAndExposeCRUDController(
                        vehicleForm, vehicleRepository, ReflectionBasedEntityForm.class).setGenericTableFields("make", "model", "licensePLate"));

    }
}
