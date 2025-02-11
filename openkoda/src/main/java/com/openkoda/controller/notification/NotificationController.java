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

package com.openkoda.controller.notification;

import com.openkoda.core.security.HasSecurityRules;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.openkoda.controller.common.URLConstants.*;

@Controller
@RequestMapping({_HTML_ORGANIZATION_ORGANIZATIONID + _NOTIFICATION, _HTML + _NOTIFICATION})
public class NotificationController extends AbstractNotificationController implements HasSecurityRules {

    /**
     * <p>openAllOrganizationNotifications</p>
     * Gets all read and unread Notifications from repo as a NotificationKeeper object
     */
    @GetMapping(_ALL)
    public Object getAll(
         @PathVariable(value = ORGANIZATIONID, required = false) Long organizationId,
         @Qualifier(NOTIFICATION) Pageable notificationPageable,
         @RequestParam(required = false, defaultValue = "", name = "notification_search") String search) {
        debug("[getAll]");
        return getAllNotifications(organizationId, notificationPageable)
                .mav("notification-all");
    }

    /**
     * <p>markNotificationAsRead</p>
     * Marks all visible Notifications in dropdown as read
     */
    @PostMapping(_MARK_READ)
    public Object markNotificationAsRead(
            @PathVariable(value = ORGANIZATIONID, required = false) Long organizationId,
            @RequestParam("unreadNotifications") String unreadNotifications) {
        debug("[markNotificationAsRead]");
        markAsRead(unreadNotifications);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully marked notifications as read!");
    }

    @PostMapping(_ALL + _MARK_READ)
    public Object markReadAllNotifications(
            @PathVariable(value = ORGANIZATIONID, required = false) Long organizationId) {
        debug("[markReadAllNotifications]");
        markAllAsRead(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully marked all user's notifications as read!");
    }
}
