package org.ivanina;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class HedgehogProblem {

    public static void main(String[] args) {
        HedgehogProblem hedgehogProblem = new HedgehogProblem(
                args.length > 1 ? args[1] : "input.txt",
                args.length > 2 ? args[2] : "output.txt",
                false
        );
        hedgehogProblem.run();
    }

    public void run(){
        reader();
        processing();
        write();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                System.out.print( map[i][j] + (track[i][j]==1?"*":" ") + " ");
            }
            System.out.println();
        }
        /*System.out.println();
        for(int i = 0; i < summMap.length; i++){
            for(int j = 0; j < summMap[0].length; j++){
                System.out.print( summMap[i][j] + " ");
            }
            System.out.println();
        }*/
        System.out.println(maxApples);
        System.out.println("Finish");
    }

    private int[][] map;
    private int[][] summMap;
    private int[][] track;
    private int m, n; // M - rows, N - columns
    private int maxApples;
    private String inputFileName, outputFileName;

    /**
     * The class HedgehogProblem included to "main" class
     */
    public HedgehogProblem(String inputFileName, String outputFileName){
        this(inputFileName,outputFileName,true);
    }

    public HedgehogProblem(){
        this("input.txt","output.txt");
    }

    /**
     * inProjectModeRun is false if this HedgehogProblem is 'main' class
     */
    public HedgehogProblem(String inputFileName, String outputFileName, Boolean inProjectModeRun){
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        if(inProjectModeRun){
            String basePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +
                    this.getClass().getPackage().getName().replaceAll("\\.","/");
            if(basePath.contains(":")){
                basePath = basePath.substring(1,basePath.length());
            }
            this.inputFileName = basePath + "/" + this.inputFileName;
            this.outputFileName = basePath + "/" + this.outputFileName;
        }
    }

    public void processing(){
        summMap = new int[m][n];
        summMap[0][0] = map[0][0];

        for(int column=1;column<n;column++){
            summMap[0][column]=summMap[0][column-1]+map[0][column];
        }
        for(int row=1;row<m;++row)
        {
            summMap[row][0]=summMap[row-1][0]+map[row][0];
            for(int column=1;column<n;column++)
                if ( summMap[row-1][column] > summMap[row][column-1] ) {
                    summMap[row][column] = map[row][column] + summMap[row - 1][column];
                }else {
                    summMap[row][column] = map[row][column] + summMap[row][column - 1];

                }
        }
        maxApples = summMap[m-1][n-1];
        track[m-1][n-1] = 1;

        int row = m-1;
        int col = n-1;
        for(int step = 0 ; step < m+n-2; step++){
            int leftOffset = col > 0 ? summMap[row][col-1] : summMap[0][0]-1;
            int upOffset = row > 0 ? summMap[row-1][col] : summMap[0][0]-1;
            if(leftOffset < upOffset ){
                track[row-1][col] = 1;
                row--;
            }else{
                track[row][col-1] = 1;
                col--;
            }
        }

    }

    private void write(){
        Path outputPath = Paths.get(outputFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write(maxApples+"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void reader(){
        try (Stream<String> stream = Files.lines(
                Paths.get(
                        inputFileName
                )
        )){
            int i=0,j;
            for(Object line : stream.toArray()){
                String[] lineElements = ((String)line).split("\\s");
                if(m == 0 && lineElements.length == 2){
                    n = Integer.parseInt(lineElements[0]);
                    m = Integer.parseInt(lineElements[1]);
                    map = new int[m][n];
                    track = new int[m][n];
                }else if(lineElements.length == n) {
                    j=0;
                    for(String item : lineElements){
                        map[i][j]=Integer.parseInt(item);
                        j++;
                    }
                    i++;
                }else{
                    //TODO: Alert
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}