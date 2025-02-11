#!/bin/bash

# How to use this script:
# update license comment if necessary
# put the script in the module which classes are meant to be updated e.g. '/openkoda' or '/openkoda-extensions/documents'
# run the script

# creating license comment snippet
cat << 'EOF' > license-comment.txt
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

EOF

# creating list of java classes that misses license comment
grep -L "MIT License" -r ./src --include="*.java" > java-classes-missing-license-comment.txt

# updating java classes with missing license comment
while IFS= read -r file; do
    cat license-comment.txt "$file" > temp && mv temp "$file"
done < java-classes-missing-license-comment.txt

# cleaning
rm license-comment.txt java-classes-missing-license-comment.txt