#!/bin/sh
PATH=$PATH:/usr/sbin:/sbin
sudo brctl addbr br0
sudo ifconfig eth0 0.0.0.0
sudo brctl addif br0 eth0
sudo ifconfig br0 172.20.2.47 netmask 255.255.255.0 up
sudo route add -net 172.20.2.0 netmask 255.255.255.0 br0
sudo route add default gw 172.20.2.254 br0
sudo tunctl -b -u superman
sudo ifconfig tap0 up
sudo brctl addif br0 tap0
export SDL_VIDEO_X11_DGAMOUSE=0
sudo iptables -I RH-Firewall-1-INPUT -i br0 -j ACCEPT
#qemu-kvm ~/win2k.img -m 512 -net nic -net tap,ifname=tap0,script=no
