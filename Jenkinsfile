// This Jenkinsfile is used by Jenkins to run the DiagramConverter step of Reactome's release.
// It requires that the GenerateGraphDatabaseAndAnalysisCore step has been run successfully before it can be run.

import org.reactome.release.jenkins.utilities.Utilities

// Shared library maintained at 'release-jenkins-utils' repository.
def utils = new Utilities()

pipeline{
	agent any

    environment {
        OUTPUT_FOLDER = "diagram"
    }
	stages{
		// This stage checks that upstream project GenerateGraphDatabaseAndAnalysisCore was run successfully.
		stage('Check Graph DB & Analysis Core build succeeded'){
			steps{
				script{
                    utils.checkUpstreamBuildsSucceeded("GenerateGraphDatabaseAndAnalysisCore")
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
				    def releaseVersion = utils.getReleaseVersion()
					sh "mkdir -p ${env.OUTPUT_FOLDER}"
					withCredentials([usernamePassword(credentialsId: 'mySQLUsernamePassword', passwordVariable: 'mysqlPass', usernameVariable: 'mysqlUser')]){
						withCredentials([usernamePassword(credentialsId: 'neo4jUsernamePassword', passwordVariable: 'neo4jPass', usernameVariable: 'neo4jUser')]){
				 			sh "java -jar target/diagram-converter-jar-with-dependencies.jar --graph_user $neo4jUser --graph_password $neo4jPass --rel_user $mysqlUser --rel_password $mysqlPass --rel_database ${env.REACTOME_DB} --output ./${env.OUTPUT_FOLDER}"
					        sh "tar -zcf diagrams-v${releaseVersion}.tgz ${env.OUTPUT_FOLDER}/"
					        sh "sudo service tomcat7 stop"
							sh "sudo service neo4j stop"
							sh "sudo service neo4j start"
							sh "sudo service tomcat7 start"
						}
					}
				}
			}
		}
		stage('Post: Compare previous release file number') {
		    steps{
		        script{
		            def releaseVersion = utils.getReleaseVersion()
		            def previousReleaseVersion = utils.getPreviousReleaseVersion()
		            def previousDiagramsArchive = "diagrams-v${previousReleaseVersion}.tgz"
		            sh "mkdir -p ${previousReleaseVersion}"
		            sh "aws s3 --no-progress cp s3://reactome/private/releases/${previousReleaseVersion}/diagram_converter/${previousDiagramsArchive} ${previousReleaseVersion}/"
		            dir("${previousReleaseVersion}"){
		                sh "tar -xf ${previousDiagramsArchive}"
		            }
		            def currentDiagramsFileCount = findFiles(glob: "${env.OUTPUT_FOLDER}/*").size()
		            def previousDiagramsFileCount = findFiles(glob: "${previousReleaseVersion}/${env.OUTPUT_FOLDER}/*").size()
		            echo("Total diagram files for v${releaseVersion}: ${currentDiagramsFileCount}")
		            echo("Total diagram files for v${previousReleaseVersion}: ${previousDiagramsFileCount}")
		            sh "rm -r ${previousReleaseVersion}*"
		        }
		    }
		}
		stage('Post: Back up final graph database') {
		    steps{
		        script{
		            sh "cp -r /var/lib/neo4j/data/databases/graph.db/ ."
		            utils.createGraphDatabaseTarFile("graph.db/", "diagram_converter")
		        }
		    }
		}
		stage('Post: Move final graph db and diagrams to download folder') {
		    steps{
		        script{
		            def finalGraphDbArchive = "reactome.graphdb.tgz"
		            def releaseVersion = utils.getReleaseVersion()
		            def downloadPath = "${env.ABS_DOWNLOAD_PATH}/${releaseVersion}"
		            sh "cp diagram-converter_graph_database.dump*tgz ${finalGraphDbArchive}"
		            sh "cp ${finalGraphDbArchive} ${downloadPath}/"
		            sh "mv ${env.OUTPUT_FOLDER} ${downloadPath}/ "
		        }
		    }
		}
		// Archive everything on S3, and move the 'diagram' folder to the download/vXX folder.
		stage('Post: Archive Outputs'){
			steps{
				script{
				    def releaseVersion = utils.getReleaseVersion()
				    def dataFiles = ["diagrams-v${releaseVersion}.tgz", "reactome.graphdb.tgz"]
					def logFiles = ["reports/*"]
					// Note at time of writing diagram-converter does not output log files (but makes very, very verbose stdout)
					def foldersToDelete = []
					utils.cleanUpAndArchiveBuildFiles("diagram_converter", dataFiles, logFiles, foldersToDelete)
				}
			}
		}
	}
}
