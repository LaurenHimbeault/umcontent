
import java.io.*;
import java.util.List;
import java.util.ArrayList;

// logic layer
class NamesManager {
	private List<String> names;

	public NamesManager() {
		names = new ArrayList<String>();
	}
	
	public void saveName(String name) {
		names.add(name);
		saveToFile("names.txt");
	}

	public String getStringOfNames() {
		names.clear();
		pullNamesFromFile("names.txt");
		return String.join("\n", names);
	}

	public int numNames() {
		return names.size();
	}

	public String getNamesWithLengthCounts() {
		StringBuilder sb = new StringBuilder();
        for (String name : names) {
        	sb.append(getNameLengthLine(name));
        }          
        return sb.toString();
	}

	public String getNamesStats() {
		int totalLength = 0;
        int shortest = Integer.MAX_VALUE;
        int longest = 0;
        for (String name : names) {
            int len = name.length();
            totalLength += len;
            if (len < shortest) shortest = len;
            if (len > longest) longest = len;
        }
        double average = names.isEmpty() ? 0 : (double) totalLength / names.size();
        StringBuilder sb = new StringBuilder();
        sb.append("Average Length: ").append(String.format("%.2f", average)).append("\n");
        sb.append("Shortest Name Length: ").append(shortest).append("\n");
        sb.append("Longest Name Length: ").append(longest).append("\n");
        return sb.toString();
	}

	private String getNameLengthLine(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" -> ").append(name.length()).append(" chars\n");
		return sb.toString();
	}

	private void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String n : names) {
                writer.write(n);
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // swallow exception don't do this!
        }
	}
	
	private void pullNamesFromFile(String fileName) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            names.add(line);
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	} 
                
}