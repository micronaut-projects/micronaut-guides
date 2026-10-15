package example.micronaut;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

@GraalPyModule("pygal") // <1>
interface PygalModule {
    StackedBar StackedBar(); // <2>

    interface StackedBar { // <3>
        void add(String title, int[] i); // <4>
        Svg render(); // <5>
    }

    interface Svg { // <6>
        String decode(); // <7>
    }
}