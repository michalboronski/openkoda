INSERT INTO public.server_js (id, created_by, created_by_id, created_on, index_string, modified_by, modified_by_id, organization_id, updated_on, arguments, code, model, "name") VALUES(120171, 'admin@yourdomain.org', 10000, '2023-06-28 10:27:15.460', '120171', 'admin@yourdomain.org', 10000, NULL, '2023-07-19 12:01:54.795', '', 'let projectRepository = services.data.getRepository(''project'');
let statusRepository = services.data.getRepository(''status'');
let boardTaskRepository = services.data.getRepository(''board_task'');
let labelRepository = services.data.getRepository(''label'');

let projectForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
                "project", "readOrgData", "readOrgData",
                a => a.text("name"));


let statusForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
                "status", "readOrgData", "readOrgData",
                a => a.datalist("projects", f => f.dictionary("project"))
						.dropdown("projectId", "projects")
  						.text("name")
						.number("ordinal").validate(v => (v != null && v != '''') ? null : "not.valid"));

let boardTaskForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
                "task", "readOrgData", "readOrgData",
                a => a.datalist("statuses", f => f.dictionary("status"))
  						.datalist("labels", f => f.dictionary("label"))
                        .dropdown("statusId", "statuses")
  						.text("name")
  						.textarea("description")
						.checkboxList("labelsId", "labels")
  						.files("filesId", (d,r) => r.getFileDtos(f.entity), "image/png,image/jpeg")
				);

let labelForm = services.frontendMappingDefinition.createFrontendMappingDefinition(
                "label", "readOrgData", "readOrgData",
                a => a.text("name"));

services.customisation.registerFrontendMapping(projectForm, projectRepository);
services.customisation.registerHtmlCrudController(projectForm, projectRepository).setGenericTableFields("name").setTableView("projects-all");
services.customisation.registerApiCrudController(projectForm, projectRepository);

services.customisation.registerFrontendMapping(statusForm, statusRepository);
services.customisation.registerApiCrudController(statusForm, statusRepository);
services.customisation.registerHtmlCrudController(statusForm, statusRepository).setGenericTableFields("name");

services.customisation.registerFrontendMapping(boardTaskForm, boardTaskRepository);
services.customisation.registerApiCrudController(boardTaskForm, boardTaskRepository);
services.customisation.registerHtmlCrudController(boardTaskForm, boardTaskRepository).setGenericTableFields("name", "description");

services.customisation.registerFrontendMapping(labelForm, labelRepository);
services.customisation.registerApiCrudController(labelForm, labelRepository);
services.customisation.registerHtmlCrudController(labelForm, labelRepository).setGenericTableFields("name");

model', '{}', 'forms');


INSERT INTO public.frontend_resource (id, created_by, created_by_id, created_on, index_string, modified_by, modified_by_id, organization_id, updated_on, "content", draft_content, include_in_sitemap, is_public, "name", required_privilege, "type", url_path) VALUES(2136, 'admin@yourdomain.org', 10000, '2023-06-29 12:10:14.475', 'boards boards ui_component 00000/2136', 'admin@yourdomain.org', 10000, NULL, '2023-07-19 12:25:46.479', '<!--DEFAULT CONTENT-->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" lang="en" layout:decorate="~{${defaultLayout}}">
<head>
	<style>
	  	.listing-card {
	  		min-width: 15rem;
	  	}

	  	.listing-card + .listing-card {
		  	border-left: 1px solid #d1d3e2;
	  	}

	  	.listings {
		  	display: flex;
		  	flex-direction: row;
		  	padding: 0.5rem;
			width: 100%;
			overflow-x: auto;
			white-space: nowrap;
	  	}

	  	.listing-header {
		 	padding: 0.3rem 1.0rem;
	  	}

		.task-card {
			margin: 0.4rem;
		  	padding: 0.4rem;
		  	border: 1px solid #d1d3e2;
		  	border-radius: 0.5rem;
  			transition: 0.15s;
		}

	  	.task-card:hover {
			background-color: #f6f7fa;
	  	}

	  	.task-card a {
  			text-decoration: none;
		  	color: black;
	  	}

	  	.listing-card a {
  			text-decoration: none;
		  	color: black;
	  	}

	  	.header-wrapper a {
  			text-decoration: none;
		  	color: black;
		  	text-wrap: nowrap;
	  	}

	  	.button-wrapper {
			display: flex;
			flex-basis: content;
		}

	  	.label-tag {
			padding: 0.3rem;
			border-radius: 0.5rem;
		  	font-size: 0.8rem;
		  	margin: 0 0.5rem 0.5rem 0;
			color: #fff;
			background-color: #6750a4;
			border-color: #6750a4;
		}

	  	.card-name-wrapper {
			margin-bottom: 0.5rem;
		  	text-wrap: balance;
	  	}

	  	.label-wrapper {
		  	display: flex;
		  	flex-wrap: wrap;
	  	}
	</style>
</head>
<body>
<div class="container">
    <h1 layout:fragment="title"/>
    <div layout:fragment="content">
	  	<div class="mb-4 row">
		  	<div class="py-3 d-flex flex-row justify-content-between col-12">
			  	<div class="header-wrapper">
				  <a th:href="${''/html/project/'' + project.id + ''/settings''}">
					  <h2 class="m-0 font-weight-bold col-2" th:text="${project.name}"/>
				  </a>
			  	</div>
				<div class="col-6"></div>
			  	<div class="col-4 button-wrapper">
					<a href="/html/status/new/settings">
						<button class="btn btn-primary inline" th:text="${''New status''}"></button>
					</a>
				  	<div style="margin-left: 0.6rem;"></div>
					<a href="/html/task/new/settings">
						<button class="btn btn-primary inline" th:text="${''New task''}"></button>
					</a>
				  	<div style="margin-left: 0.6rem;"></div>
				  	<a href="/html/label/new/settings">
						<button class="btn btn-primary inline" th:text="${''New label''}"></button>
					</a>
			  	</div>
			</div>
		  	<div class="listings">
				<th:block th:each="status : ${#lists.sort(statuses)}">
					<div class="mb-4 listing-card col-3">
					  	<div style="display: flex;">
							<div class="col-10">
								<a th:href="${''/html/status/'' + status.id + ''/settings''}">
									<h6 class="m-0 font-weight-bold listing-header" th:text="${status.name}"></h6>
								</a>
							</div>
							<div class="col-2">
								<a th:replace="~{forms::single-nostyle-button-post-form-with-confirm-class(${''/html/status/'' + status.id + ''/remove''},
											   ''<i class=&quot;fa-solid fa-trash-can&quot;></i>'', #{boards.delete.status.message},''d-inline-flex'', ''bg-transparent border-0 pr-0 font-weight-normal ml-2 text-danger'')}"/>
							</div>
					  	</div>
					  	<th:block th:each="boardTask : ${status.boardTasks}">
							<div class="task-card">
							  <div>
								<div class="card-name-wrapper">
								  <a th:href="${''/html/task/'' + boardTask.id + ''/settings''}">
									<span th:text="${boardTask.name}"/>
								  </a>
								  <a th:replace="~{forms::single-nostyle-button-post-form-with-confirm-class(${''/html/task/'' + boardTask.id + ''/remove''},
												 ''<i class=&quot;fa-solid fa-trash-can&quot;></i>'', ''Are you sure?'',''d-inline-flex'', ''bg-transparent border-0 pr-0 font-weight-normal ml-2 text-danger'')}"/>
								</div>
								<div class="label-wrapper">
								  <th:block th:each="label : ${boardTask.labels}">
									<span class="label-tag" th:text="${label.name}"/>
								  </th:block>
								</div>
							  </div>
							</div>
						 </th:block>
					</div>
				</th:block>
		  	</div>
	  	</div>
    </div>
</div>
</body>
</html>
', NULL, false, false, 'boards', NULL, 'UI_COMPONENT', 'boards');

INSERT INTO public.controller_endpoint (id, created_by, created_by_id, created_on, index_string, modified_by, modified_by_id, organization_id, updated_on, code, frontend_resource_id, http_headers, http_method, model_attributes, response_type, sub_path) VALUES(2135, 'admin@yourdomain.org', 10000, '2023-06-29 12:10:24.894', '', 'admin@yourdomain.org', 10000, NULL, '2023-07-18 13:07:38.761', 'flow.thenSet("project", a=> a.services.data.getRepository(''project'').findOne( a.params.get(''id'')+"" ))
 .thenSet("statuses", a => a.services.data.getRepository(''status'').findBy( (root, query, cb) => {
    let id = a.params.get("id");
    return cb.equal( root.get("projectId"), id);
  })
);
', 2136, NULL, 0, NULL, 'HTML', '');

INSERT INTO public.event_listener
(id, created_by, created_by_id, created_on, modified_by, modified_by_id, updated_on, consumer_class_name, consumer_method_name, consumer_parameter_class_name, event_class_name, event_name, event_object_type, index_string, organization_id, static_data_1, static_data_2, static_data_3, static_data_4)
VALUES(281, 'admin@yourdomain.org', 10000, '2023-05-15 18:34:53.617', 'admin@yourdomain.org', 10000, '2023-05-15 18:34:53.617', 'com.openkoda.core.customisation.ServerJSRunner', 'startCustomisationServerJs', 'java.time.LocalDateTime', 'com.openkoda.core.service.event.ApplicationEvent', 'APPLICATION_STARTED', 'java.time.LocalDateTime', 'com.openkoda.core.service.event.applicationevent application_started java.time.localdatetime com.openkoda.core.customisation.serverjsrunner startcustomisationserverjs orgid: 00000/281', NULL, 'forms', '0', '0', '0');


