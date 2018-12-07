pipeline {
  agent {
    node {
      label "mobile"
    }
  }
  tools {
    jdk 'jdk1.8.0_92'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    timeout(time: 1, unit: 'HOURS')
    timestamps()
  }
  stages {
    stage('Build') {
      steps {
        bat """
            set ANDROID_HOME=C:\\Users\\build\\.android-sdk
            gradlew build
          """
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: '**/build/outputs/apk/release/*.apk', fingerprint: true
      androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/build/reports/lint-results*.xml', unHealthy: ''
      junit allowEmptyResults: true, testResults: "**/build/test-results/**/*.xml"
      pmd canComputeNew: false, defaultEncoding: '', failedTotalHigh: '1', healthy: '', pattern: '**/build/reports/pmd-output.xml', unHealthy: '', unstableTotalAll: '1'
      findbugs canComputeNew: false, defaultEncoding: '', excludePattern: '', failedTotalHigh: '1', healthy: '', includePattern: '', pattern: '**/build/reports/findbugs-output.xml', unHealthy: '', unstableTotalAll: '1'
      jacoco()
    }
    failure {
      slackSend color: "danger", message: "${env.JOB_NAME} ${env.BUILD_NUMBER} - Failed (<${env.BUILD_URL}|Open>)"
    }
  }
}
