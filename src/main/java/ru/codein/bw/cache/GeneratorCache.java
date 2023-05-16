package ru.codein.bw.cache;

import lombok.Getter;
import ru.codein.bw.generator.Generator;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GeneratorCache extends Cache<Generator> {
    @Getter
    private final List<Generator> generators = new ArrayList<>();

    @Override
    public void add(Generator item) {
        generators.add(item);
    }
}
