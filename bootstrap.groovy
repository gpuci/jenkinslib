/**
 * Copyright 2017 Andres Rodriguez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

def jobDefinition = ''

stage('bootstrap') {
    def jenkinslibScm = modernSCM(github(checkoutCredentialsId: '53aea1ee-975f-4a80-9ba1-bdf55f01e2c0',
                                         id: 'a6ad71fe-f669-4dcc-852d-0e3ce3734a78',
                                         repoOwner: 'gpuci',
                                         repository: 'jenkinslib'))
    library identifier: 'gpuci@master', retriever: jenkinslibScm

    switch("${JOB_NAME}") {
        case 'amdgpu-master':
            library identifier: 'amdgpu@master', retriever: jenkinslibScm
            jobDefinition = amdgpu
            break
        default:
            error "Failed to identify project"
    }

    jobDefinition.onLoad()
}

try {
    jobDefinition.onMain()
}
catch(e) {
    jobDefinition.onError(e)
}
finally {
    jobDefinition.onFinish()
}
