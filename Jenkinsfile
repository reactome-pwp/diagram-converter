import groovy.json.JsonSlurper
// This Jenkinsfile is used by Jenkins to run the DiagramConverter step of Reactome's release.
// It requires that the GenerateGraphDatabaseAndAnalysisCore step has been run successfully before it can be run.
def currentRelease

pipeline{
	agent any

	stages{
		// This stage checks that upstream projects AddLinks-Download and Orthoinference were run successfully for their last build.
		stage('Check Graph DB & Analysis Core build succeeded'){
			steps{
				script{
					currentRelease = (pwd() =~ /Releases\/(\d+)\//)[0][1];
					// This queries the Jenkins API to confirm that the most recent build of GenerateGraphDatabaseAndAnalysisCore was successful.
					def graphDBUrl = httpRequest authentication: 'jenkinsKey', validResponseCodes: "${env.VALID_RESPONSE_CODES}", url: "${env.JENKINS_JOB_URL}/job/$currentRelease/job/GenerateGraphDatabaseAndAnalysisCore/lastBuild/api/json"
					if (graphDBUrl.getStatus() == 404) {
						error("GenerateGraphDatabaseAndAnalysisCore has not yet been run. Please complete a successful build.")
					} else {
						def graphDBJson = new JsonSlurper().parseText(graphDBUrl.getContent())
						if (graphDBJson['result'] != "SUCCESS"){
							error("Most recent GenerateGraphDatabaseAndAnalysisCore build status: " + graphDBJson['result'] + ". Please complete a successful build.")
						}
					}
				}
			}
		}
	}
}
