package split.compositepattern;

import java.util.List;
import split.compositepattern.composite.Composite;
import split.compositepattern.leaf.ALeafComponent;
import split.compositepattern.leaf.BLeafComponent;
import split.compositepattern.leaf.CLeafComponent;

public class Main {

    public static void main(String[] args) {
        final Composite composite = new Composite();

        composite.addComponent(new ALeafComponent());
        composite.addComponent(new BLeafComponent());
        composite.addComponent(new CLeafComponent());

        final List<String> inputs = List.of("a", "b", "c");
        inputs.forEach(composite::operation);
    }
}
