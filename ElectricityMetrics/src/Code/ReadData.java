package Code;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class ReadData {

    private Database db = new Database();
    String csvFile = "Test.csv";
    String cvsSplitBy = ";";
    BufferedReader br;
    Map<String, Double> mediaDia = new TreeMap<String, Double>();
    Map<String, Double> desvioDia = new TreeMap<String, Double>();
    Map<String, Double> mediaMes = new TreeMap<String, Double>();
    Map<String, Double> desvioMes = new TreeMap<String, Double>();
    Map<Integer, Integer> sombra = new TreeMap<Integer, Integer>();


    public ReadData() {
        db = new Database();
        try {
            db.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getMonthByYear(String year) {
        String query_getMonth = "select distinct month from energy_history where year = " + year + ";";

        ResultSet rs = db.getData(query_getMonth);
        ArrayList<String> m = new ArrayList<String>();
        m.add("Mês");
        try {
            while (rs.next()) {
                m.add(String.valueOf(rs.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    public ArrayList<String> getDayByMonthYear(String year, String month) {
        String query = "select distinct day from energy_history where year = " + year + " and month = " + month + ";";

        ResultSet rs = db.getData(query);
        ArrayList<String> d = new ArrayList<String>();
        d.add("Dias");
        try {
            while (rs.next()) {
                d.add(String.valueOf(rs.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public ArrayList<String> years() {
        String query_getYears = "select distinct year from energy_history group by year;";
        ResultSet rs = db.getData(query_getYears);
        ArrayList<String> y = new ArrayList<String>();
        y.add("Ano");
        try {
            while (rs.next()) {
                y.add(String.valueOf(rs.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return y;
    }

    public Map<String, Double> getMediaDia() {
        if (mediaDia.size() == 0) {
            CalcMediaDia();
        }
        return mediaDia;
    }

    public Map<String, Double> getMediaDia(String year, String month, String day) {
        String date = year + "/" + month + "/" + day;
        Map<String, Double> res = new TreeMap<>();

        res.put(date, mediaDia.get(date));

        return res;
    }

    public Map<String, Double> getMediaDia(String year, String month) {
        String date = year + "/" + month;
        Map<String, Double> res = new TreeMap<>();
        for (String s : mediaDia.keySet()) {
            if (s.contains(date)) {
                res.put(s, mediaDia.get(s));
            }
        }

        return res;
    }

    public Map<String, Double> getMediaDia(String year) {
        String date = year;
        Map<String, Double> res = new TreeMap<>();
        for (String s : mediaDia.keySet()) {
            if (s.contains(date)) {
                res.put(s, mediaDia.get(s));
            }
        }

        return res;
    }

    public Map<String, Double> getDesvioDia() {
        if (desvioDia.size() == 0) {
            CalcDesvioDia();
        }
        return desvioDia;
    }

    public Map<String, Double> getDesvioDia(String year, String month, String day) {
        String date = year + "/" + month + "/" + day;
        Map<String, Double> res = new TreeMap<>();

        res.put(date, desvioDia.get(date));

        return res;
    }

    public Map<String, Double> getDesvioDia(String year) {
        String date = year;
        Map<String, Double> res = new TreeMap<>();
        for (String s : desvioDia.keySet()) {
            if (s.contains(date)) {
                res.put(s, desvioDia.get(s));
            }
        }


        return res;
    }

    public Map<String, Double> getDesvioDia(String year, String month) {
        String date = year + "/" + month;
        Map<String, Double> res = new TreeMap<>();
        for (String s : desvioDia.keySet()) {
            if (s.contains(date)) {
                res.put(s, desvioDia.get(s));
            }
        }


        return res;
    }

    public Map<String, Double> getMediaMes() {
        if (mediaMes.size() == 0) {
            CalcMediaMes();
        }
        return mediaMes;
    }


    public Map<String, Double> getDesvioMes() {
        if (desvioMes.size() == 0) {
            CalcDesvioMes();
        }
        return desvioMes;
    }

    public void CalcMediaDia() {
        String query = "select year,month,day,avg(ch1_kw_avg) from energy_history group by month,year,day;";
        try {
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3);

                mediaDia.put(date, Double.parseDouble(rs.getString(4)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* public void CalcDesvioDia(){
        String query = "select year,month,day,AVG((ch1_kw_avg - sub.a) * (ch1_kw_avg  - sub.a)) as var from energy_history , " +
                " (SELECT AVG(ch1_kw_avg) AS a FROM energy_history) AS sub group by month,year,day;";
        try{
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3);

                desvioDia.put(date,Math.sqrt(Double.parseDouble(rs.getString(4))));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public void CalcDesvioDia() {
        Map<String, Integer> cont = new HashMap<String, Integer>();
        String query = "select year,month,day,ch1_kw_avg as m from energy_history;";
        try {
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2) + "/" + rs.getString(3);
                if (desvioDia.containsKey(date)) {
                    desvioDia.put(date, desvioDia.get(date) +Math.pow(Double.parseDouble(rs.getString(4)) - mediaDia.get(date),2) );
                    cont.put(date, cont.get(date) + 1);
                } else {
                    desvioDia.put(date, Math.pow(Double.parseDouble(rs.getString(4)) - mediaDia.get(date),2));
                    cont.put(date, 1);
                }

            }
            for (String s : desvioDia.keySet()) {
                desvioDia.put(s, Math.sqrt(desvioDia.get(s) / cont.get(s)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void CalcMediaMes() {
        String query = "select year, month, avg(ch1_kw_avg) from energy_history group by year,month;";
        try {
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2);

                mediaMes.put(date, Double.parseDouble(rs.getString(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
        public void CalcDesvioMes(){
            String query = "select year,month,AVG((ch1_kw_avg - sub.a) * (ch1_kw_avg  - sub.a)) as var from energy_history , (SELECT AVG(ch1_kw_avg) AS a FROM energy_history) AS sub group by month,year;";

            try{
                ResultSet rs = db.getData(query);
                while (rs.next()) {
                    String date = rs.getString(1) + "/" + rs.getString(2);

                    if(!desvioMes.containsKey(date)){
                        desvioMes.put(date, Math.sqrt(Double.parseDouble(rs.getString(3))));
                    }else{
                        desvioMes.put(date,Math.sqrt(Double.parseDouble(rs.getString(3))));
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    */
    public void CalcDesvioMes() {
        Map<String, Integer> cont = new HashMap<String, Integer>();
        String query = "select year,month,day,ch1_kw_avg as m from energy_history;";
        try {
            ResultSet rs = db.getData(query);
            while (rs.next()) {
                String date = rs.getString(1) + "/" + rs.getString(2);

                if (!desvioMes.containsKey(date)) {
                    desvioMes.put(date,Math.pow( Double.parseDouble(rs.getString(4))-mediaMes.get(date),2) );
                    cont.put(date,1);
                } else {
                    desvioMes.put(date, desvioMes.get(date)+Math.pow( Double.parseDouble(rs.getString(4))-mediaMes.get(date),2));
                    cont.put(date,cont.get(date)+1);
                }

            }
            for (String s : desvioMes.keySet()) {
                desvioMes.put(s, Math.sqrt(desvioMes.get(s) / cont.get(s)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double getDouble(String s) {
        double d;
        try {
            d = Double.parseDouble(s);
            return d;
        } catch (NumberFormatException n) {
            return 0;
        }

    }

    public Map<Integer,Integer> getSombra(){
        String query = "select hour,count(*) from energy_history where ch1_kw_avg <= 4.83 group by hour;";

        try{
            ResultSet rs = db.getData(query);
            while(rs.next()){
                sombra.put(rs.getInt(1),rs.getInt(2));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return sombra;
        }
    }


}


