#!/usr/bin/env groovy

/*
 <!--===========================================================================
 Copyright (c) 2009, Pentaho Engineering Team
 All rights reserved.
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the Pentaho Corporation nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.
 THIS SOFTWARE IS PROVIDED BY Pentaho Engineering Team ''AS IS'' AND ANY
 EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ============================================================================-->
 */

cl = new CliBuilder(usage: "projcreator.groovy <proj-name> [options]")
cl.h(longOpt:'help', 'Show usage information and quit')
cl.p(longOpt:'plugin', required:false, 'Create a Pentaho BI Platform plugin project')
cl.d(longOpt:'pentaho-dir', args:1, required:false, 'Path to base of Pentaho BI Platform deployment, REQUIRED if a plugin project')
cl.i(longOpt:'with-ivy', required:false, 'Create an IVY-based project')
cl.o(longOpt:'organisation', args:1, required:false, 'The name of your organisation (package name)')

if(!args) { cl.usage(); return }

def parse(CliBuilder cli) {
	if(args.length == 0) { cli.usage(); System.exit(1) }
	def opt
	if(args.length > 1) {
		opt = cli.parse(args[1..-1])
	}else {
		opt = cli.parse(args)
	}
	if(!opt) { System.exit(1) }
	if(opt.h) { cl.usage(); System.exit(0) }
	if(opt.p && !opt.d) { 
		println 'error: -d option is required when creating a plugin project'
		cl.usage(); System.exit(0) 
	}
	if(args[0].startsWith('-')) {
		println "error: proj-name cannot start with a '-'"
		cl.usage(); System.exit(0) 
	}
	
	return opt
}

def opts = parse(cl)

def projName = args[0]
def projNameNL = projName //natural language version of the project name, e.g. "My Project" vs "myProject"

ivy = opts.i
pentahoDir = opts.d
plugin = opts.p
organisation = (opts.o)?opts.o:'myorganisation'

println """*******************************************************************************
* Running project creator with:
* project name: ${projName}
* ivy dependency management: ${ivy}
* Pentaho dir: ${pentahoDir}
* plugin project: ${plugin}
* organisation: ${organisation}
*******************************************************************************"""

def SUBFLOOR_URL='http://subfloor.googlecode.com/svn/trunk'
def IVY_RESOURCES_URL='http://source.pentaho.org/svnroot/platform-plugins/echo-plugin/trunk'

//
//Create dirs
//
println "creating project dirs for '${projName}'..."
def projDir = new File(projName)
def buildResDir = new File(projDir, 'build-res')
buildResDir.mkdirs()
def srcDir = new File(projDir, 'src').mkdir()
def packageResDir = new File(projDir, 'package-res')
packageResDir.mkdir()

//
//Download the subfloor ant files
//
println 'downloading latest ant build files...'
new File(buildResDir, 'subfloor.xml').write new URL("${SUBFLOOR_URL}/subfloor.xml").text
new File(buildResDir, 'subfloor-pkg.xml').write new URL("${SUBFLOOR_URL}/subfloor-pkg.xml").text

//
//Create a custom build.properties file
//
println 'creating build properties files...'
def buildProperties = new Properties()
buildProperties.setProperty 'project.revision', 'TRUNK-SNAPSHOT'
buildProperties.setProperty 'ivy.artifact.id', projName
buildProperties.setProperty 'impl.title', projNameNL
if(plugin) {
	buildProperties.setProperty 'pentaho.dir', pentahoDir
	buildProperties.setProperty 'pentaho.classes.dir', '${pentaho.dir}/tomcat/webapps/pentaho/WEB-INF/classes'
	buildProperties.setProperty 'pentaho.lib.dir', '${pentaho.dir}/tomcat/webapps/pentaho/WEB-INF/lib'
	buildProperties.setProperty 'pentaho.solutions.dir', '${pentaho.dir}/pentaho-solutions'
}
buildProperties.setProperty 'dependency.bi-platform.revision', 'TRUNK-SNAPSHOT'
buildProperties.setProperty 'ivy.artifact.group', organisation 
def buildPropertiesComment = ''' 
# Auto-generated build properties file.  Description of properties:
# project.revision - the version of your plugin
# ivy.artifact.id - the name of your final artifact (jar and/or zip or tar.gz)
# impl.title - natural language description of your plugin (may contain spaces)
# '''
buildProperties.store new FileOutputStream(new File(projDir, 'build.properties')), buildPropertiesComment

