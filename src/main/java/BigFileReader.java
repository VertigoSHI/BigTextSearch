import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class BigFileReader
{
    private String path;
    private File dataSource;
    private BufferedReader reader;
    private Integer linesCounter = 0;
    BigFileReader(File file) throws Exception
    {
        if(!file.exists())
        {
            throw new Exception("对应数据源文件不存在");
        }
        dataSource = file;
        path = dataSource.getPath();
        reader = new BufferedReader(new FileReader(file));
    }
    BigFileReader(String path) throws Exception {
        File file = new File(path);
        if(!file.exists())
        {
            throw new Exception("对应数据源文件不存在");
        }
        this.path = path;
        dataSource = file;
        reader = new BufferedReader(new FileReader(file));
    }

    Data readLine() throws IOException
    {
        linesCounter++;
        String line;
        if((line=reader.readLine())!=null)
        {
            return new Data(linesCounter,line);
        }
        return null;
    }
}

/**
 * 代表从文件中读取的每一行数据
 * 包含行号和字符串内容
 */
class Data
{
    volatile int line;
    volatile String data;

    Data(int line,String data)
    {
        this.line = line;
        this.data = data;
    }

    @Override
    public String toString() {
        return line+"-"+data+"\n";
    }

    public int getLine() {
        return line;
    }

    public String getData() {
        return data;
    }

    public static Data fromString(String line)
    {
        String[] lines  =  line.split("[-]",2);
        return new Data(Integer.parseInt(lines[0]),lines[1]);
    }

    /**
     * 精髓
     * 放入hashmap的时候只按照data进行hash
     */
    public int hashCode()
    {
        return data.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data1 = (Data) o;
        return Objects.equals(data, data1.data);
    }

    public Data copyFrom(Data data)
    {
        if(data!=null)
        {
            this.data = data.getData();
            this.line = data.getLine();
            return this;
        }
        else return null;
    }
    public void copyTo(Data data)
    {
        if(data==null)
        {
            throw new NullPointerException();
        }
        else
        {
            data.line = this.line;
            data.data = this.data;
        }
    }
    public Data deepCopy()
    {
        return new Data(this.getLine(),this.getData());
    }

}
