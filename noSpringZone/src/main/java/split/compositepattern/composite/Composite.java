package split.compositepattern.composite;

import java.util.ArrayList;
import java.util.List;
import split.compositepattern.Component;
import split.compositepattern.ConditionalComponent;

public class Composite implements Component {

    private final List<ConditionalComponent> components = new ArrayList<>();

    public void addComponent(final ConditionalComponent component) {
        components.add(component);
    }

    public void removeComponent(final ConditionalComponent component) {
        components.remove(component);
    }

    @Override
    public void operation(final String input) {
        final ConditionalComponent usableComponent1 = components.stream()
                .filter(usableComponent -> usableComponent.isUsable(input))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);

        usableComponent1.operation(input);
    }
}
