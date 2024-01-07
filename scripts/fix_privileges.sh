##!/usr/bin/env bash
#chmod +x /home/ubuntu/server/* jar
#chmod +x /home/ubuntu/server/* war
#chmod +x /home/ubuntu/server/start_server.sh
#chmod +x /home/ubuntu/server/stop_server.sh


#sudo systemctl daemon-reload

#!/bin/bash

#service_name="rebootServer.service"
#service_path="/etc/systemd/system/$service_name"
#original_service_path="/server/scripts/$service_name"
#
## Kiểm tra xem dịch vụ đã tồn tại hay chưa
#if systemctl list-units --full --all | grep -Fq "$service_name"; then
#    echo "Dịch vụ $service_name đã tồn tại. Không cần thêm mới."
#else
#    # Copy tệp .service vào thư mục systemd và làm mới systemd
#    cp "$original_service_path" "$service_path"
#    systemctl daemon-reload
#
#    echo "Đã thêm mới dịch vụ $service_name và làm mới systemd."
#fi


#Để xóa service_name khỏi danh sách dịch vụ trên systemd, bạn có thể sử dụng các bước sau:
 #
 #Dừng Dịch Vụ (Nếu Đang Chạy):
 #sudo systemctl stop "$service_name"
 #
 #Tắt Dịch Vụ Để Không Khởi Động Cùng Hệ Thống:
 #sudo systemctl disable "$service_name"
 #
 #Xóa Tệp .service và Các Tệp Kết Cấu Liên Quan (Nếu Có):
 #sudo rm "/etc/systemd/system/$service_name"
 #
 #Nếu bạn có thư mục service_name.service.d, bạn cũng có thể xóa nó:
 #sudo rm -r "/etc/systemd/system/$service_name.service.d"
 #
 #Làm Mới systemd để Áp Dụng Thay Đổi:
 #sudo systemctl daemon-reload
 #Xóa Dịch Vụ Khỏi Danh Sách Các Dịch Vụ Trong systemd:
 #
 #sudo systemctl reset-failed "$service_name"


#Check xem service đã có chưa
# systemctl list-units --full --all | grep -Fq "$service_name"
# echo $?
# Kết quả của $? sẽ là mã thoát (exit code) của lệnh trước đó. Nếu nó là 0, đó có nghĩa là grep đã tìm thấy kết quả.

#cach tao service (file.service)
#    # Copy tệp .service vào thư mục systemd và làm mới systemd
#    cp "$original_service_path" "$service_path"
#    systemctl daemon-reload

# enable dịch vụ start up cùng môi trường
#sudo systemctl enable rebootServer

# Delete build artifacts after install
#[stderr]rm: it is dangerous to operate recursively on '/'
#[stderr]rm: use --no-preserve-root to override this failsafe

# you should avoid using rm -rf /opt/codedeploy-agent/deployment-root/ directly, as it might interfere with the CodeDeploy agent's operation.
#sudo rm -rf /opt/codedeploy-agent/deployment-root/*