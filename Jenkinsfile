pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven_3_9'
    }

    environment {
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE = 's13sh/ebanking:latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'home', url: 'https://github.com/saishkulkarni/ebanking-SpringBoot-RestApi'
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t ebanking:latest .'
                    bat "docker tag ebanking:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE}"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                    echo %DOCKER_PASS% | docker login %DOCKER_REGISTRY% -u %DOCKER_USER% --password-stdin
                    docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}
                    """
                }
            }
        }

        stage('Trigger Render Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'render-api-key', variable: 'RENDER_API_KEY'),
                    string(credentialsId: 'render-service-id', variable: 'RENDER_SERVICE_ID')
                ]) {
                    bat """
                    echo Triggering Render deployment via API for service: %RENDER_SERVICE_ID%
                    curl -X POST "https://api.render.com/v1/services/%RENDER_SERVICE_ID%/deploys" ^
                        -H "Accept: application/json" ^
                        -H "Authorization: Bearer %RENDER_API_KEY%" ^
                        -H "Content-Type: application/json" ^
                        -d "{}"
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment triggered successfully!'
        }
        failure {
            echo '❌ Build or deploy failed!'
        }
    }
}
