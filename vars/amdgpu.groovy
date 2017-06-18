// vars/amdgpu.groovy

def onLoad() {
    gpuci.say 'Loaded library: amdgpu.groovy'
}

def onMain() {
    gpuci.say 'In Main'
}

def onError(e) {
    gpuci.say 'In Error: ${e}'
}

def onFinish() {
    gpuci.say 'In Finish'
}
