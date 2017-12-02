package Code;

import java.util.Comparator;

public class ComparadorMes implements Comparator<String> {

    public int compare(String o1, String o2) {
        String[] data1 =o1.split("/");
        String[] data2 =o2.split("/");

        if(Integer.parseInt(data1[0])>Integer.parseInt(data2[0]))
            return 1;
        else
            if(Integer.parseInt(data1[0])<Integer.parseInt(data2[0]))
                return -1;
            else
                if(Integer.parseInt(data1[1])>Integer.parseInt(data2[1]))
                    return 1;
                else
                    if(Integer.parseInt(data1[1])<Integer.parseInt(data2[1]))
                        return -1;
                    else
                        return 0;



    }




    }
