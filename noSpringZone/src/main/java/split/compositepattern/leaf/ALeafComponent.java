package split.compositepattern.leaf;

import split.compositepattern.ConditionalComponent;

public class ALeafComponent implements ConditionalComponent {

    private final String usableSign = "a";

    @Override
    public void operation(final String input) {
        System.out.println("I'm A LeafComponent");
    }

    @Override
    public boolean isUsable(final String input) {
        return usableSign.equals(input);
    }
}
