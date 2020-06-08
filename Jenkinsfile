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
		// This stage builds the jar file using maven.
		stage('Setup: Build jar file'){
			steps{
				script{
					sh "mvn clean package"
				}
			}
		}
		stage('Main: Run Diagram-Converter'){
			steps{
				script{
					def diagramFolder = "diagram"
					sh "mkdir ${diagramFolder}"
					withCredentials([usernamePassword(credentialsId: 'mySQLUsernamePassword', passwordVariable: 'mysqlPass', usernameVariable: 'mysqlUser')]){
						withCredentials([usernamePassword(credentialsId: 'neo4jUsernamePassword', passwordVariable: 'neo4jPass', usernameVariable: 'neo4jUser')]){
							sh "java -jar target/diagram-converter-jar-with-dependencies.jar --graph_user $neo4jUser --graph_password $neo4jPass --rel_user $mysqlUser --rel_password $mysqlPass --rel_database ${env.REACTOME} --output ./${diagramFolder}
						}
					}
				}
			}
		}
	}
}
