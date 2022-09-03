import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MusicHandler {

    private int total; 
    private int totalToRead;
    private int numBytesRead;
    private int numBytesToRead;
    private byte[] buffer;
    private long stoppedTotal;
    private AudioFormat wav;
    private SourceDataLine lineIn;
    private DataLine.Info info;
    private FileInputStream fis;

    private MusicInfo tm;

    public MusicHandler(MusicInfo tm) {
        this.tm = tm;
    }

    public void playTrack() {
        try {
            wav = new AudioFormat(44100, 16, 2, true, false);
            info = new DataLine.Info(SourceDataLine.class, wav);

            numBytesToRead = tm.getFmt().getTotalLength();
            buffer = new byte[numBytesToRead];
            total = 0;

            if (!AudioSystem.isLineSupported(info)) {
                System.out.print("no support for " + wav.toString());
            }

            if (lineIn != null) {
                lineIn.close();
            }
            lineIn = (SourceDataLine) AudioSystem.getLine(info);
            lineIn.open(wav);
            
            lineIn.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        try {
                            AudioFormat format = lineIn.getFormat();
                            // find out how many bytes you have to skip, this depends on bytes per frame (a.k.a. frameSize)
                            long stoppedTotal = (format.getFrameSize() * ((int)format.getFrameRate()));
                            // now skip until the correct number of bytes have been skipped
                            int justSkipped = 0;
                            while (stoppedTotal > 0 && (justSkipped = (int)fis.skip(stoppedTotal)) > 0) {
                                stoppedTotal -= justSkipped;
                            }
                        }
                        catch (IOException e) {}
                    }
                }    
            });


            lineIn.start();
            fis = new FileInputStream(tm.getPcmFile());
            fis.skip(tm.getFmt().getStartOffset());
            totalToRead = tm.getFmt().getTotalLength();

            while (total < totalToRead) {
                numBytesRead = fis.read(buffer, 0, numBytesToRead);
                if (numBytesRead == -1) break;
                total += numBytesRead;
                lineIn.write(buffer, 0, numBytesRead);
            }

        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SourceDataLine getLineIn() {
        return lineIn;
    }
    public int getNumBytesRead() {
        return numBytesRead;
    }
    public int getTotal() {
        return total;
    }
    public long getStoppedTotal() {
        return stoppedTotal;
    }
}
