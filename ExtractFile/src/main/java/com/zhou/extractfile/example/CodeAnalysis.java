package com.zhou.extractfile.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * 项目文件统计分析
 * 1.项目代码行数统计(总数，按类型分类)，项目文件大小个数统计(总计，按分类统计)
 * 2.各类文件占比(代码行数占比，文件大小占比)，文件代码数，文件大小排行榜
 * @goal codeanalysis
 * @phase compile
 * Created by Zhou Yibing on 2015/12/17.
 */
public class CodeAnalysis extends AbstractMojo{

    public static final String[] DEFAULT_FILETYPE=new String[]{"java","xml","sql","properties","jpg","gif","png","js","css","html","htm","jsp","jar","txt"};
    public static final int SORT_ASC=0;//升序
    public static final int SORT_DESC=1;//倒序

    /**
     * @parameter expression="${project.basedir}"
     * @required
     * @readonly
     */
    private File baseDir;//调用方项目目录

    /**
     * @parameter alias="includes"
     * @readonly
     */
    private String[] fileTypes;//统计的文件类型

    /**
     * @parameter default-value=true
     * @readonly
     */
    private boolean showRatio;//是否显示占比
    /**
     * @parameter default-value=true
     * @readonly
     */
    private boolean showRank;//是否显示排名
    /**
     * @parameter default-value=1
     * @readonly
     */
    private int rankType;//排名方式
    /**
     * @parameter default-value=10
     * @readonly
     */
    private int rankLimit;//显示前多少名

    private long totalLines;//总代码行
    private long totalSize;//总大小
    private List<FileStatistic> allFileStatistic;//全部文件统计

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if(null==fileTypes)
            fileTypes = DEFAULT_FILETYPE;
        //1.根据提供的文件类型，分别创建FileTypeStatistic
        Map<String,FileTypeStatistic> fileTypeStatistics = new HashMap<String,FileTypeStatistic>(fileTypes.length);
        for(int i=0;i<fileTypes.length;i++){
            fileTypeStatistics.put(fileTypes[i],new FileTypeStatistic(fileTypes[i]));
        }
        allFileStatistic = new ArrayList<FileStatistic>();
        //2.从根目录遍历分析文件
        traverseFiles(baseDir,fileTypeStatistics);
        //3.分析统计结果
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("The ").append(baseDir)
                .append(" has ").append(allFileStatistic.size()).append(" specified typed files.")
                .append(" Total size:").append(totalSize).append("B")
                .append(" Total Lines:").append(totalLines);
        getLog().info(stringBuffer.toString());
        if(showRatio){
            stringBuffer.delete(0,stringBuffer.length());
            stringBuffer.append("The Line ratio:");
            StringBuffer sizePrintBuffer = new StringBuffer("The Size ratio:");
            for(Map.Entry entry : fileTypeStatistics.entrySet()){
                FileTypeStatistic fileTypeStatistic = (FileTypeStatistic)entry.getValue();
                stringBuffer.append(entry.getKey()).append("(").append(formatNumF(fileTypeStatistic.getTotalLines()/(double)totalLines)).append("),");
                sizePrintBuffer.append(entry.getKey()).append("(").append(formatNumF(fileTypeStatistic.getTotalSize()/(double)totalSize)).append("),");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
            sizePrintBuffer.deleteCharAt(sizePrintBuffer.length()-1);
            getLog().info(stringBuffer.toString());
            getLog().info(sizePrintBuffer.toString());
        }
        if(showRank){
            allFileRank();
            rankByFileType(fileTypeStatistics);
        }
    }

