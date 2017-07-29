/**
 * Copyright 2017 Chuck Wolber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package idx;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 * @author chuckwolber
 */
public class IDX
{
    private InputStream _idxFile;
    private IDXFormatType _formatType = IDXFormatType.UNKNOWN;
    private int _dimensions = 0;
    private long _objectSize = 1;
    private long _numObjects = 0;
    private long _objectPointer = 0;
    private final ArrayList<Integer> _dimensionList = new ArrayList<>();

    public static IDX getInstance(InputStream idxFile) throws IOException, IDXException {
        IDX idx = new IDX();
        idx.openIdxStream(idxFile);
        return idx;
    }
    
    public ArrayList<Integer> dimensionList() {
        return _dimensionList;
    }
    
    public long numObjects() {
        return _numObjects;
    }
    
    public long objectPointer() {
        return _objectPointer;
    }
    
    public ArrayList<Integer> getNextObject() throws IDXException, IOException {
        ArrayList<Integer> result = new ArrayList<>();
        for (long i=0; i < _objectSize * _formatType.bytes(); i++) {
            int value = _idxFile.read();
            if (value < 0)
                throw new IDXException("Insufficient bytes available!");
            result.add(0xff & value);
        }
        _objectPointer++;
        return result;
    }
    
    private void openIdxStream(InputStream idxFile) throws IDXException, IOException {
        _idxFile = idxFile;
        readMagic();
        _numObjects = readUnsignedInteger();
        for (int i=1; i<_dimensions; i++)
            _dimensionList.add((int)(0xffffffff & readUnsignedInteger()));
        _dimensionList.forEach((dimension) -> {
            _objectSize *= dimension;
        });
    }
    
    private void readMagic() throws IDXException, IOException {
        byte[] magic = new byte[4];
        int bytesRead = _idxFile.read(magic);
        if (bytesRead < magic.length)
            throw new IDXException("Magic number is missing.");
        if (magic[0] != 0x0 || magic[1] != 0x0)
            throw new IDXException("Invalid magic number.");
        readFormat(magic);
        readDimensions(magic);
    }
    
    private void readFormat(byte[] magic) throws IDXException {
        _formatType = IDXFormatType.typeForType(magic[2]);
        if (_formatType == IDXFormatType.UNKNOWN)
            throw new IDXException("Invalid data type.");
    }
    
    private void readDimensions(byte[] magic) throws IDXException {
        _dimensions = 0xff & magic[3];
        if (_dimensions < 1)
            throw new IDXException("No dimensions found!");
        if (_dimensions == 1)
            _dimensionList.add(1);
    }
    
    private long readUnsignedInteger() throws IOException, IDXException {
        byte[] bytes = new byte[4];
        int bytesRead = _idxFile.read(bytes);
        if (bytesRead < 4)
            throw new IDXException("Unable to read unsigned integer!");
        return (0xff & bytes[0]) << 24 | 
               (0xff & bytes[1]) << 16 | 
               (0xff & bytes[2]) << 8  | 
                0xff & bytes[3];
    }
    
    public String description() {
        String desc = "";
        desc += "Type: " + _formatType.toString() + "\n";
        desc += "Total Object Dimensions: " + _dimensions + "\n";
        desc += "Objects: " + _numObjects + "\n";
        for (int i=0; i<_dimensionList.size(); i++)
            desc += "Object Dimension " + i + ": " + _dimensionList.get(i) + "\n";
        return desc;
    }
}
