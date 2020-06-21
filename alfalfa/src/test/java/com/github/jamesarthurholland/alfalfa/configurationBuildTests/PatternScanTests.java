package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

        import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
        import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
        import org.junit.jupiter.api.Test;

        import java.nio.file.Path;
        import java.nio.file.Paths;

public class PatternScanTests {

    @Test
    public void patternScan() {
        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
        Pattern pattern = new PatternFileScanner(patternPath).scan();
        System.out.println("PatternScanTest");
    }

    @Test
    public void clonePattern() {
        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
        Pattern a = new PatternFileScanner(patternPath).scan();
        Pattern b = a.copy();

        assert(a.equals(b));
    }
}
