INSERT INTO public.frontend_resource (id, created_by, created_by_id, created_on, index_string, modified_by, modified_by_id, organization_id, updated_on, "content", draft_content, include_in_sitemap, public_level, "name", required_privilege, "type", resource_type, url_path)
VALUES (nextval('seq_organization_related_id'), 'admin@yourdomain.org',   10000, '2023-07-10 23:54:28.45942+02', 'asset-management asset-management ui_component 00000/120166', 'admin@yourdomain.org', 10000, NULL, '2023-07-10 23:54:49.219742+02', '
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" lang="en"
    layout:decorate="~{${defaultLayout}}">

<head>
    <script>
        /* Custom script here */
    </script>
</head>

<body>
    <div class="container-fluid">
        <div layout:fragment="content" class="card shadow mb-4 border-0">
            <style>
                /* Custom style here */
            </style>
            <div class="card-header py-3 d-flex flex-row justify-content-between">
                <div class="dropdown no-arrow dropdown-sm">
                    <a class="dropdown-toggle" href="#" role="button" id="eventMenuLink" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-ellipsis-v fa-sm fa-fw text-gray-400"></i>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right shadow animated--fade-in"
                        aria-labelledby="eventMenuLink">
                        <a class="dropdown-item" th:if="${@auth.hasGlobalPrivilege(''canManageBackend'')}"
                            th:href="${@url.form(''asset'')}">New asset</a>
                        <a class="dropdown-item" th:if="${@auth.hasGlobalPrivilege(''canManageBackend'')}"
                            th:href="${@url.all(''asset'')}">All asset</a>
                    </div>
                </div>
                <div class="card-header-buttons">
                    <a th:if="${@auth.hasGlobalPrivilege(''canManageBackend'')}" class="btn-primary btn btn-sm"
                        th:href="${@url.form(''asset'')}">New asset</a>
                    <a th:if="${@auth.hasGlobalPrivilege(''canManageBackend'')}" class="btn-primary btn btn-sm"
                        th:href="${@url.all(''asset'')}">All asset</a>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 class="m-0 font-weight-bold" th:text="${''Assets total number: '' + assetsNumber }">Assets
                                total number</h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <tbody>
                                        <tr style="color: red;">
                                            <td>in use</td>
                                            <td th:text="${in_use}"></td>
                                        </tr>
                                        <tr style="color: green;">
                                            <td>broken</td>
                                            <td th:text="${broken}"></td>
                                        </tr>
                                        <tr style="color: blue;">
                                            <td>in repair</td>
                                            <td th:text="${in_repair}"></td>
                                        </tr>
                                        <tr style="color: orange;">
                                            <td>retired</td>
                                            <td th:text="${retired}"></td>
                                        </tr>
                                        <tr style="color: brown;">
                                            <td>utilized</td>
                                            <td th:text="${utilized}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="card shadow mb-4">
                        <canvas id="assetStatusChart" style="width:100%;max-width:600px"></canvas>
                        <script type="text/javascript" th:inline="javascript">
                            const xValues = ["in use", "broken", "in repair", "retired", "utilized"];
                            var in_use = /*[[${in_use}]]*/;
                            var broken = /*[[${broken}]]*/;
                            var in_repair = /*[[${in_repair}]]*/;
                            var retired = /*[[${retired}]]*/;
                            var utilized = /*[[${utilized}]]*/;
                            const yValues = [in_use, broken, in_repair, retired, utilized];
                            const barColors = ["red", "green", "blue", "orange", "brown"];

                            new Chart("assetStatusChart", {
                                type: "bar",
                                data: {
                                    labels: xValues,
                                    datasets: [{
                                        backgroundColor: barColors,
                                        data: yValues
                                    }]
                                },
                                options: {
                                    legend: { display: false },
                                    title: {
                                        display: true,
                                        text: "Asset Status Distribution"
                                    }
                                }
                            });
                        </script>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                            <h6 class="m-0 font-weight-bold"
                                th:text="${''Assets total value: '' + price}">Assets
                                total value</h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <tbody>
                                        <tr style="color: red;">
                                            <td>Total depreciation </td>
                                            <td th:text="${depreciationTotal}"></td>
                                        </tr>
                                        <tr style="color: green;">
                                            <td>Average depreciation value </td>
                                            <td th:text="${depreciationAvg}"></td>
                                        </tr>
                                        <tr style="color: blue;">
                                            <td>Maintenance costs</td>
                                            <td th:text="${maintenanceCosts}"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="card shadow mb-4">
                        <canvas id="assetLineChart" style="width:100%;max-width:600px"></canvas>
                        <script type="text/javascript" th:inline="javascript">
                            var depricationMonth = /*[[${depricationMonth}]]*/;
                            var maintenanceMonth = /*[[${maintenanceMonth}]]*/;
                            const months = [];
                            const depricationData = [];
                            const depricationAvgData = [];
                            const maintenanceData = [];
                            for (let i = 0; i < depricationMonth.length; i++) {
                                months[i] = depricationMonth[i][0] + "-" + depricationMonth[i][1];
                                depricationData[i] = depricationMonth[i][2]
                                depricationAvgData[i] = depricationMonth[i][3]
                                maintenanceData[i] = maintenanceMonth[i][2]
                            }

                            new Chart("assetLineChart", {
                                type: "line",
                                data: {
                                    labels: months,
                                    datasets: [{
                                        data: depricationData,
                                        borderColor: "red",
                                        fill: false
                                    }, {
                                        data: depricationAvgData,
                                        borderColor: "green",
                                        fill: false
                                    }, {
                                        data: maintenanceData,
                                        borderColor: "blue",
                                        fill: false
                                    }]
                                },
                                options: {
                                    legend: { display: false }
                                }
                            });
                        </script>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card shadow mb-4">
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-0 font-weight-bold" th:text="${''Assets per employee: ''}">assets per employee</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th> User</th>
                                        <th> asset</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr th:each="instance : ${assetsPerEmployee}">
                                        <td th:text="${instance[0]}">num</td>
                                        <td th:text="${instance[1]}">num</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>

</html>
', NULL, false, false, 'asset-management', NULL, 'UI_COMPONENT', 'HTML', 'asset-management');


INSERT INTO public.controller_endpoint (id, created_by, created_by_id, created_on, index_string, modified_by, modified_by_id, organization_id,updated_on, code, frontend_resource_id, http_headers, http_method, model_attributes, response_type, sub_path)
VALUES (nextval('seq_organization_related_id'), 'admin@yourdomain.org', 10000, '2023-07-10 23:55:10.714542+02', '', 'admin@yourdomain.org', 10000, NULL, '2023-07-10 23:55:10.714542+02', '
flow.thenSet("assetsNumber", a => a.services.data.getRepository("asset").count())
.thenSet("title", a => "Asset managament dashboard")
.thenSet("orgId", a => a.model.get("organizationEntityId") != null ? a.model.get("organizationEntityId") : 121)
.thenSet("in_use", a => a.services.data.getRepository("asset").getCountByStatus(Java.type("openkoda.model.Status").in_use,a.model.get("orgId")))
.thenSet("broken", a => a.services.data.getRepository("asset").getCountByStatus(Java.type("openkoda.model.Status").broken,a.model.get("orgId")))
.thenSet("in_repair", a => a.services.data.getRepository("asset").getCountByStatus(Java.type("openkoda.model.Status").in_repair,a.model.get("orgId")))
.thenSet("retired", a => a.services.data.getRepository("asset").getCountByStatus(Java.type("openkoda.model.Status").retired,a.model.get("orgId")))
.thenSet("utilized", a => a.services.data.getRepository("asset").getCountByStatus(Java.type("openkoda.model.Status").utilized,a.model.get("orgId")))
.thenSet("price", a => a.services.data.getRepository("asset").getPriceSum(a.model.get("orgId")))
.thenSet("depreciationTotal", a => a.services.data.getRepository("asset").getTotalDepreciation(a.model.get("orgId")))
.thenSet("depreciationAvg", a => a.services.data.getRepository("asset").getAvgDepreciation(a.model.get("orgId")))
.thenSet("maintenanceCosts", a => a.services.data.getRepository("maintenance").getMaintenanceCosts(a.model.get("orgId")))
.thenSet("assetsPerEmployee", a => a.services.data.getRepository("asset").getAssetsPerEmployee(a.model.get("orgId")))
.thenSet("depricationMonth", a => a.services.data.getRepository("asset").getDepreciationByMonth(a.model.get("orgId")))
.thenSet("maintenanceMonth", a => a.services.data.getRepository("maintenance").getMaintenanceCostsByMonth(a.model.get("orgId")))',
        (select id from public.frontend_resource where name='asset-management' and type='UI_COMPONENT'), NULL, 0, NULL, 'HTML', '');


INSERT INTO public.server_js (id,     created_by,         created_by_id, created_on,                index_string,   modified_by,             modified_by_id, organization_id,    updated_on,           arguments, code, model, "name")
VALUES(nextval('seq_organization_related_id'), 'admin@yourdomain.org', 10000,      '2023-06-30 10:00:00.000', '120171',         'admin@yourdomain.org',     10000,           NULL  ,     '2023-06-30 11:00:00.000', '',
'

let assetRepository = services.data.getRepository("asset");
let maintenanceRepository = services.data.getRepository("maintenance");
let typeRepository = services.data.getRepository("type");
let vendorRepository = services.data.getRepository("vendor");

let assetForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
        "asset", "readOrgData", "manageOrgData",
        a => a.text("name")
                .textarea("description")
                .datalist("type", f => f.dictionary("type"))
                .dropdown("typeId", "type")
                .text("location")
                .datalist("user", f => f.dictionary("user"))
                .dropdown("userId", "user")
                .datetime("purchaseDate")
                .files("filesId",  (d,r) => r.getFileDtos(d.entity), "image/png,image/jpeg")
  				.datalist("status", f => f.enumDictionary(Java.type("openkoda.model.Status").values()))
				.dropdown("status", "status")
  				.number("price")
  				.number("operatingCost")
  				.number("disposalCost")
  				.number("depreciationRate")
  				.text("depreciationValue")
);

let maintenanceForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
        "maintenance", "readOrgData", "manageOrgData",
        a => a.datalist("assets", f => f.dictionary("asset"))
                .dropdown("assetId", "assets")
                .datetime("serviceStartDate")
                .datetime("serviceEndDate")
                .datalist("vendor", f => f.dictionary("vendor"))
                .dropdown("vendorId", "vendor")
                .textarea("description")
  				.number("costOfService")
);

let typeForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
        "type", "readOrgData", "manageOrgData",
        a => a.text("type"));

let vendorForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
        "vendor", "readOrgData", "manageOrgData",
        a => a.text("name")
                .textarea("contactInformation")
                .textarea("products")
);

services.customisation.registerFrontendMapping(assetForm, assetRepository);
services.customisation.registerApiCrudController(assetForm, assetRepository);
services.customisation.registerHtmlCrudController(assetForm, assetRepository).setGenericTableFields("name", "location", "userId");

services.customisation.registerFrontendMapping(maintenanceForm, maintenanceRepository);
services.customisation.registerApiCrudController(maintenanceForm, maintenanceRepository);
services.customisation.registerHtmlCrudController(maintenanceForm, maintenanceRepository).setGenericTableFields("assetId","serviceStartDate","serviceEndDate","vendorId");

services.customisation.registerFrontendMapping(typeForm, typeRepository);
services.customisation.registerApiCrudController(typeForm, typeRepository);
services.customisation.registerHtmlCrudController(typeForm, typeRepository).setGenericTableFields("type");

services.customisation.registerFrontendMapping(vendorForm, vendorRepository);
services.customisation.registerApiCrudController(vendorForm, vendorRepository);
services.customisation.registerHtmlCrudController(vendorForm, vendorRepository).setGenericTableFields("name", "contactInformation", "products");

model', '{}', 'forms');

INSERT INTO public.event_listener (id, created_by, created_by_id, created_on, modified_by, modified_by_id, updated_on, consumer_class_name, consumer_method_name, consumer_parameter_class_name, event_class_name, event_name, event_object_type, index_string, organization_id, static_data_1, static_data_2, static_data_3, static_data_4)
VALUES (nextval('seq_organization_related_id'), 'admin@yourdomain.org', 10000, '2023-06-30 10:00:00.000', 'admin@yourdomain.org', 10000, '2023-06-30 11:00:00.000', 'com.openkoda.core.customisation.ServerJSRunner', 'startCustomisationServerJs', 'java.time.LocalDateTime', 'com.openkoda.core.service.event.ApplicationEvent', 'APPLICATION_STARTED', 'java.time.LocalDateTime', 'com.openkoda.core.service.event.applicationevent application_started java.time.localdatetime com.openkoda.core.customisation.serverjsrunner startcustomisationserverjs orgid: 00000/281', NULL, 'forms', '0', '0', '0');
