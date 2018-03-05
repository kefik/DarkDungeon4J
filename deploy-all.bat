cd DarkDungeon4J-Utils
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J-Utils Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)


cd ..\DarkDungeon4J
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)

cd ..\DarkDungeon4J-Loader
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J-Loader Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)

cd ..\DarkDungeon4J-Agents
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J-Agents Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)

cd ..\DarkDungeon4J-Visualization
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J-Visualization Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)

cd ..\DarkDungeon4J-Adventure
call mvn deploy
if errorlevel 1 (
   cd ..
   echo DarkDungeon4J-Adventure Failure Reason Given is %errorlevel%
   exit /b %errorlevel%
)

cd ..

