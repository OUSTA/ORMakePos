set OR_ROOT_DIR=C:\Users\Vikram Middha\Desktop\Vikram\Espresso\Open Rate\ORMakePos\ORMakePos
set LIB_DIR=%OR_ROOT_DIR%\lib
set OR_LIB=%OR_ROOT_DIR%\ORlib\OpenRateV1.3.0.0.jar
set APP_LIB_DIR=%OR_ROOT_DIR%\dist
set PROPERTIES_DIR=%OR_ROOT_DIR%\properties
set CLASSPATH=.;%OR_LIB%;%APP_LIB_DIR%\ORMakePos.jar;%LIB_DIR%\sqljdbc4.jar;%LIB_DIR%\nanoxml-lite-2.2.3.jar;%LIB_DIR%\commons-pool-1.4.jar;%LIB_DIR%\commons-collections-3.2.1.jar;%LIB_DIR%\log4j-1.2.13.jar;%LIB_DIR%\commons-lang-2.4.jar;%LIB_DIR%\jakarta-oro-2.0.8.jar;%LIB_DIR%\c3p0-0.9.1.2.jar;%PROPERTIES_DIR%

cd %OR_ROOT_DIR%
java -Xms512m -Xmx1024m -cp %CLASSPATH% OpenRate.OpenRate -p MakePlus.properties.xml
