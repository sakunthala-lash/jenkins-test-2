pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/sakunthala-lash/jenkins-test-2.git'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                     // If the build is triggered by a pull request, use the PR reference
                    if (env.CHANGE_ID) {
                        echo "Building Pull Request #${env.CHANGE_ID}"
                        // Checkout PR commit
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: "refs/pull/${env.CHANGE_ID}/merge"]],
                            userRemoteConfigs: [[url: "${GIT_REPO}"]]
                        ])
                    } else {
                        // Checkout main branch if not triggered by a PR
                        echo "Building branch: ${env.BRANCH_NAME}"
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: 'refs/heads/main']],
                            userRemoteConfigs: [[url: "${GIT_REPO}"]]
                        ])
                    }
                }
            }
        }
        stage('Debug PR Variables') {
            steps {
                script {
                    echo "BRANCH_NAME: ${env.BRANCH_NAME}"
                    echo "CHANGE_ID: ${env.CHANGE_ID}"
                    echo "CHANGE_TARGET: ${env.CHANGE_TARGET}"
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
                githubNotify('success', 'Jenkins build and tests passed!')
            }
        }
        failure {
            script {
                githubNotify('failure', 'Build or tests failed. Fix before merging!')
            }
        }
    }
}

def githubNotify(String status, String description) {
    withCredentials([string(credentialsId: 'id', variable: 'GITHUB_TOKEN')]) {
        def commitHash = bat(script: 'git rev-parse HEAD', returnStdout: true).trim().split("\r?\n")[-1].trim()

        bat """
            curl -X POST -H "Authorization: token %GITHUB_TOKEN%" ^
            -H "Accept: application/vnd.github.v3+json" ^
            -d "{\\"state\\": \\"${status}\\", \\"description\\": \\"${description}\\", \\"context\\": \\"Jenkins CI\\"}" ^
            "https://api.github.com/repos/sakunthala-lash/jenkins-test-2/statuses/${commitHash}"
        """
    }
}
