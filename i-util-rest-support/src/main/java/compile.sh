#!/usr/bin/env bash

if [ -d classes ]; then
    rm -rf classes;
fi
mkdir classes

javac -cp $JAVA_HOME/lib/tools.jar com/mythsman/test/Getter* -d classes/

javac -cp classes -d classes -processor com.mythsman.test.GetterProcessor com/mythsman/test/App.java

javap -p classes com/mythsman/test/App.class

java -cp classes com.mythsman.test.App