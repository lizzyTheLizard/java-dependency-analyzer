//file:noinspection GroovyAssignabilityCheck
//file:noinspection GrUnresolvedAccess
File buildLog = new File(basedir, 'build.log')
assert buildLog.exists()
assert buildLog.text.contains("Plugin is running in Project kotlin-maven-example-it-basic")
