package split.compositepattern.leaf;

import split.compositepattern.ConditionalComponent;

public class CLeafComponent implements ConditionalComponent {

    private final String usableSign = "c";

    @Override
    public void operation(final String input) {
        System.out.println("I'm C LeafComponent");
    }

    @Override
    public boolean isUsable(final String input) {
        return usableSign.equals(input);
    }
}
