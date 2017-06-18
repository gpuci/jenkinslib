stage('bootstrap') {
    def jenkinslibScm = modernSCM(github(checkoutCredentialsId: '53aea1ee-975f-4a80-9ba1-bdf55f01e2c0',
                                         id: 'a6ad71fe-f669-4dcc-852d-0e3ce3734a78',
                                         repoOwner: 'gpuci',
                                         repository: 'jenkinslib'))

    def jobDefinition;
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
