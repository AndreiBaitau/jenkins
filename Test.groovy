job("Hometask20"){
   scm {
        github('AndreiBaitau/jenkins', 'master')
    }
            steps {
              shell('''
                ls -l
                pwd
                rm -rf /var/lib/jenkins/jobs/*0.*
                pwd
                ls -l /var/lib/jenkins/jobs/
                mv /var/lib/jenkins/workspace/Hometask20/jobs/* '/var/lib/jenkins/jobs/'
                ls -l /var/lib/jenkins/jobs/
''')
     
        }

    }
