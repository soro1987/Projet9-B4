pipeline {
    agent any
    //Technologie du projets
    tools {
        maven 'maven3'
        jdk 'jdk8'

    }
    //Declencheur du job
    triggers {
        pollSCM('H/2 * * * *') //Sonder github chaque 30mn
    }
    //Rotation des build laisse juste les 4 dernier build
    options {
        buildDiscarder(logRotator(numToKeepStr: '4', artifactNumToKeepStr: '4'))
    }
    //Niveaux
    stages {
        stage ('Checkout source'){ //Recupération code source
            steps{
                checkout scm
            }
        }
        stage ('Start Postgres'){ //Build project
            steps{
                bat 'docker-compose up -d myerp.db'
            }
        }
        stage ('Build'){ //Build project
            steps{
                bat 'mvn clean install -X'
            }
        }
        stage ('Stop postgres'){ //Build project
            steps{
                bat 'docker-compose stop myerp.db'
            }
        }

    }
}

//cpmpare to , get credit get d
//sql ,
//formatageebit , chechk ecriture comptable