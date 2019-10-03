import java.io.IOException;

public class Driver {
    
    public static void main(String[] args) throws IOException{
        Huffman.encode("mcgee.txt", "code.txt", "mcgee.compressed");
       Huffman.decode("mcgee.compressed", "code.txt", "mcgee.uncompressed");
    }
}
