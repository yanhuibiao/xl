pipeline {
    agent any

    environment {
        REGISTRY = "https://gitee.com/yanhuibiao/xl.git"               // Docker 镜像仓库
        IMAGE_NAME = "your-project-name"             // 镜像名称
        DOCKER_CREDENTIALS_ID = "docker-credentials" // Jenkins 中的凭据 ID
        KUBECONFIG_CREDENTIALS_ID = "kubeconfig"     // K8s 凭据 ID
        BRANCH_NAME = "${env.BRANCH_NAME ?: 'main'}"
        VERSION = "v${env.BUILD_ID ?: '0.0.1-SNAPSHOT'}"         // 镜像版本号
    }

    tools {
        maven 'globalMaven'
    }

    parameters {
        string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Git 分支,会自动在构建项目中添加参数')
        choice(
                name: 'PROJECT_MODULE',
                choices: ['all', 'gateway-service'],
                description: '选择要构建的模块'
        )
        string(
                name: 'IMAGE_TAG',
                defaultValue: '0.0.1-SNAPSHOT',
                description: '镜像标签'
        )
    }

    options {
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://gitee.com/yanhuibiao/xl.git']])
            }
        }

        // stage('Build with Maven') {
        //     steps {
        //         sh '''
        //         cd ./xl-backend
        //         echo "可以换行"
        //         ls
        //         '''
        //         sh 'cd ./xl-backend && /var/jenkins_home/maven/bin/mvn clean install -DskipTests -Pprod'
        //     }
        // }

        stage('Copy the JAR package to the specified directory') {
            steps {
                sh '''# 定义源目录和目标目录
                    SOURCE_DIR="./xl-backend"
                    TARGET_DIR="./docker"
                    # 检查目标目录是否存在，不存在则创建
                    if [ ! -d "$TARGET_DIR" ]; then
                        mkdir -p "$TARGET_DIR"
                        echo "目标目录 $TARGET_DIR 已创建"
                    fi
                    # 查找所有包含jar文件的目录
                    jar_dirs=$(find "$SOURCE_DIR" -type f -name "xl-*.jar" -exec dirname {} \\; | sort | uniq)
                    # 检查是否找到任何jar文件
                    if [ -z "$jar_dirs" ]; then
                        echo "在 $SOURCE_DIR 及其子目录中未找到任何JAR文件"
                        exit 1
                    fi
                    # 循环处理每个包含jar文件的目录
                    for dir in $jar_dirs; do
                        echo "正在处理目录: $dir"
                        # 先用dirname获取去掉最后一级后的路径
                        # 再用basename获取这个路径的最后一部分
                        dir_name=$(basename "$(dirname "$dir")")
                        # 在目标目录下创建对应的子目录
                        mkdir -p "$TARGET_DIR/$dir_name"
                        # 复制该目录下的所有jar文件到目标子目录
                        find "$dir" -maxdepth 1 -type f -name "xl-*.jar" -exec cp {} "$TARGET_DIR/$dir_name" \\;
                        echo "已复制 $dir 下的JAR文件到 $TARGET_DIR/$dir_name"
                    done
                    echo "所有JAR文件复制完成"'''
            }
        }

        stage('Find JAR files') {
            steps {
                script {
                    // 使用shell命令查找所有JAR文件并获取文件名(不带路径),tr -d "[]"去除中括号
                    def jarPaths = sh(
                            script: 'find "./docker" -type f -name "xl-*.jar" -exec dirname {} \\;| sort | uniq | tr -d "[]"',
                            returnStdout: true
                    ).trim().split('\n')
                    // 将JAR文件列表保存到环境变量中供后续阶段使用,删除目录中./docker/
                    def jarPath = []
                    jarPaths.each { jar ->
                        def path = jar.substring(9)
                        if(!jarPath.contains(path)) {
                            jarPath.add(path)
                        }
                    }
                    env.JAR_FILES = jarPath
                    // 打印找到的JAR文件列表
                    echo "Found JAR files: ${env.JAR_FILES}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    if (params.PROJECT_MODULE == "all") {
                        // 分割模块列表
                        def jar_package = env.JAR_FILES.split(',')
                        // 构建所有模块
                        jar_package.each { jar ->
                            echo jar
                        }
                    } else {
                        // 构建指定模块
                        sh "docker build -f ./xl-backend/xl-deploy/src/main/resources/docker/build/Dockerfile -t ${params.PROJECT_MODULE}:${params.IMAGE_TAG} ./docker/"
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
