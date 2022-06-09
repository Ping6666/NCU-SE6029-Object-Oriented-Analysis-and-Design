@echo on
rmdir /s /q .\bin\
mkdir bin
javac -cp ".\src\;.\lib\json-simple-1.1.1.jar" .\src\UMLEditor.java -d .\bin\
cd .\bin\
java -cp "..\lib\json-simple-1.1.1.jar"; UMLEditor
cd ..
rmdir /s /q .\bin\
