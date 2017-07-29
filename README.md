# IDX
The [MNIST handwriting database](http://yann.lecun.com/exdb/mnist/) is stored in files specified by the IDX file format. As near as I can tell, Yann LeCun invented the IDX format, or at least it appears that MNIST is the only one using it. It is fairly well specified, and can probably be used in other scenarios, so I decided to write my own generalized code for reading IDX formatted files.

# Building
Project files are included, so as long as you have a recent version of [ant](https://ant.apache.org/) it should be pretty simple to build a Jar file for inclusion in other Java projects. Or you can simply import the source code directly and avoid such complications.

# Usage
1. Create a standard InputStream object for the IDX file in question.
2. Call IDX.getInstance(InputStream idxFile) to get an instance of IDX.
3. Call getNextObject() to get the next object in the IDX file.

Most of the time you will probably just call getNextObject() as many times as the value of numObjects(). Calling getNextObject() more times than there is data will result in an IDXException.

# Helper Methods
* numObjects() - Returns the number of objects in a given IDX file.
* getNextObject() - Returns the next object in the IDX file.
* dimensionList() - Returns the dimensions of the objects in a given IDX file. Each object in a given IDX file has the same dimensions.
* description() - Returns a basic human readable description of the IDX file. 

# File Format Details

The following was copied directly from Yann LeCun's MNIST [web page](http://yann.lecun.com/exdb/mnist/):

    The IDX file format is a simple format for vectors and multidimensional 
    matrices of various numerical types.
    
    The basic format is:
    
    magic number 
    size in dimension 0 
    size in dimension 1 
    size in dimension 2     
    ..... 
    size in dimension N 
    data

    The magic number is an integer (MSB first). The first 2 bytes are always 0.
    
    The third byte codes the type of the data: 
    0x08: unsigned byte 
    0x09: signed byte 
    0x0B: short (2 bytes) 
    0x0C: int (4 bytes) 
    0x0D: float (4 bytes) 
    0x0E: double (8 bytes)
    
    The 4-th byte codes the number of dimensions of the vector/matrix: 
    1 for vectors, 2 for matrices....
    
    The sizes in each dimension are 4-byte integers (MSB first, high endian, like in 
    most non-Intel processors).
    
    The data is stored like in a C array, i.e. the index in the last dimension changes 
    the fastest. 

I found a very useful addendum [here](http://www.fon.hum.uva.nl/praat/manual/IDX_file_format.html) that answered a few of my lingering questions:

    Behaviour
    ---------
    If the storage format indicates that there are more than 2 dimensions, the 
    resulting Matrix accumulates dimensions 2 and higher in the columns. For example, 
    with three dimensions of size n1, n2 and n3, respectively, the resulting Matrix 
    object will have n1 rows and n2×n3 columns.
    
    Example
    -------
    The training and testing data of the MNIST database of handwritten digits at 
    http://yann.lecun.com/exdb/mnist/ is stored in compressed IDX formatted files.
    
    Reading the uncompressed file train-images-idx3-ubyte available at 
    http://yann.lecun.com/exdb/mnist/ with 60000 images of 28×28 pixel data, will 
    result in a new Matrix object with 60000 rows and 784 (=28×28) columns. Each 
    cell will contain a number in the interval from 0 to 255.

    Reading the uncompressed file train-labels-idx1-ubyte with 60000 labels will r
    esult in a new Matrix object with 1 row and 60000 columns. Each cell will contain 
    a number in the interval from 0 to 9.
