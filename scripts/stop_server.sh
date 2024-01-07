#!/bin/bash

sudo systemctl stop rebootServer

#
#service_name="rebootServer.service"
#service_path="/etc/systemd/system/$service_name"
#original_service_path="/server/scripts/$service_name"
#
## Kiểm tra xem dịch vụ đã tồn tại hay chưa
#if systemctl list-units --full --all | grep -Fq "$service_name"; then
#  #  nếu có service rồi thì sẽ stop service
#  sudo systemctl stop rebootServer
#  echo "đã stop service $service_name"
#else
## Kiểm tra xem có quy trình Java đang chạy không
#  if pgrep -x "java" > /dev/null
#  then
#    echo "Đang có quy trình Java chạy. Đang tiêu diệt..."
#    sudo killall java
#    echo "Đã tiêu diệt quy trình Java."
#  else
#    echo "Không có quy trình Java đang chạy."
#  fi
#fi



# Kiểm tra xem có quy trình Java đang chạy không
#if pgrep -x "java" > /dev/null
#then
#  echo "Đang có quy trình Java chạy. Đang tiêu diệt..."
#  sudo killall java
#  echo "Đã tiêu diệt quy trình Java."
#else
#  echo "Không có quy trình Java đang chạy."
#fi

#
##rm -r /opt/tomcat/webapps/ROOT
##rm /opt/tomcat/webapps/ROOT.war
#
#cp server/ROOT.war /opt/tomcat/webapps

#copy service nếu chưa tồn tại
