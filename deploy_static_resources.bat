del /S /Q target\classes\static >log.txt
XCOPY src\main\resources\static target\classes\static /y /E >log.txt
del /S /Q log.txt