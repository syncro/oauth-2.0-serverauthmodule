library 'smartlogic-common@ta6508-auth-by-okta'

DOCKER_IMAGE = 'maven:3.5'

smartlogic.options().setMaxConcurrentBuilds(1)
smartlogic.options().setParameters([

  booleanParam(
    name: 'MAVEN_DEBUG',
    defaultValue: false,
    description: 'Enable Maven debug mode'
  )

])

smartlogic.buildInDocker(DOCKER_IMAGE) {
  smartlogic.utils().catchError("BUILD", {build()})
  smartlogic.utils().catchError("ARCHIVE", {archive()})
  smartlogic.utils().catchError("VERIFY", {verify()})
}

def build() {
  mvn("verify")
}

def mvn(args) {
  if (params.MAVEN_DEBUG) {
    args = "-X ${args}"
  }
  sh("mvn -B ${args}")
}

def archive() {
  stage('Archive') {
    archiveArtifacts '**/*.jar'
  }
}

def verify() {
}
