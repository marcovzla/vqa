#!/bin/bash

# usage: label_imagges.sh /path/to/images output/ ../yolo-9000/darknet

absolute_path() {
    echo "$(cd "$(dirname "$1")" && pwd)/$(basename "$1")"
}

imagedir="$(absolute_path "$1")"
outdir="$(absolute_path "$2")"
darknet="$(absolute_path "$3")"

pushd $darknet

for image in $imagedir/*.jpg; do
    labels="$outdir/$(basename "$image" ".jpg").txt"
    ./darknet detector test cfg/combine9k.data cfg/yolo9000.cfg ../yolo9000-weights/yolo9000.weights $image > $labels
done

popd