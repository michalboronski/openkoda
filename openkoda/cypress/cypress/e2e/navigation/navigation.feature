Feature: Navigation

  Background:
    Given I am logged as user "admin" with password "admin123"

  Scenario Outline: Global admin can navigate through admin pages
    When I open "/html/dashboard" page
    And I click "<adminPageLinkLabel>" in admin menu
    Then I should see "<targetPageUrl>" page
    Examples:
      | adminPageLinkLabel         | targetPageUrl              |

      | Reports                    | /html/queryreport/all      |
      | Reporting AI               | /html/cn/reporting-ai      |

      | Forms                      | /html/form/all             |
      | Server-Side Code           | /html/serverjs/all         |
      | Web Endpoints              | /html/webendpoint/all      |

      | Dashboard Builder          | /html/pagebuilder/all      |
      | File                       | /html/file/all             |
      | CMS                        | /html/frontendresource/all |
      | Email Settings             | /html/integrations         |
      | Import/Export                | /html/components           |
#      | Documents          | /html/file/document        |

      | Users                      | /html/user/all             |
      | Organizations              | /html/organization/all     |
      | Roles                      | /html/role/all             |
      | Privileges                 | /html/privilege/all        |

      | Job Request                | /html/scheduler/all        |
      | Event Listeners            | /html/eventlistener/all    |
      | Custom Events              | /html/customevent/all      |

      | Audit                      | /html/audit/all            |
      | Logs                       | /html/logs/all             |
      | System Health              | /html/system-health        |
      | Threads                    | /html/thread               |

