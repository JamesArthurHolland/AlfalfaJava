package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

        import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
        import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
        import org.junit.jupiter.api.Test;
        import org.yaml.snakeyaml.Yaml;

        import javax.swing.text.html.Option;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.Map;
        import java.util.Optional;

public class PatternScanTests {

    @Test
    public void patternScan() {
        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
        Pattern pattern = PatternFileScanner.scanRootPatternFile(patternPath);
        System.out.println("PatternScanTest");
    }
}
