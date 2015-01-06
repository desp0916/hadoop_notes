#!/bin/sh

cd /root/tmp
tar xzvf lzo-2.08.tar.gz
cd lzo-2.08
CFLAGS=-m64 CXXFLAGS=-m64 ./configure
make
make install
