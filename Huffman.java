
        import java.io.*;
        import java.util.Collection;
        import java.util.Map;
        import java.util.PriorityQueue;
        import java.util.Scanner;
        import java.util.TreeMap;


        /**
         *This program to encode an ASCII file using a Huffmancode and print the result in
         * code.txt file and Also it compressed the mcgee file.
         * Name : Amarjit Singh
         * Course: CS 340
         */

        public class Huffman {


            private char letter;
            private int freq;            // store frequency
            private String huffcode;     //store huffcode

            //default constructor
            public Huffman(char letter) {
                freq = 0;
                huffcode = "";
                this.letter = letter;
            }

            // getter and setter freq,huffcode,letter

            public int getFrequency() {
                return freq;
            }


            public void setFrequency(int freq) {
                this.freq = freq;
            }

            public String getHuffmanCode() {
                return huffcode;
            }

            public void setHuffmanCode(String huffcode) {
                this.huffcode = huffcode;
            }


            // inner class for huffman code
            static class TreeNode implements Comparable<TreeNode>{

                //declare variable for Huffcode,left, and right
                private Huffman datacode;
                private TreeNode left;
                private TreeNode right;


                 // Default constructor for TreeNode.
                    public TreeNode(){
                    datacode = null;
                    left = null;
                    right = null;
                }
                //class constructor for treenode
                public TreeNode ( Huffman datacode, TreeNode left, TreeNode right){
                    this.datacode = datacode;
                    this.left = left;
                    this.right = right;
                }
                // getter and setter for datacode,left and right
                public Huffman getdatacode() {

                    return datacode;
                }

                public void setData(Huffman datacode) {

                    this.datacode =datacode;
                }

                public TreeNode getleft() {
                    return left;
                }

                public void setLeft(TreeNode left) {
                    this.left = left;
                }

                public TreeNode getright() {
                    return right;
                }

                public void setRight(TreeNode right) {

                    this.right = right;
                }

                public boolean leaf(){

                    return (right == null) && (left == null);
                }
                //compare method for traverse tree

                public int compareTo(TreeNode o) {
                    if(datacode.freq > o.datacode.freq){
                        return 1;
                    }
                    else if(datacode.freq < o.datacode.freq){
                        return -1;
                    }
                    else{
                        return 0;
                    }
                }
            }


            public static void encode(String originalFilename, String codeFilename, String compressedFilename) throws IOException {
                // Step-1: create the table of frequencies
                Map<Character,Huffman> map = calcaluteLetterFreq(originalFilename);

                // Step-2: create the binary tree using Huffman encoding algorithm
                TreeNode binaryTree = huffman(map.values());

                // Step-3: traverse the encoding tree and record the huffman code for each letters
                travertree(binaryTree, "");
                writefile(map, codeFilename);

                // Step-4: process the original final again to produce compressed file
                compress(originalFilename, compressedFilename, map);
            }


             // Decompress a compressed file, using Huffman coding, and save the output file.

            public static void decode(String compressedFilename, String codeFilename, String decompressedFilename) throws FileNotFoundException {
                //declare the variable
                String HuffCode = "";
                BitInputStream fileinput;
                PrintWriter printerwriter;
                int readbit;

                //load the code into map function
                Map<String, Character> map = loadcode(codeFilename);

               //putting commpressed file into BitInputStream
                fileinput= new BitInputStream(compressedFilename);

                //printerwriter printing decompressed
                printerwriter = new PrintWriter(new File(decompressedFilename));

                //reading first bit in the file
                readbit= fileinput.nextBit();
                HuffCode += readbit+"";


                        //reading every character until reach last letter
                        while( readbit != -1){

                        if(map.containsKey(HuffCode)){
                            char letter;
                            //print each letter
                            letter= map.get(HuffCode);
                            printerwriter.print(letter);
                            printerwriter.flush();
                            HuffCode = "";

                        }
                        readbit = fileinput.nextBit();
                        HuffCode+=  readbit+"";
                    }
                    printerwriter.close();
                System.out.print(HuffCode);
            }

            private static Map<String, Character> loadcode(String codeFilename) throws FileNotFoundException
            {
               //calling map function
                Map<String, Character> map = new TreeMap<String, Character>();
                {
                    Scanner fileinput = new Scanner(new File(codeFilename));
                    //running the loop until last letter
                    while(fileinput.hasNext()){
                        Character letter = (char)fileinput.nextInt();
                        fileinput.nextInt();
                        String hffCode = fileinput.nextLine().trim();

                        // put huffcode and letter
                        map.put(hffCode, letter);
                    }
                    fileinput.close();
                }
                return map;
        }


            private static Map<Character, Huffman> calcaluteLetterFreq(String filename) throws IOException {
                   //assign first value
                    int value=1;
                    //map function
                    Map<Character, Huffman> map= new TreeMap<Character, Huffman>();

                    RandomAccessFile finpute = new RandomAccessFile(new File(filename), "r");
                    int input = (int) finpute.read();

                    //run the until last character
                    while (input != -1) {
                        Character letter = (char) input;

                        /*check the first letter in ASCII table then
                        assign the frequency =1 and if the letter exist then adding 1
                        to the frequency in else statement
                        */
                        if (!map.containsKey(letter))
                        {
                            Huffman fc;
                            fc = new Huffman((char) input);
                            fc.setFrequency(value);
                            map.put(letter, fc);
                        } else
                            {
                            Huffman fc = map.get(letter);    //
                            int updated=fc.getFrequency()+1;
                            fc.setFrequency(updated);
                        }
                        input = (int)finpute.read();
                    }
                    finpute.close();

                return map;
            }

            /**
             * Build a binary  tree from using letter frequency data

             */
            private static TreeNode huffman(Collection<Huffman> letterFreqs){

                //size of frequency
                int n = letterFreqs.size();
                //priority queue for treenode
                PriorityQueue<TreeNode> pq = new PriorityQueue<TreeNode>();

                //insert all the leaf into pq;
                for(Huffman fc: letterFreqs){
                    TreeNode leaf = new TreeNode(fc, null, null);
                    pq.add(leaf);
                }
                for(int i=1; i < n; i++){
                    TreeNode z = new TreeNode();
                    TreeNode x = pq.remove();
                    TreeNode y = pq.remove();
                    z.setLeft(x);
                    z.setRight(y);
                    Huffman fc = new Huffman('@');
                    fc.setFrequency(x.getdatacode().getFrequency() + y.getdatacode().getFrequency());
                    z.setData(fc);
                    pq.add(z);
                }
                return pq.remove();
            }


            /*Traverse binary tree to compute huffman code for
              each letters.
            */
            private static void travertree(TreeNode key, String prefix){
                if(key == null)
                {
                    return ;
                }
                //setup the left with 0 and right with 1
                else if(key.leaf()) {
                    key.datacode.setHuffmanCode(prefix);
                }
                // assign left 0 and right 1
                travertree(key.getleft(), prefix+"0");
                travertree(key.getright(), prefix+"1");
            }
            /*
             * Write huffman code of letters to an output file
             */
            private static void writefile(Map<Character, Huffman> map, String filename) throws FileNotFoundException {

                    PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
                    //loop through each letter of keyset
                    for (Character letter : map.keySet()) {
                        // store letter,frequency, and huffmancode
                        Huffman value;
                        int freq;
                        String huffCode;

                        //get letter, frequency, and huffmancode
                        value= map.get(letter);
                        freq= value.getFrequency();
                        huffCode = value.getHuffmanCode();

                        //print to code file
                        pw.printf("%d %d %s\n", (int)letter, freq, huffCode);
                    }
                    pw.close();

            }

            /**
             Taking original file and compressed the file
             */
            private static void compress(String originalFilename, String compressedFilename, Map<Character,Huffman> map){
                try {

                    int read;
                    RandomAccessFile finpute;
                    BitOutputStream foutput;
                    // input original file and output compressed file using BitoutputStream
                    finpute = new RandomAccessFile(new File(originalFilename), "r");
                    foutput= new BitOutputStream(compressedFilename);

                    //read the first character original file
                    read = (int) finpute.read();

                    // reading each character from the original file
                    while (read != -1) {
                        Character letter = (char) read;
                        Huffman data = map.get(letter);
                        foutput.writeString(data.getHuffmanCode());
                        read= (int)finpute.read();
                    }
                    finpute.close();
                    foutput.close();
                } catch (Exception e) {
                    System.out.println("encode:ERROR: " + e.getMessage());
                    System.exit(0);
                }
            }

        }
