#!/bin/bash

if [ "$#" -ne 3 ]
then 
	echo "Copy file from Host to Docker container."
	echo 'Usage: $0 HOST_FILE_PATH CONTAINER_NAME GUEST_FILE_PATH'
	exit
fi

container_id=$(docker inspect -f '{{.Id}}' ${2})

if [ -z $container_id ] 
then
	echo 'Container $container_id does not exist.'
	exit
else
	dest_path=/var/lib/docker/devicemapper/mnt/${container_id}/rootfs"${3}"
	echo "Now copying ${1} to $dest_path ...."
	cp "${1}" $dest_path
fi

