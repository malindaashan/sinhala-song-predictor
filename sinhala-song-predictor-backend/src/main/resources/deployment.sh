#!/bin/bash

echo "Starting deployment script..."

fuser -k -n tcp 8080

rm -rf sinhala-song-predictor-ui/build

{

rm -rf sinhala-song-predictor-backend/src/main/resources/static

mkdir sinhala-song-predictor-backend/src/main/resources/static

cd sinhala-song-predictor-ui/

rm -rf build

npm install

npm run build

cp -r build/* ../sinhala-song-predictor-backend/src/main/resources/static/

cd ../

cd sinhala-song-predictor-backend/

gradle assemble

rm /opt/research/app/sinhala-song-predictor.jar

cp build/libs/sinhala-song-predictor-backend-0.0.1-SNAPSHOT.jar /opt/research/app/sinhala-song-predictor.jar

chmod 777 /opt/research/app/sinhala-song-predictor.jar

cd /opt/research/app/

nohup java -jar sinhala-song-predictor.jar > log.log 2>&1 &

echo "Deployment completed"
} | tee -a /app-script-log.log
