package ru.codein.bw.cache;

import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerCache extends Cache<Listener> {
    @Getter
    private final List<Listener> listeners = new ArrayList<>();

    @Override
    public void add(Listener item) {
        listeners.add(item);
    }
}
