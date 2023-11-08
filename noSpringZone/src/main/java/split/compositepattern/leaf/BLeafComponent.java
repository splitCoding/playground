package split.compositepattern.leaf;

import split.compositepattern.ConditionalComponent;

public class BLeafComponent implements ConditionalComponent {

    private final String usableSign = "b";

    @Override
    public void operation(final String input) {
        System.out.println("I'm B LeafComponent");
    }

    @Override
    public boolean isUsable(final String input) {
        return usableSign.equals(input);
    }
}
