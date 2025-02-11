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

import com.openkoda.core.form.AbstractOrganizationRelatedEntityForm;
import com.openkoda.core.form.CRUDControllerConfiguration;
import com.openkoda.core.security.HasSecurityRules;
import com.openkoda.core.security.OrganizationUser;
import com.openkoda.core.security.UserProvider;
import com.openkoda.model.file.File;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.openkoda.controller.common.URLConstants._HTML;
import static com.openkoda.controller.common.URLConstants._HTML_ORGANIZATION_ORGANIZATIONID;

/**
 * Controller that handles requests for generic controllers registered in {@link HtmlCRUDControllerConfigurationMap}.
 */
@RestController
@RequestMapping({_HTML_ORGANIZATION_ORGANIZATIONID + "/{obj}", _HTML + "/{obj}"})
public class CRUDControllerHtml extends AbstractCRUDController implements HasSecurityRules {

    public static final String OBJ_FILTER_PREFIX = "obj_filter_";
    @Value("${default.layout:main}")
    String defaultLayoutName;

    /** GET request that displays list of instances of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     * The list is restricted to the result of search with the search term {@param search}
     *
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @param tableFilterSearch
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @GetMapping(_ALL)
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object getAll(
            @PathVariable(name=ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey,
            @RequestParam(required = false, defaultValue = "", name = TABLE_FILTER) String tableFilterSearch, // fixme tableFilterSearch is not used
            HttpServletRequest request
            ) {
        debug("[getAll]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        OrganizationUser user = UserProvider.getFromContext().get();
        if (!user.isSuperUser()) {
            Set<Long> organizationIdsWithPrivilege = user.getOrganizationRoles().keySet().stream()
                                                       .filter( orgId -> hasGlobalOrOrgPrivilege(conf.getGetAllPrivilege(), orgId))
                                                       .collect(Collectors.toSet());
            if (notValidAccess(conf.getGetAllPrivilege(), conf.getOrganizationId(), organizationId) && organizationIdsWithPrivilege.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        return doGetAll(organizationId, objKey, tableFilterSearch, request)
                .mav(conf.getTableViewWebEndpoint() != null ? conf.getTableViewWebEndpoint() : conf.getTableView());
    }

    /** Displays a screen that allows to create a new instance of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @GetMapping(_NEW_SETTINGS)
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object create(
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey) {
        debug("[create]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getGetNewPrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doCreate(organizationId, objKey)
                .mav(conf.getSettingsView());
    }

    /** Displays a screen that allows to update an instance of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     *  The id of the instance is {@param objectId}
     * @param objectId - identifier of the entity
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @GetMapping(_ID_SETTINGS)
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object setting(
            @PathVariable(name = ID) Long objectId,
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey) {
        debug("[setting]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getGetSettingsPrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doSetting(objectId, organizationId, objKey)
                .mav(conf.getSettingsView());
    }

    /** Handles a request that creates a new instance of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     * The response is either success or error message depending on the validation of data provided in {@param form}
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @param form - data carrier
     * @param br - validation errors carrier
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @PostMapping(_NEW_SETTINGS)
    @Transactional
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object saveNew(
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey,
            @Valid AbstractOrganizationRelatedEntityForm<?,?> form, BindingResult br) {
        debug("[saveNew]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getPostNewPrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doSaveNew(organizationId, objKey, form, br)
                .mav(conf.getFormSuccessFragment(), conf.getFormErrorFragment());
    }

    /** Handles a request that updates an instance of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     *  The id of the instance is {@param objectId}. The response is either success or error message depending on the validation of data provided in {@param form}
     *
     * @param objectId - identifier of the entity
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @param form - data carrier
     * @param br - validation errors carrier
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @PostMapping(_ID_SETTINGS)
    @Transactional
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object save(
            @PathVariable(ID) Long objectId,
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey,
            @Valid AbstractOrganizationRelatedEntityForm<?,?> form, BindingResult br) {
        debug("[save]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getPostSavePrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doSave(objectId, organizationId, objKey, form, br)
                .mav(conf.getFormSuccessFragment(), conf.getFormErrorFragment());
    }

    /** Handles a request that deletes an instance of entity {@link CRUDControllerConfiguration#getEntityClass()} associated with a generic controller registered under {@param objKey}.
     *  The id of the instance is {@param objectId}. The response is either true or false depending on the success of the operation
     *
     * @param objectId - identifier of the entity
     * @param organizationId - indicates an organization to which this request is restricted
     * @param objKey - key under which the controller configuration is registered
     * @return {@linkplain com.openkoda.core.flow.PageModelMap view model}
     */
    @PostMapping(_ID_REMOVE)
    @Transactional
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object remove(
            @PathVariable(name=ID) Long objectId,
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey) {
        debug("[remove]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getPostRemovePrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doRemove(objectId, organizationId, objKey)
                .mav(a -> true, a -> false);
    }

    @GetMapping(_ID + _VIEW)
    //TODO Rule 1.4 All methods in non-public controllers must have @PreAuthorize
    public Object view(
            @PathVariable(name = ID) Long objectId,
            @PathVariable(name = ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey) {
        debug("[view]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getGetSettingsPrivilege(), conf.getOrganizationId(), organizationId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return doView(objectId, organizationId, objKey)
                .mav(conf.getReadView());
    }

    @Transactional(readOnly = true)
    @GetMapping(_REPORT + _CSV)
    public void getCsvReport(
            @PathVariable(name=ORGANIZATIONID, required = false) Long organizationId,
            @PathVariable(name="obj") String objKey,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {
        debug("[getCsvReport]");
        CRUDControllerConfiguration<?,?,?> conf = controllers.htmlCrudControllerConfigurationMap.getIgnoreCase(objKey);
        if (notValidAccess(conf.getGetAllPrivilege(), conf.getOrganizationId(), organizationId)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        File report = doGetCsvReport(organizationId, objKey, request)
                .get(file);
        services.file.getFileContentAndPrepareResponse(report, true, false, response);
    }

}
