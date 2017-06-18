pipeline {
    agent none

    stages {
        stage('bootstrap') {
            steps {
                echo 'Hello world'
            }
        }
    }
}
