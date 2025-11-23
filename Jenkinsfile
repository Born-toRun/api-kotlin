pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'dotori2/b2r-api:latest'
        BLUE_CONTAINER = 'b2r-api-blue'
        GREEN_CONTAINER = 'b2r-api-green'
        BLUE_PORT = '8443'
        GREEN_PORT = '8444'
        NGINX_PORT = '80'
        DOCKER_NETWORK = 'bridge'

        // JVM Options
        JVM_OPTS = '-Xms512m -Xmx768m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=8m -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled'

        JVM_APP_OPTIONS = """-Dserver.port=${INTERNAL_PORT} -Dserver.forward-headers-strategy=NATIVE -Dserver.servlet.session.cookie.secure=true -Dserver.servlet.session.cookie.http-only=true -Dserver.servlet.session.cookie.same-site=Lax -Ddatabase.host=${MYSQL_HOSTNAME} -Ddatabase.port=${MYSQL_PORT} -Ddatabase.database=${B2R_DATABASE} -Ddatabase.username=${B2R_DATABASE_USER} -Ddatabase.password=${B2R_DATABASE_PASSWORD} -Dkakao.apikey=${KAKAO_API_KEY} -Dkakao.client-secret=${KAKAO_CLIENT_SECRET} -Ddiscord.webhook=${B2R_DISCORD} -Dcors.origin=https://b2r.kro.kr,b2r-web.vercel.app,http://localhost:3000,http://localhost:48080,http://localhost:3001,https://client-gamma-khaki.vercel.app -Dminio.access-key=${MINIO_ACCESS_KEY} -Dminio.secret-key=${MINIO_SECRET_KEY} -Dminio.node=http://b2r.kro.kr:9000 -Dcdn.host=https://cdn.b2r.kro.kr -Dredis.host=${REDIS_HOST} -Dredis.port=${REDIS_PORT} -Dredis.password=${REDIS_PASSWORD} -Dkakao.redirectUrl=https://b2r.kro.kr:8443/login/oauth2/code/kakao -Dlogging.level.root=INFO"""

        // Application Options
        INTERNAL_PORT = '48080'
    }

    stages {
        stage('Pull Latest Image') {
            steps {
                script {
                    echo "Pulling latest image from Docker Hub..."
                    sh "docker pull ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Determine Active Environment') {
            steps {
                script {
                    // 현재 활성화된 컨테이너 확인
                    def blueRunning = sh(
                        script: "docker ps -q -f name=${BLUE_CONTAINER}",
                        returnStdout: true
                    ).trim()

                    if (blueRunning) {
                        env.ACTIVE_CONTAINER = BLUE_CONTAINER
                        env.ACTIVE_PORT = BLUE_PORT
                        env.INACTIVE_CONTAINER = GREEN_CONTAINER
                        env.INACTIVE_PORT = GREEN_PORT
                        env.NEW_ENV = 'GREEN'
                    } else {
                        env.ACTIVE_CONTAINER = GREEN_CONTAINER
                        env.ACTIVE_PORT = GREEN_PORT
                        env.INACTIVE_CONTAINER = BLUE_CONTAINER
                        env.INACTIVE_PORT = BLUE_PORT
                        env.NEW_ENV = 'BLUE'
                    }

                    echo "Active Environment: ${env.ACTIVE_CONTAINER} on port ${env.ACTIVE_PORT}"
                    echo "Deploying to: ${env.INACTIVE_CONTAINER} on port ${env.INACTIVE_PORT}"
                }
            }
        }

        stage('Deploy to Inactive Environment') {
            steps {
                script {
                    echo "Stopping and removing inactive container if exists..."
                    sh """
                        docker stop ${env.INACTIVE_CONTAINER} || true
                        docker rm ${env.INACTIVE_CONTAINER} || true
                    """

                    echo "Starting new container: ${env.INACTIVE_CONTAINER}"

                    sh """
                        docker run -d \
                            --name ${env.INACTIVE_CONTAINER} \
                            --network ${DOCKER_NETWORK} \
                            -p ${env.INACTIVE_PORT}:48080 \
                            -e JAVA_TOOL_OPTIONS='${JVM_OPTS} ${JVM_APP_OPTIONS}' \
                            --restart unless-stopped \
                            ${DOCKER_IMAGE}
                    """

                    echo "Waiting for container to start..."
                    sleep(time: 10, unit: 'SECONDS')
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    echo "Performing health check on ${env.INACTIVE_CONTAINER}..."

                    def maxRetries = 30
                    def retryCount = 0
                    def healthCheckPassed = false

                    HOST_IP=\$(ip route | grep default | awk '{print \$3}')
                    echo "Using host IP: \$HOST_IP"

                    while (retryCount < maxRetries && !healthCheckPassed) {
                        try {
                            def response = sh(
                                script: "curl -f -s -o /dev/null -w '%{http_code}' http://$HOST_IP:${env.ACTIVE_PORT}/",
                                returnStdout: true
                            ).trim()

                            if (response == '200') {
                                healthCheckPassed = true
                                echo "Health check passed! Response code: ${response}"
                            } else {
                                echo "Health check attempt ${retryCount + 1}/${maxRetries} - Response code: ${response}"
                            }
                        } catch (Exception e) {
                            echo "Health check attempt ${retryCount + 1}/${maxRetries} failed: ${e.message}"
                        }

                        if (!healthCheckPassed) {
                            sleep(time: 5, unit: 'SECONDS')
                            retryCount++
                        }
                    }

                    if (!healthCheckPassed) {
                        error("Health check failed after ${maxRetries} attempts")
                    }
                }
            }
        }

        stage('Switch Traffic') {
            steps {
                script {
                    echo "Switching traffic to ${env.INACTIVE_CONTAINER}..."

                    // Nginx 설정 업데이트 (실제 환경에 맞게 수정 필요)
                    sh """
                        # Nginx upstream을 새로운 포트로 변경
                        # 예시: sed를 사용하여 nginx 설정 파일 업데이트
                        # sudo sed -i 's/proxy_pass http:\\/\\/localhost:${env.ACTIVE_PORT}/proxy_pass http:\\/\\/localhost:${env.INACTIVE_PORT}/' /etc/nginx/sites-available/default
                        # sudo nginx -s reload

                        echo "Traffic switched to port ${env.INACTIVE_PORT}"
                    """

                    echo "Waiting for traffic to stabilize..."
                    sleep(time: 5, unit: 'SECONDS')
                }
            }
        }

        stage('Stop Old Environment') {
            steps {
                script {
                    echo "Stopping old container: ${env.ACTIVE_CONTAINER}"
                    sh """
                        docker stop ${env.ACTIVE_CONTAINER} || true
                    """

                    echo "Deployment completed successfully!"
                    echo "New active environment: ${env.INACTIVE_CONTAINER} on port ${env.INACTIVE_PORT}"
                }
            }
        }
    }

    post {
        success {
            echo "Blue-Green deployment completed successfully!"
            echo "Active Environment: ${env.NEW_ENV}"
        }
        failure {
            echo "Deployment failed! Rolling back..."
            script {
                // 실패 시 새로 배포한 컨테이너 중지
                sh """
                    docker stop ${env.INACTIVE_CONTAINER} || true
                    docker rm ${env.INACTIVE_CONTAINER} || true
                """
                echo "Rollback completed. Active environment remains: ${env.ACTIVE_CONTAINER}"
            }
        }
        always {
            // 로그 정리
            sh "docker system prune -f || true"
        }
    }
}
