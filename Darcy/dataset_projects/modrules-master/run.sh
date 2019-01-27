
export X=c:/Users/ceki/.m2/repository/ch/qos/modRules

export APP=$X/app/1.0.0-SNAPSHOT/app-1.0.0-SNAPSHOT.jar
export NAMED=$X/named/1.0.0-SNAPSHOT/named-1.0.0-SNAPSHOT.jar
export AUTO=$X/auto/1.0.0-SNAPSHOT/auto-1.0.0-SNAPSHOT.jar


java -p $APP\;$NAMED.jar -m ch.qos.modRules.app/ch.qos.modRules.app.Main 1

java -p $APP\;$NAMED.jar\;$AUTO -m ch.qos.modRules.app/ch.qos.modRules.app.Main 1

