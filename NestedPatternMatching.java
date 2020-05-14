import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

public class NestedPatternMatching {
    private Map<String, String> sample = new HashMap<>();

    @BeforeTest
    public void setup() {
        this.sample.put("evenet", "Test");
        this.sample.put("evenet = blosssm", "true");
        this.sample.put("var1", "fruit");
        this.sample.put("var2", "apple");
        this.sample.put("var3", "demo");
        this.sample.put("var2 = demo", "gala");
        this.sample.put("var1 = gala", "cherry");
        this.sample.put("id = cherry", "blossom");
        this.sample.put("this is another fruit with apple", "automation");
        this.sample.put("this is another with apple", "automation new");

        this.sample.put("id", "160");
        this.sample.put("t", "t");
        this.sample.put("r t", "rt");
        this.sample.put("e rt", "ert");
        this.sample.put("ert 160", "done");
        this.sample.put("wert", "wert");
        this.sample.put("qwert", "qwert");
        
        this.sample.put("1","1");
        this.sample.put("1 1","11");

        this.sample.put("1 11","111");
        this.sample.put("1 111","1111");
        this.sample.put("1 1111","Super");


    }

    @Test
    public void test() {
        
        patternSeachAndEvualte("<$evenet$> <$id$>"); //Test 160
        patternSeachAndEvualte("<$evenet$> <$this is another with <$var2$>$> <$var3$>"); //test automation new demo
        patternSeachAndEvualte("<$evenet$> <$this is another <$var1$> with <$var2$>$> <$var3$>"); //test automation demo
        patternSeachAndEvualte("<$e <$r <$t$>$>$> <$id$>"); //ert 160
        patternSeachAndEvualte("<$<$e <$r <$t$>$>$> <$id$>$>"); //done
        patternSeachAndEvualte("<$1 <$1 <$1 <$1 <$1$>$>$>$>$> -- <$<$e <$r <$t$>$>$> <$id$>$> 123456 <$evenet$> <$id$>"); //Super done 123456 Test 160

    }

    private void patternSeachAndEvualte(String str) {
        String s = "<$";
        String e = "$>";

        int i = str.indexOf(s);
        int j = 1;
        Map<String, Integer> map = new HashMap<>();
        while (i >= 0) {
            map.put("s" + j, i);
            j++;
            i = str.indexOf(s, i + 1);
        }

        i = str.indexOf(e);
        j = 1;
        while (i >= 0) {
            map.put("e" + j, i);
            j++;
            i = str.indexOf(e, i + 1);
        }

        final Map<String, Integer> smap = map.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Set<String> keys = smap.keySet();

        ArrayList<String> groups = new ArrayList();
        while (!keys.isEmpty()) {
            Object[] x = keys.toArray();

            for (int m = 0; m < x.length - 1; m++) {
                if (!x[m].toString().substring(0, 1).
                        equalsIgnoreCase(
                                x[m + 1].toString().substring(0, 1))
                ) {
                    groups.add(str.substring(map.get(x[m]) + 2,map.get(x[m + 1])));
                    keys.remove(x[m]);
                    keys.remove(x[m + 1]);
                    break;
                }
            }
        }

        for (int y = 0; y < groups.size(); y++) {
            String exp = groups.get(y).toString();
            String value = evaluateExpression(exp);

            for(int z=0;z < groups.size();z++){
                String oldExp = groups.get(z).toString();
                String newExp = oldExp.replace("<$"+exp+"$>",value);
                str = str.replace("<$"+exp+"$>",value);
                groups.set(z,newExp);

            }
        }
        System.out.println(str);
    }

        private String evaluateExpression(String exp) {
        return sample.get(exp);
    }
}
