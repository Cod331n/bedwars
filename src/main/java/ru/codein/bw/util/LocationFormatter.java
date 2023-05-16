package ru.codein.bw.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class LocationFormatter {
    public static Location format(String string) {
        String[] splitted = string.split(",");
        System.out.println(Arrays.toString(splitted));
        String world = splitted[0];
        double x = Double.parseDouble(splitted[1]);
        double y = Double.parseDouble(splitted[2]);
        double z = Double.parseDouble(splitted[3]);
        float pitch = 0f;
        float yaw = 0f;
        if (splitted.length > 4) {
            pitch = Float.parseFloat(splitted[4]);
        }
        if (splitted.length > 5) {
            yaw = Float.parseFloat(splitted[5]);
        }

        return new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

    public static List<Location> format(List<String> list) {
        List<Location> result = new ArrayList<>();
        list.forEach(e -> {
            String[] splitted = e.split(",");
            String world = splitted[0];
            double x = Double.parseDouble(splitted[1]);
            double y = Double.parseDouble(splitted[2]);
            double z = Double.parseDouble(splitted[3]);

            result.add(new Location(Bukkit.getWorld(world), x, y, z));
        });

        return result;
    }
}
