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

package com.openkoda.uicomponent;

import com.openkoda.model.notification.Notification;
import com.openkoda.uicomponent.annotation.Autocomplete;

public interface EventServices {

    @Autocomplete(doc="Emits a custom event with attached data (2nd param)")
    <T> boolean emitEventAsync(String name, T object);

    @Autocomplete(doc="Create global notification that affects all users in the system")
    Notification createGlobalNotification(String type, String message, String requiredPrivilege, String attachmentURL);

    @Autocomplete(doc="Create user notification that affects user with given identifier")
    Notification createUserNotification(String type, String message, String requiredPrivilege, Long userId, String attachmentURL);

    @Autocomplete(doc="Create organization notification that affects all users in the organization of given identifier")
    Notification createOrganizationNotification(String type, String message, Long organizationId, String requiredPrivilege, String attachmentURL);

    @Autocomplete(doc="Create role based organization notification that affects all users with role of given role identifier in the organization of given organization identifier")
    Notification createRoleOrganizationNotification(String type, String message, Long organizationId, String requiredPrivilege, String attachmentURL, Long roleId);
}
