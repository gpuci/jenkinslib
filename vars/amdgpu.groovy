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

String tarArchive;

def amdgpuCheckout() {
    gpuci.githubCheckout('gpuci', 'mesa', 'master', 'mesa')
    gpuci.githubCheckout('gpuci', 'libdrm', 'master', 'libdrm')

    if (gpuci.inDevEnv()) {
        gpuci.githubCheckout('gpuci', 'jenkinslib', 'dev', 'jenkinslib')
    } else {
        gpuci.githubCheckout('gpuci', 'jenkinslib', 'master', 'jenkinslib')
    }

    gpuci.githubCheckout('gpuci', 'jenkinslib', 'master', 'jenkinslib')
    gpuci.githubCheckout('gpuci', 'llvm', 'master', 'llvm')
}

def amdgpuBuild(dir) {
    def makeopts = "-f jenkinslib/amdgpu.mk -j${gpuci.getNodeThreads()} O=${dir}"
    sh "make ${makeopts} clean"
    sh "make ${makeopts} "
}

def amdgpuArchive(dir) {
    def archiveDir = gpuci.dirname(dir)
    def amdgpu.tarArchive = gpuci.basename(dir)
    def nodeRoot = pwd()

    sh ("cd ${archiveDir} && tar -cj" + \
        " --exclude='${amdgpu.tarArchive}/build'" + \
        " -f ${nodeRoot}/${amdgpu.tarArchive}.tar.bz2" + \
        " ${amdgpu.tarArchive}")

    archiveArtifacts "**/${amdgpu.tarArchive}.tar.bz2"
}

def buildStage() {
    node('build') {
        def buildDir = "build/${env.JOB_BASE_NAME}-${env.BUILD_NUMBER}"
        sh "rm -rf build/"
        sh "rm -f ${env.JOB_BASE_NAME}-*"

        amdgpuCheckout()
        amdgpuBuild(buildDir)
        amdgpuArchive(buildDir)
    }
}

def testStage() {
    node ('kv') {
        unarchive mapping: ["${amdgpu.tarArchive}.tar.bz2": "${amdgpu.tarArchive}.tar.bz2.tar.bz2"]
        sh "tar xjf ${amdgpu.tarArchive}"
        sh "find ."
    }
}

def onLoad() {
    echo 'Loaded library: amdgpu.groovy'
}

def onMain() {
    echo 'In Main'
    stage('build') {
        buildStage()
    }

    stage('test') {
        testStage()
    }
}

def onError(e) {
    echo "In Error: ${e}"
}

def onFinish() {
    echo 'In Finish'
}