    private void allFileRank(){
        getLog().info("=================All files line rank=====================");
        Collections.sort(allFileStatistic, new Comparator<FileStatistic>() {
            @Override
            public int compare(FileStatistic o1, FileStatistic o2) {
                if(SORT_ASC==rankType)
                    return (int) (o1.getTotalLines()-o2.getTotalLines());
                else if(SORT_DESC==rankType)
                    return (int) (o2.getTotalLines()-o1.getTotalLines());
                return 0;
            }
        });
        FileStatistic fileStatistic = null;
        for(int i=0;i<rankLimit;i++){
            fileStatistic = allFileStatistic.get(i);
            getLog().info((i+1)+"."+fileStatistic.fileName+"("+fileStatistic.getTotalLines()+")");
        }

        getLog().info("=================All files size rank=====================");
        Collections.sort(allFileStatistic, new Comparator<FileStatistic>() {
            @Override
            public int compare(FileStatistic o1, FileStatistic o2) {
                if(SORT_ASC==rankType)
                    return (int) (o1.getTotalSize()-o2.getTotalSize());
                else if(SORT_DESC==rankType)
                    return (int) (o2.getTotalSize()-o1.getTotalSize());
                return 0;
            }
        });
        for(int i=0;i<rankLimit;i++){
            fileStatistic = allFileStatistic.get(i);
            getLog().info((i+1)+"."+fileStatistic.fileName+"("+fileStatistic.getTotalSize()+"B)");
        }
    }

    private void rankByFileType(Map<String,FileTypeStatistic> fileTypeStatistics){
        getLog().info("=================Rank by FileType=====================");
        for(Map.Entry entry : fileTypeStatistics.entrySet()){
            FileTypeStatistic fileTypeStatistic = (FileTypeStatistic)entry.getValue();
            getLog().info("-------Rank in "+entry.getKey()+" files---------");
            List<FileStatistic> lineList = fileTypeStatistic.lineList(rankType,rankLimit);
            FileStatistic fileStatistic = null;
            if(null!=lineList)
            for(int i=0;i<lineList.size();i++){
                fileStatistic = lineList.get(i);
                getLog().info((i+1)+"."+fileStatistic.fileName+"("+fileStatistic.getTotalLines()+")");
            }
            List<FileStatistic> sizeList = fileTypeStatistic.sizeList(rankType,rankLimit);
            if(null!=sizeList)
            for(int i=0;i<sizeList.size();i++){
                fileStatistic = sizeList.get(i);
                getLog().info((i+1)+"."+fileStatistic.fileName+"("+fileStatistic.getTotalSize()+"B)");
            }
        }
    }

