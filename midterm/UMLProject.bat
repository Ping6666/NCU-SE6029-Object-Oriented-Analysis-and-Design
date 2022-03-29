@echo on
rmdir /s .\class\
mkdir class
javac .\UMLEditor.java -d .\class\
cd .\class\
java UMLEditor
cd ..
rmdir /s .\class\
