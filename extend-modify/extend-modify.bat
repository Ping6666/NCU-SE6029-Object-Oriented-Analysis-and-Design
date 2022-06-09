@echo on
rmdir /s /q .\bin\
mkdir bin
javac -cp ".\src" .\src\bgWork\Launcher.java -d .\bin\
cd .\bin
java bgWork/Launcher
cd ..
rmdir /s /q .\bin\
