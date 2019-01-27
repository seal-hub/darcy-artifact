## Description

There are two modules in this project:

* module `lib` with one exported package `foo` and class `Foo`
* module `app` (depends on `lib`) with one exported package `bar` and class `Bar`

Module `lib` artifact contains its sources. Module `app` loads those sources via `Module.getResourceAsStream`.

## Steps to reproduce

1. Run `./c`, observe the following line in the output:
    ```
    lib.getResourceAsStream("foo/Foo.java") = null
    ```
   Which means that `app` cannot get to the resources in `lib`
2. Uncomment the (seemingly useless) `opens foo` line in `lib/module-info.java`
3. Run `./c` again, observe the following line in the output:
    ```
    lib.getResourceAsStream("foo/Foo.java") = java.util.zip.ZipFile$ZipFileInflaterInputStream@19dfb72a
    ```
   Which means that `app` now _can_ get the needed resource from `lib`!
