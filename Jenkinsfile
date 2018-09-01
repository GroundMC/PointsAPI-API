pipeline {
  agent any
  tools {
        maven 'Maven3'
        jdk 'Java9'
  }
  options {
    buildDiscarder logRotator(numToKeepStr: '10')
  }
  stages {
    stage('Clean') {
      steps {
        sh 'mvn clean'
      }
    }
    stage('Compile') {
      steps {
        sh 'mvn compile'
      }
    }
    stage('Package') {
      steps {
        sh 'mvn package'
      }
    }
    stage('Install') {
      steps {
        sh 'mvn install'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', excludes: 'target/original*'
      }
    }
  }
}
