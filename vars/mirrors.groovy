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

def onLoad() {
    echo 'Loaded library: mirrors.groovy'
}

def onMain() {
    stage('build') {
        node('build') {
            gpuci.updateMirror("llvm-git", "git@github.com:llvm-mirror/llvm.git", "master",
                               "llvm", "git@github.com:gpuci/llvm.git", "master")
            gpuci.updateMirror("libdrm-fdo", "git://anongit.freedesktop.org/mesa/drm", "master",
                               "libdrm", "git@github.com:gpuci/libdrm.git", "master")
            gpuci.updateMirror("mesa-fdo", "git://anongit.freedesktop.org/git/mesa/mesa", "master",
                               "mesa", "git@github.com:gpuci/mesa.git", "master")
        }
    }
}

def onError(e) {
    echo "Error: ${e}"
}

def onFinish() {
}
