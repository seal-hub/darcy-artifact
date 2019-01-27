import os
import sys
import subprocess
for root, dirs, files in os.walk(sys.argv[1]):
    for file in files:
        if file.endswith(".class"):
        	if not file.startswith("module-info"):
        		# print(root)
        		i = root.split('/')[10]+'-'+file.split('.')[0]
        		subprocess.call(['java', '-jar', 'Classycle1.4.2/classycle.jar', '-xmlFile=XMLreports/'+str(i)+'.xml', os.path.join(root, file)])
				#print(os.path.join(root, file))
