import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class MusicFMTData {
    private String name;
    private int startOffset;
    private int introLength;
    private int totalLength;
    private byte[] fmtHeader;

    public MusicFMTData(String name, int startOffset, int introLength, int totalLength, byte[] fmtHeader) {
        this.name = name;
        this.startOffset = startOffset;
        this.introLength = introLength;
        this.totalLength = totalLength;
        this.fmtHeader = fmtHeader;
    }
    
    public MusicFMTData(byte[] bytes) {
        try(EndianDataInputStream is = new EndianDataInputStream(new ByteArrayInputStream(bytes))) {
            is.order(ByteOrder.LITTLE_ENDIAN);
    
            this.name = new String(is.readNBytes(16), StandardCharsets.US_ASCII).replaceAll("\\p{C}", "");
            this.startOffset = is.readInt();
            is.skip(4);
            this.introLength = is.readInt();
            this.totalLength = is.readInt();
            this.fmtHeader = is.readNBytes(18);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Invalid byte array");
        }
    }


    public String getName() {
        return name;
    }
    public int getStartOffset() {
        return startOffset;
    }
    public int getIntroLength() {
        return introLength;
    }
    public int getTotalLength() {
        return totalLength;
    }
    public byte[] getFmtHeader() {
        return fmtHeader;
    }
}
