pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/sakunthala-lash/jenkins-test-2.git'
    }

    stages {
        stage('Display Ref') {
            steps {
                script {
                    // Access the Git ref (branch or tag) on Windows using Batch commands
                    def gitRef = bat(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    echo "The Git reference (ref) is: ${gitRef}"
                    
                    // Get commit hash from the current HEAD
                    def commitHash = bat(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    echo "Building commit: ${commitHash}"
                }
            }
        }
        
        stage('Checkout') {
            steps {
                script {
                    echo "Building for PR branch: ${params.GIT_BRANCH}"
                    
                    // Checkout specific Git ref
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${gitRef}"]],
                        userRemoteConfigs: [[url: "${GITHUB_REPO}"]]
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
                        githubNotify('failure', 'Maven build failed. Check logs.', commitHash)
                        throw e
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                githubNotify('success', 'Jenkins build and tests passed!', commitHash)
            }
        }
        failure {
            script {
                githubNotify('failure', 'Build or tests failed. Fix before merging!', commitHash)
            }
        }
    }
}

def githubNotify(String status, String description, String commitHash) {
    withCredentials([string(credentialsId: 'id', variable: 'GITHUB_TOKEN')]) {
        echo "Notifying GitHub for commit: ${commitHash}"

        bat """
            curl -X POST -H "Authorization: token %GITHUB_TOKEN%" ^
            -H "Accept: application/vnd.github.v3+json" ^
            -d "{\\"state\\": \\"${status}\\", \\"description\\": \\"${description}\\", \\"context\\": \\"Jenkins CI\\"}" ^
            "https://api.github.com/repos/sakunthala-lash/jenkins-test-2/statuses/${commitHash}"
        """
    }
}
