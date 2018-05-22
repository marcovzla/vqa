# vqa

## jenny

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
