@Library('smartlogic-common@v2') _

DOCKER_IMAGE = 'maven:3.5'

smartlogic([
  docker: DOCKER_IMAGE,
  builder: smartlogic.mavenBuilder(args: {getMavenArgs()}),
  archive: {archive()},
  verify: {verify()},
  parameters: []
])

def getMavenArgs() {
  []
}

def archive() {
  archiveArtifacts '**/*.jar*'
}

def verify() {
}
