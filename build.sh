#!/bin/zsh

mvn clean package -Dmaven.test.skip=true
aws s3 cp target/awsdemo-0.0.1-SNAPSHOT.jar s3://sb1.cuteworld.top/awsdemo/app.jar --profile cn