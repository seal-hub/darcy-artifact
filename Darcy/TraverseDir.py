import os
import sys
import subprocess

count = 0
for root, dirs, files in os.walk(sys.argv[1]):
    for file in files:
        if file.endswith(".class"):
        	if not file.startswith("module-info"):
        		i = str(count)+'-'+file.split('.')[0]
        		count = count+1
        		subprocess.call(['java', '-jar', 'Classycle1.4.2/classycle.jar', '-xmlFile=XMLreports/'+str(i)+'.xml', os.path.join(root, file)])