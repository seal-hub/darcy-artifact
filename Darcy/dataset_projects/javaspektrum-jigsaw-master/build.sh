# clean
rm -rf build
mkdir build


# compile
javac -d build -modulesourcepath src $(find src -name "*.java")

# pack
rm -rf mlib
mkdir mlib

jar --create --file mlib/Mail@1.0.jar --module-version 1.0 -C build/Mail .
jar --create --file mlib/MailAPI@1.0.jar --module-version 1.0 -C build/MailAPI .
jar --create --file mlib/MailClient@1.0.jar --module-version 1.0 --main-class de.qaware.mail.client.MailClient -C build/MailClient .

# link
rm -rf mailclient
jlink --modulepath $JAVA_HOME/jmods:mlib --addmods MailClient,Mail --output mailclient

# run
java -mp mlib -m MailClient

cd mailclient/bin 
./MailClient

