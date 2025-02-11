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

package com.openkoda;

import com.openkoda.core.helper.SpringProfilesHelper;
import com.openkoda.repository.NativeQueries;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.openkoda.core.helper.SpringProfilesHelper.SPRING_PROFILES_ACTIVE_ENV;
import static com.openkoda.core.helper.SpringProfilesHelper.SPRING_PROFILES_ACTIVE_PROP;
import static com.openkoda.service.dynamicentity.DynamicEntityRegistrationService.buildAndLoadDynamicClasses;

public class OpenkodaApp {

    @Autowired
    NativeQueries nativeQueries;

    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException {
        boolean isForce = args != null && Arrays.stream(args).anyMatch(a -> "--force".equals(a));
        App.initializationSafetyCheck(isForce);
        OpenkodaApp.startOpenkodaApp(App.class, args);
    }

    public static void startOpenkodaApp(Class appClass, String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
        setProfiles(args);
        JDBCApp.main(args);
        if(!SpringProfilesHelper.isInitializationProfile()) {
            buildAndLoadDynamicClasses(App.class.getClassLoader());
        }
        App.startApp(appClass, args, true);
    }
    /* Used for setting active spring profiles when they are provided in args array

    * */
    private static void setProfiles(String[] args){
        if(System.getProperty(SPRING_PROFILES_ACTIVE_PROP) == null && args != null && args.length > 0){
            //profiles are set as param in command line (for components app)
            //important: the argument in command line -Dspring-boot.run.profiles is switched to --spring.profiles.active in args variable
            List<String> filteredArgs = Stream.of(args).filter(a -> a.contains(SPRING_PROFILES_ACTIVE_PROP)).toList();
            if(!filteredArgs.isEmpty()) {
                System.setProperty(SPRING_PROFILES_ACTIVE_PROP, filteredArgs.get(0).split("=")[1]);
            } else if (System.getenv(SPRING_PROFILES_ACTIVE_ENV) != null) {
//                In case we start Openkoda as a cloud instance configured via environment properties in a Dockerfile
                System.setProperty(SPRING_PROFILES_ACTIVE_PROP, System.getenv(SPRING_PROFILES_ACTIVE_ENV));
            }
        }
    }
}
