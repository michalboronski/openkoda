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

import com.openkoda.repository.Repositories;
import com.openkoda.repository.SecureRepositories;
import com.openkoda.repository.file.SecureFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;


@ExtendWith(MockitoExtension.class)
public abstract class AbstractWithSecureMocksTest {

    @Mock
    protected SecureFileRepository fileRepository;

    @Mock
    public SecureRepositories secure;

    @Mock
    public Repositories repositories;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field secureField = Repositories.class.getDeclaredField("secure");
        secureField.setAccessible(true);
        secureField.set(repositories, secure);

        Field fileField = SecureRepositories.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(secure, fileRepository);
    }

}
