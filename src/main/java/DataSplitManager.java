import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class DataSplitManager
{
    private static int num;
    private static String path;
    private static List<File> fileList;
    private static ConcurrentHashMap<Integer, FileOutputStream> hashWriterList;
    private static ConcurrentHashMap<Integer, BufferedReader> hashReaderList;

    public static void init(int num,String path) throws Exception
    {
        if(num<=0)
        {
            throw new Exception("不合法的hash值上限");
        }
        DataSplitManager.num = num;
        DataSplitManager.path = path;
        fileList = Collections.synchronizedList(new LinkedList<>());
        hashReaderList = new ConcurrentHashMap<>(num);
        hashWriterList = new ConcurrentHashMap<>(num);
        File folder = new File(path+"/"+"Dataset");
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        IntStream.rangeClosed(0,num-1).parallel().forEach(n->{
            File file = new File(folder.getAbsolutePath()+"/"
                    +"DataSet"+n+".txt");
            try
            {
                if(!file.exists())
                {
                    file.delete();
                }
                file.createNewFile();
                fileList.add(file);
                hashReaderList.put(n,new BufferedReader(new FileReader(file)));
                hashWriterList.put(n,new FileOutputStream(file));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
    public static void dataGiveOut(Data data) throws IOException {
        FileOutputStream fileOutputStream = hashWriterList.get(Math.abs(data.hashCode())%num);

        if(fileOutputStream!=null)
        {
            fileOutputStream.write(data.toString().getBytes(StandardCharsets.UTF_8));
        }
        else
        {
            System.out.println(data.hashCode()%num+"对应文件为空");
        }
    }

    public static Data findResult()
    {
        //全局最优文件
       final Data result = new Data(Integer.MAX_VALUE,null);
        hashReaderList.entrySet().parallelStream().forEach(hash_reader_entry->{
            //对每个文件进行处理
            //复写过HashCode函数 按字符串hash 并且计算出现次数
            HashMap<Data,Integer> lines = new HashMap<>();
            try {
                BufferedReader reader = hash_reader_entry.getValue();
                String readingLine;
                while ((readingLine = reader.readLine())!=null)
                {
                    Data readingLineData = Data.fromString(readingLine);
                    lines.put(readingLineData,lines.getOrDefault(readingLineData,0)+1);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //局部最优结果
            Data tempResult = new Data(Integer.MAX_VALUE,null);
            lines.entrySet().parallelStream().forEach(e->{
                if(e.getValue()==1)
                {
                    //System.out.println(e.getKey()+"value:"+e.getValue());
                    synchronized (tempResult)
                    {
                        if(e.getKey().line<tempResult.getLine())
                        {
                            e.getKey().copyTo(tempResult);
                        }
                    }
                }
            });
            //System.out.println(tempResult);
            synchronized (result)
            {
                if(tempResult.line<result.line)
                {
                   result.copyFrom(tempResult);
                }
            }
            //永远不要在程序中显式的调用gc
            //System.gc();
        });
        return result;
    }

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir")+"/src/main/resources/Dataset");
        if(!file.exists())
        {
            //System.out.println(1);
            System.out.println(file.mkdirs());
        }
        System.out.println();
        System.out.println(file.getAbsolutePath());
    }

}
