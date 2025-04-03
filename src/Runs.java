import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Runs {
    private String name;
    private double time;
    private String date;

    private static final DecimalFormat timeFormat = new DecimalFormat("0.00");

    public Runs(String name, double time) {
        this.name = name;
        this.time = time;
    }
    public Runs(){
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getTime() {
        return time;
    }
    public void setTime(double time) {
        this.time = time;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void saveInformation() {
        try {
            ObjectMapper objMapper = new ObjectMapper();
            File file = new File("C:\\Users\\GS\\HW2.402100486\\src\\players.json");
            List<Runs> players = new ArrayList<>();

            if (file.exists() && file.length() > 0) {
                players = objMapper.readValue(file, new TypeReference<List<Runs>>() {});
            }

            String currentDateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            Runs newRun = new Runs(name, Double.parseDouble(timeFormat.format(time)));
            newRun.setDate(currentDateTime);
            players.add(newRun);

            objMapper.writerWithDefaultPrettyPrinter().writeValue(file, players);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred while saving player data.");
        }
    }
}
