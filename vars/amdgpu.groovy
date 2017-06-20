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

def amdgpuCheckout() {
    gpuci.githubCheckout('gpuci', 'mesa', 'master', 'mesa')
    gpuci.githubCheckout('gpuci', 'libdrm', 'master', 'libdrm')
    gpuci.githubCheckout('gpuci', 'jenkinslib', 'master', 'jenkinslib')
    gpuci.githubCheckout('gpuci', 'llvm', 'master', 'llvm')
}

def amdgpuBuild(dir) {
    def makeopts = "-f jenkinslib/amdgpu.mk -j${gpuci.getNodeThreads()} O=${dir}"
    sh "make ${makeopts} clean"
    sh "make ${makeopts} "
}

def amdgpuArchive(dir) {
    sh "tar --exclude='${dir}/build' -cjf ${dir}.tar.bz2 ${dir}"
    archive "${dir}.tar.bz2"
}

def onLoad() {
    echo 'Loaded library: amdgpu.groovy'
}

def onMain() {
    echo 'In Main'
    stage('build') {
        node('build') {
            def buildDir = "build/${env.JOB_BASE_NAME}-${env.BUILD_NUMBER}"
            sh 'rm -rf build/'

            amdgpuCheckout()
            amdgpuBuild(buildDir)
            amdgpuArchive(buildDir)
        }
    }
}

def onError(e) {
    echo "In Error: ${e}"
}

def onFinish() {
    echo 'In Finish'
}
