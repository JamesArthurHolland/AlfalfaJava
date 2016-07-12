import java.util.ArrayList;

/**
 * Created by jamie on 04/07/16.
 */
public class CompilerResult
{
    protected String fileName;
    protected ArrayList<String> output;

    public CompilerResult(String fileName, ArrayList<String> output) {
        this.fileName = fileName;
        this.output = output;
    }

    public ArrayList<String> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<String> output) {
        this.output = output;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
