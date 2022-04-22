import java.util.Scanner;
import java.util.zip.Checksum;
import java.util.Arrays;

/**
 * This class implements the sender.
 */
public class MessageSender
{
    // maximum transfer unit (frame length limit)
    private final int mtu;
    // Source of the messages.
    private final Scanner stdin;

    /**
     * Create and initialize a new MessageSender.
     *
     * @param mtu the maximum transfer unit (MTU) (the length of a frame must
     * not exceed the MTU)
     */
    public MessageSender(int mtu) {
        this.mtu = mtu;
        this.stdin = new Scanner(System.in);
    }

    /**
     * Read a line from standard input layer and break it into frames
     * that are output on standard output, one frame per line.
     * Report any errors on standard error.
     */
    public void sendMessage() {
        String message = stdin.nextLine();
        if(message != null){
            if(error(message, mtu) == true){
                if(message.length() <= mtu-10){
                    System.out.println(fFrame(message));
                }else{
                    dFrame(message);
                }
            }
        }
        else{
            System.err.println("No message received.");
        }
    }

    public boolean error(String msg, int mtu){
        if(mtu == 10 && msg.length() != 0){
            System.err.println("message must be empty for an MTU of 10");
            return false;
        }
        if(mtu < 9){
            System.err.println("Invalid MTU size, <=9");
            return false;
        }
        if(mtu <= 10 && msg.length() >10 ){
            System.err.println("MTU size does not meet the requirment of the message");
            return false;
        }
        return true;
    }

    public void dFrame(String s){
        int max = mtu-10;
            int numberOfFrames = (int)Math.ceil((double)s.length()/max);
        for(int i = 0; i <= s.length(); i+=mtu-10){
            if(i >= s.length() - max){
                String seg = s.substring(i, s.length());
                if(seg.length() > 99){
                    System.err.println("Segment size to big");
                    return;
                }
                System.out.println(fFrame(seg));
            }else{
                String seg = s.substring(i, i+mtu -10);
                if(seg.length() > 99){
                    System.err.println("Segment size to big");
                    return;
                }
                String x = "D~"+seg.length()+"~"+seg+"~";
                String f ="[D~"+seg.length()+"~"+seg+"~"+checkSum(x)+"]";
                System.out.println(f);
            }
        }
    }

    public String fFrame(String s){
        if(s.length() > 99){
            System.err.println("Segment size to big");
            return null;
        }else{
            if(s.length() < 10){
                String x = "F~0"+s.length()+"~"+s+"~";
                String f ="[F~0"+s.length()+"~"+s+"~"+checkSum(x)+"]";
                return f;
            }else{
                String x = "F~"+s.length()+"~"+s+"~";
                String f ="[F~"+s.length()+"~"+s+"~"+checkSum(x)+"]";
                return f;
            }
        }

    }

    public static String checkSum(String s){
        int c = 0;
        for(int i = 0; i < s.length(); i++){
            c += (char) s.charAt(i);
        }
        String c16 = null;
        if(c < 16){
            c16 = "0";
        }
        c16 += Integer.toHexString(c);
        c16 = c16.substring(c16.length()-2);
        return c16;
    }
}
