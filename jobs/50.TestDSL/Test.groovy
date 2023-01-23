pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    stages {
        stage('Clone repository') {
            steps {
                git url: 'https://github.com/AndreiBaitau/jenkins.git', branch: 'master'
            }
        }
        stage('Checking repository'){
            steps {
                sh "ls -l"
                sh "pwd"
                sh "rm -rf /var/lib/jenkins/jobs/*0.*"
            }
        }
        stage('Moving jobs from git to jenkins_home'){
            steps{
                sh "pwd"
                sh "ls -l /var/lib/jenkins/jobs/"
                sh "mv /var/lib/jenkins/workspace/LetsTry/jobs/* '/var/lib/jenkins/jobs/'"
                sh "ls -l /var/lib/jenkins/jobs/"
            }
        }
        }
    }
