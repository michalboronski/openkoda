/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.openkoda.controller;

import com.openkoda.controller.common.PageAttributes;
import com.openkoda.core.controller.generic.AbstractController;
import com.openkoda.core.flow.Flow;
import com.openkoda.core.flow.PageAttr;
import com.openkoda.core.flow.PageModelMap;
import com.openkoda.core.form.*;
import com.openkoda.core.security.HasSecurityRules;
import com.openkoda.core.security.OrganizationUser;
import com.openkoda.core.security.UserProvider;
import com.openkoda.model.PrivilegeBase;
import com.openkoda.model.common.SearchableOrganizationRelatedEntity;
import com.openkoda.model.file.File;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.openkoda.core.repository.common.SearchableFunctionalRepositoryWithLongId.searchSpecificationFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AbstractCRUDController extends AbstractController implements HasSecurityRules {

    public PageModelMap doGetAll(Long organizationId, String objKey, String tableFilterSearch, HttpServletRequest request) {
        debug("[doGetAll] organizationId: {}, objKey: {}, tableFilterSearch: {}", organizationId, objKey, tableFilterSearch);
        Pageable aPageable = createPageable(request, objKey);
        String search = createSearch(request, objKey);
        Tuple2<String, Map<String, String>> remainingParams = createRemainingParams(request, objKey);
        Map<String, String> objFilters = createFilters(request, objKey);

        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        Set<Long> organizationIdsWithPrivilege;
        OrganizationUser user = UserProvider.getFromContext().get();
        if(user.isSuperUser()) {
            organizationIdsWithPrivilege = null;
        } else {
            organizationIdsWithPrivilege = user.getOrganizationRoles().keySet().stream()
                .filter( orgId -> hasGlobalOrOrgPrivilege(conf.getGetAllPrivilege(), orgId))
                .collect(Collectors.toSet());
        }

        // if there is no sorting set in the request, by default sort using the first column available (usually ID or NAME)
        if(aPageable.getSort().isUnsorted() &&  conf.getTableFormFieldNames().length > 0) {
            String sortColumn = conf.getTableFormFieldNames()[0];
            aPageable = PageRequest.of(aPageable.getPageNumber(), aPageable.getPageSize(), Sort.Direction.ASC, sortColumn);
        }

        final Pageable finalPageable = aPageable;
        Map<String, Boolean> fieldColumnVisibility = new HashMap<>();
        return Flow.init(componentProvider)
                .thenSet(searchTerm, a -> search)
                .thenSet(PageAttributes.objFilters, a -> objFilters)
                .thenSet((PageAttr<Page<SearchableOrganizationRelatedEntity>>)conf.getEntityPageAttribute(), a ->  {
                    final Long effectiveOrganizationId = (organizationId == null && organizationIdsWithPrivilege != null && organizationIdsWithPrivilege.size() == 1) ? organizationIdsWithPrivilege.iterator().next() : organizationId;
                    if(effectiveOrganizationId != null) {
                            return (Page<SearchableOrganizationRelatedEntity>) conf.getSecureRepository()
                        .search(search, effectiveOrganizationId, searchSpecificationFactory(tableFilterSearch).and(conf.getAdditionalSpecification()), finalPageable, ReflectionBasedEntityForm.getFilterTypesAndValues(conf.getFrontendMappingDefinition(), objFilters));
                    } else {
                        return (Page<SearchableOrganizationRelatedEntity>) conf.getSecureRepository()
                            .search(search, organizationIdsWithPrivilege, searchSpecificationFactory(tableFilterSearch).and(conf.getAdditionalSpecification()), finalPageable, ReflectionBasedEntityForm.getFilterTypesAndValues(conf.getFrontendMappingDefinition(), objFilters));
                    }
                })
                .thenSet(genericTableViewList, a -> ReflectionBasedEntityForm.calculateFieldsValuesWithReadPrivileges(conf.getFrontendMappingDefinition(), a.result.toList(), conf.getTableFormFieldNames(), fieldColumnVisibility, organizationId, false))
                .thenSet(genericTableViewHeaders, a -> ReflectionBasedEntityForm.getFieldsHeaders(conf.getFrontendMappingDefinition(), conf.getTableFormFieldNames(), fieldColumnVisibility, organizationId))
                .thenSet(genericTableFilters, a -> ReflectionBasedEntityForm.getFilterFields(conf.getFrontendMappingDefinition(), conf.getFilterFieldNames()))
                .thenSet(genericViewNavigationFragment, a -> conf.getNavigationFragment())
                .thenSet(isMapEntity, a -> conf.isMapEntity())
                .thenSet(frontendMappingDefinition, a -> conf.getFrontendMappingDefinition())
                .thenSet(menuItem, a -> conf.getMenuItem() != null ? conf.getMenuItem() : objKey)
                .thenSet(organizationRelatedObjectKey, a -> objKey)
                .thenSet(organizationRelatedForm, a -> conf.createNewForm())
                .thenSet(isAuditable, a -> services.customisation.isAuditableClass(conf.getEntityClass()))
                .thenSet(remainingParameters, remainingParametersMap, a -> remainingParams)
                .execute();
    }

    public PageModelMap doCreate(Long organizationId, String objKey) {
        debug("[doCreate] organizationId: {}, objKey: {}", organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return Flow.init(transactional)
                .thenSet(conf.getFormAttribute(), a -> conf.createNewForm(organizationId, null))
                .thenSet(genericViewNavigationFragment, a -> conf.getNavigationFragment())
                .thenSet(menuItem, a -> conf.getMenuItem() != null ? conf.getMenuItem() : objKey)
                .thenSet(genericFormGridIndexer, a -> new FormGridIndexer())
                .execute();
    }

    public PageModelMap doSetting(Long objectId, Long organizationId, String objKey) {
        debug("[doSetting] objectId: {}, organizationId: {}, objKey: {}", objectId, organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return Flow.init(componentProvider, objectId)
                .then(a -> conf.getSecureRepository().findOne(objectId))
                .thenSet(conf.getFormAttribute(), ac -> conf.createNewForm(organizationId, (SearchableOrganizationRelatedEntity) ac.result))
                .thenSet(genericViewNavigationFragment, a -> conf.getNavigationFragment())
                .thenSet(menuItem, a -> conf.getMenuItem() != null ? conf.getMenuItem() : objKey)
                .thenSet(genericFormGridIndexer, a -> new FormGridIndexer())
                .execute();
    }

    public PageModelMap doSaveNew(Long organizationId, String objKey, AbstractOrganizationRelatedEntityForm form, BindingResult br) {
        debug("[doSaveNew] organizationId: {}, objKey: {}", organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return ((Flow<Object, AbstractOrganizationRelatedEntityForm, DefaultComponentProvider>)
                Flow.init(componentProvider, conf.getFormAttribute(), form))
                .then(a -> services.validation.validateAndPopulateToEntity(form, br, conf.createNewEntity(organizationId)))
                .then(a -> (SearchableOrganizationRelatedEntity)conf.getSecureRepository().saveOne(a.result))
                .thenSet(conf.getFormAttribute(), a -> conf.createNewForm(organizationId, a.result))
                .thenSet(organizationRelatedObjectKey, a -> objKey)
                .execute();
    }

    public PageModelMap doSave(Long objectId, Long organizationId, String objKey, AbstractOrganizationRelatedEntityForm form, BindingResult br) {
        debug("[doSave] objectId: {}, organizationId: {}, objKey: {}", objectId, organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return ((Flow<Object, AbstractOrganizationRelatedEntityForm, DefaultComponentProvider>)
                Flow.init(componentProvider, conf.getFormAttribute(), form))
                .then(a -> (SearchableOrganizationRelatedEntity)conf.getSecureRepository().findOne(objectId))
                .then(a -> services.validation.validateAndPopulateToEntity(form, br,a.result))
                .then(a -> (SearchableOrganizationRelatedEntity)conf.getSecureRepository().saveOne(a.result))
                .thenSet(conf.getFormAttribute(), a -> conf.createNewForm(organizationId, a.result))
                .execute();
    }

    public PageModelMap doRemove(Long objectId, Long organizationId, String objKey) {
        debug("[doRemove] objectId: {}, organizationId: {}, objKey: {}", objectId, organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return Flow.init(componentProvider, objectId)
                .then(a -> conf.getSecureRepository().deleteOne(objectId))
                .execute();
    }

    public PageModelMap doView(Long objectId, Long organizationId, String objKey) {
        debug("[doView] objectId: {}, organizationId: {}, objKey: {}", objectId, organizationId, objKey);
        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        return Flow.init(componentProvider, objectId)
                .thenSet(organizationRelatedObjectKey, a -> conf.getFrontendMappingDefinition().name)
                .thenSet(organizationRelatedEntity, a -> (SearchableOrganizationRelatedEntity) conf.getSecureRepository().findOne(objectId))
                .then(ac -> conf.createNewForm(organizationId, (SearchableOrganizationRelatedEntity) ac.result))
                .thenSet(organizationRelatedObjectMap, a -> convertToKeyValueMap(a.result))
                .thenSet(genericViewNavigationFragment, a -> conf.getNavigationFragment())
                .thenSet(menuItem, a -> conf.getMenuItem() != null ? conf.getMenuItem() : objKey)
                .execute();
    }

    public PageModelMap doGetCsvReport(Long organizationId, String objKey, HttpServletRequest request) {
        debug("[doGetCsvReport] organizationId: {}, objKey: {}", organizationId, objKey);

        String search = createSearch(request, objKey);
        Map<String, String> objFilters = createFilters(request, objKey);

        CRUDControllerConfiguration conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        Map<String, Boolean> fieldColumnVisibility = new HashMap<>();
        return Flow.init(componentProvider)
                .thenSet(genericTableHeaders, a -> conf.getReportFormFieldNames())
                .then(a -> (List<SearchableOrganizationRelatedEntity>) conf.getSecureRepository()
                        .search(search, organizationId, conf.getAdditionalSpecification(), ReflectionBasedEntityForm.getFilterTypesAndValues(conf.getFrontendMappingDefinition(), objFilters)))
                .thenSet(genericTableViewList, a -> ReflectionBasedEntityForm.calculateFieldsValuesWithReadPrivileges(conf.getFrontendMappingDefinition(), a.result, a.model.get(genericTableHeaders), fieldColumnVisibility, organizationId, true))
                .thenSet(genericTableViewHeaders, a -> ReflectionBasedEntityForm.getFieldsHeaders(conf.getFrontendMappingDefinition(), a.model.get(genericTableHeaders), fieldColumnVisibility, organizationId))
                .thenSet(file, a -> {
                    try {
                        return services.csv.createCSV(String.format("%s_%s.csv", objKey, dtf.format(LocalDateTime.now())), a.model.get(genericTableViewList), a.model.get(genericTableHeaders));
                    } catch (IOException | SQLException e) {
                        error("[getCsvReport]", e);
                        return null;
                    }
                })
                .execute();
    }

    boolean notValidAccess(PrivilegeBase privilege, Long confOrganizationId, Long organizationId){
        return !hasGlobalOrOrgPrivilege(privilege, organizationId);
    }

    private Map<FrontendMappingFieldDefinition, Object> convertToKeyValueMap(AbstractOrganizationRelatedEntityForm form) {
        List<FrontendMappingFieldDefinition> fields = form.frontendMappingDefinition.getValuedTypeFields();
        Map<FrontendMappingFieldDefinition, Object> map = new LinkedHashMap<>();
        if(form.dto instanceof OrganizationRelatedMap) {
            OrganizationRelatedMap dto = ((OrganizationRelatedMap) form.dto);
            for (FrontendMappingFieldDefinition f : fields) {
                map.put(f, dto.get(f.getName()));
            }
        } else {
            for (FrontendMappingFieldDefinition f : fields) {
                if(PropertyUtils.isReadable(form.dto, f.getName())) {
                    try {
                        map.put(f, PropertyUtils.getProperty(form.dto, f.getName()));
                    } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                        error("[convertToKeyValueMap]", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return map;
    }

    public static String createSearch(HttpServletRequest request, String qualifier) {
        String result = request.getParameter(qualifier + "_search");
        if(result == null) {
            Optional<Map.Entry<String, String[]>> searches = request.getParameterMap().entrySet().stream().filter( e -> StringUtils.equalsAnyIgnoreCase(qualifier  + "_search", e.getKey())).findFirst();
            if(searches != null && searches.isPresent() && searches.get().getValue().length > 0) {
                return searches.get().getValue()[searches.get().getValue().length - 1];
            }
        } else {
            return result;
        }
        
        return "";
    }

    public static Tuple2<String, Map<String, String>> createRemainingParams(HttpServletRequest request, String qualifier) {
        StringBuilder result = new StringBuilder();
        Map<String, String> resultMap = new HashMap<>();
        String prefix = qualifier + "_";
        Enumeration<String> n = request.getParameterNames();
        while (n.hasMoreElements()) {
            String k = n.nextElement();
            if (StringUtils.startsWithIgnoreCase(k, prefix)) { continue; }
            String[] vals = request.getParameterValues(k);
            String v = vals != null ? vals[vals.length - 1] : null;
            resultMap.put(k, v);
            result.append('&');
            result.append(k);
            result.append('=');
            result.append(v);
        }
        return Tuples.of(result.toString(), resultMap);
    }

    public static Map<String, String> createFilters(HttpServletRequest request, String qualifier) {
        Map<String, String> result = new HashMap<>();
        String prefix = qualifier + "_filter_";
        Enumeration<String> n = request.getParameterNames();
        while (n.hasMoreElements()) {
            String s = n.nextElement();
            if (s.toLowerCase().startsWith(prefix)) {
                String v = request.getParameter(s);
                if (StringUtils.isNotBlank(v)) {
                    result.put(s.substring(prefix.length()), request.getParameter(s));
                }
            }
        }

        return result;
    }

    public static Pageable createPageable(HttpServletRequest request, String qualifier) {
        int page = NumberUtils.isParsable(request.getParameter(qualifier + "_page")) ? Integer.parseInt(request.getParameter(qualifier + "_page")) : 0;
        int size = NumberUtils.isParsable(request.getParameter(qualifier + "_size")) ? Integer.parseInt(request.getParameter(qualifier + "_size")) : 10;
        String sortParam = request.getParameter(qualifier + "_sort");

        if (sortParam != null && !sortParam.isEmpty()) {
            String[] sortParams = sortParam.split(",");
            Sort sort;
            if (sortParams.length > 1) {
                Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
                sort = Sort.by(direction, sortParams[0]);
            } else {
                sort = Sort.by(sortParams[0]);
            }
            return PageRequest.of(page, size, sort);
        } else {
            return PageRequest.of(page, size);
        }
    }
}
