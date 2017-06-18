// vars/amdgpu.groovy

def onLoad() {
    echo 'Loaded library: amdgpu.groovy'
}

def onMain() {
    echo 'In Main'
}

def onError(e) {
    echo 'In Error: ${e}'
}

def onFinish() {
    echo 'In Finish'
}
