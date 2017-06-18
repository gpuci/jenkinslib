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

def gitCheckout(url, branch, dir) {
    echo "Checkout url:'${url}' branch:'${branch}' dir:'${dir}'"
    checkout([$class: 'GitSCM',
             branches: [[name: branch]],
             doGenerateSubmoduleConfigurations: false,
             extensions: [[$class: 'CleanCheckout'],
             [$class: 'RelativeTargetDirectory', relativeTargetDir: dir]],
             submoduleCfg: [],
             userRemoteConfigs: [[credentialsId: '53aea1ee-975f-4a80-9ba1-bdf55f01e2c0',
             url: url]]])
}

def githubCheckout(user, project, branch, dir) {
    gitCheckout("git@github.com:${user}/${project}.git", branch, dir)
}

def getNodeThreads() {
    return sh(returnStdout: true, script: 'nproc').trim()
}
