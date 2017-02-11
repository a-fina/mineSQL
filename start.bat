@ECHO OFF

SET BD=%~dp0
SET CP=%BD%jdom\jdom.jar
SET CP=%CP%;%BD%swt\swt.jar

start /B java -jar target/mineSQL-0.9.9a-full.jar
