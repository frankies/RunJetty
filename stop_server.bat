 @echo off
 
REM Shutdown jetty server with
call env.bat  

if not "%1" == "" port=%1 

call %CMD% %port% --shutdown

:end
@echo on