    /**
     * 格式数字为百分比
     * @param s
     * @return
     */
    private String formatNumF(double s) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);// 设置保留小数位数
        return nf.format(s);
    }

    private void traverseFiles(File rootFile, Map<String,FileTypeStatistic> fileTypeStatistics){
        //1.从根路径出发，不断往下递归搜寻文件，将文件装换问FileStatisitc
        if(rootFile.isFile()) {
            FileStatistic fileStatistic = new FileStatistic(rootFile);
            FileTypeStatistic fileTypeStatistic = fileTypeStatistics.get(fileStatistic.getFileType());
            if(null!=fileTypeStatistic){
                //2.根据文件类型将FileStatistic归类到FileTypeStatistic,同时添加到总FileStatistic中
                fileTypeStatistic.addFileStatistic(fileStatistic);
                totalLines += fileStatistic.getTotalLines();
                totalSize += fileStatistic.getTotalSize();
                allFileStatistic.add(fileStatistic);
            }
        }else if(rootFile.isDirectory()){
            File[] childFiles = rootFile.listFiles();
            for(File file:childFiles)
                traverseFiles(file,fileTypeStatistics);
        }
    }

    class FileTypeStatistic{//按文件类型统计
        private String fileType;//文件类型(xml,java,properties,txt等)
        private long totalLines;//总行数，对于文本类文件可用，其他二进制文件不可用如图片，视频等
        private long totalSize;//总的文件大小
        private List<FileStatistic> fileStatisticList;

        public FileTypeStatistic(String fileType) {
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }

        public long getTotalLines() {
            return totalLines;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void addFileStatistic(FileStatistic fileStatistic){
            totalLines+=fileStatistic.getTotalLines();
            totalSize+=fileStatistic.getTotalSize();
            if(null==fileStatisticList) fileStatisticList = new ArrayList<FileStatistic>();
            fileStatisticList.add(fileStatistic);
        }

         /*
         * 代码行排行榜
         * @param sort
         * @param limit
         * @return
         */
        public List<FileStatistic> lineList(final int sort,int limit){
            if(null==fileStatisticList||fileStatisticList.size()==0)
                return null;
            Collections.sort(fileStatisticList, new Comparator<FileStatistic>() {
                @Override
                public int compare(FileStatistic o1, FileStatistic o2) {
                    if(sort==SORT_ASC)
                       return (int) (o1.getTotalLines()-o2.getTotalLines());
                    else if(sort==SORT_DESC)
                       return (int) (o2.getTotalLines()-o1.getTotalLines());
                    return 0;
                }
            });
            if(fileStatisticList.size()<limit)
                limit = fileStatisticList.size();
            return fileStatisticList.subList(0,limit-1);
        }

         /*
         * 文件大小排行榜
         * @param sort 排序方式，倒序/升序
         * @param limit 取多少名
         * @return
         */
        public List<FileStatistic> sizeList(final int sort,int limit){
            if(null==fileStatisticList||fileStatisticList.size()==0)
                return null;
            Collections.sort(fileStatisticList, new Comparator<FileStatistic>() {
                @Override
                public int compare(FileStatistic o1, FileStatistic o2) {
                    if(sort==SORT_ASC)
                        return (int) (o1.getTotalLines()-o2.getTotalLines());
                    else if(sort==SORT_DESC)
                        return (int) (o2.getTotalLines()-o1.getTotalLines());
                    return 0;
                }
            });
            if(fileStatisticList.size()<limit)
                limit = fileStatisticList.size();
            return fileStatisticList.subList(0,limit-1);
        }
    }

    class FileStatistic{//单个文件统计
        private long totalLines;//文件代码行数
        private long totalSize;//文件大小
        private String fileType;//文件类型
        private String fileName;//文件名
        private String filePath;//文件路径
        public final String[] txtFileType=new String[]{"txt","java","js","css","html","jsp","htm","properties","xml","sql"};
        public FileStatistic(){};
        public FileStatistic(File file){
            this.setFileType(getFileType(file));
            this.setFileName(file.getName());
            this.setFilePath(file.getAbsolutePath());
            this.setTotalSize(file.length());
            if(isTxtFile(this.fileType)) {
                this.setTotalLines(getLines(file));
            }
        }

        //读取文件代码行数
        private long getLines(File file) {
            if(file.length()>=2147483648L) {//如果文件大于2G则不读取
                getLog().warn("The file["+file.getAbsolutePath()+"] is large than 2GB,i could not parse it.");
                return 0;
            }
            long readline=0;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                while (reader.ready()){
                    reader.readLine();
                    readline++;
                }
            } catch (FileNotFoundException e) {
                getLog().warn("I can't find  this file["+file.getAbsolutePath()+"],please checked it.");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(null!=reader)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return readline;
        }

        //是否是文本文件
        public boolean isTxtFile(String fileType){
            for(String txt:txtFileType){
                if(txt.equals(fileType)) return true;
            }
            return false;
        }

        private String getFileType(File file) {
            String fileName = file.getName();
            return fileName.substring(fileName.lastIndexOf(".")+1);
        }

        public long getTotalLines() {
            return totalLines;
        }

        public void setTotalLines(long totalLines) {
            this.totalLines = totalLines;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    public static void main(String[] args){
        CodeAnalysis codeAnalysis = new CodeAnalysis();
        try {
            codeAnalysis.execute();
        } catch (MojoExecutionException e) {
            e.printStackTrace();
        } catch (MojoFailureException e) {
            e.printStackTrace();
        }
    }
}
