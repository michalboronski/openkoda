@tables
Feature: Tables
  Background:
    Given I am logged as user "admin" with password "admin123"

  Scenario Outline: Global admin can see default values in admin tables
    When I open "<tablePageUrl>" page
    Then I should find "<expectedTableColumnValues>" in the visible table
    Examples:
      | tablePageUrl           | expectedTableColumnValues     |
      | /html/organization/all | 121;ACME                      |
      | /html/role/all         | 1;ROLE_UNAUTHENTICATED;GLOBAL |
      | /html/role/all         | 2;ROLE_ADMIN;GLOBAL           |
      | /html/role/all         | 3;ROLE_USER;GLOBAL            |
      | /html/role/all         | 4;ROLE_ORG_ADMIN;ORG          |
      | /html/role/all         | 5;ROLE_ORG_USER;ORG           |
      | /html/user/all         | ?;Mark Administrator;?        |

#    @focus
  Scenario Template: Global admin can sort values lexicographically in admin tables
    When I open "<page>" page
    And I click "<sortColumn>" column header
    Then I should see "<sortPage>" page
    And I should find data sorted "<sortType>" by "<sortColumn>" column in "<sortOrder>" direction
    Examples:
      | page                       | sortColumn        | sortType | sortOrder | sortPage                                                                                                                             |
      | /html/user/all             | name              | lexic    | ASC       | /html/user/all?user_page=0&user_size=20&user_sort=name,ASC&user_search=                                                              |
      | /html/organization/all     | name              | lexic    | ASC       | /html/organization/all?organization_page=0&organization_size=20&organization_sort=name,ASC&organization_search=                      |
      | /html/audit/all            | createdOn         | lexic    | ASC       | /html/audit/all?audit_page=0&audit_size=20&audit_sort=createdOn,ASC&audit_search=                                                    |
      | /html/audit/all            | ipAddress         | lexic    | ASC       | /html/audit/all?audit_page=0&audit_size=20&audit_sort=ipAddress,ASC&audit_search=                                                    |
      | /html/role/all             | name              | lexic    | ASC       | /html/role/all?role_page=0&role_size=20&role_sort=name,ASC&role_search=                                                              |
      | /html/frontendresource/all | name              | lexic    | DESC      | /html/frontendresource/all?frontendresource_page=0&frontendresource_size=10&frontendresource_sort=name,DESC&frontendresource_search= |
#      no data
#      | /html/eventlistener/all    | eventClassName    | lexic    | ASC       | /html/eventlistener/all?event_page=0&event_size=20&event_sort=eventClassName,ASC&event_search=                                       |
#      no data
#      | /html/eventlistener/all    | eventName         | lexic    | ASC       | /html/eventlistener/all?event_page=0&event_size=20&event_sort=eventName,ASC&event_search=                                            |
#      no data
#      | /html/eventlistener/all    | consumerClassName | lexic    | ASC       | /html/eventlistener/all?event_page=0&event_size=20&event_sort=consumerClassName,ASC&event_search=                                    |
#      cronExpression data suddenly disappears
#      | /html/scheduler/all        | cronExpression    | lexic    | ASC       | /html/scheduler/all?scheduler_page=0&scheduler_size=20&scheduler_sort=cronExpression,ASC&scheduler_search=                           |
      | /html/scheduler/all        | eventData         | lexic    | ASC       | /html/scheduler/all?scheduler_page=0&scheduler_size=20&scheduler_sort=eventData,ASC&scheduler_search=                                |
      | /html/organization/all     | id                | linear   | ASC       | /html/organization/all?organization_page=0&organization_size=20&organization_sort=id,ASC&organization_search=                        |
      | /html/audit/all            | id                | linear   | ASC       | /html/audit/all?audit_page=0&audit_size=20&audit_sort=id,ASC&audit_search=                                                           |
      | /html/role/all             | id                | linear   | ASC       | /html/role/all?role_page=0&role_size=20&role_sort=id,ASC&role_search=                                                                |
      | /html/frontendresource/all | id                | linear   | ASC       | /html/frontendresource/all?frontendresource_page=0&frontendresource_size=10&frontendresource_sort=id,ASC&frontendresource_search=    |
#      | /html/eventlistener/all    | id                | linear   | ASC       | /html/eventlistener/all?event_page=0&event_size=20&event_sort=id,ASC&event_search=                                                   |
      | /html/scheduler/all        | id                | linear   | ASC       | /html/scheduler/all?scheduler_page=0&scheduler_size=20&scheduler_sort=id,ASC&scheduler_search=                                       |

  Scenario Template: Global admin can use pagination in admin tables
#    Given I am expecting no less than "<numberOfRecords>" number of records in "<column>" column
    When I open "<page>" page with url parameter "<pageSize>"
    And I should see pagination links list with class "<paginationId>"
    Then I click "<linkName>" arrow
    And I should see "<urlParameters>" parameter in page URL
    And I should see "<previousLinkArrow>" arrow
    Examples:
      | page                       | urlParameters                                   | linkName | previousLinkArrow | paginationId       | column           | numberOfRecords | pageSize                |
      | /html/audit/all            | audit_page=1&audit_size=1                       | Next     | Previous          | pagination-wrapper | audit            | 2               | audit_size=1            |
      | /html/user/all             | user_page=1&user_size=1                         | Next     | Previous          | pagination-wrapper | user             | 2               | user_size=1             |
      | /html/organization/all     | organization_page=1&organization_size=1         | Next     | Previous          | pagination-wrapper | organization     | 2               | organization_size=1     |
      | /html/role/all             | role_page=1&role_size=1                         | Next     | Previous          | pagination-wrapper | role             | 2               | role_size=1             |
      | /html/frontendresource/all | frontendresource_page=1&frontendresource_size=1 | Next     | Previous          | pagination-wrapper | frontendResource | 2               | frontendresource_size=1 |
#      | /html/eventlistener/all    | event_page=1&event_size=1                       | Next     | Previous          | pagination-wrapper | event            | 2               | event_size=1            |
      | /html/scheduler/all        | scheduler_page=1&scheduler_size=1               | Next     | Previous          | pagination-wrapper | scheduler        | 2               | scheduler_size=1        |

  Scenario Template: Global admin don't see pagination in admin tables
#    Given I am expecting no less than "<numberOfRecords>" number of records in "<column>" column
    When I open "<page>" page with page size URL parameter equal to number of records in "<column>" column
    And I should not see pagination links in list with id "<paginationId>"

    Examples:
      | page                        | column           | numberOfRecords | paginationId |
      | /html/audit/all             | audit            | 1               | pagination   |
      | /html/user/all              | user             | 1               | pagination   |
      | /html/organization/all      | organization     | 1               | pagination   |
      | /html/role/all              | role             | 1               | pagination   |
      | /html/frontendresource/all  | frontendResource | 1               | pagination   |
      | /html/eventlistener/all     | event            | 1               | pagination   |
      | /html/scheduler/all         | scheduler        | 1               | pagination   |
