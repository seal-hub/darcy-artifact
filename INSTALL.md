For installing Darcy, you can leverage the virtual machine image we provide [here](https://drive.google.com/file/d/1xRyR_K3LABRaVe9iX6wfc77rx0CQ9oph/view?usp=sharing). There is a directory `Darcy` on the desktop which contains all the source files and the runAll script, which runs all the Darcy's components in the required order. 
The username and password for logging into the VM image are:
```
Username: negar
password : "1"
```

Darcy's repository also contains the input data for executing Darcy; this data were used in evaluating Darcy as reported in the paper. This input data contains open-source Java-9 applications which are located in a folder named “dataset_projects” at the Darcy directory in this repo. You  can use any of the Java projects within this folder, however, for more convenience, you can use the following example input:
1. First go to `Darcy` directory and run the `runAll.sh` script:
```
cd Darcy
./runAll.sh
```
2. When it asks for "_Project Path_", enter the following address:

`/home/negar/Desktop/Darcy/Java9-Module-Inconsistencies/dataset_projects/sense-nine-start-point`

The output for this example input is provided in `exampleOutPut.txt` in the root directory.
