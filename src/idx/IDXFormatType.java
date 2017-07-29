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

/**
 *
 * @author chuckwolber
 */
public enum IDXFormatType
{
    UNKNOWN       (IDXFormatType.unkwnType, 0),
    UNSIGNED_BYTE (IDXFormatType.uByteType, 1),
    SIGNED_BYTE   (IDXFormatType.sByteType, 1),
    SHORT         (IDXFormatType.shortType, 2),
    INT           (IDXFormatType.integType, 4),
    FLOAT         (IDXFormatType.floatType, 4),
    DOUBLE        (IDXFormatType.dubleType, 8);
 
    private static final byte unkwnType = 0x00;
    private static final byte uByteType = 0x08;
    private static final byte sByteType = 0x09;
    private static final byte shortType = 0x0b;
    private static final byte integType = 0x0c;
    private static final byte floatType = 0x0d;
    private static final byte dubleType = 0x0e;
    
    private final byte _type;
    private final int _bytes;
    
    IDXFormatType(byte type, int bytes) {
        _type = type;
        _bytes = bytes;
    }
    
    public static IDXFormatType typeForType(byte type) {
        switch (type) {
            case IDXFormatType.uByteType:
                return IDXFormatType.UNSIGNED_BYTE;
            case IDXFormatType.sByteType:
                return IDXFormatType.SIGNED_BYTE;
            case IDXFormatType.shortType:
                return IDXFormatType.SHORT;
            case IDXFormatType.integType:
                return IDXFormatType.INT;
            case IDXFormatType.floatType:
                return IDXFormatType.FLOAT;
            case IDXFormatType.dubleType:
                return IDXFormatType.DOUBLE;
        }
        return IDXFormatType.UNKNOWN;
    }
    
    public byte type() {
        return _type;
    }
    
    public int bytes() {
        return _bytes;
    }
}
