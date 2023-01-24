pipeline {
      environment {
    registry = "andreibaitov/jenkins"
    registryCredential = 'docker'

  }
      agent { label 'master'}
  stages {
      

    stage('Cloning Git') {
      steps {
        
        
        git 'https://github.com/AndreiBaitau/jenkins.git'
        sh 'ls -l'
      }
    }

    stage ("Lint dockerfile") {
        agent {
            docker {
                image 'hadolint/hadolint:latest-debian'
                label 'master'
            }
        }
        steps {
            sh 'hadolint Dockerfile | tee -a hadolint_lint.txt'
        }
        post {
            always {
                archiveArtifacts 'hadolint_lint.txt'
                            
            }

        }
    }
   
    stage('Building image') {
      steps{ 

        script {
             dockerImage = docker.build registry + ":$BUILD_NUMBER" , "--network host ."
        }
      }
    }
   

    stage('Push Image to repo') {
      steps{
        script {
            docker.withRegistry( 'andreibaitov/jenkins', registryCredential ) {
            dockerImage.push()
          }
        }
      }
    }
    stage('Deploy in pre-prod') {
      steps{
          withKubeConfig([credentialsId: 'mykubeconfig']) {
          sh "kubectl get pods --namespace=pre=prod"
          sh "kubectl apply -f jenkins.yaml --namespace=pre-prod"
          sleep 4
          sh "kubectl get pods --namespace=pre=prod"
          }
      }
    }
    stage('Deploy in prod') {
      steps{
        script {
          catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE'){
            def depl = true
            try{
              input("Deploy in prod?")
            }
            catch(err){
              depl = false
            }
            try{
              if(depl){
                withKubeConfig([credentialsId: 'mykubeconfig']) {
                sh "kubectl apply -f jenkins.yaml --namespace=prod"
                sleep 4
                sh "kubectl get pods --namespace=prod"
                sh "kubectl delete -f jenkins.yaml --namespace=pre-prod"
                }
              }
            }
            catch(Exception err){
              error "Deployment failed"
            }
          }
        }
      }
    }
  }
  post {
    success {
      slackSend (color: '#00FF00', message: "Deployment success: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'")
    }
    failure {
      slackSend (color: '#FF0000', message: "Deployment failed: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'")
    }
  }
}