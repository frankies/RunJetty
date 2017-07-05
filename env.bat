set port=8080

set lib=%~dp0jetty_server\RunJetty.jar
set deploy_war=%~dp0ROOT.war 
set run_dir=%~dp0jetty_server

rem set deps=%run_dir%;%lib%;%run_dir%/RunJetty_lib/apache-el-8.5.9.1.jar;%run_dir%/RunJetty_lib/apache-jsp-8.5.9.1.jar;%run_dir%/RunJetty_lib/apache-jsp-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/apache-jstl-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/asm-5.1.jar;%run_dir%/RunJetty_lib/asm-commons-5.1.jar;%run_dir%/RunJetty_lib/asm-tree-5.1.jar;%run_dir%/RunJetty_lib/ecj-4.4.2.jar;%run_dir%/RunJetty_lib/javax.annotation-api-1.2.jar;%run_dir%/RunJetty_lib/javax.servlet-api-3.1.0.jar;%run_dir%/RunJetty_lib/jetty-annotations-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-http-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-io-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-jndi-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-plus-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-schemas-3.1.jar;%run_dir%/RunJetty_lib/jetty-security-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-server-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-servlet-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-util-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-webapp-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/jetty-xml-9.4.6.v20170531.jar;%run_dir%/RunJetty_lib/taglibs-standard-impl-1.2.5.jar;%run_dir%/RunJetty_lib/taglibs-standard-spec-1.2.5.jar;




  set deps=.;%run_dir%
  FOR /R "%run_dir%\RunJetty_lib" %%s in (*.jar) do ( 
      set "deps=%deps% %%s" 
  )
   @echo %deps%
 

if "%JAVA_HOME%" == "" JAVA_HOME=%~dp0

set EXEC_JAVA=%JAVA_HOME%\bin\java.exe  

set CMD=%EXEC_JAVA% -cp %deps% org.jetty.run.App

rem @echo %deps%