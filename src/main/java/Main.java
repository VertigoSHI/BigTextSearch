public class Main
{
    public static void main(String[] args) {
        String root = System.getProperty("user.dir");
        String sourcePath = root + "/MyBigFile.txt";
        String resultPath = root+"/src/main/resources";
        try {
            System.out.println("测试开始");
            Long start = System.currentTimeMillis();
            BigFileReader bigFileReader = new BigFileReader(sourcePath);
            DataSplitManager.init(1000,resultPath);
            Data lineData;
            long start1 = System.currentTimeMillis();
            System.out.println("----------"+"开始数据分发"+"-----------");
            while ((lineData = bigFileReader.readLine())!=null)
            {
                DataSplitManager.dataGiveOut(lineData);
            }
            System.out.println("-----------"+"数据分发完毕 共耗时"+(System.currentTimeMillis()-start1)+"-------------");
            Data result = DataSplitManager.findResult();
            System.out.println(result);
            System.out.println(Math.abs(result.hashCode())%1000);
            System.out.println("测试结束 共用时" + (System.currentTimeMillis()-start));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
