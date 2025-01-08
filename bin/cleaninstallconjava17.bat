@echo off 
set JAVA_HOME=C:\Program Files\java\jdk-17
set PATH=C:\Program Files\java\jdk-17\bin;%PATH% 
echo Display java version
	 
set "param1=%~1"
setlocal EnableDelayedExpansion
if "!param1!"=="" ( mvn -DskipTests=true clean install )

if defined param1 ( mvn -DskipTests=true clean %param1% )


