pipeline {
    agent any

    environment {
        REGISTRY = "https://gitee.com/yanhuibiao/xl.git"               // Docker 镜像仓库
        IMAGE_NAME = "your-project-name"             // 镜像名称
        DOCKER_CREDENTIALS_ID = "docker-credentials" // Jenkins 中的凭据 ID
        KUBECONFIG_CREDENTIALS_ID = "kubeconfig"     // K8s 凭据 ID
        BRANCH_NAME = "${env.BRANCH_NAME ?: 'main'}"
        VERSION = "v${env.BUILD_ID}"                 // 镜像版本号
    }

    parameters {
        string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Git 分支')
    }

    options {
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${params.GIT_BRANCH}", url: 'https://your.git.repo/your-project.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install -DskipTests -Pprod'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry("https://${REGISTRY}", DOCKER_CREDENTIALS_ID) {
                        def app = docker.build("${REGISTRY}/${IMAGE_NAME}:${VERSION}", ".")
                        app.push()
                        app.push("latest") // 可选：打 latest 标签
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
                    sh '''
                        export KUBECONFIG=$KUBECONFIG
                        kubectl set image deployment/${IMAGE_NAME} ${IMAGE_NAME}=${REGISTRY}/${IMAGE_NAME}:${VERSION} -n your-namespace
                        kubectl rollout status deployment/${IMAGE_NAME} -n your-namespace
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ 构建 & 部署成功，版本号：${VERSION}"
        }
        failure {
            echo "❌ 构建失败，请检查日志"
        }
    }
}
