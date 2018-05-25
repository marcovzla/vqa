# vqa

## coco on jenny

These are instructions to install the coco api in jenny.
We assume you are using conda.
The coco dataset is located at `/data/nlp/corpora/COCO`.

To install the coco api in jenny:
```
# create conda environment
conda create -n vqa python=2
source activate vqa
# install dependencies
conda install cython matplotlib ipython jupyter scikit-image
# clone coco api
git clone https://github.com/cocodataset/cocoapi
cd cocoapi
# create symlinks to dataset in jenny
ln -s /data/nlp/corpora/COCO/images images
ln -s /data/nlp/corpora/COCO/annotations/annotations annotations
# make python api
cd PythonAPI
make
```
Once everything has been installed, you can run jupyter notebook.
Jupyter opens a browser by default, but we don't want that so we will
use the `--no-browser` option.
```
jupyter notebook --no-browser
```
Jupyter will print something like this:
```
    Copy/paste this URL into your browser when you connect for the first time,
    to login with a token:
        http://localhost:8888/?token=3c0235703fa440de24b4b85d07346043a5d4be892472a2ee&token=3c0235703fa440de24b4b85d07346043a5d4be892472a2ee
```
This is the url that you will open in your local machine after opening an ssh tunnel to jenny.
To open the ssh tunnel in your local machine:
```
ssh -N -f -L localhost:8888:localhost:8888 user@jenny
```
Now open the url in your local machine and use the notebook.
Once you are done, find the process (in your local machine) and kill it:
```
$ ps aux | grep localhost:8888
marco     4947  0.0  0.0 168464  3588 ?        Ss   01:04   0:00 ssh -N -f -L localhost:8888:localhost:8888 jenny
marco     5276  0.0  0.0 119532  1048 pts/2    S+   01:13   0:00 grep --color=auto localhost:8888
$ kill -9 4947
```

### yolo9000 on jenny

```
git clone --recursive https://github.com/philipperemy/yolo-9000.git
cd yolo-9000
cat yolo9000-weights/x* > yolo9000-weights/yolo9000.weights # it was generated from split -b 95m yolo9000.weights
md5sum yolo9000-weights/yolo9000.weights
# d74ee8d5909f3b7446e9b350b4dd0f44  yolo9000.weights
cd darknet
vim Makefile
# change first two lines to GPU=1 and CUDNN=1
make
```

Test things are working:
```
$ ./darknet detector test cfg/combine9k.data cfg/yolo9000.cfg ../yolo9000-weights/yolo9000.weights data/dog.jpg
layer     filters    size              input                output
    0 conv     32  3 x 3 / 1   544 x 544 x   3   ->   544 x 544 x  32
    1 max          2 x 2 / 2   544 x 544 x  32   ->   272 x 272 x  32
    2 conv     64  3 x 3 / 1   272 x 272 x  32   ->   272 x 272 x  64
    3 max          2 x 2 / 2   272 x 272 x  64   ->   136 x 136 x  64
    4 conv    128  3 x 3 / 1   136 x 136 x  64   ->   136 x 136 x 128
    5 conv     64  1 x 1 / 1   136 x 136 x 128   ->   136 x 136 x  64
    6 conv    128  3 x 3 / 1   136 x 136 x  64   ->   136 x 136 x 128
    7 max          2 x 2 / 2   136 x 136 x 128   ->    68 x  68 x 128
    8 conv    256  3 x 3 / 1    68 x  68 x 128   ->    68 x  68 x 256
    9 conv    128  1 x 1 / 1    68 x  68 x 256   ->    68 x  68 x 128
   10 conv    256  3 x 3 / 1    68 x  68 x 128   ->    68 x  68 x 256
   11 max          2 x 2 / 2    68 x  68 x 256   ->    34 x  34 x 256
   12 conv    512  3 x 3 / 1    34 x  34 x 256   ->    34 x  34 x 512
   13 conv    256  1 x 1 / 1    34 x  34 x 512   ->    34 x  34 x 256
   14 conv    512  3 x 3 / 1    34 x  34 x 256   ->    34 x  34 x 512
   15 conv    256  1 x 1 / 1    34 x  34 x 512   ->    34 x  34 x 256
   16 conv    512  3 x 3 / 1    34 x  34 x 256   ->    34 x  34 x 512
   17 max          2 x 2 / 2    34 x  34 x 512   ->    17 x  17 x 512
   18 conv   1024  3 x 3 / 1    17 x  17 x 512   ->    17 x  17 x1024
   19 conv    512  1 x 1 / 1    17 x  17 x1024   ->    17 x  17 x 512
   20 conv   1024  3 x 3 / 1    17 x  17 x 512   ->    17 x  17 x1024
   21 conv    512  1 x 1 / 1    17 x  17 x1024   ->    17 x  17 x 512
   22 conv   1024  3 x 3 / 1    17 x  17 x 512   ->    17 x  17 x1024
   23 conv  28269  1 x 1 / 1    17 x  17 x1024   ->    17 x  17 x28269
   24 detection
mask_scale: Using default '1.000000'
Loading weights from ../yolo9000-weights/yolo9000.weights...Done!
data/dog.jpg: Predicted in 0.091908 seconds.
car: 70%
canine: 56%
bicycle: 57%
```

Note the inference time. Inference should take less than a second on a GPU,
and several seconds on a CPU.
