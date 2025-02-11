@active @current
Feature: Uber

  Background:
    Given I am logged as user "admin" with password "admin123"

  @focus
  Scenario Outline: Global admin can submit uber form
    When I open "<formPageUrl>" page
    And I click on "<linkName>" link
    And I fill "<formName>" with values "<setFormValues>"
    And I fill "<formName>" code editor with code snippet "<codeSnippet>" and update "<tableName>"
    And I submit form "<formName>"
    Then I should see updated "<targetPageUrl>" page
    Examples:
      | formPageUrl    | linkName | formName                | setFormValues                            | targetPageUrl         | codeSnippet  | tableName   |
      | /html/form/all | New      | organizationRelatedForm | name=uberEntity;newTableName=uber_entity | /html/form/*/settings | gigaUberForm | uber_entity |
