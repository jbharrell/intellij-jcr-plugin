package velir.intellij.cq5.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JCRModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {
	private List<Pair<String,String>> mySourcePaths;
	private String myContentEntryPath;
	private JCRModuleConfiguration.State state;

	public JCRModuleBuilder () {
		super();

		state = new JCRModuleConfiguration.State();
	}

	@Override
	public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
		//TODO: implement
	}

	@Override
	public ModuleType getModuleType() {
		return JCRModuleType.getInstance();
	}

  @Nullable
  public String getContentEntryPath() {
    if (myContentEntryPath == null) {
      final String directory = getModuleFileDirectory();
      if (directory == null) {
        return null;
      }
      new File(directory).mkdirs();
      return directory;
    }
    return myContentEntryPath;
  }

  public void setContentEntryPath(String moduleRootPath) {
    final String path = acceptParameter(moduleRootPath);
    if (path != null) {
      try {
        myContentEntryPath = FileUtil.resolveShortWindowsName(path);
      }
      catch (IOException e) {
        myContentEntryPath = path;
      }
    }
    else {
      myContentEntryPath = null;
    }
    if (myContentEntryPath != null) {
      myContentEntryPath = myContentEntryPath.replace(File.separatorChar, '/');
    }
  }

	public List<Pair<String, String>> getSourcePaths() {
		if (mySourcePaths == null) {
			final List<Pair<String, String>> paths = new ArrayList<Pair<String, String>>();
			@NonNls final String path = getContentEntryPath() + File.separator + "jcr_root";
			new File(path).mkdirs();
			paths.add(Pair.create(path, ""));
			return paths;
		}
		return mySourcePaths;
	}

	public void setSourcePaths(List<Pair<String, String>> pairs) {
		mySourcePaths = pairs != null? new ArrayList<Pair<String, String>>(pairs) : null;
	}

	public void addSourcePath(Pair<String, String> stringStringPair) {
	    if (mySourcePaths == null) {
			mySourcePaths = new ArrayList<Pair<String, String>>();
		}
		mySourcePaths.add(stringStringPair);
	}
}
