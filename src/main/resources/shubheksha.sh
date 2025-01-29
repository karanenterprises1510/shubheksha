# Moving to working directory!
cd /home/ec2-user/wholeSaleNK/

PATH=/usr/bin/:$PATH

export PATH

LOG_DIR=/log/shubheksha

# Making Directory for logs
mkdir -p $LOG_DIR

find $LOG_DIR -type f -mtime +5 -exec rm {} \;

LOG_FILE=$LOG_DIR/shubheksha_`date +%m_%d_%y`.log

echo "*********** Starting Shubheksha JAR Execution ***********" >> $LOG_FILE

echo "PATH:$PATH" >> $LOG_FILE

date >> $LOG_FILE

nohup java -Xms512m -Xmx512m -jar -Dspring.profiles.active=prod shubhekshaApi-0.0.1.jar >> $LOG_FILE &

find /log/shubheksha/*.log -mtime +7 -exec rm {} \;
date >> $LOG_FILE