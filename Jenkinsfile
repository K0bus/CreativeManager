pipeline {
    agent any
    tools {
        maven "3.9.9"
        jdk "openjdk-23.0.2"
    }
    options {
        disableConcurrentBuilds()
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install -Dbuild.number=${BUILD_NUMBER}'
            }
        }
        stage('Archive Artifacts') {
            steps {
                sh 'rm -rf artifacts'
                sh 'mkdir artifacts'
                sh 'cp target/CreativeManager*.jar artifacts/'
                archiveArtifacts artifacts: 'artifacts/*.jar', followSymlinks: false
            }
        }
    }
}
