pipeline {
    agent none

    stages {
        stage('bootstrap') {
            echo 'Hello world'
        }
    }
}
