pipeline {
    agent any

    environment {
        GITHUB_REPO = 'https://github.com/sakunthala-lash/jenkins-test-2.git'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "refs/heads/${env.GIT_BRANCH}"]],
                        userRemoteConfigs: [[url: GITHUB_REPO]]
                    ])
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
            when {
                expression { env.CHANGE_ID != null } // Run only for PRs
            }
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
