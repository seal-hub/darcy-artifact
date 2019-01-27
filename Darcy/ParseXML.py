import xml.etree.ElementTree as etree
import os
import xlsxwriter

# workbook = xlsxwriter.Workbook('StaticDependencies.xlsx')
# worksheet = workbook.add_worksheet()
# row = 0
txt_file= open("pkg_graph.txt","w")
txt_classes_file= open("class_pkg_graph.txt","w")
txt_provides_uses_file= open("interface_abstract_classes.txt","w")

pckgNum = 0
pkgList = [] 

for root, dirs, files in os.walk("XMLreports/"):
    for file in files:
        if file.endswith(".xml"):
            e = etree.parse(os.path.join(root, file)).getroot()
            # row = row+1
            for pkgs in e.findall('packages'):
                for pkg in pkgs.findall('package'):
                    # worksheet.write(row, 0, 'Package Name')
                    # print("pkg--",pkg.get('name')," ::: usesExternal: ", pkg.get('usesExternal'))
                    # worksheet.write(row,1, pkg.get('name'))
                    # row=row+1
                    # worksheet.write(row, 0, 'Package Uses External')
                    pckgNum = pckgNum +1
                    for ref in pkg.findall('packageRef'):
                        # worksheet.write(row,1, ref.get('name'))
                        txt_file.write(pkg.get('name')+":"+ref.get('name')+"\n")
                        pkgList.append(pkg.get('name'))
                        # row = row + 1

            for classes in e.findall('classes'):
                for cl in classes.findall('class'):
                    # print("class--",cl.get('name')," ::: usesExternal: ", cl.get('usesExternal'))
                    #checking for provides and uses:
                    if(cl.get('type')=="interface" or cl.get('type')== "abstract class"):
                        txt_provides_uses_file.write(cl.get('name')+":"+cl.get('type')+"\n")
                        for ref in cl.findall('classRef'):
                            if(ref.get('type') == "usedBy"):
                                txt_provides_uses_file.write(cl.get('name')+"-Used By-"+ref.get('name'+"\n"))
                    for pkg in pkgList:
                        if(cl.get('name').startswith(pkg)):
                            txt_classes_file.write(pkg+":"+cl.get('name')+"\n")


                    # worksheet.write(row,0, "Class Name")
                    # worksheet.write(row,1, cl.get('name'))
                    # row=row+1
                    # worksheet.write(row,0, "Class Uses External")
                    # for ref in cl.findall('classRef'):
                    #     worksheet.write(row,1, ref.get('name'))
                    #     row = row + 1

txt_file.close()
txt_classes_file.close()
txt_provides_uses_file.close()

print(":::: Number of Packages = ",pckgNum, " ::::")

# workbook.close()