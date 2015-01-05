package intermediaryControl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import backEnd.CryptoMethod;
import backEnd.FileModel;

public class Controller {
	
	private Map<Path, FileModel> data;
	private Path outputFolder;
	private boolean recursionOpt;
	private boolean deleteAfter;
	private CryptoMethod direction; 
	
	private Path lastSavedPath = Paths.get(System.getProperty("user.home"));
	
	public Controller() {
		this(null, null, false, false, CryptoMethod.Encrypt);
	}
	
	public Controller(Map<Path, FileModel> items, Path outFile, boolean searchSubDir, boolean delAfter, CryptoMethod cm) {
		data = items;
		outputFolder = outFile;
		recursionOpt = searchSubDir;
		deleteAfter = delAfter;
		direction = cm;
	}
	
	public Map<Path, FileModel> getData() {
		Map<Path, FileModel> result = new HashMap<Path, FileModel>(data);
		return result;
	}
	
	public Path getOutputFolder() {
		Path p = outputFolder;
		return p;
	}
	
	public boolean getRecursionOpt() {
		return recursionOpt;
	}
	
	public boolean getDeleteAfterOpt() {
		return deleteAfter;
	}
	
	public void setData(Map<Path, FileModel> updatedData) {
		data = new HashMap<Path, FileModel>(updatedData);
	}
	
	public void setOutputFolder(Path updatedPath) {
		outputFolder = updatedPath;
	}
	
	public void setRecursionOpt(boolean value) {
		recursionOpt = value;
	}
	
	public void setDeleteAfterOpt(boolean value) {
		deleteAfter = value;
	}
	
	public void clearData() {
		if (data != null)
			data.clear();
	}
	
	public int getDataSize() {
		if (data != null)
			return data.size();
		return -1;
	}
	
	// http://stackoverflow.com/questions/4191687/how-to-calculate-mean-median-mode-and-range-from-a-set-of-numbers
	//
	public Path getHighestAvgParentFolder() {
		if (data == null)
			return null;
		Path result = null;
		Map<Path, Integer> counts = new TreeMap<Path, Integer>();
		for (Entry<Path, FileModel> e : data.entrySet()) {
			Path p = null;
			if (!Files.isDirectory(e.getKey()) )
				p = e.getKey().getParent();
			else
				p = e.getKey();
			int n = 0;
			if (counts.containsKey(p))
				n = counts.get(p) + 1;
			else
				n = 1;
			counts.put(p, n);
		}
		int h = -1;
		for (Entry<Path, Integer> e : counts.entrySet()) {
			int val = e.getValue();
			if (val > h) {
				result = e.getKey();
				h = val;
			}
		}
		lastSavedPath = result;
		return result;
	}
	
	public Path getLastSaved() {
		Path p = lastSavedPath;
		return p;
	}
	
	public void setLastSaved(Path p) {
		lastSavedPath = p;
	}
	
	public void setData(List<Path> list) {
		Map<Path, FileModel> m = new HashMap<Path, FileModel>();
		for (Path p : list) {
			m.put(p, FileModel.create(p, recursionOpt));
		}
		data = m;
	}
	
	public CryptoMethod getCryptoMethod() {
		return direction;
	}

	public void setCryptoMethod(CryptoMethod cm) {
		direction = cm;
	}
}
