// The library version is controlled from the Jenkins configuration
// To force a version add after lib '@' followed by the version.
@Library(value = 'msaas-shared-lib', changelog = false) _

node {
    // setup the global static configuration
    config = setupMsaasPipeline('msaas-config.yaml')
}

pipeline {

    agent {
        kubernetes {
            label "${config.pod_label}"
            yamlFile 'KubernetesPods.yaml'
        }
    }

    post {
        always {
            sendMetrics(config)
        }
        fixed {
            emailext(
                subject: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' ${currentBuild.result}", 
                body: """
                        Job Status for '${env.JOB_NAME} [${env.BUILD_NUMBER}]': ${currentBuild.result}\n\nCheck console output at ${env.BUILD_URL}
                """, 
                to: 'some_email@intuit.com'
            )
        }
        unsuccessful {
            emailext(
                subject: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' ${currentBuild.result}", 
                body: """
                        Job Status for '${env.JOB_NAME} [${env.BUILD_NUMBER}]': ${currentBuild.result}\n\nCheck console output at ${env.BUILD_URL}
                """, 
                to: 'some_email@intuit.com'
            )
        }
    }

    stages {
        stage('BUILD:') {
            when {
                anyOf {
                    branch 'msaas'
                    changeRequest()
                }
            }
            stages {
                stage('Get Next Release Version') {
                    steps {
                        container('cdtools') {
                            autoPRCheck()
                            autoReleaseVersion(config)
                        }
                    }
                }
                stage('Docker Multi Stage Build') {
                    steps {
                        container('podman') {
                            podmanBuild("--rm=false --build-arg=\"build=${env.BUILD_URL}\" --build-arg appVersion=${config.version} -t ${config.image_full_name} .")
                            podmanMount(podmanFindImage([image: 'build', build: env.BUILD_URL]), {steps,mount -> 
                                    sh(label: 'copy outputs to workspace', script: "cp -r ${mount}/usr/src/app/target ${env.WORKSPACE}")
                                })
                            podmanPush(config)
                            podmanTag(podmanFindImage([image: 'build', build: env.BUILD_URL]), config.test_image_full_name)
                            podmanPush([image_full_name: config.test_image_full_name, 
                                    service_name: config.service_name, 
                                    registry: config.registry])
                        }
                    }
                }
                stage('Publish') {
                    post {
                        always {
                            sendBuildMetrics(config)
                        }
                    }
                    parallel {
                        stage('Report Coverage & Unit Test Results') {
                            steps {
                                junit '**/surefire-reports/**/*.xml'
                                jacoco()
                                codeCov(config)
                            }
                        }
                        stage('CPD Certification & Publish') {
                            steps {
                                container('cpd2') {
                                    intuitCPD2Podman(config, "-i ${config.image_full_name} --buildfile Dockerfile")
                                }
                                container('podman') {
                                    podmanPull(config, config.image_full_name)
                                    podmanInspect(config, '-s', 'image-metadata.json')
                                    archiveArtifacts(artifacts: 'image-metadata.json', allowEmptyArchive: true)
                                }
                            }
                        }
                        stage('Code Analysis') {
                            when {expression {return config.SonarQubeAnalysis}}
                            steps {
                                container('podman') {
                                    // copy from container bundle for sonar analysis
                                    podmanMount(podmanFindImage([image: 'build', build: env.BUILD_URL]), {steps,mount -> 
                                            sh(label: 'copy outputs to workspace', script: "cp -r ${mount}/usr/src ${env.WORKSPACE}/bundle")
                                        })
                                    podmanBuild("-f Dockerfile.sonar --build-arg=\"sonar=${config.SonarQubeEnforce}\" .")
                                }
                            }
                        }
                    }
                }
                stage('Create Release') {
                    when {expression {return env.BRANCH_NAME == 'master' && !skipRelease(config)}}
                    options {
                        lock(resource: 'git', inversePrecedence: true)
                        timeout(time: 22, unit: 'MINUTES')
                    }
                    steps {
                        container('cdtools') {
                            createRelease(config)
                        }
                    }
                }
                // jira transitioning
                stage('Transition Jira Tickets') {
                    steps {
                        script {
                            if (env.BRANCH_NAME != 'msaas' && changeRequest()) {
                                transitionJiraTickets(config, 'Ready for Review')
                            } else if (env.BRANCH_NAME == 'msaas') {
                                transitionJiraTickets(config, 'Closed')
                            }
                        }
                    }
                }
            }
        }
        stage('PR:') {
            when {changeRequest()}
            post {
                success {
                    transitionJiraTickets(config, 'Ready for Review')
                }
            }
            stages {
                stage('Build karate container') {
                    steps {
                        container('podman') {
                            podmanBuild("--rm=false -t ${config.karate_image_full_name} -f Dockerfile.karate .")
                            podmanPush([image_full_name: config.karate_image_full_name, 
                                    service_name: config.service_name, 
                                    registry: config.registry])
                        }
                    }
                }
                stage('Component Test') {
                    options {
                        timeout(time: 20, unit: 'MINUTES')
                    }
                    agent {
                        kubernetes {
                            yaml """
                        apiVersion: v1
                        kind: Pod
                        spec:
                            containers:
                             - name: karate
                               image: ${config.karate_image_full_name}
                               tty: true
                               imagePullPolicy: Always
                               readinessProbe:
                                 httpGet:
                                   scheme: HTTP
                                   path: /health/full
                                   port: 9000
                                 initialDelaySeconds: 15
                                 periodSeconds: 5
                             - name: app
                               image: ${config.image_full_name}
                               tty: true
                               imagePullPolicy: Always
                               env:
                                 - name: APP_NAME
                                   value: ${config.service_name}
                                 - name: APP_ENV
                                   value: integration
                                 - name: ASSET_ID
                                   value: ${config.asset_id}
                                 - name: ASSET_ALIAS
                                   value: ${config.asset_alias}
                                 - name: L1
                                   value: ${config.l1}
                                 - name: L2
                                   value: ${config.l2}
                               readinessProbe:
                                 httpGet:
                                   scheme: HTTPS
                                   path: /health/full
                                   port: 8443
                                 initialDelaySeconds: 15
                                 periodSeconds: 5
                             - name: test
                               image: ${config.test_image_full_name}
                               tty: true
                               command: [ "cat" ]
                               imagePullPolicy: Always
                    """
                        }
                    }
                    stages {
                        stage('Component Functional/Perf Tests') {
                            parallel {
                                stage('Functional Tests') {
                                    post {
                                        success {
                                            githubNotify context: 'Component Functional Tests', credentialsId: 'github-svc-sbseg-ci', gitApiUrl: config['github_api_endpoint'], description: 'Tests Passed', status: 'SUCCESS', targetUrl: env.JOB_URL + '/' + env.BUILDNUMBER + '/Component_20Functional_20Test_20Results'
                                        }
                                        failure {
                                            githubNotify context: 'Component Functional Tests', credentialsId: 'github-svc-sbseg-ci', gitApiUrl: config['github_api_endpoint'], description: 'Tests are failing', status: 'FAILURE', targetUrl: env.RUN_DISPLAY_URL
                                        }
                                    }
                                    steps {
                                        script {
                                            try {
                                                container('test') {
                                                    sh label: 'Run Component Tests', script: 'mvn -s settings.xml -f app/pom.xml test -Pkarate -Dkarate.env=mock -Dkarate.mock.port=9000 -Dkarate.server.port=8443'
                                                }
                                            }finally {
                                                publishHTML target: [
                                                        allowMissing: false, 
                                                        alwaysLinkToLastBuild: false, 
                                                        keepAll: true, 
                                                        reportDir: 'app/target/cucumber-html-reports', 
                                                        reportFiles: 'overview-features.html', 
                                                        reportName: 'Component Functional Test Results'
                                                    ]
                                            }
                                        }
                                    }
                                }
                                stage('Perf Tests') {
                                    post {
                                        success {
                                            githubNotify context: 'Component Perf Tests', credentialsId: 'github-svc-sbseg-ci', gitApiUrl: config['github_api_endpoint'], description: 'Tests Passed', status: 'SUCCESS', targetUrl: env.JOB_URL + '/' + env.BUILDNUMBER + '/Component_20Perf_20Test_20Results'
                                        }
                                        failure {
                                            githubNotify context: 'Component Perf Tests', credentialsId: 'github-svc-sbseg-ci', gitApiUrl: config['github_api_endpoint'], description: 'Tests are failing', status: 'FAILURE', targetUrl: env.RUN_DISPLAY_URL
                                        }
                                    }
                                    steps {
                                        script {
                                            try {
                                                container('test') {
                                                    sh label: 'Run Perf Tests', script: 'mvn -s settings.xml -f app/pom.xml gatling:test -Pperf -Dkarate.env=mock -Dkarate.mock.port=9000 -Dkarate.server.port=8443'
                                                }
                                            }finally {
                                                publishHTML target: [
                                                        allowMissing: false, 
                                                        alwaysLinkToLastBuild: false, 
                                                        keepAll: true, 
                                                        reportDir: 'app/target/gatling', 
                                                        reportFiles: '**/index.html', 
                                                        reportName: 'Component Perf Test Results'
                                                    ]
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('qal-usw2-eks') {
            when {
                beforeOptions true
                allOf {
                    branch 'msaas'
                    not {changeRequest()}
                }
            }
            post {
                always {
                    sendDeployMetrics(config, [boxsetVersion: "${config.image_full_name}", envName: 'qal-usw2-eks'])
                }
            }
            options {
                lock(resource: getEnv(config, 'qal-usw2-eks').namespace, inversePrecedence: true)
                timeout(time: 32, unit: 'MINUTES')
            }
            stages {
                stage('Scorecard Check') {
                    when {expression {return config.enableScorecardReadinessCheck}}
                    steps {
                        scorecardPreprodReadiness(config, 'qal-usw2-eks')
                    }
                }
                stage('Deploy') {
                    steps {
                        container('cdtools') {
                            // This has to be the first action in the first sub-stage
                            milestone(ordinal: 10, label: 'Deploy-qal-usw2-eks-milestone')
                            gitOpsDeploy(config, 'qal-usw2-eks', config.image_full_name)
                        }
                    }
                }
                stage('Test') {
                    steps {
                        script {
                            if (env.BUILD_NUMBER != '1') {
                                retry(5) {
                                    script {
                                        try {
                                            container('test') {
                                                sh label: 'Run Karate Tests', script: "mvn -s settings.xml -f app/pom.xml verify -Dmaven.repo.local=/var/run/shared-m2/repository -Pkarate-remote -Dkarate.env=qal-usw2-eks -DkubernetesServiceName=${config.kubernetesServiceName} -DjacocoServiceEndpoint=${config['environments']['qal-usw2-eks']['jacoco_endpoint']}"
                                            }
                                        }finally {
                                            publishHTML target: [
                                                    allowMissing: true, 
                                                    alwaysLinkToLastBuild: false, 
                                                    keepAll: true, 
                                                    reportDir: 'app/target/cucumber-html-reports', 
                                                    reportFiles: 'overview-features.html', 
                                                    reportName: 'Integration test results'
                                                ]
                                            publishHTML target: [
                                                    allowMissing: true, 
                                                    alwaysLinkToLastBuild: false, 
                                                    keepAll: true, 
                                                    reportDir: 'app/target/test-results/coverage/jacoco/', 
                                                    reportFiles: 'index.html', 
                                                    reportName: 'Integration test coverage'
                                                ]
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Transition Jira Tickets') {
                    when {expression {return config.enableJiraTransition}}
                    steps {
                        transitionJiraTickets(config, 'Deployed to PreProd')
                    }
                }
            }
        }
        stage('e2e-usw2-eks') {
            when {
                beforeOptions true
                allOf {
                    branch 'msaas'
                    not {changeRequest()}
                }
            }
            post {
                always {
                    sendDeployMetrics(config, [boxsetVersion: "${config.image_full_name}", envName: 'e2e-usw2-eks'])
                }
            }
            options {
                lock(resource: getEnv(config, 'e2e-usw2-eks').namespace, inversePrecedence: true)
                timeout(time: 32, unit: 'MINUTES')
            }
            stages {
                stage('Scorecard Check') {
                    when {expression {return config.enableScorecardReadinessCheck}}
                    steps {
                        scorecardPreprodReadiness(config, 'e2e-usw2-eks')
                    }
                }
                stage('Deploy') {
                    steps {
                        container('cdtools') {
                            // This has to be the first action in the first sub-stage
                            milestone(ordinal: 20, label: 'Deploy-e2e-usw2-eks-milestone')
                            gitOpsDeploy(config, 'e2e-usw2-eks', config.image_full_name)
                        }
                    }
                }
                stage('Test') {
                    steps {
                        script {
                            if (env.BUILD_NUMBER != '1') {
                                script {
                                    try {
                                        container('test') {
                                            sh label: 'Run Karate Tests', script: "mvn -s settings.xml -f app/pom.xml verify -Dmaven.repo.local=/var/run/shared-m2/repository -Pkarate -Dkarate.env=${getEnvName(config, 'e2e-usw2-eks')}"
                                        }
                                    }finally {
                                        publishHTML target: [
                                                allowMissing: true, 
                                                alwaysLinkToLastBuild: false, 
                                                keepAll: true, 
                                                reportDir: 'app/target/cucumber-html-reports', 
                                                reportFiles: 'overview-features.html', 
                                                reportName: 'Integration test results'
                                            ]
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Transition Jira Tickets') {
                    when {expression {return config.enableJiraTransition}}
                    steps {
                        transitionJiraTickets(config, 'Deployed to PreProd')
                    }
                }
            }
        }
        stage('prf-usw2-eks') {
            when {
                beforeOptions true
                allOf {
                    branch 'master'
                    not {changeRequest()}
                }
            }
            post {
                always {
                    sendDeployMetrics(config, [boxsetVersion: "${config.image_full_name}", envName: 'prf-usw2-eks'])
                }
            }
            options {
                lock(resource: getEnv(config, 'prf-usw2-eks').namespace, inversePrecedence: true)
                timeout(time: 32, unit: 'MINUTES')
            }
            stages {
                stage('Scorecard Check') {
                    when {expression {return config.enableScorecardReadinessCheck}}
                    steps {
                        scorecardPreprodReadiness(config, 'prf-usw2-eks')
                    }
                }
                stage('Deploy') {
                    steps {
                        container('cdtools') {
                            // This has to be the first action in the first sub-stage
                            milestone(ordinal: 30, label: 'Deploy-prf-usw2-eks-milestone')
                            gitOpsDeploy(config, 'prf-usw2-eks', config.image_full_name)
                        }
                    }
                }
                stage('Test') {
                    steps {
                        retry(5) {
                            script {
                                try {
                                    container('test') {
                                        sh label: 'Run Karate Tests', script: "mvn -s settings.xml -f app/pom.xml verify -Dmaven.repo.local=/var/run/shared-m2/repository -Pkarate -Dkarate.env=${getEnvName(config, 'prf-usw2-eks')}"
                                    }
                                }finally {
                                    publishHTML target: [
                                            allowMissing: true, 
                                            alwaysLinkToLastBuild: false, 
                                            keepAll: true, 
                                            reportDir: 'app/target/cucumber-html-reports', 
                                            reportFiles: 'overview-features.html', 
                                            reportName: 'Integration test results'
                                        ]
                                }
                            }
                        }
                    }
                }
                stage('Transition Jira Tickets') {
                    when {expression {return config.enableJiraTransition}}
                    steps {
                        transitionJiraTickets(config, 'Deployed to PreProd')
                    }
                }
            }
        }
    }
}