#!/bin/bash

# download images
mkdir images
cd images
curl -O http://images.cocodataset.org/zips/train2014.zip 
curl -O http://images.cocodataset.org/zips/val2014.zip
unzip train2014.zip
unzip val2014.zip

# download annotations
cd ..
mkdir annotations
cd annotations
curl -O http://images.cocodataset.org/annotations/annotations_trainval2014.zip
curl -O http://images.cocodataset.org/annotations/image_info_test2014.zip
unzip annotations_trainval2014.zip
unzip image_info_test2014.zip
