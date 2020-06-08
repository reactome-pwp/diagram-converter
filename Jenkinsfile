import groovy.json.JsonSlurper
// This Jenkinsfile is used by Jenkins to run the DiagramConverter step of Reactome's release.
// It requires that the GenerateGraphDatabaseAndAnalysisCore step has been run successfully before it can be run.
def currentRelease
def diagramFolder = "diagram"
pipeline{
	agent any

	stages{
		// This stage checks that upstream project GenerateGraphDatabaseAndAnalysisCore was run successfully.
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
		// Execute the jar file, producing the diagram JSON files.
		stage('Main: Run Diagram-Converter'){
			steps{
				script{
					sh "mkdir ${diagramFolder}"
					withCredentials([usernamePassword(credentialsId: 'mySQLUsernamePassword', passwordVariable: 'mysqlPass', usernameVariable: 'mysqlUser')]){
						withCredentials([usernamePassword(credentialsId: 'neo4jUsernamePassword', passwordVariable: 'neo4jPass', usernameVariable: 'neo4jUser')]){
							sh "java -jar target/diagram-converter-jar-with-dependencies.jar --graph_user $neo4jUser --graph_password $neo4jPass --rel_user $mysqlUser --rel_password $mysqlPass --rel_database ${env.REACTOME} --output ./${diagramFolder}"
						}
					}
				}
			}
		}
		// Archive everything on S3, and move the 'diagram' folder to the download/vXX folder.
		stage('Post: Archive Outputs'){
			steps{
				script{
					def s3Path = "${env.S3_RELEASE_DIRECTORY_URL}/${currentRelease}/diagram_converter"
					def diagramArchive = "diagrams-v${currentRelease}.tgz"
					sh "tar -zcvf ${diagramArchive} ${diagramFolder}"
					sh "mv ${diagramFolder} ${env.ABS_DOWNLOAD_PATH}/${currentRelease}/" 
					sh "gzip reports/*"
					sh "aws s3 --no-progress cp ${diagramArchive} $s3Path/"
					sh "aws s3 --no-progress --recursive cp reports/ $s3Path/reports/"
					sh "rm -r ${diagramArchive} reports"
				}
			}
		}
	}
}
