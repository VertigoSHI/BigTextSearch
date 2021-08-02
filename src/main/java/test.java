import org.junit.Assert;
import org.junit.Test;

public class test
{
    @Test
    public void hashTest()
    {
        Data d1 = Data.fromString("64767-fkuepl");
        Data d2 = Data.fromString("144196-fkuepl");
        assert d1.hashCode() == d2.hashCode();
    }

    @Test
    public void hashOut()
    {
        Data temp = Data.fromString("1582-xp");
        System.out.println(temp.hashCode());
    }

}
