For installing Darcy, You can leverage the virtual machine image we provide [here](https://drive.google.com/file/d/1JYc39S89OSM6FIC_6gjmMdNUsrYiEdQ6/view?usp=sharing). The username and password for logging into the VM image are:
```
Username: negar
password : "1"
```
In this virtual machine image, all the source files and the `runAll.sh` script are located in the following address:
```
~/Desktop/Darcy/Java9-Module-Inconsistencies
```

The `Java9-Module-Inconsistencies` directory also contains the input data for executing Darcy; this data were used in evaluating Darcy as reported in the paper. This input data contains open-source Java-9 applications which are located in a folder named `dataset_projects` with the following address:
```
~/Desktop/Darcy/Java9-Module-Inconsistencies/dataset_projects
```
You  can use any of the Java projects within this folder as an input to the `runAll.sh` script; however, for more convenience, you can use the following example input:
1. Go to `Java9-Module-Inconsistencies` directory and run the script:
```
cd ~/Desktop/Darcy/Java9-Module-Inconsistencies
./runAll.sh
```
2. When it asks for "_Project Path_", enter the following address:

`/home/negar/Desktop/Darcy/Java9-Module-Inconsistencies/dataset_projects/sense-nine-start-point`

It will also ask for the login password (for the user mentioned earlier) while running the script, which you should enter "1".
A copy of the output for this specific input is provided in `exampleOutput.txt` at the root directory of the GitHub repository. Note that the statistics regarding the execution time at the end of the output may slightly vary.
