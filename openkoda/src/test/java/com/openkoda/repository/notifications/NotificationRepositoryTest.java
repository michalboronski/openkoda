package com.openkoda.repository.notifications;

import com.openkoda.model.*;
import com.openkoda.model.notification.Notification;
import com.openkoda.repository.organization.OrganizationRepository;
import com.openkoda.repository.specifications.NotificationSepcifications;
import com.openkoda.repository.user.RoleRepository;
import com.openkoda.repository.user.UserRepository;
import com.openkoda.repository.user.UserRoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.openkoda.model.notification.Notification.NotificationType.PRIMARY;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"SimplifyStreamApiCallChains", "FieldCanBeLocal"})
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private static boolean testDataInitialized = false;
    private static User gregor, frank, kate;
    private static Role globalAdmin, orgAdmin, orgManager, globalUser, orgUser, orgGuest;
    private static Organization rd, hr;

    @BeforeEach
    void setUp() {
        if (testDataInitialized) return;

        gregor = userRepository.save(new User("grzegorz", "brzeczyszczykiewicz", "gb@fb.com"));
        frank = userRepository.save(new User("franek", "dolas", "fd@fb.com"));
        kate = userRepository.save(new User("kate", "kowalsky", "kk@fb.com"));

        globalAdmin = roleRepository.save((Role) new GlobalRole("ADMIN"));
        globalUser = roleRepository.save((Role) new GlobalRole("USER"));
        orgAdmin = roleRepository.save((Role) new OrganizationRole("ADMIN"));
        orgUser = roleRepository.save((Role) new OrganizationRole("USER"));
        orgManager = roleRepository.save((Role) new OrganizationRole("MANAGER"));
        orgGuest = roleRepository.save((Role) new OrganizationRole("GUEST"));

        rd = organizationRepository.save(new Organization("R&D"));
        hr = organizationRepository.save(new Organization("HR"));

        UserRole gregorIsAdmin = new UserRole(10L, gregor.getId(), globalAdmin.getId());
        UserRole gregorIsUser = new UserRole(11L, gregor.getId(), globalUser.getId());
        UserRole gregorIsGuestInRd = new UserRole(12L, gregor.getId(), orgGuest.getId(), rd.getId());
        UserRole gregorIsGuestInHr = new UserRole(13L, gregor.getId(), orgGuest.getId(), hr.getId());

        UserRole frankIsUser = new UserRole(20L, frank.getId(), globalUser.getId());
        UserRole frankIsAdminInHr = new UserRole(21L, frank.getId(), orgAdmin.getId(), hr.getId());
        UserRole frankIsGuestInRd = new UserRole(22L, frank.getId(), orgGuest.getId(), rd.getId());

        UserRole kateIsManagerInHr = new UserRole(30L, kate.getId(), orgManager.getId(), hr.getId());
        UserRole kateIsGuestInRd = new UserRole(32L, kate.getId(), orgGuest.getId(), rd.getId());

        userRoleRepository.saveAll(List.of(
                gregorIsAdmin, gregorIsUser, gregorIsGuestInRd, gregorIsGuestInHr,
                frankIsUser, frankIsAdminInHr, frankIsGuestInRd,
                kateIsManagerInHr, kateIsGuestInRd
        ));

        // global
        notificationRepository.save(new Notification("[GLOBAL] invitation to quarterly company meeting", PRIMARY, "ALL"));
        notificationRepository.save(new Notification("[GLOBAL] feedback request on recent team event", PRIMARY, "ALL"));
        notificationRepository.save(new Notification("[GLOBAL] new remote work guidelines", PRIMARY, "ALL"));

        // user
        notificationRepository.save(new Notification("[USER][G] R&D: results of recent project audit", PRIMARY, "ALL", gregor.getId()));
        notificationRepository.save(new Notification("[USER][G] security training completion reminder", PRIMARY, "ALL", gregor.getId()));

        notificationRepository.save(new Notification("[USER][F] HR: reminder to complete annual review", PRIMARY, "ALL", frank.getId()));
        notificationRepository.save(new Notification("[USER][F] scheduled maintenance of HR systems", PRIMARY, "ALL", frank.getId()));

        notificationRepository.save(new Notification("[USER][K] new task assigned to HR team", PRIMARY, "ALL", kate.getId()));
        notificationRepository.save(new Notification("[USER][K] upcoming leadership training for managers", PRIMARY, "ALL", kate.getId()));

        // organization
        notificationRepository.save(new Notification("[ORG][RD] R&D: new research guidelines issued", PRIMARY, rd.getId(), "ALL"));
        notificationRepository.save(new Notification("[ORG][RD] mandatory training for R&D staff on new tools", PRIMARY, rd.getId(), "ALL"));
        notificationRepository.save(new Notification("[ORG][HR] HR policy on remote work updated", PRIMARY, hr.getId(), "ALL"));
        notificationRepository.save(new Notification("[ORG][HR] update on HR compliance requirements", PRIMARY, hr.getId(), "ALL"));

        // global role
        notificationRepository.save(new Notification("[ROLE GLOBAL][ADMIN] team meeting", PRIMARY, null, "ALL", globalAdmin.getId()));
        notificationRepository.save(new Notification("[ROLE GLOBAL][USER] team journey", PRIMARY, null, "ALL", globalUser.getId()));

        // role in organization
        notificationRepository.save(new Notification("[ROLE IN ORG][RD][ORG][ADMIN] R&D project documentation update", PRIMARY, rd.getId(), "ALL", orgAdmin.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][RD][ORG][ADMIN] mandatory R&D project audit", PRIMARY, rd.getId(), "ALL", orgAdmin.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][RD][ORG][MANAGER] new safety protocol in R&D", PRIMARY, rd.getId(), "ALL", orgManager.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][RD][ORG][MANAGER] new R&D resource allocation guidelines", PRIMARY, rd.getId(), "ALL", orgManager.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][RD][ORG][GUEST] upcoming HR orientation session", PRIMARY, rd.getId(), "ALL", orgGuest.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][HR][ORG][GUEST] HR access restrictions update", PRIMARY, hr.getId(), "ALL", orgGuest.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][HR][ORG][ADMIN] changes to HR compliance forms", PRIMARY, hr.getId(), "ALL", orgAdmin.getId()));
        notificationRepository.save(new Notification("[ROLE IN ORG][HR][ORG][MANAGER] update in HR policies", PRIMARY, hr.getId(), "ALL", orgManager.getId()));

        testDataInitialized = true;
    }

    @Test
    public void finds_global_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(null, null, null, null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(null, emptySet(), emptySet());
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_organization_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(null, null, Set.of(hr.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(null, emptySet(), Set.of(hr.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_multiple_organizations_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(null, null, Set.of(hr.getId(), rd.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(null, emptySet(), Set.of(hr.getId(), rd.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ORG][RD] R&D: new research guidelines issued",
                        "[ORG][RD] mandatory training for R&D staff on new tools"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_user_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(kate.getId(), null, null, null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(kate.getId(), emptySet(), emptySet());
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][K] new task assigned to HR team",
                        "[USER][K] upcoming leadership training for managers"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_user_and_single_global_role_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(gregor.getId(), Set.of(globalUser.getId()), null, null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(gregor.getId(), Set.of(globalUser.getId()), emptySet());
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][G] R&D: results of recent project audit",
                        "[USER][G] security training completion reminder",
                        "[ROLE GLOBAL][USER] team journey"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_user_and_multiple_global_roles_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(gregor.getId(), Set.of(globalUser.getId(), globalAdmin.getId()), null, null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(gregor.getId(), Set.of(globalUser.getId(), globalAdmin.getId()), emptySet());
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][G] R&D: results of recent project audit",
                        "[USER][G] security training completion reminder",
                        "[ROLE GLOBAL][USER] team journey",
                        "[ROLE GLOBAL][ADMIN] team meeting"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_user_and_single_organization_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(frank.getId(), null, Set.of(hr.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(frank.getId(), emptySet(), Set.of(hr.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][F] HR: reminder to complete annual review",
                        "[USER][F] scheduled maintenance of HR systems",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_user_and_multiple_organization_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(frank.getId(), null, Set.of(hr.getId(), rd.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(frank.getId(), emptySet(), Set.of(hr.getId(), rd.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][F] HR: reminder to complete annual review",
                        "[USER][F] scheduled maintenance of HR systems",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ORG][RD] R&D: new research guidelines issued",
                        "[ORG][RD] mandatory training for R&D staff on new tools"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_organization_user_role_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(frank.getId(), Set.of(orgAdmin.getId()), Set.of(hr.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(frank.getId(), Set.of(orgAdmin.getId()), Set.of(hr.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][F] HR: reminder to complete annual review",
                        "[USER][F] scheduled maintenance of HR systems",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ROLE IN ORG][HR][ORG][ADMIN] changes to HR compliance forms"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Disabled("Returns not expected notifications that are either dedicated to gregor or to no one (regarding current users and roles)")
    @Test
    public void finds_multiple_organizations_user_roles_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(frank.getId(), Set.of(orgAdmin.getId(), orgGuest.getId()), Set.of(hr.getId(), rd.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(frank.getId(), Set.of(orgAdmin.getId(), orgGuest.getId()), Set.of(hr.getId(), rd.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[USER][F] HR: reminder to complete annual review",
                        "[USER][F] scheduled maintenance of HR systems",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ORG][RD] R&D: new research guidelines issued",
                        "[ORG][RD] mandatory training for R&D staff on new tools",
                        "[ROLE IN ORG][RD][ORG][GUEST] upcoming HR orientation session",
                        "[ROLE IN ORG][HR][ORG][ADMIN] changes to HR compliance forms"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Test
    public void finds_single_organization_role_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(null, Set.of(orgAdmin.getId()), Set.of(hr.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(null, Set.of(orgAdmin.getId()), Set.of(hr.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ROLE IN ORG][HR][ORG][ADMIN] changes to HR compliance forms"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    @Disabled("Returns not expected notifications that are either dedicated to gregor or to no one (regarding current users and roles)")
    @Test
    public void finds_multiple_organizations_roles_notifications() {
        // when
        Page<NotificationKeeper> notifications = notificationRepository.findAll(null, Set.of(orgAdmin.getId(), orgGuest.getId()), Set.of(hr.getId(), rd.getId()), null);
        Specification<Notification> spec = NotificationSepcifications.allUnreadForUser(null, Set.of(orgAdmin.getId(), orgGuest.getId()), Set.of(hr.getId(), rd.getId()));
        List<Notification> notificationsBySpec = notificationRepository.findAll(spec);

        // then
        assertThat(notifications.getContent())
                .extracting(keeper -> keeper.getNotification().getMessage())
                .containsExactlyInAnyOrder(
                        "[GLOBAL] invitation to quarterly company meeting",
                        "[GLOBAL] feedback request on recent team event",
                        "[GLOBAL] new remote work guidelines",
                        "[ORG][HR] HR policy on remote work updated",
                        "[ORG][HR] update on HR compliance requirements",
                        "[ORG][RD] R&D: new research guidelines issued",
                        "[ORG][RD] mandatory training for R&D staff on new tools",
                        "[ROLE IN ORG][RD][ORG][GUEST] upcoming HR orientation session",
                        "[ROLE IN ORG][HR][ORG][ADMIN] changes to HR compliance forms"
                );
        assertSameMessages(notifications, notificationsBySpec);
    }

    private void assertSameMessages(Page<NotificationKeeper> fromByQuery, List<Notification> fromBySpecification) {
//        if (true) return; // spec assertion turned off
        List<String> byQuery = fromByQuery.getContent().stream()
                                          .map(keeper -> keeper.getNotification().getMessage())
                                          .toList();
        List<String> bySpecification = fromBySpecification.stream()
                                                          .map(Notification::getMessage)
                                                          .toList();
        Assertions.assertThat(byQuery).containsExactlyInAnyOrderElementsOf(bySpecification);
    }

}
