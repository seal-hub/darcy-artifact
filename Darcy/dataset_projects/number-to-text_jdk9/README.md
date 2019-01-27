# number-to-text-jdk9

Run `run.sh` to test the application from the commandline:

With a language flag:
```
$ sh run.sh 23456 no
[INFO] ------------------------------------------------------------------------
[INFO] --- exec-maven-plugin:1.6.0:exec (default-cli) @ number_main ---
tjuetre tusen fire hundre og femtiseks
[INFO] ------------------------------------------------------------------------
```

Without a language flag (defaults to English):
```
$ sh run.sh 23456
[INFO] ------------------------------------------------------------------------
[INFO] --- exec-maven-plugin:1.6.0:exec (default-cli) @ number_main ---
twentythree thousand four hundred and fiftysix
[INFO] ------------------------------------------------------------------------
```

Or use maven exec-plugin inside the `number_main` module:

```
$ mvn exec:exec -Dnumber="$1" -Dlang="$2"
[INFO] ------------------------------------------------------------------------
[INFO] --- exec-maven-plugin:1.6.0:exec (default-cli) @ number_main ---
one hundred and twentythree
[INFO] ------------------------------------------------------------------------
```
