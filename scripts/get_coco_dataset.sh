#!/bin/bash

# validate command line arguments
if [ $# == 0 ]; then
    echo "no arguments given"
    exit 1
fi

# use specified directory
mkdir -p $1
pushd $1

# download images
mkdir images
pushd images
curl -O http://images.cocodataset.org/zips/train2017.zip
curl -O http://images.cocodataset.org/zips/val2017.zip
unzip train2017.zip
unzip val2017.zip

# download annotations
popd
mkdir annotations
pushd annotations
curl -O http://images.cocodataset.org/annotations/annotations_trainval2017.zip
curl -O http://images.cocodataset.org/annotations/stuff_annotations_trainval2017.zip
unzip annotations_trainval2017.zip
unzip stuff_annotations_trainval2017.zip

# back to where we started
popd
popd
