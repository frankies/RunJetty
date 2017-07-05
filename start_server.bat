 @echo off

rem Copy database  
set db_name=test
set db_file_mv=%db_name%.mv.db
set db_file_tr=%db_name%.trace.db
if not exist "%USERPROFILE%\%db_file_mv%" (
  copy %~dp0db\%db_file_mv% %USERPROFILE%
)
if not exist "%USERPROFILE%\%db_file_tr%" (
  copy %~dp0db\%db_file_tr% %USERPROFILE%
)


REM Start jetty server with ROOT.war deploying...  
call env.bat  

 
if not "%1"=="" (
   set port=%1
)
  
REM Open the browser for user automatically.
explorer "http://localhost:%port%/"

REM Waitting for server setupig.. 
call %CMD% %deploy_war% %1

:end
@echo on
