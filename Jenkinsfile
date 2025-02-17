pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/sakunthala-lash/jenkins-test-2.git'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                      def branch = scm.branches[0].name.replaceFirst("*/", "")
                    echo "Detected Branch: ${branch}"

                        checkout([
                        $class: 'GitSCM',
                        branches: [[name: "refs/heads/${branch}"]],
                        userRemoteConfigs: [[url: env.GITHUB_REPO]]
                    ])
                    
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    try {
                        dir('C:/Users/sakua/.jenkins/workspace/Pipeline - 14022025/demo') {
                            bat 'mvn clean install'
                        }
                    } catch (Exception e) {
                        githubNotify('failure', 'Maven build failed. Check logs.')
                        throw e
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                echo "IN THE POST SUCCESS"
                githubNotify('success', 'Jenkins build and tests passed!')
            }
        }
        failure {
            script {
                echo "IN THE POST FAILURE"
                githubNotify('failure', 'Build or tests failed. Fix before merging!')
            }
        }
    }
}

def githubNotify(String status, String description) {
    withCredentials([string(credentialsId: 'id', variable: 'GITHUB_TOKEN')]) {
        def commitHash = bat(script: 'git rev-parse HEAD', returnStdout: true).trim().replaceAll("\r\n", "").replaceAll("\n", "")
        
        echo "Current Commit SHA: ${commitHash}"
                            echo "Detected Branch: ${branch}"

        bat """
            curl -X POST -H "Authorization: token %GITHUB_TOKEN%" ^
            -H "Accept: application/vnd.github.v3+json" ^
            -d "{\\"state\\": \\"${status}\\", \\"description\\": \\"${description}\\", \\"context\\": \\"Jenkins CI\\"}" ^
            "https://api.github.com/repos/sakunthala-lash/jenkins-test-2/statuses/${commitHash}"
        """
    }
}

