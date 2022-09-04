import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MusicInfo {
    private static final int FMT_SONG_LENGTH = 52;
    private File pcmFile;
    private byte[] pcm;
    private MusicFMTData fmt;

    public MusicInfo(File pcm, File fmt, int index) {
        // Load fmt data
        try (RandomAccessFile raf = new RandomAccessFile(fmt, "r")) {
            byte[] fmtBytes = new byte[FMT_SONG_LENGTH];
            raf.skipBytes(index * FMT_SONG_LENGTH);
            raf.read(fmtBytes, 0, FMT_SONG_LENGTH);
            this.fmt = new MusicFMTData(fmtBytes);
        } 
        catch (IOException e) {
            throw new IllegalArgumentException("Invalid FMT file");
        }

        // Load music data
        try (EndianDataInputStream is = new EndianDataInputStream(new FileInputStream(pcm))) {
            this.pcm = is.readNBytes(this.fmt.getTotalLength());
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Invalid PCM file");
        }
        this.pcmFile = pcm;
    }


    public byte[] getPcm() {
        return this.pcm;
    }
    public MusicFMTData getFmt() {
        return this.fmt;
    }
    public File getPcmFile() {
        return this.pcmFile;
    }
}