if(ivy) {
	println 'IVY: downloading current ivysettings.xml'
	new File(projDir, 'ivysettings.xml').write new URL("${IVY_RESOURCES_URL}/ivysettings.xml").text
	
	//
	//Generate the ivy.xml
	//
	println 'IVY: creating ivy.xml...'
	def ivyXmlFile = new File(projDir, "ivy.xml")
	ivyXmlFile.write '<?xml version="1.0" encoding="UTF-8"?>\n'
	def writer = new StringWriter()
	def builder = new groovy.xml.MarkupBuilder(writer)
	
	builder.'ivy-module'(version:"2.0"){
		info(organisation:'${ivy.artifact.group}', module:'${ivy.artifact.id}', revision:'${project.revision}')
		configurations {
			conf(name:'default')
			conf(name:'test', visibility:'private')
		}
		publications {
			artifact(name:'${ivy.artifact.id}', type:'jar', conf:"default")
		}
		dependencies {
			dependency(org:"pentaho", name:"pentaho-bi-platform-api", rev:'${dependency.bi-platform.revision}', changing:true, transitive:false)
			dependency(org:"commons-logging", name:"commons-logging-api", rev:'1.1', transitive:false)
		}
	}
	ivyXmlFile.write writer.toString()
}

if(plugin) {
	//
	//Generate the plugin.xml
	//
	println 'PLUGIN: creating plugin.xml for an Action type plugin...'
	def actionClass = "${projName}Action"
	def projPackage = "org.${organisation}.${projName}"
	
	def pluginXmlFile = new File(packageResDir, "plugin.xml")
	
	pluginXmlFile.write '<?xml version="1.0" encoding="UTF-8"?>\n'
	def writer = new StringWriter()
	def builder = new groovy.xml.MarkupBuilder(writer)
	
	builder.plugin(title:"Sample plugin xml for ${projName}"){
		bean(id:actionClass, class:"${projPackage}.${actionClass}")
	}
	pluginXmlFile.append writer.toString()
}

//
//Generate the build.xml
//
println 'creating project ant file...'
def buildXmlFile = new File(projDir, "build.xml")

buildXmlFile.write "<project name=\"${projName}\" basedir=\".\" default=\"default\">"
buildXmlFile.append '''
<!-- Import the subfloor-pkg.xml file which contains all the default ant targets -->
  <import file="build-res/subfloor-pkg.xml" />

'''

if(plugin) {
	buildXmlFile.append '''
  <target name="install">
    <unzip src="${dist.dir}/${package.basename}.zip" dest="${pentaho.solutions.dir}/system" overwrite="true">
    </unzip>
    
    <copy todir="${pentaho.solutions.dir}">
      <fileset dir="${basedir}/solutions/"/>
    </copy>
  </target>

  <!-- 
  override the compile classpath to include the pentaho libraries from your platform installation
  -->
  <path id="classpath">
    <fileset dir="${devlib.dir}">
      <include name="*.jar" />
    </fileset>
    <fileset dir="${lib.dir}">
      <include name="*.jar" />
    </fileset>
    <fileset dir="${pentaho.lib.dir}">
      <include name="pentaho-bi-platform-*.jar" />
    </fileset>
    <dirset dir="${pentaho.classes.dir}" />
  </path>
  
  <!-- Copy all jars over to the plugin zip, 
  except for pentaho platform jars which the platform provides -->
  <target name="assemble.copy-libs">
    <copy todir="${approot.stage.dir}/lib">
      <fileset dir="${lib.dir}" excludes="pentaho-bi-platform-*.jar" />
      <fileset file="${dist.dir}/${ivy.artifact.id}-${project.revision}.jar" />
    </copy>
  </target>
  '''
}
if(!ivy) {
	buildXmlFile.append '''
  <!-- 
  The following overrides are in place to suppress IVY dependency management.  If you
  want to turn IVY back on, comment out these overrides. 
  -->
  <!-- Set default target to skip the ivy "resolve" step -->
  <target name="default" depends="clean-all,dist,package" />
  
  <!-- Set the clean-all target to skip the "clean-jars" step.  We do not want our build
  process to delete the "lib" dir -->
  <target name="clean-all" depends="clean"/>

'''
}
buildXmlFile.append '</project>'

println """*******************************************************************************
* DONE. Now change directories to '${projName}' and run 'ant' to build your project
* and 'ant install' to deploy it to your Pentaho BI Platform
*******************************************************************************"""

